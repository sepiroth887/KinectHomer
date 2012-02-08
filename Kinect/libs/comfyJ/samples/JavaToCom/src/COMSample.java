/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.automation.types.Variant;
import comsample.comsamplelib.ComTest;
import comsample.comsamplelib.IComTest;

/**
 * This sample creates COM object and calles its methods.
 *
 * <p>You should register dll containing the COM object:
 * <ul>
 *  <li>Start command line tool (like cmd)</li>
 *  <li>Go to bin folder of the sample</li>
 *  <li>Execute command: <br>
 * regsvr32 ComSample.dll</li>
 * </ul>
 *
 * <p>You can find implementation of COM methods in native folder of the sample.
 * These methods return constant BSTR, int, VARIANT values through output parameters.
 *
 * @author Alexei Orischenko
 */
public class COMSample
{
    public static void main(String[] args)
    {
        // Initialize COM system
        ComFunctions.coInitialize();

        // Create COM object
        IComTest comTest = ComTest.create(ClsCtx.INPROC_SERVER);

        String str = comTest.getString().getValue();
        System.out.println("str = " + str);

        int i = (int)comTest.getInteger().getValue();
        System.out.println("i = " + i);

        Variant v = comTest.getVariant();
        System.out.println("v = " + v.getValue());
    }
}