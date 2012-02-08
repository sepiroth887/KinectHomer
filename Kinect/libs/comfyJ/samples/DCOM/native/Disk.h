// Disk.h : Declaration of the CDisk

#ifndef __DISK_H_
#define __DISK_H_

#include "resource.h"       // main symbols

/////////////////////////////////////////////////////////////////////////////
// CDisk
class ATL_NO_VTABLE CDisk : 
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<CDisk, &CLSID_Disk>,
	public IDispatchImpl<IDisk, &IID_IDisk, &LIBID_DCOMSERVERLib>
{
public:
	CDisk()
	{
	}

DECLARE_REGISTRY_RESOURCEID(IDR_DISK)

DECLARE_PROTECT_FINAL_CONSTRUCT()

BEGIN_COM_MAP(CDisk)
	COM_INTERFACE_ENTRY(IDisk)
	COM_INTERFACE_ENTRY(IDispatch)
END_COM_MAP()

// IDisk
public:
	STDMETHOD (GetDFreeSpace)(BSTR disk, __int64* space);
};

#endif //__DISK_H_
