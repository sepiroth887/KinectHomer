/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.OleFunctions;
import msmapi.msmapi.IMapiMessages;
import msmapi.msmapi.IMapiSession;
import msmapi.msmapi.MAPIMessages;
import msmapi.msmapi.MAPISession;

/**
 * This sample demonstrates how to send a email via MAPI.
 *
 * This sample requires generated stubs for the MAPI COM type library.
 * You can generate stubs using the Code Generator application.
 */
public class MapiIntegrationSample
{
    public static void main(String[] args)
    {
        OleFunctions.oleInitialize();

        IMapiSession session = MAPISession.create(ClsCtx.INPROC_SERVER);
        // true parameter to create the new session; false to connect to existing one
        session.setNewSession(VariantBool.TRUE);
        // Specify the necessary profile name
        session.setUserName(new BStr("outlook"));
        session.signOn();

        // Create the new message and specify the session ID
        IMapiMessages message = MAPIMessages.create(ClsCtx.INPROC_SERVER);
        message.setSessionID(session.getSessionID());

        // Compose the message
        message.compose();
        message.setMsgSubject(new BStr("Subject"));
        message.setMsgNoteText(new BStr("Text"));
        message.setRecipAddress(new BStr("user_name@mail.com"));
        message.resolveName();

        message.send(new Variant(false));

        session.signOff();
    }
}