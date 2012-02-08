// ComTest.cpp : Implementation of CComTest
#include "stdafx.h"
#include "ComSample.h"
#include "ComTest.h"

/////////////////////////////////////////////////////////////////////////////
// CComTest


STDMETHODIMP CComTest::getString(BSTR *result)
{
	*result = SysAllocString(L"JNIWrapper test");

	return S_OK;
}

STDMETHODIMP CComTest::getInteger(int *result)
{
	*result = 2004;

	return S_OK;
}

STDMETHODIMP CComTest::getVariant(VARIANT *result)
{
	VariantInit(result);
	result->vt = VT_BSTR;
	result->bstrVal = SysAllocString(L"Variant test");

	return S_OK;
}
