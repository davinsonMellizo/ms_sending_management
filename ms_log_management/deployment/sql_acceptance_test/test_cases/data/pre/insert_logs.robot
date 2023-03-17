*** Settings ***
Library         OperatingSystem
Library         DatabaseLibrary
Resource        ../../../resources/connection_database_postgresql.resource

Suite Setup         Setup
Suite Teardown      Disconnect From Database


*** Variables ***
${var}    ./resources/test_data/prepare_db_client.sql

*** Test Cases ***

Check insert data logs DB
    Execute Sql String  INSERT INTO schalerd.log (log_key, document_type, document_number, contact, date_creation) VALUES('123', '0', 2000000000, '3215058449', '2022-03-05 10:10:25-05');
    Execute Sql String  INSERT INTO schalerd.log (log_key, document_type, document_number, contact, date_creation) VALUES('123', '0', 2000000000, '3215058449', '2022-03-05 10:10:25-05');
    ${output}   Query  select count(*) from schalerd.log;
    log to console  ${output[0][0]}
    should be True   ${output[0][0]} > 1

