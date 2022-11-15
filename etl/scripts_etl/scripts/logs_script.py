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

LOGS_COLUMNS: List[str] = ['id', 'log_key' ,'document_type' ,'document_number' ,'log_type' ,'medium' ,
	'contact' ,'message_sent' ,'consumer' ,'alert_id' ,'alert_description','transaction_id','amount',	'response_code'  ,
	'response_description' ,'priority int2','provider','template','operation_id','operation_description','date_creation']

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
logs_dyf = logs_dyf.filter(
        lambda x: x.date_creation < date_sub(current_date(), 30) )

# Seleccionar campos necesarios del DynamicFrame de logs
logs_dyf = logs_dyf.select_fields(LOGS_COLUMNS)
   

# Convertir DynamicFrame a Apache DataFrame
logs_df = logs_dyf.toDF()



logs_df.write.parquet('s3://{bucket_destination_path}/date_sub(current_date(), 31)') 

# Finalizar Job
job.commit()
