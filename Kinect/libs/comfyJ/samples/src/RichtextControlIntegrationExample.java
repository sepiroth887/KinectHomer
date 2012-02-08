/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.ole.impl.IOleObjectImpl;
import com.jniwrapper.win32.ole.types.OleVerbs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This sample demonstrates simple integration with Microsoft RichtextCtrl control.
 */
public class RichtextControlIntegrationExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("TestFrame");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        final OleContainer container = new OleContainer();
        contentPane.add(container, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton open = new JButton(new AbstractAction("Open file...") {
            public void actionPerformed(ActionEvent e) {
                IOleObjectImpl oleObject = container.getOleObject();
                Automation automation = new Automation(oleObject);
                automation.setProperty("FileName", new Object[] {"c:\\Hello.rtf"});
                automation.release();
            }
        });
        controls.add(open);

        JButton getText = new JButton(new AbstractAction("Get text") {
            public void actionPerformed(ActionEvent e) {
                IOleObjectImpl oleObject = container.getOleObject();
                Automation automation = new Automation(oleObject);
                Variant text = automation.getProperty("Text");
                System.out.println(text.getValue());
                automation.release();
            }
        });
        controls.add(getText);

        JButton setText = new JButton(new AbstractAction("Set 'Hello, World!' text") {
            public void actionPerformed(ActionEvent e) {
                IOleObjectImpl oleObject = container.getOleObject();
                Automation automation = new Automation(oleObject);
                automation.setProperty("Text", "Hello, World!");
                automation.release();
            }
        });
        controls.add(setText);

        contentPane.add(controls, BorderLayout.NORTH);

        container.createObject("RICHTEXT.RichtextCtrl.1");

        frame.addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {
                container.doVerb(OleVerbs.SHOW);
            }

            public void windowClosing(WindowEvent windowEvent) {
                container.destroyObject();
            }
        });

        frame.setVisible(true);
    }
}