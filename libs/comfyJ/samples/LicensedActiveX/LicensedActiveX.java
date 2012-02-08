/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.ole.types.OleVerbs;
import com.jniwrapper.win32.ole.IOleObject;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.impl.IOleObjectImpl;
import com.jniwrapper.win32.com.impl.IClassFactory2Impl;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.Pointer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This sample demonstrates how to call correctly
 * licensed COM component from Java side using ComfyJ
 * Additional information can be found in readme.txt
 *
 * @author Igor Novikov
 */
public class LicensedActiveX extends JFrame
{
    private OleContainer _container;

    public LicensedActiveX()
    {
        super("LicensedActiveXSample");

        _container = new OleContainer();
        Container contentPane = getContentPane();

        JPanel activeXPanel = new JPanel();
        doCreateOleObject();

        _container.setSize(600, 400);
        activeXPanel.add(_container);

        contentPane.add(activeXPanel);


        addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                _container.doVerb(OleVerbs.SHOW);
            }
        });
    }
private void doCreateOleObject()
    {
        try
        {
            OleMessageLoop.invokeMethod(this, "createOleObject");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void createOleObject()
    {
        OleFunctions.oleInitialize();
        try
        {
            CLSID firstCLSID = new CLSID(CLSID.createFromProgID("testLicensedActiveX.LicensedActiveX"));
            IClassFactory2Impl classFactory = new IClassFactory2Impl();
            IUnknownImpl object = new IUnknownImpl();

            BStr bstrKey = new BStr("kiphefdhdgchijcelemdpc");

//            classFactory.createInstance(null, new IID(firstCLSID), classFactory);
            ComFunctions.coGetClassObject(firstCLSID, ClsCtx.INPROC_SERVER, new Pointer.Void(), classFactory);

//            To get real license key uncomment these lines and run on licensed machine:
//
//            classFactory.requestLicKey(new UInt32(), bstrKey);
//            System.out.println("bstrKey: "+bstrKey);

            classFactory.createInstanceLic(
                    null,
                    null,
                    object.getIID(),
                    bstrKey,
                    object
            );

            IOleObject oleObject = new IOleObjectImpl(object);

            _container.insertObject(oleObject);
        }
        catch (ComException e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        LicensedActiveX sample = new LicensedActiveX();
        sample.setSize(640, 480);
        sample.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sample.setLocationRelativeTo(null);
        sample.setVisible(true);
    }


}