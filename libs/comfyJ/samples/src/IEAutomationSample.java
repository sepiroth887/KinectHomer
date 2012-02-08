import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.ole.types.OleVerbs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This example demonstrates the embedding of Browser component into Java.
 */
public class IEAutomationSample extends JFrame
{
    private OleContainer container = new OleContainer();

    public IEAutomationSample() throws HeadlessException
    {
        super("Internet Explorer ");

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(container);

        container.createObject("Shell.Explorer"); // Create Browser component by its ProgID

        final Automation webBrowserAutomation = new Automation(container.getOleObject());
        webBrowserAutomation.invoke("Navigate2", new Object[]{"www.teamdev.com"});

        Variant documentVariant = webBrowserAutomation.getProperty("Document");
        IDispatch document = documentVariant.getPdispVal();
        System.out.println("document = " + document.isNull());

        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                // Show browser component, when parent widnow is shown
                container.doVerb(OleVerbs.SHOW);
            }

            public void windowClosing(WindowEvent e) {
                // Destroy the object when window is closed
                container.destroyObject();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        final IEAutomationSample sample = new IEAutomationSample();
        sample.setSize(800, 600);
        sample.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sample.setLocationRelativeTo(null);
        sample.setVisible(true);
    }
}
