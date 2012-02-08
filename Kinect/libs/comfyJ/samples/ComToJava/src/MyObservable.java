/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.jniwrapper.Int;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.Variant;

/**
 * Observable dispatch interface.
 *
 * @author Serge Piletsly
 */
public interface MyObservable extends IDispatch {

    public void addObserver(IDispatch observer);

    public void notifyObservers(Variant value);

    public Int countObservers();

    public void simulateEventsFromJava();
}