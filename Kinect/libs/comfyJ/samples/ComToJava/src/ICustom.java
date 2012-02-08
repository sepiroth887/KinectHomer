/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;

/**
 * @author Vladimir Kondrashchenko
 */
public interface ICustom extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{13292FC1-9F07-45ef-A444-152BF217982F}";

    public void setString(BStr value, Int32 number);

    public BStr getString();
}