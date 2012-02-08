using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace JavaTypeLibTest
{
    class Program
    {
        static void Main(string[] args)
        {
            //create object
            TypeLibComServerSampleLib.TypeLibComServerSample sampleObject = new TypeLibComServerSampleLib.TypeLibComServerSample();

            //call methods via default interface
            sampleObject.voidMethod();

            //obtain interface reference
            TypeLibComServerSampleLib.ISample sample = sampleObject as TypeLibComServerSampleLib.ISample;

            //call interface methods
            sample.voidMethod();
            sample.oneParamMethod(1010);
            sample.threeParamMethod(1234, "test string", 12.223);

            //methods with return value
            String stringValue = sample.stringReturnMethod();
            int intValue = sample.intReturnMethodWithParam(stringValue);

            //obtain interface reference (via interface method)
            TypeLibComServerSampleLib.ISample2 sample2 = sample.getSample2();
            sample2.sample2Method();

            //obtain interface reference
            TypeLibComServerSampleLib.INativeParamSample nativeParam = sampleObject as TypeLibComServerSampleLib.INativeParamSample;

            //calling interface with Java native parameter types
            nativeParam.nativeMethod1(123);
            nativeParam.nativeMethod2("string 1", "string 2");
        }
    }
}
