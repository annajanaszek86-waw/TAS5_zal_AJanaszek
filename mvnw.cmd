@echo off
setlocal

set MAVEN_VERSION=3.9.9
set PROJECT_DIR=%~dp0
set MAVEN_DIR=%PROJECT_DIR%.mvn\apache-maven-%MAVEN_VERSION%
set MAVEN_CMD=%MAVEN_DIR%\bin\mvn.cmd
set MAVEN_ZIP=%PROJECT_DIR%.mvn\apache-maven-%MAVEN_VERSION%-bin.zip

if not exist "%MAVEN_CMD%" (
    echo Maven %MAVEN_VERSION% was not found in this project. Downloading...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $projectDir='%PROJECT_DIR%'; $mavenVersion='%MAVEN_VERSION%'; $mavenZip='%MAVEN_ZIP%'; New-Item -ItemType Directory -Force -Path (Join-Path $projectDir '.mvn') | Out-Null; Invoke-WebRequest -Uri ('https://archive.apache.org/dist/maven/maven-3/' + $mavenVersion + '/binaries/apache-maven-' + $mavenVersion + '-bin.zip') -OutFile $mavenZip; Expand-Archive -Path $mavenZip -DestinationPath (Join-Path $projectDir '.mvn') -Force"
    if errorlevel 1 exit /b 1
)

call "%MAVEN_CMD%" %*
