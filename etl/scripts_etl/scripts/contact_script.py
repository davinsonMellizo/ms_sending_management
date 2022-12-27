"""
Script para completar los datos de contacto faltantes del usuario en el archivo CSV.
"""
import sys
from datetime import datetime
from typing import List

from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from pyspark.sql import DataFrame
from pyspark.sql.functions import col, lit, when

# Glue Context
args = getResolvedOptions(sys.argv, [
    'JOB_NAME', 'env', 'source_massive_file_path', 'processed_file_path',
    'consumer_id', 'data_enrichment'
])

glueContext = GlueContext(SparkContext())
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args['JOB_NAME'], args)

# Job parameters
env: str = args['env']
data_enrichment: str = args['data_enrichment']
source_massive_file_path: str = args['source_massive_file_path']
processed_file_path: str = args['processed_file_path']
consumer_id: str = args['consumer_id']

# Glue
GLUE_DATABASE: str = f'nu0154001-alertas-{env}-db'
GLUE_DATABASE_TABLE: str = 'alertdcd_schalerd_contact'

# Buckets
SOURCE_BUCKET: str = f'nu0154001-alertas-{env}-glue-data'
BUCKET_TARGET: str = f'nu0154001-alertas-{env}-glue-processed-data'

# Mensajes de error
USER_ID_MSG_ERR: str = 'El número de documento no se encontro en la base de datos'
EMAIL_MSG_ERR: str = 'El correo electrónico no se encontro en la base de datos'
SMS_MSG_ERR: str = 'El número de celular no se encontro en la base de datos'

# Tipo de canales
CHANNEL_SMS: str = 'SMS'
CHANNEL_EMAIL: str = 'EMAIL'
CHANNEL_PUSH: str = 'PUSH'

# ID canal de contacto
CONTACT_MEDIUM_SMS: str = '0'
CONTACT_MEDIUM_EMAIL: str = '1'
CONTACT_MEDIUM_PUSH: str = '2'

# Número de filas a escribir por archivo
CSV_ROWS_LIMIT: int = 6700

# Fecha para agrupar los archivos procesados
FOLDER_DATE = datetime.now().strftime("%Y-%m-%d-%H-%M-%S")

# Columnas a seleccionar del DynamicFrame de contactos
CONTACTS_COLUMNS: List[str] = ['document_number', 'id_contact_medium', 'value']


# Funciones
def get_coalesce(number_rows: int) -> int:
    """Obtiene el número de particiones por archivo"""
    if number_rows <= CSV_ROWS_LIMIT:
        return 1
    return round(number_rows / CSV_ROWS_LIMIT)


def write_df(dataframe: DataFrame, channel_type: str) -> None:
    """Escribe el DataFrame en un bucket de S3 en formato de archivo CSV"""
    rows = dataframe.count()
    if rows > 0:
        dataframe.drop('ChannelType')\
            .coalesce(get_coalesce(rows))\
            .write \
            .options(header=True, delimiter=';', quote='') \
            .mode('append') \
            .csv(f's3://{BUCKET_TARGET}/{channel_type}/{processed_file_path}/{FOLDER_DATE}')


# Leer archivo CSV con los datos a procesar
massive_df = spark.read \
    .options(header=True, delimiter=';') \
    .csv(f's3://{SOURCE_BUCKET}/{source_massive_file_path}')

# Agregar ID del consumidor
massive_df = massive_df.withColumn('ConsumerId', lit(consumer_id))

print('MASSIVE_DF_COUNT:', massive_df.count())

# Verificar si es necesario enriquecer los datos
if data_enrichment == 'true':

    # Obtener datos del Data Catalog
    contacts_dyf = glueContext.create_dynamic_frame.from_catalog(
        database=GLUE_DATABASE,
        table_name=GLUE_DATABASE_TABLE
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


# Obtener Dataframes por tipo de canal
email_df = massive_df.filter(col('ChannelType') == CHANNEL_EMAIL)
sms_df = massive_df.filter(col('ChannelType') == CHANNEL_SMS)
push_df = massive_df.filter(col('ChannelType') == CHANNEL_PUSH)

# Escribir DataFrame en bucket de S3 en formato de archivo CSV separados por tipo de canal
write_df(email_df, CHANNEL_EMAIL.lower())
write_df(sms_df, CHANNEL_SMS.lower())
write_df(push_df, CHANNEL_PUSH.lower())

# Finalizar Job
job.commit()
