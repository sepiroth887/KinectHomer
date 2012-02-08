/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.types.OleVerbs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This sample demonstrates the new features of the OleContainer class:
 *  - static OLE rendering
 *  - saving/loading of OLE object to/from Java byte array/input stream
 *  - focus listening
 *
 * @author Serge Piletsky
 */
public class OleContainerFeaturesDemo extends JFrame
{
    // The file name to save/load the binary content of the embedded OLE object
    private static final String TEST_FILE_NAME = "C:\\Test.dat";

    private OleContainer _oleContainer;

    // Java byte array to store byte image of OLE object
    private byte[] _bytes = null;

    public OleContainerFeaturesDemo()
    {
        super("New OleContainer Features Demo");

        _oleContainer = new OleContainer();
        _oleContainer.setBackground(Color.white);
        _oleContainer.createObject(new File("C:\\Test.xls"));

        // Add listener to receive focus lost/gained events from the OleContainer.
        _oleContainer.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent e)
            {
                System.out.println("e = " + e);
            }

            public void focusLost(FocusEvent e)
            {
                System.out.println("e = " + e);
            }
        });


        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(_oleContainer, BorderLayout.CENTER);

        JPanel actionsPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionsPane.setPreferredSize(new Dimension(150, 100));

        final JTextField fooTextField = new JTextField();
        fooTextField.setPreferredSize(new Dimension(100, 20));
        actionsPane.add(fooTextField);

        // Action to save OLE object to the file using output stream.
        final JButton saveToFile = new JButton(new AbstractAction("Save OLE object to the file")
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(TEST_FILE_NAME));
                    _oleContainer.save(fileOutputStream);
                    fileOutputStream.close();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        });
        actionsPane.add(saveToFile);

        // Action to load OLE object from the file using input stream.
        final JButton loadFromFile = new JButton(new AbstractAction("Load OLE object from the file")
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    FileInputStream fileInputStream = new FileInputStream(new File(TEST_FILE_NAME));
                    _oleContainer.load(fileInputStream);
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
        actionsPane.add(loadFromFile);

        // Action to load OLE object from the file using input stream.
        final JButton loadFromArray = new JButton(new AbstractAction("Load OLE object from the byte array")
        {
            public void actionPerformed(ActionEvent e)
            {
                _oleContainer.load(_bytes);
            }
        });
        actionsPane.add(loadFromArray);

        // Action to load OLE object from the file using input stream.
        final JButton saveToArray = new JButton(new AbstractAction("Save OLE object to the byte array")
        {
            public void actionPerformed(ActionEvent e)
            {
                _bytes = _oleContainer.save();
            }
        });
        actionsPane.add(saveToArray);

        final JButton show = new JButton(new AbstractAction("Show OLE object to the file")
        {
            public void actionPerformed(ActionEvent e)
            {
                _oleContainer.doVerb(OleVerbs.SHOW);
            }
        });
        actionsPane.add(show);

        final JButton hide = new JButton(new AbstractAction("Hide OLE object")
        {
            public void actionPerformed(ActionEvent e)
            {
                _oleContainer.doVerb(OleVerbs.HIDE);
            }
        });
        actionsPane.add(hide);

        contentPane.add(actionsPane, BorderLayout.NORTH);

        addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
//                _oleContainer.doVerb(OleVerbs.SHOW);
            }

            public void windowClosing(WindowEvent e)
            {
                _oleContainer.destroyObject();
                OleFunctions.oleUninitialize();
            }
        });
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                OleFunctions.oleInitialize();
                createGUI();
            }
        });
    }

    private static void createGUI()
    {
        OleContainerFeaturesDemo drawTest = new OleContainerFeaturesDemo();
        drawTest.setSize(900, 700);
        drawTest.setLocationRelativeTo(null);
        drawTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawTest.setVisible(true);
    }
}