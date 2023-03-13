"""
Script to fill in the missing user contact data in the CSV file.
"""
import json
import sys
from typing import List

from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from pyspark.sql import DataFrame
from pyspark.sql.functions import col, lit, udf, when
from pyspark.sql.types import StringType

# Glue Context
args = getResolvedOptions(
    sys.argv,
    [
        "JOB_NAME",
        "env",
        "source_massive_file_path",
        "consumer_id",
        "data_enrichment",
        "provider",
        "priority",
        "remitter_id",
    ],
)

glueContext = GlueContext(SparkContext.getOrCreate())
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args["JOB_NAME"], args)

# Job parameters
env: str = args["env"]
data_enrichment: str = args["data_enrichment"]
source_massive_file_path: str = args["source_massive_file_path"]
consumer_id: str = f"|{args['consumer_id']}"
provider: str = args["provider"]
priority: str = args["priority"]
remitter_id: int = int(args["remitter_id"])

# Glue
GLUE_DATABASE: str = f"nu0154001-alertas-{env}-db"

# Buckets
BUCKET_SOURCE: str = f"nu0154001-alertas-{env}-glue-source-data"
BUCKET_TARGET: str = f"nu0154001-alertas-{env}-glue-processed-data"

# Error messages
USER_ID_MSG_ERR: str = "El numero de documento no se encontro en la base de datos"
EMAIL_MSG_ERR: str = "El correo electronico no se encontro en la base de datos"
SMS_MSG_ERR: str = "El numero de celular no se encontro en la base de datos"
PUSH_MSG_ERR: str = "El usuario no esta suscrito a push"

# Channel types
CHANNEL_SMS: str = "SMS"
CHANNEL_EMAIL: str = "EMAIL"
CHANNEL_PUSH: str = "PUSH"

# Id contact channel
CONTACT_MEDIUM_SMS: str = "0"
CONTACT_MEDIUM_EMAIL: str = "1"
CONTACT_MEDIUM_PUSH: str = "2"

# Number of rows to write per file
CSV_ROWS_LIMIT: int = 6700

# Columns to select from the DynamicFrame of contacts
CONTACTS_COLUMNS: List[str] = ["document_number", "id_contact_medium", "value"]

# Output fields in EMAIL files
EMAIL_COLUMNS: List[str] = ["Email", "Attachment", "Template", "Subject", "Data", "Message"]

# Output fields in SMS files
SMS_COLUMNS: List[str] = ["PhoneIndicator", "Phone", "Template", "Data", "Url", "Message"]

# Output fields in PUSH files
PUSH_COLUMNS: List[str] = ["TypeId", "UserId", "Application", "Template", "Data", "Message"]


# Functions
def get_coalesce(number_rows: int) -> int:
    """Gets the number of partitions per file"""
    if number_rows <= CSV_ROWS_LIMIT:
        return 1
    return round(number_rows / CSV_ROWS_LIMIT)


def get_processed_file_path(is_error: bool = False) -> str:
    """Gets the path to the processed file"""
    processed_file_path: str = source_massive_file_path.removesuffix(".csv")
    if processed_file_path[0] == "/":
        processed_file_path = processed_file_path.removeprefix("/")

    if is_error == False:
        return processed_file_path

    path_list = processed_file_path.split("/")
    file_name = path_list[-1]
    path_list.pop()
    return f"{'/'.join(path_list)}/error/{file_name}"


def get_provider_id(channel: str) -> str | None:
    provider_found = list(filter(lambda p: p.get("channelType") == channel, json.loads(provider)))
    return provider_found[0].get("idProvider") if provider_found else None


def add_remitter_value(df: DataFrame) -> DataFrame:
    remitter_df = glueContext.create_dynamic_frame.from_catalog(
        database=GLUE_DATABASE, table_name="alertdcd_schalerd_remitter"
    )

    remitter_df = remitter_df.filter(lambda x: x.state == "Activo" and x.id == remitter_id).select_fields(["mail"])

    if remitter_df.count() == 0:
        print("REMITTER NOT FOUND")
        return df

    return df.withColumn("Subject", lit(remitter_df.toDF().first()["mail"]))


