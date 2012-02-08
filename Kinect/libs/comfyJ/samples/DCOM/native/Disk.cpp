// Disk.cpp : Implementation of CDisk
#include "stdafx.h"
#include "DComServer.h"
#include "Disk.h"
#include <iostream>
#include ".\disk.h"

/////////////////////////////////////////////////////////////////////////////
// CDisk


	STDMETHODIMP CDisk::GetDFreeSpace(BSTR drive, __int64* space)
	{
	USES_CONVERSION ;
	
	PULARGE_INTEGER secperclu = new ULARGE_INTEGER;
	PULARGE_INTEGER bypersec = new ULARGE_INTEGER;
	PULARGE_INTEGER nooffreclu = new ULARGE_INTEGER;
	PULARGE_INTEGER totalFree = new ULARGE_INTEGER;
	
	GetDiskFreeSpaceEx ( OLE2T ( drive ), secperclu, bypersec, totalFree) ;
	*space = totalFree->QuadPart; 

	return S_OK ;
	}
