*** Settings ***
Library         OperatingSystem
Library         DatabaseLibrary
Resource        ../../../resources/connection_database_postgresql.resource

Suite Setup         Setup
Suite Teardown      Disconnect From Database


*** Variables ***
${var}    ./resources/test_data/prepare_db_consumer.sql

*** Test Cases ***
Check number logs DB
    ${output}   Query  select count(*) from schalerd.log where date_creation < (CURRENT_DATE - interval '30 day');
    log to console  ${output[0][0]}
    should be equal as strings   ${output[0][0]}    0
