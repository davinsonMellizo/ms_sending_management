"""
Script to transform the data to the SMS format of the provider Masivian.
"""

import sys

from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from pyspark.sql.functions import concat, regexp_replace

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
BUCKET_TARGET: str = f"nu0154001-alertas-{env}-glue-processed-masiv"

# TODO: revisar la ruta de acuerdo a la prioridad
# Functions
def get_processed_file_path() -> str:
    """Gets the path to the processed file"""
    processed_file_path = source_file_path
    if source_file_path[0] == "/":
        processed_file_path = source_file_path.removeprefix("/")

    path_list = processed_file_path.split("/")
    path_list.pop()
    return "/".join(path_list)


sms_df = spark.read.options(header=True, delimiter=";").csv(f"s3://{BUCKET_SOURCE}/{source_file_path}")

logger.info(f"SMS_COUNT: {sms_df.count() - 1}")

first_row = sms_df.first()

# Replace the + sign of the phone indicator
sms_df = sms_df.filter(~sms_df.PhoneIndicator.startswith("|")).withColumn(
    "PhoneIndicator", regexp_replace("PhoneIndicator", "\+", "")
)

# Concatenate the indicator and the phone number
sms_df = sms_df.select(
    concat(sms_df.PhoneIndicator, sms_df.Phone).alias("numero"), "Template", "Data", "Url", "Message"
)

# Select the fields with the provider's format
sms_df = sms_df.withColumnRenamed("Message", "mensaje").select("numero", "mensaje")

# Write CSV separated by channel type to S3
# TODO: Validar si el formato para SMS es TXT y se usa cualquier separador
file_path = f"s3://{BUCKET_TARGET}/sms"
sms_df.coalesce(1).write.options(header=True, delimiter=";", quote="").mode("append").csv(file_path)
