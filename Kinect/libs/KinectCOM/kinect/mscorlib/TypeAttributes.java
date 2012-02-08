package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration TypeAttributes.
 */
public class TypeAttributes extends ComEnumeration
{
    public static final int TypeAttributes_VisibilityMask = 7;
    public static final int TypeAttributes_NotPublic = 0;
    public static final int TypeAttributes_Public = 1;
    public static final int TypeAttributes_NestedPublic = 2;
    public static final int TypeAttributes_NestedPrivate = 3;
    public static final int TypeAttributes_NestedFamily = 4;
    public static final int TypeAttributes_NestedAssembly = 5;
    public static final int TypeAttributes_NestedFamANDAssem = 6;
    public static final int TypeAttributes_NestedFamORAssem = 7;
    public static final int TypeAttributes_LayoutMask = 24;
    public static final int TypeAttributes_AutoLayout = 0;
    public static final int TypeAttributes_SequentialLayout = 8;
    public static final int TypeAttributes_ExplicitLayout = 16;
    public static final int TypeAttributes_ClassSemanticsMask = 32;
    public static final int TypeAttributes_Class = 0;
    public static final int TypeAttributes_Interface = 32;
    public static final int TypeAttributes_Abstract = 0x80;
    public static final int TypeAttributes_Sealed = 0x100;
    public static final int TypeAttributes_SpecialName = 0x400;
    public static final int TypeAttributes_Import = 0x1000;
    public static final int TypeAttributes_Serializable = 0x2000;
    public static final int TypeAttributes_StringFormatMask = 0x30000;
    public static final int TypeAttributes_AnsiClass = 0;
    public static final int TypeAttributes_UnicodeClass = 0x10000;
    public static final int TypeAttributes_AutoClass = 0x20000;
    public static final int TypeAttributes_CustomFormatClass = 0x30000;
    public static final int TypeAttributes_CustomFormatMask = 0xc00000;
    public static final int TypeAttributes_BeforeFieldInit = 0x100000;
    public static final int TypeAttributes_ReservedMask = 0x40800;
    public static final int TypeAttributes_RTSpecialName = 0x800;
    public static final int TypeAttributes_HasSecurity = 0x40000;

    public TypeAttributes()
    {
    }

    public TypeAttributes(long val)
    {
        super(val);
    }

    public TypeAttributes(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new TypeAttributes(this);
    }
}