package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration MethodImplAttributes.
 */
public class MethodImplAttributes extends ComEnumeration
{
    public static final int MethodImplAttributes_CodeTypeMask = 3;
    public static final int MethodImplAttributes_IL = 0;
    public static final int MethodImplAttributes_Native = 1;
    public static final int MethodImplAttributes_OPTIL = 2;
    public static final int MethodImplAttributes_Runtime = 3;
    public static final int MethodImplAttributes_ManagedMask = 4;
    public static final int MethodImplAttributes_Unmanaged = 4;
    public static final int MethodImplAttributes_Managed = 0;
    public static final int MethodImplAttributes_ForwardRef = 16;
    public static final int MethodImplAttributes_PreserveSig = 0x80;
    public static final int MethodImplAttributes_InternalCall = 0x1000;
    public static final int MethodImplAttributes_Synchronized = 32;
    public static final int MethodImplAttributes_NoInlining = 8;
    public static final int MethodImplAttributes_NoOptimization = 0x40;
    public static final int MethodImplAttributes_MaxMethodImplVal = 0xffff;

    public MethodImplAttributes()
    {
    }

    public MethodImplAttributes(long val)
    {
        super(val);
    }

    public MethodImplAttributes(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MethodImplAttributes(this);
    }
}