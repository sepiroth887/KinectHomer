package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration FieldAttributes.
 */
public class FieldAttributes extends ComEnumeration
{
    public static final int FieldAttributes_FieldAccessMask = 7;
    public static final int FieldAttributes_PrivateScope = 0;
    public static final int FieldAttributes_Private = 1;
    public static final int FieldAttributes_FamANDAssem = 2;
    public static final int FieldAttributes_Assembly = 3;
    public static final int FieldAttributes_Family = 4;
    public static final int FieldAttributes_FamORAssem = 5;
    public static final int FieldAttributes_Public = 6;
    public static final int FieldAttributes_Static = 16;
    public static final int FieldAttributes_InitOnly = 32;
    public static final int FieldAttributes_Literal = 0x40;
    public static final int FieldAttributes_NotSerialized = 0x80;
    public static final int FieldAttributes_SpecialName = 0x200;
    public static final int FieldAttributes_PinvokeImpl = 0x2000;
    public static final int FieldAttributes_ReservedMask = 0x9500;
    public static final int FieldAttributes_RTSpecialName = 0x400;
    public static final int FieldAttributes_HasFieldMarshal = 0x1000;
    public static final int FieldAttributes_HasDefault = 0x8000;
    public static final int FieldAttributes_HasFieldRVA = 0x100;

    public FieldAttributes()
    {
    }

    public FieldAttributes(long val)
    {
        super(val);
    }

    public FieldAttributes(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new FieldAttributes(this);
    }
}