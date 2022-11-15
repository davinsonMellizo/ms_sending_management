"""
Script para subir los logs de la rds de logs de alertas hacia s3 en formato parquet
"""
import sys
from typing import List

from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from pyspark.sql import *
from datetime import datetime


# Glue Context
args = getResolvedOptions(sys.argv, ['JOB_NAME', 'glue_database', 'glue_database_table', 'bucket_destination_path'])

glueContext = GlueContext(SparkContext())
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args['JOB_NAME'], args)

# Job parameters
glue_database = args['glue_database']
glue_database_table = args['glue_database_table']
bucket_destination_path = args['bucket_destination_path']

# Obtener datos del Data Catalog
logs_dyf = glueContext.create_dynamic_frame.from_catalog(database=glue_database,table_name=glue_database_table)

print('Logs number dyf:', logs_dyf.count())

# filtrar los logs ateriores a 30 días
logs_dyf = logs_dyf.filter(logs_dyf["date_creation"] < date_sub(current_date(), 30))
   
  
# Convertir DynamicFrame a Apache DataFrame
logs_df = logs_dyf.toDF()



logs_df.write.parquet('s3://{bucket_destination_path}/date_sub(current_date(), 31)') 

# Finalizar Job
job.commit()
