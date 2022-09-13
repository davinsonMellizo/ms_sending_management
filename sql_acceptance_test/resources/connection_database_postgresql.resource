*** Settings ***
Library     DatabaseLibrary
Library     OperatingSystem

*** Variables ***
${DBName}       %{db}
${DBHost}       %{host}
${DBPort}       %{port}
${DBUser}       %{user}
${DBPass}       %{password}

*** Keywords ***
Setup  
    ${output}       ${output}   Connect To Database     psycopg2     ${DBName}       ${DBUser}       ${DBPass}       ${DBHost}       ${DBPort}
    should be equal as strings   ${output}    None
    [Return]    ${output}