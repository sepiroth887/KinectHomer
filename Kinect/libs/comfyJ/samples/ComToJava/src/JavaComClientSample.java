/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.SafeArray;
import com.jniwrapper.win32.automation.types.VarType;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.impl.IPersistImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;

/**
 * This is the sample Java Com Server client. It shows the usage of the {@link JavaComServerSample} class after
 * registering it with the help of the ServerManager utility or
 * the {@link com.jniwrapper.win32.tools.RegisterDispatchComServer} class.
 */
public class JavaComClientSample
{
    private static final String ADD_METHOD_NAME = "add";
    private static final String GET_PROG_ID_METHOD_NAME = "getProgId";
    private static final String GET_VERSION_INDEPENDENT_PROG_ID_METHOD_NAME = "getVersionIndependentProgId";
    private static final String GET_COM_SERVER_DESCRIPTION_METHOD_NAME = "getComServerDescription";
    private static final String GET_SAFE_ARRAY_METHOD_NAME = "getSafeArray";
    private static final String GET_EMPTY_VARIANT_METHOD_NAME = "getEmptyVariant";
    private static final String GET_INTEGER_VARIANT_METHOD_NAME = "getIntegerVariant";

    public static void main(String[] args) throws Exception
    {
        Variant variant = new Variant(new Variant(100));
        variant.getVt().setValue(VarType.VT_VARIANT);
//        System.out.println("variant.getValue() = " + variant.getValue());

        Runnable runnable = new Runnable()
        {
            public void run()
            {
                try
                {
                    IDispatch dispatch = new IDispatchImpl(CLSID.createFromProgID("jniwrapper.comtojavasample"),
                            ClsCtx.LOCAL_SERVER);

                    IPersistImpl p = new IPersistImpl(dispatch);
                    CLSID clsid = new CLSID();
                    p.getClassID(clsid);
                    System.out.println("ClassID = " + clsid);

                    Variant result = new Variant();
                    Automation automation = new Automation(dispatch);

                    Variant left = new Variant(10.0);
                    Variant right = new Variant(20.0);
                    result = automation.invoke(ADD_METHOD_NAME, new Variant[]{left, right});
                    System.out.println(left.getValue() + " + " + right.getValue() + " = " + result.getDblVal());

                    result = automation.invoke(GET_PROG_ID_METHOD_NAME);
                    System.out.println("PROG_ID = " + result.getValue());

                    result = automation.invoke(GET_VERSION_INDEPENDENT_PROG_ID_METHOD_NAME);
                    System.out.println("VERSION_INDEPENDENT_PROG_ID = " + result.getValue());

                    result = automation.invoke(GET_COM_SERVER_DESCRIPTION_METHOD_NAME);
                    System.out.println("COM_SERVER_DESCRIPTION = " + result.getValue());


                    result = automation.invoke(GET_SAFE_ARRAY_METHOD_NAME);
                    SafeArray safeArray = result.getSafeArray();
                    System.out.println(GET_SAFE_ARRAY_METHOD_NAME + ":");
                    for (int i = 0; i < safeArray.getCount(); i++)
                    {
                        Object o = safeArray.get(i);
                        System.out.println("Element # " + i + " = " + o);
                    }

                    result = automation.invoke(GET_EMPTY_VARIANT_METHOD_NAME);
                    System.out.println(GET_EMPTY_VARIANT_METHOD_NAME + " = " + result.getValue());

                    result = automation.invoke(GET_INTEGER_VARIANT_METHOD_NAME);
                    System.out.println(GET_INTEGER_VARIANT_METHOD_NAME + " = " + result.getValue());
                }
                catch (ComException e)
                {
                    e.printStackTrace();
                }
            }
        };
        OleMessageLoop.invokeAndWait(runnable);
        OleMessageLoop.stop();
    }
}