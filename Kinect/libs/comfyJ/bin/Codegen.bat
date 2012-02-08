@echo off

REM Copyright (c) 2002-2009 TeamDev Ltd. All rights reserved.

if not exist jniwrap.lic goto noJNIWrapperLic 

set CLASS_PATH=../lib/antlr.jar;../lib/jniwrap-3.8.3.jar;../lib/jniwrap-generator-3.8.3.jar;../lib/winpack-3.8.3.jar;../WinPack/lib/winpack-3.8.3.jar;../lib/slf4j-api-1.5.8.jar;../lib/slf4j-simple-1.5.8.jar;
start "" javaw.exe -cp "%CLASS_PATH%" com.jniwrapper.generator.Application
goto end

:noJNIWrapperLic
echo Cannot start Code Generator: no JNIWrapper license file found. Make sure that the jniwrap.lic file is located in the current folder.
echo.
pause
goto end

:end