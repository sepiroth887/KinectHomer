--------------------------------------------------------------------------------

Eclipse plugin 'Code Generator for ComfyJ' v1.0.0

                       Quick Guide

	Copyright (c) 2000-2007 TeamDev Ltd. All Rights Reserved.
--------------------------------------------------------------------------------


================================================================================
1.DESCRIPTION

The main purpose  of this plugin  is  to let  the user launch Code Generator for 
ComfyJ within the  Eclipse IDE. The code which is  generated is  formatted under 
the project (or workspace) settings.

================================================================================
2. INSTALLATION

1. Put the comfyJGeneratorPlugin-1.0.0.jar into the plugins directory of Eclipse IDE 
(Eclipse 3.2 and higher is supported);
2. Install ComfyJ if it is not yet installed;
3. Make sure there is jdk 1.4.x or higher installed.

================================================================================
3. SETTINGS

1. Navigate to 'Window->Preferences...->Code Generator for ComfyJ';
2. Select if  you wish to  use project JDK  or another one  to generate the code. 
Please note,  that  Code Generator  needs  tools.jar  which  can  be found in lib 
directory of JDK. Eclipse uses JRE, so you can either  configure Eclipse, so that 
JRE home directory would refer to JDK location, or just select 'Use specific JDK' 
and specify the path to JDK;
3. Input ComfyJ home directory location in the 'ComfyJ path' field.

=================================================================================
4.USAGE

1. Select Java project, you wish to generate the code to;
2. Choose 'Code Generator for ComfyJ -> Generate...' item in the pop-up menu;
3. In the shown dialog choose the package, you wish to generate the code to;
4. Code Generator for ComfyJ would be launched and after you close the application, 
the code  formatter  will  format  the  generated  code  in accordance with project
(workspace) settings.
