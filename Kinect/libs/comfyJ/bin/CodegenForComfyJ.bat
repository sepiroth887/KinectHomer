@echo off

REM Copyright (c) 2002-2010 TeamDev Ltd. All rights reserved.

REM Usage 1: CodegenForComfyJ.bat {guid guid_value | progid progid_value } version destFolder packageName [-disp]
REM -------
REM       guid guid_value    generate by GUID of type library
REM                          generate by ProgID of one of the components that belongs to the library
REM       version            type library version
REM       destFolder         destination path where the generated sources will be stored
REM       packageName        destination package name to generate
REM       -disp              generate stubs for dispatch interfaces
REM       -automation        generate automation-only stubs for all interfaces that support automation
REM       -orphan            generate stubs for interfaces that do not have explicitly defined parent interface and inherit them from IDispatch
REM       -jar jar_name      compile generated stubs and pack them into jar
REM       -javadoc jar_path  generate javadoc for stubs
REM.
REM For example (by guid): CodegenForComfyJ.bat guid 00062FFF-0000-0000-C000-000000000046 9.2 src outlook
REM.
REM For example (by progid):  CodegenForComfyJ.bat progid shell.explorer 1.1 src explorer
REM stubs for dispinterfaces: CodegenForComfyJ.bat progid shell.explorer 1.1 src explorer -disp
REM.
REM Usage 2: To run Generator GUI start without parameters.

if not exist jniwrap.lic goto noJNIWrapperLic 
if not exist comfyj.lic goto noComfyJLic

set CLASS_PATH=../lib/jniwrap-3.8.3.jar
set CLASS_PATH=%CLASS_PATH%;../lib/winpack-3.8.3.jar
set CLASS_PATH=%CLASS_PATH%;../lib/comfyj-2.9.jar
set CLASS_PATH=%CLASS_PATH%;../lib/comfyj-generator-2.9.jar
set CLASS_PATH=%CLASS_PATH%;../lib/commons-cli-1.0.jar
set CLASS_PATH=%CLASS_PATH%;../lib/velocity-dep-1.3.1.jar
set CLASS_PATH=%CLASS_PATH%;../lib/slf4j-api-1.5.8.jar
set CLASS_PATH=%CLASS_PATH%;../lib/slf4j-simple-1.5.8.jar

set MAIN_CLASS_NAME=com.jniwrapper.win32.com.generator.Application
set VM_OPTIONS=-Xms48m -Xmx256m

if "%1"=="" goto ui

if "%1"=="-h" goto usage

if "%1"=="/?" goto usage

if "%1"=="module" goto byModule

if not "%1"=="guid" if not "%1"=="progid" goto usage

:byGuid
SET GUID_OPT=-%1
SHIFT
SET GUID_VAL=%1
SHIFT
SET VERSION_VAL=%1
SHIFT

SET PACKAGE=-package %2
if "%2"=="" SET PACKAGE=
if "%2"=="-disp" SET PACKAGE=-disp
if "%2"=="-automation" SET PACKAGE=-automation
if "%2"=="-jar" SET PACKAGE=-jar
if "%2"=="-javadoc" SET PACKAGE=-javadoc
if "%2"=="-orphan" SET PACKAGE=-orphan

java.exe %VM_OPTIONS% -cp "%CLASS_PATH%" %MAIN_CLASS_NAME% %GUID_OPT% %GUID_VAL% -version %VERSION_VAL -path %1 %PACKAGE% %3 %4 %5 %6 %7 %8 %9
goto end

:ui
set JVM_ARGS=-DJAVA_HOME="%JAVA_HOME%"
set CLASS_PATH=%CLASS_PATH%;%JAVA_HOME%\lib\tools.jar
start "" javaw.exe %JVM_ARGS% %VM_OPTIONS% -cp "%CLASS_PATH%" %MAIN_CLASS_NAME%
goto end

:byModule
SET MODULE_OPT=-%1
SHIFT
SET MODULE_PATH=-%1
SHIFT

SET PACKAGE=-package %2
if "%2"=="" SET PACKAGE=
if "%2"=="-disp" SET PACKAGE=-disp
if "%2"=="-automation" SET PACKAGE=-automation
if "%2"=="-jar" SET PACKAGE=-jar
if "%2"=="-javadoc" SET PACKAGE=-javadoc
if "%2"=="-orphan" SET PACKAGE=-orphan

java.exe %VM_OPTIONS% -cp "%CLASS_PATH%" %MAIN_CLASS_NAME% %MODULE_OPT% %MODULE_PATH -path %1 %PACKAGE% %3 %4 %5 %6 %7 %8 %9
goto end

:noJavaHome
echo.
echo Error: JAVA_HOME environment variable is not set, or cannot locate javaw.exe. 
echo Please set JAVA_HOME to point to Java 2 SDK and try again. 
echo.
goto end

:noJNIWrapperLic
echo Cannot start Code Generator: no JNIWrapper license file found. Ensure the license is located in the current folder.
echo.
pause
goto end

:noComfyJLic
echo Cannot start Code Generator: no ComfyJ license file found. Ensure the license is located in the current folder.
echo.
pause
goto end

:usage
echo.
echo Usage 1: CodegenForComfyJ.bat {guid guid_value or progid progid_value } version destFolder packageName [-disp]
echo -------
echo       guid guid_value    generate by GUID of type library
echo                          generate by ProgID of one of the components that belongs to the library
echo       version            type library version
echo       destFolder         destination path where the generated sources will be stored
echo       packageName        destination package name to generate
echo       -disp              generate stubs for dispatch interfaces
echo       -automation        generate automation-only stubs for all interfaces that support automation
echo       -jar jar_name      compile generated stubs and pack them into jar
echo       -javadoc jar_path  generate javadoc for stubs
echo.
echo For example (by guid): CodegenForComfyJ.bat guid 00062FFF-0000-0000-C000-000000000046 9.2 src outlook
echo.
echo For example (by progid):  CodegenForComfyJ.bat progid shell.explorer 1.1 src explorer
echo stubs for dispinterfaces: CodegenForComfyJ.bat progid shell.explorer 1.1 src explorer -disp
echo.
echo Usage 2: To run Generator GUI start without parameters.
echo.
echo Usage 3: module module_path destFolder packageName [-disp]
echo -------
echo       module module_path generate stubs by module (dll), 
echo                          module_path is full path to dll
echo       destFolder         destination path where the generated sources will be stored
echo       packageName        destination package name to generate
echo       -disp              generate stubs for dispatch interfaces
echo       -automation        generate automation-only stubs for all interfaces that support automation
echo       -jar jar_name      compile generated stubs and pack them into jar
echo       -javadoc jar_path  generate javadoc for stubs
echo.
echo For example: CodegenForComfyJ.bat module E:\Projects\JNIWrapper\TestModules\lmpcrypto.dll c:\jniw lmpcrypto
:end