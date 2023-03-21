"""
Script to transform the data to the EMAIL format of the Masivian provider
"""

import sys

from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from pyspark.sql import DataFrame
from pyspark.sql.functions import col, explode, from_json, lit, map_keys, regexp_replace
from pyspark.sql.types import MapType, StringType

# Glue Context
args = getResolvedOptions(sys.argv, ["JOB_NAME", "env", "source_file_path"])

glueContext = GlueContext(SparkContext.getOrCreate())
spark = glueContext.spark_session
logger = glueContext.get_logger()
job = Job(glueContext)
job.init(args["JOB_NAME"], args)

# Job parameters
env: str = args["env"]
source_file_path: str = args["source_file_path"]

# Buckets
BUCKET_SOURCE: str = f"nu0154001-alertas-{env}-glue-processed-data"
BUCKET_TARGET: str = f"nu0154001-alertas-{env}-processed-masiv"

# Template type
TEMPLATE_TYPE = "masiv-template/html"


# Functions
def get_processed_file_path() -> str:
    """Gets the path to the processed file"""
    processed_file_path = source_file_path
    if source_file_path[0] == "/":
        processed_file_path = source_file_path.removeprefix("/")

    path_list = processed_file_path.split("/")
    path_list.pop()
    return "/".join(path_list)


def write_df(df: DataFrame) -> None:
    """Writes the DataFrame to an S3 bucket in CSV file format"""
    file_path = f"s3://{BUCKET_TARGET}/{get_processed_file_path()}"
    df = df.drop("Attachment", "Message")
    df.coalesce(1).write.options(header=True, delimiter=";", quote="").mode("append").csv(file_path)


# Read file to process
email_df = spark.read.options(header=True, delimiter=";").csv(f"s3://{BUCKET_SOURCE}/{source_file_path}")
logger.info(f"EMAIL_COUNT: {email_df.count() - 1}")

first_row = email_df.first()

# Rename columns
email_df = (
    email_df.filter(~email_df.Email.startswith("|"))
    .withColumnRenamed("Email", "correo")
    .withColumnRenamed("Template", "plantilla")
    .withColumnRenamed("Subject", "Asunto")
)

# Add remitter and template type columns
email_df = email_df.withColumn("remitente", lit(first_row["Subject"])).withColumn("tipo", lit(TEMPLATE_TYPE))

# Convert Data column from String to Map
email_df = (
    email_df.withColumn("Data", regexp_replace("Data", '"\{', "{"))
    .withColumn("Data", regexp_replace("Data", '\}"', "}"))
    .withColumn("Data", regexp_replace("Data", '""', '"'))
)

email_df = email_df.withColumn("Data", from_json(email_df.Data, MapType(StringType(), StringType())))

# Convert JSON value from Data column to columns
keys_df = email_df.select(explode(map_keys(email_df.Data))).distinct()
keys_list = sorted(keys_df.rdd.map(lambda x: x[0]).collect())
key_cols = list(map(lambda x: col("Data").getItem(x).alias(str(x)), keys_list))
email_df = email_df.select(
    "correo", "remitente", "Asunto", "tipo", "plantilla", *key_cols, "Attachment", "Url", "Message"
)

write_df(email_df)

# Finish Job
job.commit()
