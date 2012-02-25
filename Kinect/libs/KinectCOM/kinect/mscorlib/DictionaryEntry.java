package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;

/**
 * Represents COM record DictionaryEntry.
 */
public class DictionaryEntry extends Structure
{
    private IUnknown __key;
    private IUnknown __value;

    public DictionaryEntry()
    {
            __key = new IUnknownImpl();
            __value = new IUnknownImpl();

        init();
    }

    public DictionaryEntry(DictionaryEntry that)
    {
        __key = (IUnknown) ( (Parameter)  that.__key).clone();
        __value = (IUnknown) ( (Parameter)  that.__value).clone();

        init();
    }

    private void init()
    {
        init(
            new Parameter[] {
               (Parameter)  __key,
               (Parameter)  __value
            },
            (short)4
        );
    }

    public IUnknown get_key()
    {
        return  __key;
    }

    public IUnknown get_value()
    {
        return  __value;
    }

    public Object clone()
    {
        DictionaryEntry copy = new DictionaryEntry();
        copy.initFrom(this);
        
        return copy;
    }}