def write_df(df: DataFrame, channel: str, is_df_error=False) -> None:
    """Writes the DataFrame to an S3 bucket in CSV file format"""
    if df.count() > 0:
        file_path = f"s3://{BUCKET_TARGET}/{channel.lower()}/{get_processed_file_path()}"
        if is_df_error:
            file_path = f"s3://{BUCKET_SOURCE}/{get_processed_file_path(is_error=True)}"

        df.coalesce(1).write.options(header=True, delimiter=";", quote="").mode("append").csv(file_path)


def add_header_row(df: DataFrame, channel: str) -> DataFrame:
    """Agregar fila con datos del consumidor y proveedor"""
    if df.count() == 0:
        return df

    df = df.drop("Error")
    header_row = spark.createDataFrame([(None,) * 13], df.schema)

    if channel == CHANNEL_EMAIL:
        header_row = (
            header_row.withColumn("Email", lit(consumer_id))
            .withColumn("Attachment", lit(get_provider_id(CHANNEL_EMAIL)))
            .withColumn("Template", lit(priority))
        )
        return add_remitter_value(header_row).union(df).select(EMAIL_COLUMNS)

    elif channel == CHANNEL_SMS:
        return (
            header_row.withColumn("PhoneIndicator", lit(consumer_id))
            .withColumn("Phone", lit(get_provider_id(CHANNEL_SMS)))
            .withColumn("Template", lit(priority))
            .union(df)
            .select(SMS_COLUMNS)
        )
    else:
        return (
            header_row.withColumn("TypeId", lit(consumer_id))
            .withColumn("UserId", lit(get_provider_id(CHANNEL_PUSH)))
            .withColumn("Application", lit(priority))
            .union(df)
            .select(PUSH_COLUMNS)
        )


@udf(returnType=StringType())
def get_phone_indicator(phone: str) -> str | None:
    return phone if phone is None else phone.split(")")[0].removeprefix("(")


@udf(returnType=StringType())
def get_phone(phone: str) -> str | None:
    return phone if phone is None else phone.split(")")[1]


def check_errors(df: DataFrame) -> DataFrame:
    """
    Assign the error message if the email or phone does not exist in the DB,
    or the user is not subscribed to push
    """
    df_new = df.withColumn(
        "Error",
        when(
            (df.ChannelType == CHANNEL_EMAIL) & (df.UserIdNotFound.isNull()) & (df.id_contact_medium.isNull()),
            EMAIL_MSG_ERR,
        ).otherwise(df.Error),
    )

    df_new = df_new.withColumn(
        "Error",
        when(
            (df_new.ChannelType == CHANNEL_SMS)
            & (df_new.UserIdNotFound.isNull())
            & (df_new.id_contact_medium.isNull()),
            SMS_MSG_ERR,
        ).otherwise(df_new.Error),
    )

    return df_new.withColumn(
        "Error",
        when(
            (df_new.ChannelType == CHANNEL_PUSH)
            & (df_new.UserIdNotFound.isNull())
            & (df_new.id_contact_medium.isNull()),
            PUSH_MSG_ERR,
        ).otherwise(df_new.Error),
    )


def complete_data(df: DataFrame) -> DataFrame:
    """Complete the data of email, cell phone indicator and cell phone number"""
    df_new = df.withColumn(
        "Email",
        when(
            (df.ChannelType == CHANNEL_EMAIL) & (df.Email.isNull()) & (df.id_contact_medium == CONTACT_MEDIUM_EMAIL),
            df.value,
        ).otherwise(df.Email),
    )

    df_new = df_new.withColumn(
        "Phone",
        when(
            (df_new.ChannelType == CHANNEL_SMS)
            & (df_new.Phone.isNull())
            & (df_new.id_contact_medium == CONTACT_MEDIUM_SMS),
            get_phone(df_new.value),
        ).otherwise(df_new.Phone),
    )

    df_new = df_new.withColumn(
        "PhoneIndicator",
        when(
            (df_new.ChannelType == CHANNEL_SMS)
            & (df_new.PhoneIndicator.isNull())
            & (df_new.id_contact_medium == CONTACT_MEDIUM_SMS),
            get_phone_indicator(df_new.value),
        ).otherwise(df_new.PhoneIndicator),
    )

    return df_new.drop(*CONTACTS_COLUMNS, "contact_medium", "UserIdNotFound")


# Read CSV file with data to be processed
massive_df = spark.read.options(header=True, delimiter=";").csv(f"s3://{BUCKET_SOURCE}/{source_massive_file_path}")
print("MASSIVE_COUNT = ", massive_df.count())

