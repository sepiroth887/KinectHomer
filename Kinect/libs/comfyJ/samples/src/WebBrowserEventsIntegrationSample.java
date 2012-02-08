/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.Function;
import com.jniwrapper.Int32;
import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import com.jniwrapper.win32.ole.types.OleVerbs;
import com.jniwrapper.win32.shdocvw.DWebBrowserEvents2;
import com.jniwrapper.win32.shdocvw.server.DWebBrowserEvents2Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This sample demonstrates how to install COM object events listener
 * using connection points. <br>

 * This sample requires generated stubs for COM type library:
 * Description: Microsoft Internet Controls
 * ProgID:      Shell Explorer
 * GUID:        {EAB22AC0-30C1-11CF-A7EB-0000C05BAE0B}
 * In the package: com.jniwrapper.win32
 *
 * You can generate stubs using the Code Generator application.
 */
public class WebBrowserEventsIntegrationSample extends JFrame
{
    private JLabel _status;
    private JProgressBar _progressBar;

    public WebBrowserEventsIntegrationSample()
    {
        super("Internet Explorer integration");

        getContentPane().setLayout(new BorderLayout());

        final OleContainer container = new OleContainer();
        container.createObject("Shell.Explorer");
        getContentPane().add(container, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                container.doVerb(OleVerbs.INPLACEACTIVATE);
                Automation automation = new Automation(container.getOleObject());
                automation.invoke("Navigate2", new Object[]{"http://www.teamdev.com"});
            }
        });

        // Create class factory server for our DWebBrowserEvents2Impl
        IClassFactoryServer server = new IClassFactoryServer(DWebBrowserEvents2Handler.class);
        server.registerInterface(IDispatch.class, new IDispatchVTBL(server));
        server.setDefaultInterface(IDispatch.class);
        IClassFactory factory = server.createIClassFactory();

        // Create instance of DWebBrowserEvents2Handler with a class factory
        IDispatchImpl handler = new IDispatchImpl();
        factory.createInstance(null, handler.getIID(), handler);
        // Get the newest instance of java COM object from the server
        DWebBrowserEvents2Handler webBrowserEvents2Handler = (DWebBrowserEvents2Handler) server.getInstances().pop();
        // Add add a property change listener to monitor property changing
//        webBrowserEvents2Handler.addPropertyChangeListener(new PropertyChangeListener()
//        {
//            public void propertyChange(PropertyChangeEvent evt)
//            {
//                System.out.println("Prop change");
//                // handle PROPERTY_STATUS property change
//                if (evt.getPropertyName().equals(DWebBrowserEvents2Handler.PROPERTY_STATUS))
//                {
//                    final String statusText = (String) evt.getNewValue();
//                    _status.setText(statusText);
//                }
//                // handle PROPERTY_PROGRESS property change
//                else if (evt.getPropertyName().equals(DWebBrowserEvents2Handler.PROPERTY_PROGRESS))
//                {
//                    // assume that old value is a max progress value
//                    final IntegerParameter progressMaxValue = (IntegerParameter) evt.getOldValue();
//                    final IntegerParameter progressValue = (IntegerParameter) evt.getNewValue();
//                    final int max = (int) progressMaxValue.getValue();
//                    _progressBar.setMaximum(max);
//                    final int current = (int) progressValue.getValue();
//                    _progressBar.setValue(current == -1 ? max : current);
//                }
//            }
//        });

        // Create IConnectionPointContainer to ActiveX object, which is embedded into OleContainer
        IConnectionPointContainer connectionPointContainer =
                new IConnectionPointContainerImpl(container.getOleObject());
        // Find a necessary connection point
        IConnectionPoint connectionPoint =
                connectionPointContainer.findConnectionPoint(new IID(DWebBrowserEvents2.INTERFACE_IDENTIFIER));
        // Advise our handler
        connectionPoint.advise(handler);

        _status = new JLabel();
        _progressBar = new JProgressBar();

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(_status, BorderLayout.WEST);
        statusPanel.add(_progressBar, BorderLayout.EAST);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);
    }

    static final Function FUNCTION_SafeArrayGetElement = ComFunctions.getInstance().getFunction("SafeArrayGetElement");

    public static class DWebBrowserEvents2Handler extends DWebBrowserEvents2Server
    {
        public static final String PROPERTY_STATUS = "status";
        public static final String PROPERTY_PROGRESS = "progress";

        private final PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

        public DWebBrowserEvents2Handler(CoClassMetaInfo info)
        {
            super(info);
        }

        public void statusTextChange(BStr /*[in]*/ Text)
        {
            _propertyChangeSupport.firePropertyChange(PROPERTY_STATUS, "", Text.getValue());
            System.out.println("status text");
        }

        public void progressChange(Int32 /*[in]*/ Progress, Int32 /*[in]*/ ProgressMax)
        {
            _propertyChangeSupport.firePropertyChange(PROPERTY_PROGRESS,
                    ProgressMax,
                    Progress);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener)
        {
            _propertyChangeSupport.addPropertyChangeListener(listener);
        }
    }

    public static void main(String[] args)
    {
        WebBrowserEventsIntegrationSample test = new WebBrowserEventsIntegrationSample();
        test.setSize(640, 480);
        test.setLocationRelativeTo(null);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        test.setVisible(true);
    }
}