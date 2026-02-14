@echo off
REM Maven Wrapper for Windows
REM Attempts to find Maven installation and run it

setlocal enabledelayedexpansion

REM Try to find Maven in common locations
set "MAVEN_HOME="

if exist "C:\Program Files\apache-maven-3.9.6" (
    set "MAVEN_HOME=C:\Program Files\apache-maven-3.9.6"
) else if exist "C:\Program Files\maven" (
    set "MAVEN_HOME=C:\Program Files\maven"
) else if exist "C:\apache-maven" (
    set "MAVEN_HOME=C:\apache-maven"
) else if defined M2_HOME (
    set "MAVEN_HOME=%M2_HOME%"
) else (
    echo Maven is not installed or not found in common locations.
    echo Please install Maven from: https://maven.apache.org/download.cgi
    echo Or set MAVEN_HOME environment variable.
    exit /b 1
)

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Maven executable not found in: %MAVEN_HOME%\bin\mvn.cmd
    exit /b 1
)

echo Using Maven from: %MAVEN_HOME%
"%MAVEN_HOME%\bin\mvn.cmd" %*

