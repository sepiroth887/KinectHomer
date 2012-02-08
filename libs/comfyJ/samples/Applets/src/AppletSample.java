/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.*;
import com.jniwrapper.util.AppletHelper;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.ui.User32;
import com.jniwrapper.win32.ui.Wnd;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

 /**
  * Sample of applet that calls win32 functions using JNIWrapper.
  * To run this sample, execute the Applets/build.xml ant build file and run the appletsample.html.
  * @author Alexei Orischenko
  */
public class AppletSample extends Applet
{
    public AppletSample()
    {
    }

    public void init()
    {
        super.init();

        try
        {
            AppletHelper.getInstance().init(this);
        }
        catch (IOException e)
        {
        }

        Library lib = new Library(Library.NATIVE_CODE);
        lib.load();
        boolean success = lib.isLoaded();
        if (success)
        {
            System.out.println("LOADED");
        }
        else
        {
            System.out.println("NOT LOADED");
        }

        Button button = new Button("Show Message Box");
        final Wnd wnd = new Wnd(this);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                show(wnd, "JNIWrapper in Applets", "Native function is called", 0);
            }
        });

        Panel demoPanel = new Panel();
        demoPanel.add(button);

        Panel textPanel = new Panel();
        textPanel.setLayout(new GridLayout(2, 1));
        textPanel.add(new Label("JNIWrapper in Applets"));
        textPanel.add(new Label("This sample demonstates call of native function from applet:"));

        setLayout(new BorderLayout());

        add(textPanel, BorderLayout.NORTH);
        add(demoPanel, BorderLayout.WEST);
    }

    public void start()
    {
        AppletHelper.getInstance().start();

        System.out.println("Start");
    }

    public void stop()
    {
        System.out.println("Stop");

        AppletHelper.getInstance().stop();
    }

    public static int show(Wnd hWnd, String title, String message, int flags)
    {
        final FunctionName FUNCTION_MESSAGE_BOX = new FunctionName("MessageBox");

        final Function function = User32.getInstance().getFunction(FUNCTION_MESSAGE_BOX.toString());
        Int result = new Int();
        function.invoke(result, hWnd, new Str(message), new Str(title), new UInt(flags));
        return (int)result.getValue();
    }
}