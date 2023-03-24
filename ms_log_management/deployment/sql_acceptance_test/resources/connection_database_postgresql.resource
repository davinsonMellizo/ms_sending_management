*** Settings ***
Library     DatabaseLibrary
Library     OperatingSystem
<<<<<<< HEAD
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
=======
Library    secret.py

*** Variables ***
${secretName}    %{secret_name}
*** Keywords ***
Setup
    ${config}=    get_secret    ${secretName}
    ${DBName}=        Set Variable    ${config['dbname']}
    ${DBHost}=        Set Variable    ${config['host']}
    ${DBPort}=        Set Variable    ${config['port']}
    ${DBUser}=        Set Variable    ${config['username']}
    ${DBPass}=        Set Variable    ${config['password']}
>>>>>>> 0cd808ca2c4a0e1b4c9ce864422697b520bb1690
    ${output}       ${output}   Connect To Database     psycopg2     ${DBName}       ${DBUser}       ${DBPass}       ${DBHost}       ${DBPort}
    should be equal as strings   ${output}    None
    [Return]    ${output}