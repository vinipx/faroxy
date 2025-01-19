@echo off
setlocal enabledelayedexpansion

echo Setting up Faroxy Proxy Server...

:: Get the current directory
set "SCRIPT_DIR=%~dp0"
set "SCRIPT_DIR=!SCRIPT_DIR:~0,-1!"

:: Create start.bat if it doesn't exist
if not exist "%SCRIPT_DIR%\start.bat" (
    echo @echo off > "%SCRIPT_DIR%\start.bat"
    echo setlocal enabledelayedexpansion >> "%SCRIPT_DIR%\start.bat"
    echo. >> "%SCRIPT_DIR%\start.bat"
    echo :: Default ports >> "%SCRIPT_DIR%\start.bat"
    echo if "%%FAROXY_SERVER_PORT%%"=="" set FAROXY_SERVER_PORT=8080 >> "%SCRIPT_DIR%\start.bat"
    echo if "%%FAROXY_PROXY_PORT%%"=="" set FAROXY_PROXY_PORT=8888 >> "%SCRIPT_DIR%\start.bat"
    echo. >> "%SCRIPT_DIR%\start.bat"
    echo echo Starting Faroxy with: >> "%SCRIPT_DIR%\start.bat"
    echo echo Server port: %%FAROXY_SERVER_PORT%% >> "%SCRIPT_DIR%\start.bat"
    echo echo Proxy port: %%FAROXY_PROXY_PORT%% >> "%SCRIPT_DIR%\start.bat"
    echo. >> "%SCRIPT_DIR%\start.bat"
    echo cd /d "%%~dp0" >> "%SCRIPT_DIR%\start.bat"
    echo call gradlew.bat run --args="--server.port=%%FAROXY_SERVER_PORT%%" -Dspring.output.ansi.enabled=ALWAYS -Dlogging.level.io.faroxy=INFO -Dprism.verbose=true -Dprism.order=sw -Dfaroxy.proxy.port=%%FAROXY_PROXY_PORT%% >> "%SCRIPT_DIR%\start.bat"
)

:: Create stop.bat if it doesn't exist
if not exist "%SCRIPT_DIR%\stop.bat" (
    echo @echo off > "%SCRIPT_DIR%\stop.bat"
    echo cd /d "%%~dp0" >> "%SCRIPT_DIR%\stop.bat"
    echo for /f "tokens=2" %%%%a in ('tasklist ^| findstr "java.exe"') do ( >> "%SCRIPT_DIR%\stop.bat"
    echo     taskkill /F /PID %%%%a >> "%SCRIPT_DIR%\stop.bat"
    echo ) >> "%SCRIPT_DIR%\stop.bat"
)

:: Create directory for doskey macros if it doesn't exist
if not exist "%USERPROFILE%\faroxy" mkdir "%USERPROFILE%\faroxy"

:: Create macros file
set "MACRO_FILE=%USERPROFILE%\faroxy\faroxy_macros.doskey"
echo proxystart=cd /d "%SCRIPT_DIR%" $T start.bat >> "%MACRO_FILE%"
echo proxystop=cd /d "%SCRIPT_DIR%" $T stop.bat >> "%MACRO_FILE%"

:: Create a registry entry to load the macros on CMD startup
reg add "HKCU\Software\Microsoft\Command Processor" /v "AutoRun" /t REG_SZ /d "doskey /macrofile=\"%MACRO_FILE%\"" /f

echo.
echo Setup complete! The following commands are now available:
echo   proxystart - Start the Faroxy Proxy Server
echo   proxystop  - Stop the Faroxy Proxy Server
echo.
echo Note: You need to restart any open Command Prompt windows for the changes to take effect.
echo.

pause
