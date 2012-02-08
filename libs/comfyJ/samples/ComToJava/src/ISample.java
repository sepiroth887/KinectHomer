/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.DoubleFloat;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.SafeArray;
import com.jniwrapper.win32.automation.types.Variant;

public interface ISample extends IDispatch
{
    DoubleFloat add(DoubleFloat a, DoubleFloat b);

    DoubleFloat subtract(DoubleFloat a, DoubleFloat b);

    BStr getProgId();

    BStr getVersionIndependentProgId();

    BStr getComServerDescription();

    SafeArray getSafeArray();

    Variant getEmptyVariant();

    Variant getIntegerVariant();
}