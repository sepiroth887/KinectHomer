/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.jniwrapper.util.AppletHelper;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.ole.types.OleVerbs;

import java.applet.Applet;
import java.awt.*;
import java.io.IOException;

/**
 * This sample demonstrates how to embed IE ActiveX component into a Java applet.
 * To run this sample, execute the Applets/build.xml ant build file and run ShellExplorerSample.html.
 *
 * @author Serge Piletsky
 */
public class ShellExplorerApplet extends Applet {
    private static final String shellExplorer = "Shell.Explorer";
    private OleContainer _oleContainer;
    private static final String URL = "http://www.google.com";

    private OleMessageLoop ownMessageLoop = new OleMessageLoop();

    public void init() {
        super.init();
        System.out.println("ENTER: ShellExplorerApplet.init()");
        try {
            AppletHelper.getInstance().init(this);
            AppletHelper.getInstance().start();

            ownMessageLoop.doStart();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // then OleContainer itself
        _oleContainer = new OleContainer(ownMessageLoop);
        _oleContainer.createObject(shellExplorer);

        setLayout(new BorderLayout());
        add(_oleContainer, BorderLayout.CENTER);

        System.out.println("LEAVE: ShellExplorerApplet.init()");
    }

    public void start() {
        System.out.println("ENTER: ShellExplorerApplet.start()");
        // Start ComAppletHelper first
        AppletHelper.getInstance().start();

        _oleContainer.doVerb(OleVerbs.INPLACEACTIVATE);
        Automation automation = new Automation(_oleContainer.getOleObject());
        automation.invoke("Navigate2", new Object[]{URL});

        System.out.println("LEAVE: ShellExplorerApplet.start()");
    }

    public void stop() {
        System.out.println("ENTER: ShellExplorerApplet.stop()");
        _oleContainer.destroyObject();
        ownMessageLoop.doStop();
        AppletHelper.getInstance().stop();
        System.out.println("LEAVE: ShellExplorerApplet.stop()");
    }
}