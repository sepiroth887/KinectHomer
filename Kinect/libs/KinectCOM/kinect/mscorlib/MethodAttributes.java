package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration MethodAttributes.
 */
public class MethodAttributes extends ComEnumeration
{
    public static final int MethodAttributes_MemberAccessMask = 7;
    public static final int MethodAttributes_PrivateScope = 0;
    public static final int MethodAttributes_Private = 1;
    public static final int MethodAttributes_FamANDAssem = 2;
    public static final int MethodAttributes_Assembly = 3;
    public static final int MethodAttributes_Family = 4;
    public static final int MethodAttributes_FamORAssem = 5;
    public static final int MethodAttributes_Public = 6;
    public static final int MethodAttributes_Static = 16;
    public static final int MethodAttributes_Final = 32;
    public static final int MethodAttributes_Virtual = 0x40;
    public static final int MethodAttributes_HideBySig = 0x80;
    public static final int MethodAttributes_CheckAccessOnOverride = 0x200;
    public static final int MethodAttributes_VtableLayoutMask = 0x100;
    public static final int MethodAttributes_ReuseSlot = 0;
    public static final int MethodAttributes_NewSlot = 0x100;
    public static final int MethodAttributes_Abstract = 0x400;
    public static final int MethodAttributes_SpecialName = 0x800;
    public static final int MethodAttributes_PinvokeImpl = 0x2000;
    public static final int MethodAttributes_UnmanagedExport = 8;
    public static final int MethodAttributes_RTSpecialName = 0x1000;
    public static final int MethodAttributes_ReservedMask = 0xd000;
    public static final int MethodAttributes_HasSecurity = 0x4000;
    public static final int MethodAttributes_RequireSecObject = 0x8000;

    public MethodAttributes()
    {
    }

    public MethodAttributes(long val)
    {
        super(val);
    }

    public MethodAttributes(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MethodAttributes(this);
    }
}