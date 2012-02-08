import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.Int32;

public interface TestComInterface extends IDispatch {
    
    void myMethod(BStr param1, Int32 param2, BStr param3, VariantBool param4);
    
}
