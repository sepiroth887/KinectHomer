package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;

/**
 * Represents COM record RuntimeMethodHandle.
 */
public class RuntimeMethodHandle extends Structure
{
    private IUnknown _m_value;

    public RuntimeMethodHandle()
    {
            _m_value = new IUnknownImpl();

        init();
    }

    public RuntimeMethodHandle(RuntimeMethodHandle that)
    {
        _m_value = (IUnknown) ( (Parameter)  that._m_value).clone();

        init();
    }

    private void init()
    {
        init(
            new Parameter[] {
               (Parameter)  _m_value
            },
            (short)4
        );
    }

    public IUnknown getM_value()
    {
        return  _m_value;
    }

    public Object clone()
    {
        RuntimeMethodHandle copy = new RuntimeMethodHandle();
        copy.initFrom(this);
        
        return copy;
    }}