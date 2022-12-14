"""
Script para completar los datos de contacto faltantes del usuario en el archivo CSV.
"""
import sys
from typing import List

from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from pyspark.sql.functions import col, lit, when


# Mensajes de error
USER_ID_MSG_ERR: str = 'El número de documento no se encontro en la base de datos'
EMAIL_MSG_ERR: str = 'El correo electrónico no se encontro en la base de datos'
SMS_MSG_ERR: str = 'El número de celular no se encontro en la base de datos'

# Channels type
CHANNEL_SMS: str = 'SMS'
CHANNEL_EMAIL: str = 'EMAIL'
CHANNEL_PUSH: str = 'PUSH'

# ID medio de contacto
CONTACT_MEDIUM_SMS: str = '0'
CONTACT_MEDIUM_EMAIL: str = '1'
CONTACT_MEDIUM_PUSH: str = '2'

# Columnas a seleccionar del DynamicFrame de contactos
CONTACTS_COLUMNS: List[str] = ['document_number', 'id_contact_medium', 'value']


# Glue Context
args = getResolvedOptions(sys.argv, [
    'JOB_NAME', 'glue_database', 'glue_database_table', 'data_enrichment',
    'source_massive_file_path', 'bucket_destination_path', 'consumer_id'
])

glueContext = GlueContext(SparkContext())
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args['JOB_NAME'], args)

# Job parameters
glue_database = args['glue_database']
glue_database_table = args['glue_database_table']
data_enrichment = args['data_enrichment']
source_massive_file_path = args['source_massive_file_path']
bucket_destination_path = args['bucket_destination_path']
consumer_id = args['consumer_id']


# Leer archivo CSV con los datos a procesar
massive_df = spark.read \
    .options(header=True, delimiter=';') \
    .csv(f's3://{source_massive_file_path}')

# Agregar ID del consumidor
massive_df = massive_df.withColumn('ConsumerId', lit(consumer_id))

print('MASSIVE_DF_COUNT:', massive_df.count())

