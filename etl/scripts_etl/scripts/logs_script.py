"""
Script para subir los logs de la rds de logs de alertas hacia s3 en formato parquet
"""
import sys
from typing import List
import psycopg2
import boto3
import os
import json

from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from pyspark.sql import *
from datetime import date, timedelta
from pyspark.sql.functions import *
from botocore.exceptions import ClientError

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

#Parameters database
ENDPOINT="host"
PORT="port"
USER="username"
REGION="us-east-1"
DBNAME="dbname"
PWD = "password"

#Obtener secretos
def get_secret():

    secret_name = "alertas-logs-dev-secretrds-CNX"
    region_name = "us-east-1"
    session = boto3.session.Session()
    client = session.client(
        service_name='secretsmanager',
        region_name=region_name
    )

    try:
        get_secret_value_response = client.get_secret_value(
            SecretId=secret_name
        )
    except ClientError as e:
        raise e

    secret_string = get_secret_value_response['SecretString']
    return json.loads(secret_string) 

secret_json = get_secret()   
PWD = secret_json[PWD]
ENDPOINT=secret_json[ENDPOINT]
PORT=secret_json[PORT]
USER=secret_json[USER]
DBNAME=secret_json[DBNAME]
# End connection

# Obtener datos del Data Catalog
logs_dyf = glueContext.create_dynamic_frame.from_catalog(database=glue_database,table_name=glue_database_table)

print('Logs number dyf:', logs_dyf.count())

date_compare = date.today() - timedelta(30)
date_data = date.today() - timedelta(31)
print(date_compare)

logs_dyf = logs_dyf.filter(lambda x: x.date_creation.date() < date_compare)
print('Logs number dyf filter:', logs_dyf.count())

glueContext.write_dynamic_frame.from_options(
    frame=logs_dyf.coalesce(1),
    connection_type='s3',
    format='parquet',
    connection_options={
        'path': f's3://{bucket_destination_path}/{date_data}',
    },
   format_options={
        'useGlueParquetWriter': True
    },
)
job.commit()
# Finalizar Job

#connection to database and delete data 
session = boto3.Session()
client = session.client('rds')

try:
    conn = psycopg2.connect(host=ENDPOINT, port=PORT, database=DBNAME, user=USER, password=PWD)
    cur = conn.cursor()
    cur.execute("""delete from schalerd.log where date_creation < %s""",(date_compare,))
    conn.commit()
    cur.close()
except Exception as e:
    print("Database connection failed due to {}".format(e))   
finally:
    if conn is not None:
        conn.close()

