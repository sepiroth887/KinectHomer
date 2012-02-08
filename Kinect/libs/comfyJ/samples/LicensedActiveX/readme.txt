This sample demonstrates how to use licensed COM components with ComfyJ package.
TestActiveX folder contains VB6 source code for testLicensedActiveX.ocx
library. Compiled library can be found in the same folder. First of all you
need registering this library in your system using next console command:

regsvr32 testLicensedActiveX.ocx

After that you can run DemoLicensedActiveX native demo application which uses
the component in run-time mode.

To use this library in development mode you should add testLicensedActiveX.key
to system registry.

LicensedActiveX.java sample demonstrates correct COM component usage in run-time
mode and how to get valid license key in development mode.

Please note that original path for VB projects is E:\Projects\ This info can be
important if you need to modify or rebuild testLicensedActiveX.ocx library.
