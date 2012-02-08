package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM record StreamingContext.
 */
public class StreamingContext extends Structure
{
    private IUnknown _m_additionalContext;
    private StreamingContextStates _m_state;

    public StreamingContext()
    {
            _m_additionalContext = new IUnknownImpl();
            _m_state = new StreamingContextStates();

        init();
    }

    public StreamingContext(StreamingContext that)
    {
        _m_additionalContext = (IUnknown) ( (Parameter)  that._m_additionalContext).clone();
        _m_state = (StreamingContextStates) (  that._m_state).clone();

        init();
    }

    private void init()
    {
        init(
            new Parameter[] {
               (Parameter)  _m_additionalContext,
                _m_state
            },
            (short)4
        );
    }

    public IUnknown getM_additionalContext()
    {
        return  _m_additionalContext;
    }

    public StreamingContextStates getM_state()
    {
        return  _m_state;
    }

    public Object clone()
    {
        StreamingContext copy = new StreamingContext();
        copy.initFrom(this);
        
        return copy;
    }}