/*
 * Copyright (c) 2000-2011 TeamDev Ltd. All rights reserved.
 */

import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.OleFunctions;
import outlook.outlook.*;
import outlook.outlook.impl._MailItemImpl;

/**
 * This sample demonstrates working with Outlook contacts.
 * <p/>
 * This sample requires generated stubs for COM type library:
 * Description: Microsoft Outlook 11.0 Object Library
 * ProgID:      Outlook.Application
 * GUID:        {00062FFF-0000-0000-C000-000000000046}
 * In the package: outlook
 * <p/>
 * You can generate stubs using the Code Generator application.
 */
public class SendEMailSample
{
    private _NameSpace _mapiNS;
    private _Application _application;

    public static void main(String[] args)
    {
        OleFunctions.oleInitialize();

        SendEMailSample outlookContactsSample = new SendEMailSample();
        outlookContactsSample.login();
        outlookContactsSample.sendEmail();
        outlookContactsSample.logout();
    }

    public void sendEmail()
    {
        IDispatch item = _application.createItem(new OlItemType(OlItemType.olMailItem));
        _MailItem mailItem = new _MailItemImpl(item);
        mailItem.setTo(new BStr("serge.piletsky@gmail.com"));
        mailItem.setSubject(new BStr("Test message"));
        mailItem.setBody(new BStr("This is the body of the test e-mail message"));
        mailItem.send();
    }

    public void logout()
    {
        _mapiNS.logoff();
    }

    public void login() throws ComException
    {
        _application = Application.create(ClsCtx.LOCAL_SERVER);
        _mapiNS = _application.getNamespace(new BStr("MAPI"));
        _mapiNS.logon(new Variant("Outlook"),
                new Variant(""),
                new Variant(false),
                new Variant(false));
    }
}