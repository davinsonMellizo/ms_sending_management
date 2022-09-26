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
    Execute Sql String    DELETE FROM schalerd.client c where c.document_number = 123;
    ${output}   Query  select document_number from schalerd.client where document_number=123;
    log to console  ${output}
    should be equal as strings   ${output}    []

Check contact exist in DB 
    Execute Sql String  INSERT INTO schalerd.client (document_number, id_document_type, key_mdm, enrollment_origin, id_state, creation_user, created_date, modified_date) VALUES(123, 1, 'key', 'APP', 1, 'user', '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05');
    Execute Sql String  INSERT INTO schalerd.contact (document_number, id_document_type, segment, id_contact_medium, value, id_state, created_date, modified_date, previous) VALUES(123,1, 'Personas', 0, '3217937584', 1, '2021-02-16 10:10:25-05','2021-02-16 10:10:25-05', false);
    ${output}   Query  select value from schalerd.contact where document_number=123;
    log to console  ${output[0][0]}
    should be equal as strings   ${output[0][0]}    3217937584

Check client is deleted from DB 
    Execute Sql String    DELETE FROM schalerd.client c where c.document_number = 123;
    ${output}   Query  select document_number from schalerd.client where document_number=123;
    log to console  ${output}
    should be equal as strings   ${output}    []