/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.jniwrapper.util.AppletHelper;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.ole.types.OleVerbs;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Sample of applet that embeds COM objects using JNIWrapper.
 * To run this sample, execute the Applets/build.xml ant build file and run the comappletsample.html.
 * This sample needs the Word a.doc file to be placed in the root of disk C (C:/a.doc).
 *
 * @author Alexei Orischenko
 */
public class ComAppletSample extends Applet {
    private OleContainer _oleContainer;
    private JFrame _frame;
    private static final File FILE = new File("C:/a.doc");
    private OleMessageLoop ownMessageLoop = new OleMessageLoop();

    public void init() {
        super.init();

        System.out.println("AppletSample.init");

        try {
            AppletHelper.getInstance().init(this);
            AppletHelper.getInstance().start();

            ownMessageLoop.doStart();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            createOleContainer();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            showStatus(e.getMessage());
            return;
        }
        createGUI();
    }

    private void createGUI() {
        System.out.println("createGUI: ENTER");
        JFrame frame = new JFrame("TEST");
        _frame = frame;

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2, 1));
        textPanel.add(new Label("JNIWrapper in Applets"));
        textPanel.add(new Label("This sample demonstates COM component embedded into applet:"));

        frame.getContentPane().setLayout(new BorderLayout());

        frame.getContentPane().add(textPanel, BorderLayout.NORTH);
        frame.getContentPane().add(_oleContainer, BorderLayout.CENTER);
        frame.setSize(new Dimension(720, 480));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                exit();
            }

        });

        System.out.println("createGUI: LEAVE");
    }

    private void createOleContainer() throws FileNotFoundException {
        _oleContainer = new OleContainer(ownMessageLoop);
        try {
            _oleContainer.createObject(FILE);
        }
        catch (ComException e) {
            int CANNOT_OPEN_CODE = 0x800401EA;
            if (e.getHResult() == CANNOT_OPEN_CODE) {
                throw new FileNotFoundException(FILE + " not found.");
            }
        }
    }

    private void exit() {
        _oleContainer.destroyObject();
    }

    public void start() {
        AppletHelper.getInstance().start();
        _oleContainer.doVerb(OleVerbs.SHOW);

    }

    public void stop() {
        System.out.println("Stop");

        _frame.setVisible(false);
        _oleContainer.destroyObject();
        ownMessageLoop.doStop();
        AppletHelper.getInstance().stop();
    }
}