*** Settings ***
Library     DatabaseLibrary
Library     OperatingSystem
#Library    secret.py

*** Variables ***
#${secretName}    %{secret-name}
#${arnRole}    %{arn-role}

${DBName}    %{db-name}
${DBHost}    %{host}
${DBPort}    %{port}
${DBUser}    %{user}
${DBPass}    %{password}

*** Keywords ***
Setup
    #${config}=    get_secret    ${secretName}    ${arnRole}
    #${DBName}=        Set Variable    ${config['dbname']}
    #${DBHost}=        Set Variable    ${config['host']}
    #${DBPort}=        Set Variable    ${config['port']}
    #${DBUser}=        Set Variable    ${config['username']}
    #${DBPass}=        Set Variable    ${config['password']}
    ${output}       ${output}   Connect To Database     psycopg2     ${DBName}       ${DBUser}       ${DBPass}       ${DBHost}       ${DBPort}
    should be equal as strings   ${output}    None
    [Return]    ${output}