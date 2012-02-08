package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration MemberTypes.
 */
public class MemberTypes extends ComEnumeration
{
    public static final int MemberTypes_Constructor = 1;
    public static final int MemberTypes_Event = 2;
    public static final int MemberTypes_Field = 4;
    public static final int MemberTypes_Method = 8;
    public static final int MemberTypes_Property = 16;
    public static final int MemberTypes_TypeInfo = 32;
    public static final int MemberTypes_Custom = 0x40;
    public static final int MemberTypes_NestedType = 0x80;
    public static final int MemberTypes_All = 0xbf;

    public MemberTypes()
    {
    }

    public MemberTypes(long val)
    {
        super(val);
    }

    public MemberTypes(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MemberTypes(this);
    }
}