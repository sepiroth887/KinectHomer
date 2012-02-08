/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.DoubleFloat;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.SafeArray;
import com.jniwrapper.win32.automation.types.SafeArrayBound;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.DispatchComServer;
import com.jniwrapper.win32.com.IPersist;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.types.CLSID;

/**
 * This is the sample Java Com Server.You may register it using the ServerManager
 * utility and call its methods from any COM server client.
 *
 */
public class JavaComServerSample extends DispatchComServer implements IPersist, ISample
{
    public static final CLSID COM_SERVER_CLSID = new CLSID("{A35B432E-5274-4146-9858-638313EDCEA6}");
    public static final String PROG_ID = "jniwrapper.comtojavasample.1";
    public static final String VERSION_INDEPENDENT_PROG_ID = "jniwrapper.comtojavasample";
    public static final String COM_SERVER_DESCRIPTION = "JavaComServerSample is the sampple of the Java COM Server.";

    private BStr progID = new BStr(PROG_ID);
    private BStr versionIndependentProgId = new BStr(VERSION_INDEPENDENT_PROG_ID);
    private BStr comServerDescription = new BStr(COM_SERVER_DESCRIPTION);

    public JavaComServerSample(CoClassMetaInfo classImpl)
    {
        super(classImpl);
    }

    /**
     * This method retrieves the class identifier (CLSID) of an object.
     *
     * @param pClassID the class identifier (CLSID) of an object
     * @throws ComException
     */
    public void getClassID(CLSID pClassID) throws ComException
    {
        pClassID.setData1(COM_SERVER_CLSID.getData1());
        pClassID.setData2(COM_SERVER_CLSID.getData2());
        pClassID.setData3(COM_SERVER_CLSID.getData3());
        pClassID.setData4(COM_SERVER_CLSID.getData4());
    }

    public BStr getProgId()
    {
        return progID;
    }

    public BStr getVersionIndependentProgId()
    {
        return versionIndependentProgId;
    }

    public BStr getComServerDescription()
    {
        return comServerDescription;
    }

    /**
     * This method returns the sum of two numbers.
     *
     * @param a
     * @param b
     * @return the sum of two numbers.
     */
    public DoubleFloat add(DoubleFloat a, DoubleFloat b)
    {
        double left = a.getValue();
        double right = b.getValue();
        return new DoubleFloat(left + right);
    }

    /**
     * This method returns the difference of two numbers.
     *
     * @param a
     * @param b
     * @return the difference of two numbers.
     */
    public DoubleFloat subtract(DoubleFloat a, DoubleFloat b)
    {
        double left = a.getValue();
        double right = b.getValue();
        return new DoubleFloat(left - right);
    }

    /**
     * This method returns the SAFEARRAY of three BStrings.
     *
     * @return SAFEARRAY of three BStrings: {"The First Element", "The Second Element", "The Third Element"}
     */
    public SafeArray getSafeArray()
    {
        String[] aStrings = {"The First Element", "The Second Element", "The Third Element"};
        SafeArray result = new SafeArray(new
                SafeArrayBound(aStrings.length, 0), BStr.class);
        for (int nIdx = 0; nIdx < aStrings.length; nIdx++)
        {
            result.set(nIdx, new BStr(aStrings[nIdx]));
        }
        result.setAutoDelete(false);

        return result;
    }

    /**
     * This method returns the Variant with the VT_EMPTY type.
     *
     * @return the Variant with the VT_EMPTY type.
     */
    public Variant getEmptyVariant()
    {
        return new Variant();
    }

    /**
     * This method returns the Variant with the VT_I4 type and value equal to 10.
     *
     * @return the Variant with the VT_I4 type and value equal to 10.
     */
    public Variant getIntegerVariant()
    {
        int value = 10;
        return new Variant(value);
    }

}