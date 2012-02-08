/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.CoServerInfo;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.OleFunctions;
import dcom.dcomserverlib.Disk;
import dcom.dcomserverlib.impl.IDiskImpl;

/**
 * This sample demonstrates the Java DCOM client class, which calls the native server method.   
 * To run this sample:
 * <ol>
 *      <li>build the native server solution</li>
 *      <li>run DComServer.exe on the server</li>
 *      <li>change SERVER_NAME to the network name of the server</li>
 *      <li>run the sample on the client</li>
 * </ol>
 *
 */
public class DComSample
{
    public static final String SERVER_NAME = "screw";

    public static void main(String[] args)
    {
        OleFunctions.oleInitialize();

        CoServerInfo info = new CoServerInfo();
        info.setName(SERVER_NAME);

        IDiskImpl disk = new IDiskImpl();
        ComFunctions.coCreateInstanceEx(Disk.CLASS_ID, null, ClsCtx.REMOTE_SERVER, info, disk);

        BStr drive = new BStr("C:\\");
        long space = disk.getDFreeSpace(drive).getValue();
        System.out.println("Free space on drive " + drive.getValue() + " = " + space + " bytes.");
    }

}