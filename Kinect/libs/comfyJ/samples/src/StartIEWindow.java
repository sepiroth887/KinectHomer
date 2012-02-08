import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

public class StartIEWindow
{
    private static final CLSID CLSID_InternetExplorer = new CLSID("{0002DF01-0000-0000-C000-000000000046}");
    private static final IID IID_IWebBrowser2 = new IID("{D30C1661-CDAF-11D0-8A3E-00C04FC9E26E}");

    public static void main(String[] args) throws Exception {
        ComFunctions.coInitialize();

        IDispatch webBrowser = new IDispatchImpl();
        webBrowser.setAutoDelete(false);
        ComFunctions.coCreateInstance(CLSID_InternetExplorer, null, ClsCtx.LOCAL_SERVER, IID_IWebBrowser2, webBrowser);

        Automation browserAutomation = new Automation(webBrowser, true);
        browserAutomation.setDispInterface(true);
        browserAutomation.setProperty("Visible", Boolean.TRUE);
        browserAutomation.invoke("Navigate2", new Object[]{"www.teamdev.com"});

        System.out.println("Press 'Enter' to terminate IE application");
        System.in.read();

        browserAutomation.invoke("Quit");
        browserAutomation.release();
        webBrowser.setAutoDelete(false);
        webBrowser.release();
    }
}
