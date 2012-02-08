/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.Int;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.types.OleVerbs;
import operations.OfficeFileOperationsHandler;
import point.powerpoint.*;
import point.powerpoint.impl._PresentationImpl;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This sample demonstrates the technique of embedding PowerPoint application into a java application
 * using OleContainer and generated java stubs for PowerPoint application.
 *
 * This sample requires generated stubs for COM type library:
 * Description: Microsoft PowerPoint 11.0 Object Library
 * ProgID:      PowerPoint.Show
 * GUID:        {91493440-5A91-11CF-8700-00AA0060263B}
 * In the package: point
 *
 * You can generate stubs using the Code Generator application.
 *
 * @author Alexei Orischenko
 */
public class PowerPointIntegrationSample extends JFrame
{
    private static final Dimension WINDOW_SIZE = new Dimension(720, 480);

    /**
     * progid of presentation
     */
    private static final String PRESENTATION_PROGID = "Powerpoint.Show";

    private OleContainer _container;

    public PowerPointIntegrationSample()
    {
        super("JNIWrapper - Power Point Integration");

        _container = new OleContainer();
        _container.createObject(PRESENTATION_PROGID);

        getContentPane().add(_container, BorderLayout.CENTER);

        // Enable open / save operations
        _container.setFileOperationsHandler(new OfficeFileOperationsHandler(OfficeFileOperationsHandler.TYPE_POWERPOINT));
    }

    public void modifyDocument()
    {
        // get current presentation from ole object
        _PresentationImpl presentation = new _PresentationImpl(_container.getOleObject());
        presentation.setAutoDelete(false);

        try
        {
            final Slides slides = presentation.getSlides();
            ((IUnknownImpl) slides).setAutoDelete(false);

            try
            {
                addSlide(slides);
                deleteSlide(slides);
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

    private void deleteSlide(final Slides slides)
    {
        _Slide slide1 = slides.item(new Variant(1));
        ((IUnknownImpl) slide1).setAutoDelete(false);

        try
        {
            slide1.delete();
        }
        finally
        {
            slide1.release();
        }
    }

    private void addSlide(final Slides slides)
    {
        _Slide slide = slides.add(new Int(2), new PpSlideLayout(PpSlideLayout.ppLayoutText));
        ((IUnknownImpl) slide).setAutoDelete(false);

        try
        {
            modifySlide(slide);
        }
        finally
        {
            slide.release();
        }
    }

    private void modifySlide(_Slide slide)
    {
        Shapes shapes = slide.getShapes();
        ((IUnknownImpl) shapes).setAutoDelete(false);

        try
        {
            modifyShape(1, shapes, "new slide in new presentation");
            modifyShape(2, shapes, "you can modify slides in presentation");
        }
        finally
        {
            shapes.release();
        }
    }

    private void modifyShape(int shapeIndex, Shapes shapes, String text)
    {
        Shape shape = shapes.item(new Variant(shapeIndex));
        ((IUnknownImpl)shape).setAutoDelete(false);

        try
        {
            modifyShapeText(shape, text);
        }
        finally
        {
            shape.release();
        }
    }

    private void modifyShapeText(Shape shape, String text)
    {
        TextFrame textFrame = shape.getTextFrame();
        ((IUnknownImpl)textFrame).setAutoDelete(false);

        try
        {
            TextRange textRange = textFrame.getTextRange();
            ((IUnknownImpl)textRange).setAutoDelete(false);

            try
            {
                textRange.setText(new BStr(text));
            }
            finally
            {
                textRange.release();
            }
        }
        finally
        {
            textFrame.release();
        }
    }

    private static void createGUI()
    {
        final PowerPointIntegrationSample app = new PowerPointIntegrationSample();
        app.setSize(WINDOW_SIZE);
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        app.addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                // show power point
                app._container.doVerb(OleVerbs.SHOW);

                // work with power point using generated sources
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