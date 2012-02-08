import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.DispatchComServer;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.types.CLSID;

/**
 * @author Serge Piletsky
 */
public class TestComServer extends DispatchComServer implements TestComInterface {

    public static final CLSID COM_SERVER_CLSID = new CLSID("{A35B432E-5274-4146-9858-111111222222}");
    public static final String PROG_ID = "jniwrapper.testcomserver.1";
    public static final String VERSION_INDEPENDENT_PROG_ID = "jniwrapper.testcomserver";
    public static final String COM_SERVER_DESCRIPTION = "Test Com Server";

    public TestComServer(CoClassMetaInfo classImpl) {
        super(classImpl);
    }

    public void myMethod(BStr param1, Int32 param2, BStr param3, VariantBool param4) {
        System.out.println("TestComServer.myMethod: param1 = " + param1 + "; param2 = " + param2 + "; param3 = " + param3 + "; param4 = " + param4);
        param4.setBooleanValue(false);
    }
}