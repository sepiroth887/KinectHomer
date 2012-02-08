/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.gdi.Region;
import com.jniwrapper.win32.ole.types.OleVerbs;
import com.jniwrapper.win32.ui.Wnd;

import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * This sample demonstrates how to create custom shape OLE container
 * with the WMPlayer.OCX component embedded into it.  
 *
 * @author Serge Piletsky
 */
public class CustomShapeOleContainer
{
    public static void main(String[] args) throws Exception
    {
        final JFrame frame = new JFrame("Custom shape OleContainer");

        ImageIcon imageIcon = new ImageIcon(ImageIO.read(CustomShapeOleContainer.class.getResourceAsStream("res/cup.gif")));
        Region windowShape = Region.createFromImage(imageIcon.getImage());

        frame.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        OleContainer oleContainer = new OleContainer();
        oleContainer.createObject("WMPlayer.OCX");
        frame.getContentPane().add(oleContainer);

        frame.setVisible(true);

        oleContainer.doVerb(OleVerbs.INPLACEACTIVATE);

        Wnd wnd = new Wnd(oleContainer);
        wnd.setRegion(windowShape, true);

        Automation automation = new Automation(oleContainer.getOleObject());
        automation.setProperty("URL", "C:\\sample.wmv");
    }
}