# Verificar si es necesario enriquecer los datos
if data_enrichment == 'true':

    # Obtener datos del Data Catalog
    contacts_dyf = glueContext.create_dynamic_frame.from_catalog(
        database=glue_database,
        table_name=glue_database_table
    )

    print('CONTACTS_DYF_COUNT:', contacts_dyf.count())

    # Obtener los datos actuales de contactos
    contacts_dyf = contacts_dyf.filter(
        lambda x: x.id_state == 1 and x.previous is False
    )

    # Seleccionar campos necesarios del DynamicFrame de contactos
    contacts_dyf = contacts_dyf.select_fields(CONTACTS_COLUMNS)

    # Convertir las columnas a tipo string
    contacts_dyf = contacts_dyf.apply_mapping([
        ('document_number', 'long', 'document_number', 'string'),
        ('id_contact_medium', 'short', 'id_contact_medium', 'string'),
        ('value', 'string', 'value', 'string')
    ])

    print('EMAIL_AND_SMS_CONTACTS_DYF_COUNT', contacts_dyf.count())

    # Convertir DynamicFrame a Apache DataFrame
    contacts_df = contacts_dyf.toDF()

    # Seleccionar los números de documento no encontrados en la DB
    user_id_not_found_df = massive_df.join(
        contacts_df, massive_df.UserId == contacts_df.document_number, 'leftanti'
    ).select(col('UserId').alias('UserIdNotFound')).distinct()

    # Unir DataFrames para identificar los números de documento no encontrados en la DB
    massive_df = massive_df.join(
        user_id_not_found_df, massive_df.UserId == user_id_not_found_df.UserIdNotFound, 'full'
    )

    # Agregar columna con el ID del tipo de canal
    massive_df = massive_df.withColumn('contact_medium',
                                       when(massive_df.ChannelType ==
                                            CHANNEL_EMAIL, CONTACT_MEDIUM_EMAIL)
                                       .when(massive_df.ChannelType == CHANNEL_SMS, CONTACT_MEDIUM_SMS)
                                       .otherwise(CONTACT_MEDIUM_PUSH))

    # Unir DataFrame a procesar con los datos de contacto
    massive_df = massive_df.join(
        contacts_df, (massive_df.UserId == contacts_df.document_number) &
        (massive_df.contact_medium == contacts_df.id_contact_medium), 'full'
    )

    # Eliminar las filas donde todos los valores sean null
    massive_df = massive_df.dropna(subset='ChannelType')

    # Completar el indicador del número de celular
    massive_df = massive_df.fillna({'PhoneIndicator': '+57'})

    # Asignar mensaje de error si el UserID no existe en la DB
    massive_df = massive_df.withColumn('Error',
                                       when(massive_df.UserId ==
                                            massive_df.UserIdNotFound, USER_ID_MSG_ERR)
                                       .otherwise(massive_df.Error))

    # Asignar mensaje de error si el email no existe en la DB
    massive_df = massive_df.withColumn('Error',
                                       when((massive_df.ChannelType == CHANNEL_EMAIL) &
                                            (massive_df.UserIdNotFound.isNull()) &
                                            (massive_df.id_contact_medium.isNull()), EMAIL_MSG_ERR)
                                       .otherwise(massive_df.Error))

    # Asignar mensaje de error si el número de celular no existe en la DB
    massive_df = massive_df.withColumn('Error',
                                       when((massive_df.ChannelType == CHANNEL_SMS) &
                                            (massive_df.UserIdNotFound.isNull()) &
                                            (massive_df.id_contact_medium.isNull()), SMS_MSG_ERR)
                                       .otherwise(massive_df.Error))

    # Asignar el email
    massive_df = massive_df.withColumn('Email',
                                       when((massive_df.ChannelType == CHANNEL_EMAIL) &
                                            (massive_df.id_contact_medium == CONTACT_MEDIUM_EMAIL), massive_df.value)
                                       .otherwise(massive_df.Email))

    # Asignar el número de celular
    massive_df = massive_df.withColumn('Phone',
                                       when((massive_df.ChannelType == CHANNEL_SMS) &
                                            (massive_df.id_contact_medium == CONTACT_MEDIUM_SMS), massive_df.value)
                                       .otherwise(massive_df.Phone))

    # Invertir el ID del tipo de canal
    massive_df = massive_df.withColumn('contact_medium',
                                       when(massive_df.ChannelType ==
                                            CHANNEL_EMAIL, CONTACT_MEDIUM_SMS)
                                       .otherwise(CONTACT_MEDIUM_EMAIL))

    # Seleccionar campos del DataFrame de contacto
    contacts_df = massive_df.select(*CONTACTS_COLUMNS).dropna()

    # Eliminar columnas de contacto
    massive_df = massive_df.drop(*CONTACTS_COLUMNS)

    # Unir DataFrame a procesar con los datos de contacto
    massive_df = massive_df.join(
        contacts_df, (massive_df.UserId == contacts_df.document_number) &
        (massive_df.contact_medium == contacts_df.id_contact_medium), 'full'
    )

    # Si el canal es EMAIL asignar el número de celular
    massive_df = massive_df.withColumn('Phone',
                                       when((massive_df.ChannelType == CHANNEL_EMAIL) &
                                            (massive_df.id_contact_medium == CONTACT_MEDIUM_SMS), massive_df.value)
                                       .otherwise(massive_df.Phone))

    # Si el canal es SMS o PUSH asignar el email
    massive_df = massive_df.withColumn('Email',
                                       when(((massive_df.ChannelType == CHANNEL_SMS) |
                                             (massive_df.ChannelType == CHANNEL_PUSH))
                                            & (massive_df.id_contact_medium == CONTACT_MEDIUM_EMAIL),
                                            massive_df.value)
                                       .otherwise(massive_df.Email))

    # Invertir el ID del tipo de canal PUSH
    massive_df = massive_df.withColumn('contact_medium',
                                       when(massive_df.ChannelType == CHANNEL_PUSH, CONTACT_MEDIUM_SMS))

    # Eliminar columnas del DataFrame de contacto
    massive_df = massive_df.drop(*CONTACTS_COLUMNS)

    # Unir DataFrame a procesar con los datos de contacto
    massive_df = massive_df.join(
        contacts_df, (massive_df.UserId == contacts_df.document_number) &
        (massive_df.contact_medium == contacts_df.id_contact_medium), 'full'
    ).dropna(subset='ChannelType')

    # Si el canal es PUSH asignar el número de celular
    massive_df = massive_df.withColumn('Phone',
                                       when((massive_df.ChannelType == CHANNEL_PUSH)
                                            & (massive_df.id_contact_medium == CONTACT_MEDIUM_SMS),
                                            massive_df.value)
                                       .otherwise(massive_df.Phone))

    massive_df = massive_df.drop(
        *CONTACTS_COLUMNS, 'contact_medium', 'UserIdNotFound'
    )

# Escribir DataFrame en bucket de S3 en formato de archivo CSV separados por tipo de canal
massive_df.coalesce(1).write \
    .options(header=True, delimiter=';', quote='') \
    .partitionBy('ChannelType') \
    .mode('append') \
    .csv(f's3://{bucket_destination_path}')

# Finalizar Job
job.commit()
