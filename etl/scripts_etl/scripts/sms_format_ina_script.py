"""
Script to transform the data to the SMS format of the Inalambria provider
"""

import sys
from datetime import datetime

import numpy as np
from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from pyspark.sql import DataFrame, SparkSession
from pyspark.sql.functions import concat, regexp_replace
from s3fs import S3FileSystem

spark = SparkSession.builder.appName("SparkByExamples").getOrCreate()

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
logger.info(f"FILE_PATH = {source_file_path}")

# Buckets
BUCKET_SOURCE: str = f"nu0154001-alertas-{env}-glue-processed-data"
BUCKET_TARGET: str = f"nu0154001-alertas-{env}-processed-masiv"


# TODO: revisar la ruta de acuerdo a la prioridad
# Functions
def get_processed_file_path() -> str:
    """Gets the path to the processed file"""
    date_format: str = datetime.now().strftime("%Y%m%d%H:%M:%S")
    return date_format


def write_df(df: DataFrame) -> None:
    """Writes the DataFrame to an S3 bucket in CSV file format"""
    file_path = f"s3://{BUCKET_TARGET}/hola.txt"
    df = df.drop("Attachment", "Message")
    sms_pd = df.toPandas()
    s3 = S3FileSystem()
    with s3.open(file_path, "w") as f:
        np.savetxt(
            f,
            sms_pd.values,
            delimiter="\t",
            comments="",
            header="\t".join(sms_pd.columns),
            fmt="%s",
        )


# Read file to process
sms_df = spark.read.options(header=True, delimiter=";").csv(f"s3://{BUCKET_SOURCE}/{source_file_path}")
logger.info(f"SMS_COUNT: {sms_df.count() - 1}")

first_row = sms_df.first()

# Replace the + sign of the phone indicator
sms_df = sms_df.filter(~sms_df.PhoneIndicator.startswith("|")).withColumn(
    "PhoneIndicator", regexp_replace("PhoneIndicator", "\+", "")
)

# Concatenate the indicator and the phone number
sms_df = sms_df.select(
    concat(sms_df.PhoneIndicator, sms_df.Phone).alias("numero"),
    "Template",
    "Data",
    "Url",
    "Message",
)

# Select the fields with the provider's format
sms_df = sms_df.withColumnRenamed("Message", "mensaje").select("numero", "mensaje")

# Write CSV separated by channel type to S3
# TODO: Validar si el formato para SMS es TXT y se usa cualquier separador
write_df(sms_df)

# Finish Job
job.commit()
