package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.mscorlib.impl.*;

/**
 * Represents COM record InterfaceMapping.
 */
public class InterfaceMapping extends Structure
{
    private _Type _targetType;
    private _Type _interfaceType;
    private SafeArray _targetMethods;
    private SafeArray _interfaceMethods;

    public InterfaceMapping()
    {
            _targetType = new _TypeImpl();
            _interfaceType = new _TypeImpl();
            _targetMethods = new SafeArray();
            _interfaceMethods = new SafeArray();

        init();
    }

    public InterfaceMapping(InterfaceMapping that)
    {
        _targetType = (_Type) ( (Parameter)  that._targetType).clone();
        _interfaceType = (_Type) ( (Parameter)  that._interfaceType).clone();
        _targetMethods = (SafeArray) (  that._targetMethods).clone();
        _interfaceMethods = (SafeArray) (  that._interfaceMethods).clone();

        init();
    }

    private void init()
    {
        init(
            new Parameter[] {
               (Parameter)  _targetType,
               (Parameter)  _interfaceType,
                _targetMethods,
                _interfaceMethods
            },
            (short)4
        );
    }

    public _Type getTargetType()
    {
        return  _targetType;
    }

    public _Type getInterfaceType()
    {
        return  _interfaceType;
    }

    public SafeArray getTargetMethods()
    {
        return  _targetMethods;
    }

    public SafeArray getInterfaceMethods()
    {
        return  _interfaceMethods;
    }

    public Object clone()
    {
        InterfaceMapping copy = new InterfaceMapping();
        copy.initFrom(this);
        
        return copy;
    }}