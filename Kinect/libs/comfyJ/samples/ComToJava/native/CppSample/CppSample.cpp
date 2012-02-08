
#include "stdafx.h"

//author: Alexey Razoryonov
//This is the sample of the Java Com Server usage.
//It demonstrates how to create an instance of Java Com Server and work with it using both
//COM VTBL and Automation technologies. 

int _tmain(int argc, _TCHAR* argv[])
{
	HRESULT  hr;

    // Initialize COM.
    CoInitialize(NULL);

	LPCOLESTR progID = OLESTR("jniwrapper.comtojavasample");
	LPCLSID clsID = new CLSID();
	CLSIDFromProgID(progID, clsID);

	//Working with Java COM Server via the IPersist interface
	IPersist* iPersistInst;
	hr = CoCreateInstance(*clsID,
                           NULL,
                           CLSCTX_LOCAL_SERVER,
						   IID_IPersist,
                           (void **)&iPersistInst);

	if (SUCCEEDED(hr))
		printf("The instance of IPersist interface was succesfully created.\n");
	else 
		printf("Error while trying to create the instance of the IPersist interface.\n");

	CLSID resultClsID;
	iPersistInst->GetClassID(&resultClsID);
	LPOLESTR stringClsID;
	StringFromCLSID(resultClsID , &stringClsID);
	wprintf(L"GetClassID = %s%s", (wchar_t*)stringClsID, "\n");

	//Working with Java COM Server via the IDispatch interface
	IDispatch* iDispatchInst;
	hr = CoCreateInstance(*clsID,
                           NULL,
                           CLSCTX_LOCAL_SERVER,
						   IID_IDispatch,
                           (void **)&iDispatchInst);
	if (SUCCEEDED(hr))
		printf("The instance of IDispatch interface was succesfully created.\n");
	else 
		printf("Error while trying to create the instance of the IDispatch interface.\n");

	DISPID dispid;
	VARIANT result;
	OLECHAR* methodName;
	DISPPARAMS dispparamsNoArgs = {NULL, NULL, 0, 0};

	//Calling the "add" method
	methodName = L"add";
	iDispatchInst->GetIDsOfNames(IID_NULL, &methodName, 1, GetUserDefaultLCID(), &dispid);
	
	VARIANTARG* arguments = new VARIANTARG[2];
	
	VARIANTARG varg;
	VariantInit(&varg); 
	varg.vt = VT_R8; 
	varg.dblVal = 10.5; 
	arguments[0] = varg;
	
	VARIANTARG varg2;
	VariantInit(&varg2); 
	varg2.vt = VT_R8; 
	varg2.dblVal = 12.5; 
	arguments[1] = varg2;

	DISPPARAMS parameters;
	parameters.cArgs = 2; 
	parameters.rgvarg = arguments; 
	parameters.cNamedArgs = 0; 
	parameters.rgdispidNamedArgs = NULL;

	iDispatchInst->Invoke(dispid,
						IID_NULL,
						GetUserDefaultLCID(),
						DISPATCH_METHOD,
						&parameters, 
						&result, 
						NULL, 
						NULL);
	printf("%f + %f  = %f \n", varg.dblVal, varg2.dblVal, result.dblVal);

	//Calling the "getProgID" method
	methodName = L"getProgId";
	iDispatchInst->GetIDsOfNames(IID_NULL,&methodName,1,GetUserDefaultLCID(),&dispid);
	iDispatchInst->Invoke(dispid,
						IID_NULL,
						GetUserDefaultLCID(),
						DISPATCH_METHOD,
						&dispparamsNoArgs, 
						&result, 
						NULL, 
						NULL);
	wprintf(L"ProgID = %s%s", (wchar_t*)result.bstrVal, "\n");

	//Calling the "getVersionIndependentProgId" method
	methodName = L"getVersionIndependentProgId";
	iDispatchInst->GetIDsOfNames(IID_NULL,&methodName,1,GetUserDefaultLCID(),&dispid);
	iDispatchInst->Invoke(dispid,
						IID_NULL,
						GetUserDefaultLCID(),
						DISPATCH_METHOD,
						&dispparamsNoArgs, 
						&result, 
						NULL, 
						NULL);
	wprintf(L"VersionIndependentProgID = %s%s", (wchar_t*)result.bstrVal, "\n");

	//Calling the "getComServerDescription" method
	methodName = L"getComServerDescription";
	iDispatchInst->GetIDsOfNames(IID_NULL,&methodName,1,GetUserDefaultLCID(),&dispid);
	iDispatchInst->Invoke(dispid,
						IID_NULL,
						GetUserDefaultLCID(),
						DISPATCH_METHOD,
						&dispparamsNoArgs, 
						&result, 
						NULL, 
						NULL);
	wprintf(L"ComServerDescription = %s%s", (wchar_t*)result.bstrVal, "\n");

	//Calling the "getSafeArray" method
	arguments = new VARIANTARG[1];
	
	VARIANTARG vargNew;
	VariantInit(&vargNew); 
	vargNew.vt = VT_I8; 
	vargNew.intVal = 10; 
	arguments[0] = vargNew;

	parameters.cArgs = 1; 
	parameters.rgvarg = arguments; 
	parameters.cNamedArgs = 0; 
	parameters.rgdispidNamedArgs = NULL;


	methodName = L"getSafeArray";
	iDispatchInst->GetIDsOfNames(IID_NULL,&methodName,1,GetUserDefaultLCID(),&dispid);
	iDispatchInst->Invoke(dispid,
						IID_NULL,
						GetUserDefaultLCID(),
						DISPATCH_METHOD,
						&dispparamsNoArgs, 
						&result, 
						NULL, 
						NULL);	
	SAFEARRAY* safe = result.parray;

	int elementCount = safe->rgsabound->cElements;
	VARIANT* FAR rgvar = new VARIANT[elementCount];
	for(unsigned int k = 0; k < elementCount; ++k)
	    VariantInit(&rgvar[k]);

	for(long i=0; i < elementCount; i++)
	{
		SafeArrayGetElement(safe, &i, &rgvar[i]);
		wprintf(L"SafeArray [%i] = %s \n", i, rgvar[i], "\n");
	}

	for(i = 0; i < elementCount; ++i)
		VariantClear(&rgvar[i]);

	
	//Calling the "getEmptyVariant" method

	methodName = L"getEmptyVariant";
	iDispatchInst->GetIDsOfNames(IID_NULL,&methodName,1,GetUserDefaultLCID(),&dispid);
	iDispatchInst->Invoke(dispid,
						IID_NULL,
						GetUserDefaultLCID(),
						DISPATCH_METHOD,
						&dispparamsNoArgs, 
						&result, 
						NULL, 
						NULL);	
	VARIANT* variant = result.pvarVal;
	
	wprintf(L"getEmptyVariant = %s \n", variant, "\n");

	//Calling the "getIntegerVariant" method

	methodName = L"getIntegerVariant";
	iDispatchInst->GetIDsOfNames(IID_NULL,&methodName,1,GetUserDefaultLCID(),&dispid);
	iDispatchInst->Invoke(dispid,
						IID_NULL,
						GetUserDefaultLCID(),
						DISPATCH_METHOD,
						&dispparamsNoArgs, 
						&result, 
						NULL, 
						NULL);	
	int intVariantValue = result.intVal;
	
	wprintf(L"getIntegerVariant = %i \n", intVariantValue, "\n");

	// Free COM resources.
    CoUninitialize();

	std::cin.get();
	return 0;
}

