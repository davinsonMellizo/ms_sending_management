*** Settings ***
Library         OperatingSystem
Library         DatabaseLibrary
Resource        ../../resources/connection_database_postgresql.resource

Suite Setup         Setup
Suite Teardown      Disconnect From Database


*** Variables ***
${var}    ./resources/test_data/prepare_db_client.sql

*** Test Cases ***
Check client exist in DB 
    Execute Sql Script    ${var}
    ${output}   Query  select document_number from schalerd.client where document_number=123;
    log to console  ${output[0][0]}
    should be equal as strings   ${output[0][0]}    123

Check contact exist in DB 
    ${output}   Query  select value from schalerd.contact where document_number=123;
    log to console  ${output[0][0]}
    should be equal as strings   ${output[0][0]}    3217937584
