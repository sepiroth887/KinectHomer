package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration StreamingContextStates.
 */
public class StreamingContextStates extends ComEnumeration
{
    public static final int StreamingContextStates_CrossProcess = 1;
    public static final int StreamingContextStates_CrossMachine = 2;
    public static final int StreamingContextStates_File = 4;
    public static final int StreamingContextStates_Persistence = 8;
    public static final int StreamingContextStates_Remoting = 16;
    public static final int StreamingContextStates_Other = 32;
    public static final int StreamingContextStates_Clone = 0x40;
    public static final int StreamingContextStates_CrossAppDomain = 0x80;
    public static final int StreamingContextStates_All = 0xff;

    public StreamingContextStates()
    {
    }

    public StreamingContextStates(long val)
    {
        super(val);
    }

    public StreamingContextStates(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new StreamingContextStates(this);
    }
}