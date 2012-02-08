--------------------------------------------------------------------------------

IntelliJ IDEA plugin 'Code Generator for ComfyJ' v1.1

                       Quick Guide

	Copyright (c) 2000-2007 TeamDev Ltd. All Rights Reserved.
--------------------------------------------------------------------------------


================================================================================
1.DESCRIPTION

The main purpose  of this plugin  is  to let  the user launch Code Generator for 
ComfyJ within the IntelliJ IDEA. The code which is generated is  formatted under 
the project (or module) settings.

================================================================================
2. INSTALLATION

1. Put the comfyJGeneratorPlugin-1.1.jar into the plugins directory of IntelliJ IDEA;
2. Install ComfyJ if it is not yet installed;
3. Make sure there is jdk 1.4.x or higher installed.

================================================================================
3. SETTINGS

1. Navigate to 'Project Settings -> ComfyJ Generator';
2. Select if  you wish to  use project JDK  or another one  to generate the code. 
Please note,  that  Code Generator  needs  tools.jar  which  can  be found in lib 
directory of JDK.
3. Input ComfyJ home directory location in the 'ComfyJ path' field.

=================================================================================
4.USAGE

1. Select 'Code Generator for ComfyJ...' in the 'Tools' menu (or use Ctrl+Alt+G)
2. In the shown dialog choose the source folder, you wish to generate the code to
or specify path to the jar you wish to pack compilied classes to;
3. There is an option to generate Java docs, so you can specify location of the
folder to generate docs to.
4. Code Generator for ComfyJ would be launched and after you close the application, 
the code  formatter  will  format  the  generated  code  in accordance with project
settings.
