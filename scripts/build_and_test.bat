@echo off
setlocal EnableDelayedExpansion

:: ============================================
:: Build and Deploy Script for PvPPartyRemastered
:: ============================================
:: This script builds the Maven project and copies
:: the JAR to a specified directory.
::
:: Usage:
::   build_and_test.bat [target_directory]
::
:: Examples:
::   build_and_test.bat
::   build_and_test.bat "C:\MinecraftServer\plugins"
::   build_and_test.bat "..\test-server\plugins"
:: ============================================

:: Default target directory (change this to your preferred path)
set "DEFAULT_TARGET=C:\MinecraftServer\plugins"

:: Default JDK path (change this to your JDK installation path)
:: Using IntelliJ's JDK location
set "DEFAULT_JDK=%USERPROFILE%\.jdks\ms-21.0.8"

:: Use command line argument if provided, otherwise use default
if "%~1"=="" (
    set "TARGET_DIR=%DEFAULT_TARGET%"
    echo Using default target directory: !TARGET_DIR!
) else (
    set "TARGET_DIR=%~1"
    echo Using provided target directory: !TARGET_DIR!
)

:: Project configuration
set "PROJECT_DIR=%~dp0.."
set "JAR_NAME=PvPPartyRemastered-1.0-SNAPSHOT.jar"
set "BUILD_DIR=%PROJECT_DIR%\target"

echo.
echo ========================================
echo Building PvPPartyRemastered
echo ========================================
echo.

:: Check and set JAVA_HOME if not set or pointing to JRE
if not defined JAVA_HOME (
    echo [WARNING] JAVA_HOME is not set. Attempting to use default JDK path...
    if exist "%DEFAULT_JDK%" (
        set "JAVA_HOME=%DEFAULT_JDK%"
        echo Set JAVA_HOME to: !JAVA_HOME!
    ) else (
        echo [ERROR] JDK not found at default location: %DEFAULT_JDK%
        echo Please set JAVA_HOME to your JDK installation directory.
        echo Example: set JAVA_HOME=C:\Program Files\Java\jdk-21
        exit /b 1
    )
)

:: Verify JAVA_HOME points to JDK (has javac)
if not exist "%JAVA_HOME%\bin\javac.exe" (
    echo [ERROR] JAVA_HOME does not point to a valid JDK!
    echo Current JAVA_HOME: %JAVA_HOME%
    echo Please ensure JAVA_HOME points to a JDK installation (not JRE).
    if exist "%DEFAULT_JDK%\bin\javac.exe" (
        echo.
        echo Found JDK at: %DEFAULT_JDK%
        echo Run: set JAVA_HOME=%DEFAULT_JDK%
    )
    exit /b 1
)

echo Using JDK: %JAVA_HOME%
set "PATH=%JAVA_HOME%\bin;%PATH%"

:: Change to project directory
cd /d "%PROJECT_DIR%"

:: Run Maven build
echo.
echo [1/3] Running Maven build...
call mvn clean package
if errorlevel 1 (
    echo.
    echo [ERROR] Maven build failed!
    exit /b 1
)

echo.
echo [SUCCESS] Build completed successfully!
echo.

:: Check if JAR exists
if not exist "%BUILD_DIR%\%JAR_NAME%" (
    echo [ERROR] JAR file not found at: %BUILD_DIR%\%JAR_NAME%
    exit /b 1
)

:: Create target directory if it doesn't exist
echo [2/3] Checking target directory...
if not exist "%TARGET_DIR%" (
    echo Target directory does not exist. Creating: %TARGET_DIR%
    mkdir "%TARGET_DIR%"
    if errorlevel 1 (
        echo [ERROR] Failed to create target directory!
        exit /b 1
    )
)

:: Copy JAR to target directory
echo [3/3] Copying JAR to target directory...
copy /Y "%BUILD_DIR%\%JAR_NAME%" "%TARGET_DIR%\%JAR_NAME%"
if errorlevel 1 (
    echo.
    echo [ERROR] Failed to copy JAR file!
    exit /b 1
)

echo.
echo ========================================
echo Deployment Complete!
echo ========================================
echo.
echo JAR Location: %TARGET_DIR%\%JAR_NAME%
echo.
echo You can now restart your server to load the plugin.
echo.

endlocal
exit /b 0