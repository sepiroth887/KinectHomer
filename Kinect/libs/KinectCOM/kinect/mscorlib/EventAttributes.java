package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration EventAttributes.
 */
public class EventAttributes extends ComEnumeration
{
    public static final int EventAttributes_None = 0;
    public static final int EventAttributes_SpecialName = 0x200;
    public static final int EventAttributes_ReservedMask = 0x400;
    public static final int EventAttributes_RTSpecialName = 0x400;

    public EventAttributes()
    {
    }

    public EventAttributes(long val)
    {
        super(val);
    }

    public EventAttributes(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new EventAttributes(this);
    }
}