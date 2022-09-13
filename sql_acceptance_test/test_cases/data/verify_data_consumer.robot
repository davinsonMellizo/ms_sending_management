*** Settings ***
Library         OperatingSystem
Library         DatabaseLibrary
Resource        ../../resources/connection_database_postgresql.resource

Suite Setup         Setup
Suite Teardown      Disconnect From Database


*** Variables ***
${var}    ./resources/test_data/prepare_db_consumer.sql

*** Test Cases ***
Check consumer exist in DB
    Execute Sql Script    ${var}
    ${output}   Query  select segment from schalerd.consumer where id='ALE';
    log to console  ${output[0][0]}
    should be equal as strings   ${output[0][0]}    Personas