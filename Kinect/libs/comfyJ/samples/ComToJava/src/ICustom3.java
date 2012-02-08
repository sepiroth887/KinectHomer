/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.IDispatch;

/**
 * @author Vladimir Kondrashchenko
 */
public interface ICustom3 extends IDispatch
{
    public BStr getBstr();
}