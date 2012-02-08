package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration BindingFlags.
 */
public class BindingFlags extends ComEnumeration
{
    public static final int BindingFlags_Default = 0;
    public static final int BindingFlags_IgnoreCase = 1;
    public static final int BindingFlags_DeclaredOnly = 2;
    public static final int BindingFlags_Instance = 4;
    public static final int BindingFlags_Static = 8;
    public static final int BindingFlags_Public = 16;
    public static final int BindingFlags_NonPublic = 32;
    public static final int BindingFlags_FlattenHierarchy = 0x40;
    public static final int BindingFlags_InvokeMethod = 0x100;
    public static final int BindingFlags_CreateInstance = 0x200;
    public static final int BindingFlags_GetField = 0x400;
    public static final int BindingFlags_SetField = 0x800;
    public static final int BindingFlags_GetProperty = 0x1000;
    public static final int BindingFlags_SetProperty = 0x2000;
    public static final int BindingFlags_PutDispProperty = 0x4000;
    public static final int BindingFlags_PutRefDispProperty = 0x8000;
    public static final int BindingFlags_ExactBinding = 0x10000;
    public static final int BindingFlags_SuppressChangeType = 0x20000;
    public static final int BindingFlags_OptionalParamBinding = 0x40000;
    public static final int BindingFlags_IgnoreReturn = 0x1000000;

    public BindingFlags()
    {
    }

    public BindingFlags(long val)
    {
        super(val);
    }

    public BindingFlags(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new BindingFlags(this);
    }
}