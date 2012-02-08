@echo off

REM Copyright (c) 2002-2011 TeamDev Ltd. All rights reserved.

set ROOT_DIR=..

set LIB_DIRECTORY=%ROOT_DIR%/lib
set BIN_DIRECTORY=%ROOT_DIR%/bin

set CORE_CLASSES=%LIB_DIRECTORY%/jniwrap-3.8.3.jar;%LIB_DIRECTORY%/winpack-3.8.3.jar;%LIB_DIRECTORY%/comfyj-2.9.jar;%LIB_DIRECTORY%/slf4j-api-1.5.8.jar;%LIB_DIRECTORY%/slf4j-simple-1.5.8.jar
set CUSTOM_CLASSES=%ROOT_DIR%/demo/comfyjdemo.jar;

set SAMPLE_CLASSPATH=%CORE_CLASSES%;%CUSTOM_CLASSES%
set SAMPLE_MAINCLASS=com.jniwrapper.win32.samples.demo.ComfyjDemo

set PATH=%BIN_DIRECTORY%;%PATH%

start "" javaw.exe -classpath %SAMPLE_CLASSPATH% %SAMPLE_MAINCLASS%