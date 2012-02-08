@echo off

REM Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.

REM Usage 1: Command line mode
REM 
REM    Registering Java COM server
REM    ---------------------------------
REM    Usage: ServerManager.bat serverClassName -classpath classpath [-jvm jvmPath]
REM
REM       serverClassName    Java COM server class name (should extend com.jniwrapper.win32.com.DispatchComServer class).
REM       classpath          Classpath that will be used for running the server. 
REM			     Make sure that classpath includes includes specified server class and jniwrap.jar, comfyj.jar, and winpack.jar libraries.
REM       jvmPath            Optional. Specifies the JVM (java.exe or javaw.exe) that will be used
REM                          for running the server. If it is not defined, the default JVM will be used.
REM
REM    For example: ServerManager.bat com.project.MyServer -classpath "c:/MyServer/jniwrap.jar;c:/MyServer/comfyj.jar;c:/MyServer/winpack.jar;c:/MyServer/myserver.jar" -jvm "c:\j2sdk1.4.2_06\bin\javaw.exe"
REM.
REM    Unregistering Java COM server
REM    ---------------------------------
REM    Usage: ServerManager.bat -unregister {-class serverClassName -classpath classpath or -clsid clsid}
REM
REM       serverClassName    Java COM server class name to be unregistered.
REM       classpath          Classpath of the server. 
REM       clsid              Class ID (CLSID) of a Java COM server to be unregistered.
REM
REM    For example (by class): ServerManager.bat -unregister com.project.MyServer -classpath "c:/MyServer/jniwrap.jar;c:/MyServer/comfyj.jar;c:/MyServer/winpack.jar;c:/MyServer/myserver.jar"
REM    For example (by clsid): ServerManager.bat -unregister -clsid {A35B432E-5274-4146-9858-638313EDCEA6}
REM.
REM Usage 2: To run ServerManager GUI, start without parameters.

set ROOT_DIR=..

set LIB_DIRECTORY=%ROOT_DIR%/lib
set BIN_DIRECTORY=%ROOT_DIR%/bin

if not exist %BIN_DIRECTORY%/jniwrap.lic goto noJNIWrapperLic 
if not exist %BIN_DIRECTORY%/comfyj.lic goto noComfyJLic

set CORE_CLASSES=%LIB_DIRECTORY%/jniwrap-3.8.3.jar;%LIB_DIRECTORY%/winpack-3.8.3.jar;%LIB_DIRECTORY%/comfyj-2.9.jar;%LIB_DIRECTORY%/slf4j-api-1.5.8.jar;%LIB_DIRECTORY%/slf4j-simple-1.5.8.jar
set CUSTOM_CLASSES=%LIB_DIRECTORY%/comfyj-svrmanager-2.9.jar;

set SAMPLE_CLASSPATH=%CORE_CLASSES%;%CUSTOM_CLASSES%

set PATH=%BIN_DIRECTORY%;%PATH%

if [%1] == [] goto runGUI
if [%1] == [-h] goto usage
if [%1] == [/?] goto usage

set SAMPLE_MAINCLASS=com.jniwrapper.win32.tools.RegisterDispatchComServer

if [%1] == [-unregister] goto unregister

set jvmPath=javaw.exe
if not [%5] == [] set jvmPath=%5

java.exe -classpath %SAMPLE_CLASSPATH% -Dclasspath=%3 -Djvm.path=%jvmPath% %SAMPLE_MAINCLASS% %1
goto end

:unregister

if [%2] == [-clsid] goto unregByClsid

java.exe -classpath %SAMPLE_CLASSPATH%;%4 -Dunregister %SAMPLE_MAINCLASS% %2
goto end

:unregByClsid

java.exe -classpath %SAMPLE_CLASSPATH% -Dunregister %SAMPLE_MAINCLASS% %3
goto end

:runGUI

set SAMPLE_MAINCLASS=com.jniwrapper.win32.tools.ServerManager

start "" javaw.exe -classpath %SAMPLE_CLASSPATH% %SAMPLE_MAINCLASS%
goto end

:noJNIWrapperLic
echo Cannot start Code Generator: no JNIWrapper license file found. Make sure that the license is located in the current folder.
echo.
pause
goto end

:noComfyJLic
echo Cannot start Code Generator: no ComfyJ license file found. Make sure that the license is located in the current folder.
echo.
pause
goto end

:usage
echo Usage 1: Command line mode
echo. 
echo    Registering Java COM server
echo    ---------------------------------
echo    Usage: ServerManager.bat serverClassName -classpath classpath [-jvm jvmPath]
echo.
echo       serverClassName  Java COM server class name (should extend 
echo                        com.jniwrapper.win32.com.DispatchComServer class) 
echo       classpath        Classpath that will be used for running the server. 
echo                        Make sure that classpath includes specified server class
echo                        and jniwrap.jar, comfyj.jar, and winpack.jar libraries.
echo       jvmPath          Optional. Specifies the JVM (java.exe or javaw.exe) 
echo                        that will be used for running the server. 
echo                        If it is not defined, the default JVM will be used.
echo.
echo    For example: ServerManager.bat com.project.MyServer -classpath "c:/MyServer/jniwrap.jar;c:/MyServer/comfyj.jar;c:/MyServer/winpack.jar;c:/MyServer/myserver.jar" -jvm "c:\j2sdk1.4.2_06\bin\javaw.exe"
echo.
echo    Unregistering Java COM server
echo    ---------------------------------
echo    Usage: ServerManager.bat -unregister {serverClassName -classpath classpath or -clsid clsid}
echo.
echo       serverClassName  Java COM server class name to be unregistered.
echo       classpath        Classpath of the server. 
echo       clsid            Class ID of a Java COM server to be unregistered.
echo.
echo    For example (by class): ServerManager.bat -unregister com.project.MyServer -classpath "c:/MyServer/jniwrap.jar;c:/MyServer/comfyj.jar;c:/MyServer/winpack.jar;c:/MyServer/myserver.jar"
echo.
echo    For example (by clsid): ServerManager.bat -unregister -clsid {A35B432E-5274-4146-9858-638313EDCEA6}
echo.
echo Usage 2: To run ServerManager GUI start without parameters.
echo.

:end