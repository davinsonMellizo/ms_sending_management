
import boto3
import json
from botocore.exceptions import ClientError

<<<<<<< HEAD
def get_secret(secretName, arnRole):
    secret_name = secretName
    region_name = "us-east-1"

    sts_client = boto3.client('sts')
    assumed_role_object=sts_client.assume_role(
        RoleArn=arnRole,
        RoleSessionName="AssumeRoleSession1",
        DurationSeconds=1800
    )
    credentials=assumed_role_object['Credentials']
    session = boto3.Session(
        aws_access_key_id=credentials['AccessKeyId'],
        aws_secret_access_key=credentials['SecretAccessKey'],
        aws_session_token=credentials['SessionToken'],
        )
=======
def get_secret(secretName):
    secret_name = secretName
    region_name = "us-east-1"

>>>>>>> 0cd808ca2c4a0e1b4c9ce864422697b520bb1690
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

    secret = get_secret_value_response['SecretString']
    return json.loads(secret)
