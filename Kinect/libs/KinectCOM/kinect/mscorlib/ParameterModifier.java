package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.automation.types.*;

/**
 * Represents COM record ParameterModifier.
 */
public class ParameterModifier extends Structure
{
    private SafeArray __byRef;

    public ParameterModifier()
    {
            __byRef = new SafeArray();

        init();
    }

    public ParameterModifier(ParameterModifier that)
    {
        __byRef = (SafeArray) (  that.__byRef).clone();

        init();
    }

    private void init()
    {
        init(
            new Parameter[] {
                __byRef
            },
            (short)4
        );
    }

    public SafeArray get_byRef()
    {
        return  __byRef;
    }

    public Object clone()
    {
        ParameterModifier copy = new ParameterModifier();
        copy.initFrom(this);
        
        return copy;
    }}