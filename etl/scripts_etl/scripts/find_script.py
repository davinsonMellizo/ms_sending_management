from ast import arg
import sys
import boto3
from datetime import datetime, timedelta
from awsglue.transforms import *
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from awsglue.context import GlueContext
from awsglue.job import Job
from pyspark.sql.functions import col
from awsglue.dynamicframe import DynamicFrame

args = getResolvedOptions(sys.argv, ['JOB_NAME', "bucket_source_path", "bucket_destination_path", "document_type", 
                                     "document_number", "start_date", "end_date", "contact", "consumer", "provider"])

sc = SparkContext()
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args["JOB_NAME"], args)
hadoopConf = spark.sparkContext._jsc.hadoopConfiguration()
# Buckets
bucketDestinationPath = args["bucket_destination_path"]
bucketSourcePath = args["bucket_source_path"].split("/")[0]

# Filters

documentType = args["document_type"]
documentNumber = args["document_number"]
startDateTime = args["start_date"]
endDateTime = args["end_date"]
contact = args["contact"]
consumer = args["consumer"]
provider = args["provider"]

# configurations parameters

formatDate = "%Y-%m-%dT%H:%M:%S.%f"
sourcePathRegex = "test/{date}/"
sourcePaths =[]
s3BucketSourcePath = "s3://"+bucketSourcePath+"/"
client = boto3.client('s3')

def validateField(stringValue):
    isValid = True
    if stringValue == None:
        isValid = False    
    else:
        value = stringValue.strip()
        if value == "":
            isValid = False
    return  isValid

def dynamicFilter(outFrame):
    # Default values for columns
    documentNumberCondition = (col("document_number") == col("document_number"))
    documentTypeCondition = (col("document_type") == col("document_type"))
    contactCondition = (col("contact") == col("contact"))
    consumerCondition = (col("consumer") == col("consumer"))
    providerCondition = (col("provider") == col("provider"))
    if(outFrame.count() > 0):
        if(validateField(documentNumber)):
            documentNumberCondition = (col("document_number") == documentNumber)
        if(validateField(documentType)):
            documentTypeCondition = (col("document_type") == documentType)
        if(validateField(contact)):
            contactCondition = (col("contact") == contact)
        if(validateField(consumer)):
            consumerCondition = (col("consumer") == consumer)
        if(validateField(provider)):
            providerCondition = (col("provider") == provider)
        outFrame = outFrame.filter(documentNumberCondition
                                & documentTypeCondition
                                & contactCondition
                                & consumerCondition
                                & providerCondition)
    return outFrame

def existsAnyPath(paths):
    existsAny = False
    for path in paths:
        existsAny = existsOriginPath(path)
        if existsAny:
            return True    
    return existsAny


def existsOriginPath(path):
    path = path.replace(s3BucketSourcePath,'')
    originsPaths = client.list_objects(
        Bucket=bucketSourcePath, Delimiter='/', Prefix=path)
    if originsPaths.get('Contents') is None:
        print("contents is none by :" +s3BucketSourcePath+path)
        return False
    else:
        existsFiles = False
        for idx, val in enumerate(originsPaths.get('Contents')):
            if val.get("StorageClass") == "STANDARD":
                print("existsFiles for :"+path)
                existsFiles = True
        return existsFiles

def sourcePathBuild():
    start_date = datetime.strptime(startDateTime, formatDate).date()
    end_date = datetime.strptime(endDateTime, formatDate).date()
    delta = timedelta(days=1)
    while start_date <= end_date:
        sourcePath = sourcePathRegex.format(date= start_date)
        sourcePaths.append(s3BucketSourcePath+sourcePath)
        start_date += delta
    print(sourcePaths)

def timeFilter(outFrame):
    if(outFrame.count() > 0):
        outFrame = outFrame.filter(lambda x: x.date_creation >= datetime.strptime(startDateTime,formatDate) and x.date_creation <= datetime.strptime(endDateTime,formatDate))
    return outFrame

def createDynamicFrame():
    sourcePathBuild()
    if existsAnyPath(sourcePaths) == True:  
        S3bucket_node1 = glueContext.create_dynamic_frame.from_options(
            connection_type="s3",
            format="parquet",
            connection_options={
                "paths": sourcePaths,
                "recurse": True,
            },
            additional_options={
                "excludeStorageClasses": ["GLACIER", "DEEP_ARCHIVE", "GLACIER_IR"]
            },
            transformation_ctx="S3bucket_node1",
        )
        
        return S3bucket_node1
    

df = createDynamicFrame()
df.show()
if df != None:
    print("ini")
    print(df.count())
    df = timeFilter(df)
    print("pre")
    print(df.count())
    print("pos")
    df = df.toDF()
    df = dynamicFilter(df)
    print("pasa del filtro", bucketDestinationPath)
    print(df.count())
    df.coalesce(1).write.mode("overwrite").csv(f's3://{bucketDestinationPath}/test')

job.commit()
print("Proceso Terminado.")