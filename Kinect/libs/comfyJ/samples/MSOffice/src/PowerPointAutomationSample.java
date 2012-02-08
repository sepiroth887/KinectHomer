/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.types.OleVerbs;
import operations.OfficeFileOperationsHandler;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * This sample demonstrates the technique of embedding PowePoint application into a java application
 * using OleContainer and Automation.
 * 
 * @author Alexei Orischenko
 */
public class PowerPointAutomationSample extends JFrame
{
    private static final Dimension WINDOW_SIZE = new Dimension(720, 480);

    /**
     * progid of presentation
     */
    private static final String PRESENTATION_PROGID = "Powerpoint.Show";

    private OleContainer _container;

    public PowerPointAutomationSample()
    {
        super("JNIWrapper - Power Point Automation");

        _container = new OleContainer();
        _container.createObject(PRESENTATION_PROGID);

        getContentPane().add(_container, BorderLayout.CENTER);

        // Enable open / save operations
        _container.setFileOperationsHandler(new OfficeFileOperationsHandler(OfficeFileOperationsHandler.TYPE_POWERPOINT));
    }

    public void modifyDocument()
    {
        // get current presentation from ole object
        IDispatchImpl presentation = new IDispatchImpl(_container.getOleObject());
        presentation.setAutoDelete(false);

        try
        {
            Automation presentationAutomation = new Automation((IDispatchImpl) presentation);

            IDispatchImpl slides = (IDispatchImpl) presentationAutomation.getProperty("Slides").getPdispVal();
            slides.setAutoDelete(false);

            try
            {
                addSlide(slides);
            }
            finally
            {
                slides.release();
            }
        }
        finally
        {
            presentation.release();
        }
    }

    private void addSlide(IDispatchImpl slides)
    {
        Automation slidesAutomation = new Automation(slides);
        try
        {
            Variant result = (Variant)slidesAutomation.invoke("Add", new Object[] { new Integer(2), new Integer(2) } );

            IDispatchImpl pSlide = (IDispatchImpl)result.getPdispVal();
            pSlide.setAutoDelete(false);
            pSlide.release();
        }
        finally
        {
            slidesAutomation.release();
        }
    }

    private static void createGUI()
    {
        final PowerPointAutomationSample app = new PowerPointAutomationSample();
        app.setSize(WINDOW_SIZE);
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        app.addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                // show power point
                app._container.doVerb(OleVerbs.SHOW);

                // work with power point through automation
                try
                {
                    OleMessageLoop.invokeMethod(app, "modifyDocument", new Object[] {});
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            public void windowClosing(WindowEvent e)
            {
                // close presentation on exit
                app._container.destroyObject();
            }
        });

        app.setVisible(true);
    }

    public static void main(String[] args)
    {
        // initialize OLE
        OleFunctions.oleInitialize();

        createGUI();
    }
}