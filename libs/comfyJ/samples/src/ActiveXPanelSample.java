/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.ole.types.OleVerbs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This sample demonstrates embedding of WMPlayer.OCX component into
 * <code>JPanel</code>, which has <code>FlowLayout</code> manager installed.  
 *
 * @author Serge Piletsky
 */
public class ActiveXPanelSample extends JFrame
{
    public ActiveXPanelSample()
    {
        super("ActiveXPanelSample");

        Container contentPane = getContentPane();

        JPanel activeXPanel = new JPanel();
        final OleContainer oleContainer = new OleContainer();
        oleContainer.createObject("WMPlayer.OCX");

        // By default JPanel component has FlowLayout manager installed,
        // so the size of OleContainer should be necessarily specified
        oleContainer.setSize(600, 400);
        // Otherwise you can set BorderLayout
        // activeXPanel.setLayout(new BorderLayout());
        activeXPanel.add(oleContainer);

        contentPane.add(activeXPanel);

        addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                // the verb activation of OleObject should be performed only when the host window is visible
                oleContainer.doVerb(OleVerbs.INPLACEACTIVATE);
            }
        });
    }

    public static void main(String[] args)
    {
        ActiveXPanelSample sample = new ActiveXPanelSample();
        sample.setSize(640, 480);
        sample.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sample.setLocationRelativeTo(null);
        sample.setVisible(true);
    }
}