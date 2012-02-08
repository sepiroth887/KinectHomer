package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;

/**
 * Represents COM record RuntimeFieldHandle.
 */
public class RuntimeFieldHandle extends Structure
{
    private IUnknown _m_ptr;

    public RuntimeFieldHandle()
    {
            _m_ptr = new IUnknownImpl();

        init();
    }

    public RuntimeFieldHandle(RuntimeFieldHandle that)
    {
        _m_ptr = (IUnknown) ( (Parameter)  that._m_ptr).clone();

        init();
    }

    private void init()
    {
        init(
            new Parameter[] {
               (Parameter)  _m_ptr
            },
            (short)4
        );
    }

    public IUnknown getM_ptr()
    {
        return  _m_ptr;
    }

    public Object clone()
    {
        RuntimeFieldHandle copy = new RuntimeFieldHandle();
        copy.initFrom(this);
        
        return copy;
    }}