/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.com.AbstractOleControl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.types.CLSID;

import javax.swing.*;
import java.awt.*;

/**
 * Sample Swing OLE object.
 *
 * @author Serge Piletsky
 */
public class SimpleSwingActiveX extends AbstractOleControl {

    public static final CLSID COM_SERVER_CLSID = new CLSID("{A35B432E-5274-4146-9859-1AAAAAAAAAAA}");
    public static final String PROG_ID = "jniwrapper.swingActiveX.1";
    public static final String VERSION_INDEPENDENT_PROG_ID = "jniwrapper.swingActiveX";
    public static final String COM_SERVER_DESCRIPTION = "SimpleSwingActiveX is the sampple ActiveX control written on Java using ComfyJ.";

    public SimpleSwingActiveX(CoClassMetaInfo classImpl) {
        super(classImpl);
    }

    protected void initUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JButton("Click me!"), BorderLayout.NORTH);
        JPanel samples = new JPanel(new FlowLayout());
        samples.add(new JLabel("JLabel"));
        samples.add(new JButton("JButton"));
        samples.add(new JComboBox(new Object[]{"1", "2", "3"}));
        contentPane.add(samples, BorderLayout.SOUTH);
    }

    public CLSID getUserClassID() throws ComException {
        return COM_SERVER_CLSID;
    }
}