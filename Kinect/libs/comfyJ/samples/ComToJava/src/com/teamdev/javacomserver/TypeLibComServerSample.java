/*
 * Copyright (c) 2000-2011 TeamDev Ltd. All rights reserved.
 * Use is subject to license terms.
 */

package com.teamdev.javacomserver;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Int;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.DispatchComServer;
import com.jniwrapper.win32.com.IPersist;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.IID;
import com.teamdev.javacomserver.INativeParamSample;
import com.teamdev.javacomserver.ISample2;

/**
 * This is the sample Java Com Server.You may register it using the ServerManager
 * utility and call its methods from any COM server client.
 */
public class TypeLibComServerSample extends DispatchComServer implements IPersist, ISample, ISample2, INativeParamSample
{
    public static final CLSID COM_SERVER_CLSID = new CLSID("{D3C25C01-1B91-46DB-8F49-1383FF1DFCA5}");
    public static final IID TYPE_LIBRARY_IDENTIFIER = new IID("{B076AA4C-238D-48A6-8381-2EFBA353E43F}");
    public static final String PROG_ID = "typelib.comtojavasample.1";
    public static final String VERSION_INDEPENDENT_PROG_ID = "typelib.comtojavasample";
    public static final String COM_SERVER_DESCRIPTION = "test.TypeLibComServerSample is the sample of the Java COM Server usage with type library";

    private BStr progID = new BStr(PROG_ID);
    private BStr versionIndependentProgId = new BStr(VERSION_INDEPENDENT_PROG_ID);
    private BStr comServerDescription = new BStr(COM_SERVER_DESCRIPTION);

    public TypeLibComServerSample(CoClassMetaInfo classImpl) {
        super(classImpl);

        registerMethods(ISample.class);
        registerMethods(ISample2.class);
        registerMethods(INativeParamSample.class);
    }

    public void getClassID(CLSID pClassID) throws ComException {
        pClassID.setData1(COM_SERVER_CLSID.getData1());
        pClassID.setData2(COM_SERVER_CLSID.getData2());
        pClassID.setData3(COM_SERVER_CLSID.getData3());
        pClassID.setData4(COM_SERVER_CLSID.getData4());
    }

    public BStr getProgId() {
        return progID;
    }

    public BStr getVersionIndependentProgId() {
        return versionIndependentProgId;
    }

    public BStr getComServerDescription() {
        return comServerDescription;
    }

    public void nativeMethod1(int param) {
        System.out.println("nativeMethod1. param = " + param);
    }

    public void nativeMethod2(String param1, String param2) {
        System.out.println("nativeMethod2. param1 = " + param1 + " param2 = " + param2);
    }

    public void voidMethod() {
        System.out.println("voidMethod.");
    }

    public void oneParamMethod(UInt32 param) {
        System.out.println("oneParamMethod. param = " + param);
    }

    public void threeParamMethod(UInt32 param1, BStr param2, DoubleFloat param3) {
        System.out.println("threeParamMethod. param1 = " + param1 + " param2 = " + param2 + " param3 = " + param3);
    }

    public BStr stringReturnMethod() {
        System.out.println("stringReturnMethod.");
        return new BStr("stringReturnMethod result");
    }

    public Int intReturnMethodWithParam(BStr param) {
        System.out.println("stringReturnMethod. param = " + param + " returning 12345");
        return new Int(12345);
    }

    public ISample2 getSample2() {
        return new ISample2Impl(this);
    }

    public void sample2Method() {
        System.out.println("sample2 method.");
    }
}