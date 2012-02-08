package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration PropertyAttributes.
 */
public class PropertyAttributes extends ComEnumeration
{
    public static final int PropertyAttributes_None = 0;
    public static final int PropertyAttributes_SpecialName = 0x200;
    public static final int PropertyAttributes_ReservedMask = 0xf400;
    public static final int PropertyAttributes_RTSpecialName = 0x400;
    public static final int PropertyAttributes_HasDefault = 0x1000;
    public static final int PropertyAttributes_Reserved2 = 0x2000;
    public static final int PropertyAttributes_Reserved3 = 0x4000;
    public static final int PropertyAttributes_Reserved4 = 0x8000;

    public PropertyAttributes()
    {
    }

    public PropertyAttributes(long val)
    {
        super(val);
    }

    public PropertyAttributes(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new PropertyAttributes(this);
    }
}