# Get Dataframes by channel type
email_df = massive_df.filter(col("ChannelType") == CHANNEL_EMAIL)
sms_df = massive_df.filter(col("ChannelType") == CHANNEL_SMS)
push_df = massive_df.filter(col("ChannelType") == CHANNEL_PUSH)

# Check if data enrichment is necessary
if data_enrichment == "true":

    contacts_dyf = glueContext.create_dynamic_frame.from_catalog(
        database=GLUE_DATABASE, table_name="alertdcd_schalerd_contact"
    )

    # Get current contact data
    contacts_dyf = contacts_dyf.filter(lambda x: x.id_state == 1 and x.previous is False).select_fields(
        CONTACTS_COLUMNS
    )

    # Convert columns to string and DynamicFrame type to Apache DataFrame
    contacts_df = contacts_dyf.apply_mapping(
        [
            ("document_number", "long", "document_number", "string"),
            ("id_contact_medium", "short", "id_contact_medium", "string"),
            ("value", "string", "value", "string"),
        ]
    ).toDF()

    # Select document numbers not found in DB
    user_id_not_found_df = (
        massive_df.join(contacts_df, massive_df.UserId == contacts_df.document_number, "leftanti")
        .select(col("UserId").alias("UserIdNotFound"))
        .distinct()
    )

    # Join DataFrames to identify document numbers not found in the DB
    massive_df = massive_df.join(user_id_not_found_df, massive_df.UserId == user_id_not_found_df.UserIdNotFound, "full")

    # Add column with channel type ID
    massive_df = massive_df.withColumn(
        "contact_medium",
        when(massive_df.ChannelType == CHANNEL_EMAIL, CONTACT_MEDIUM_EMAIL)
        .when(massive_df.ChannelType == CHANNEL_SMS, CONTACT_MEDIUM_SMS)
        .otherwise(CONTACT_MEDIUM_PUSH),
    )

    # Join DataFrame to be processed with contact data
    massive_df = massive_df.join(
        contacts_df,
        (massive_df.UserId == contacts_df.document_number)
        & (massive_df.contact_medium == contacts_df.id_contact_medium),
        "full",
    ).dropna(subset="ChannelType")

    # Assign error message if UserID does not exist in the DB
    massive_df = massive_df.withColumn("Error", lit(None).cast(StringType()))
    massive_df = massive_df.withColumn(
        "Error", when(massive_df.UserId == massive_df.UserIdNotFound, USER_ID_MSG_ERR).otherwise(massive_df.Error)
    )

    # Check erros and complete data
    massive_df = check_errors(massive_df)
    massive_df = complete_data(massive_df)

    # Update Dataframes by channel type
    email_df = massive_df.filter((col("ChannelType") == CHANNEL_EMAIL) & col("Error").isNull())

    sms_df = massive_df.filter((col("ChannelType") == CHANNEL_SMS) & col("Error").isNull())

    push_df = massive_df.filter((col("ChannelType") == CHANNEL_PUSH) & col("Error").isNull())

    # Get error logs
    massive_error_df = massive_df.filter(col("Error").isNotNull())

    print("MASSIVE_ERROR_COUNT = ", massive_error_df.count())
    print("* EMAIL_ERROR_COUNT = ", massive_error_df.filter(col("ChannelType") == CHANNEL_EMAIL).count())
    print("* SMS_ERROR_COUNT = ", massive_error_df.filter(col("ChannelType") == CHANNEL_SMS).count())
    print("* PUSH_ERROR_COUNT = ", massive_error_df.filter(col("ChannelType") == CHANNEL_PUSH).count())

    # Write error logs
    write_df(massive_error_df, "", True)

# Add row as header
email_df = add_header_row(email_df, CHANNEL_EMAIL)
sms_df = add_header_row(sms_df, CHANNEL_SMS)
push_df = add_header_row(push_df, CHANNEL_PUSH)

print("EMAIL_COUNT = ", email_df.count() - 1)
print("SMS_COUNT = ", sms_df.count() - 1)
print("PUSH_COUNT = ", push_df.count() - 1)

# Write CSV separated by channel type to S3
write_df(email_df, CHANNEL_EMAIL)
write_df(sms_df, CHANNEL_SMS)
write_df(push_df, CHANNEL_PUSH)

# Finalizar Job
job.commit()
