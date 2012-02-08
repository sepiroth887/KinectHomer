package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;

/**
 * Represents COM record RuntimeTypeHandle.
 */
public class RuntimeTypeHandle extends Structure
{
    private IUnknown _m_type;

    public RuntimeTypeHandle()
    {
            _m_type = new IUnknownImpl();

        init();
    }

    public RuntimeTypeHandle(RuntimeTypeHandle that)
    {
        _m_type = (IUnknown) ( (Parameter)  that._m_type).clone();

        init();
    }

    private void init()
    {
        init(
            new Parameter[] {
               (Parameter)  _m_type
            },
            (short)4
        );
    }

    public IUnknown getM_type()
    {
        return  _m_type;
    }

    public Object clone()
    {
        RuntimeTypeHandle copy = new RuntimeTypeHandle();
        copy.initFrom(this);
        
        return copy;
    }}