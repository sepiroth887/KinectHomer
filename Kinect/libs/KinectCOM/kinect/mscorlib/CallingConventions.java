package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration CallingConventions.
 */
public class CallingConventions extends ComEnumeration
{
    public static final int CallingConventions_Standard = 1;
    public static final int CallingConventions_VarArgs = 2;
    public static final int CallingConventions_Any = 3;
    public static final int CallingConventions_HasThis = 32;
    public static final int CallingConventions_ExplicitThis = 0x40;

    public CallingConventions()
    {
    }

    public CallingConventions(long val)
    {
        super(val);
    }

    public CallingConventions(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new CallingConventions(this);
    }
}