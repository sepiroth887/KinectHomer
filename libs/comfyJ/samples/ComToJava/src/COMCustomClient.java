/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.impl.IPersistImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;

import java.io.IOException;

/**
 * @author Vladimir Kondrashchenko
 */
public class COMCustomClient
{
    public static final CLSID CLSID_1 = new CLSID("{69D4BFB2-2448-4175-997A-E94A4F797FA7}");
    public static final CLSID CLSID_2 = new CLSID("{B941491E-227D-46a6-91DF-20B45942CBE1}");

    public static void main(String[] args) throws Exception
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                IDispatch dispatch = new IDispatchImpl(CLSID.createFromProgID("jniwrapper.comtojava"), ClsCtx.LOCAL_SERVER);
//                IDispatch dispatch = new IDispatchImpl(CLSID_2, ClsCtx.LOCAL_SERVER);
                IPersistImpl p = new IPersistImpl(dispatch);
                CLSID clsid = new CLSID();
                p.getClassID(clsid);
                System.out.println(clsid);
                try
                {
                    System.in.read();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                System.out.println("dispatch = " + dispatch.release().getValue());
            }
        };
        OleMessageLoop.invokeAndWait(runnable);
        OleMessageLoop.stop();
    }
}