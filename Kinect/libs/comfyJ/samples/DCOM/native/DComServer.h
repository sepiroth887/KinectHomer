

/* this ALWAYS GENERATED file contains the definitions for the interfaces */


 /* File created by MIDL compiler version 6.00.0361 */
/* at Fri Apr 07 16:01:04 2006
 */
/* Compiler settings for .\DComServer.idl:
    Oicf, W1, Zp8, env=Win32 (32b run)
    protocol : dce , ms_ext, c_ext, robust
    error checks: allocation ref bounds_check enum stub_data 
    VC __declspec() decoration level: 
         __declspec(uuid()), __declspec(selectany), __declspec(novtable)
         DECLSPEC_UUID(), MIDL_INTERFACE()
*/
//@@MIDL_FILE_HEADING(  )

#pragma warning( disable: 4049 )  /* more than 64k source lines */


/* verify that the <rpcndr.h> version is high enough to compile this file*/
#ifndef __REQUIRED_RPCNDR_H_VERSION__
#define __REQUIRED_RPCNDR_H_VERSION__ 475
#endif

#include "rpc.h"
#include "rpcndr.h"

#ifndef __RPCNDR_H_VERSION__
#error this stub requires an updated version of <rpcndr.h>
#endif // __RPCNDR_H_VERSION__

#ifndef COM_NO_WINDOWS_H
#include "windows.h"
#include "ole2.h"
#endif /*COM_NO_WINDOWS_H*/

#ifndef __DComServer_h__
#define __DComServer_h__

#if defined(_MSC_VER) && (_MSC_VER >= 1020)
#pragma once
#endif

/* Forward Declarations */ 

#ifndef __IDisk_FWD_DEFINED__
#define __IDisk_FWD_DEFINED__
typedef interface IDisk IDisk;
#endif 	/* __IDisk_FWD_DEFINED__ */


#ifndef __Disk_FWD_DEFINED__
#define __Disk_FWD_DEFINED__

#ifdef __cplusplus
typedef class Disk Disk;
#else
typedef struct Disk Disk;
#endif /* __cplusplus */

#endif 	/* __Disk_FWD_DEFINED__ */


/* header files for imported files */
#include "oaidl.h"
#include "ocidl.h"

#ifdef __cplusplus
extern "C"{
#endif 

void * __RPC_USER MIDL_user_allocate(size_t);
void __RPC_USER MIDL_user_free( void * ); 

#ifndef __IDisk_INTERFACE_DEFINED__
#define __IDisk_INTERFACE_DEFINED__

/* interface IDisk */
/* [unique][helpstring][dual][uuid][object] */ 


EXTERN_C const IID IID_IDisk;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("134CA43B-A4F5-4FB5-9AE4-80FC838AB36B")
    IDisk : public IDispatch
    {
    public:
        virtual /* [helpstring][id] */ HRESULT STDMETHODCALLTYPE GetDFreeSpace( 
            /* [in] */ BSTR drive,
            /* [retval][out] */ __int64 *size) = 0;
        
    };
    
#else 	/* C style interface */

    typedef struct IDiskVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            IDisk * This,
            /* [in] */ REFIID riid,
            /* [iid_is][out] */ void **ppvObject);
        
        ULONG ( STDMETHODCALLTYPE *AddRef )( 
            IDisk * This);
        
        ULONG ( STDMETHODCALLTYPE *Release )( 
            IDisk * This);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfoCount )( 
            IDisk * This,
            /* [out] */ UINT *pctinfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfo )( 
            IDisk * This,
            /* [in] */ UINT iTInfo,
            /* [in] */ LCID lcid,
            /* [out] */ ITypeInfo **ppTInfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetIDsOfNames )( 
            IDisk * This,
            /* [in] */ REFIID riid,
            /* [size_is][in] */ LPOLESTR *rgszNames,
            /* [in] */ UINT cNames,
            /* [in] */ LCID lcid,
            /* [size_is][out] */ DISPID *rgDispId);
        
        /* [local] */ HRESULT ( STDMETHODCALLTYPE *Invoke )( 
            IDisk * This,
            /* [in] */ DISPID dispIdMember,
            /* [in] */ REFIID riid,
            /* [in] */ LCID lcid,
            /* [in] */ WORD wFlags,
            /* [out][in] */ DISPPARAMS *pDispParams,
            /* [out] */ VARIANT *pVarResult,
            /* [out] */ EXCEPINFO *pExcepInfo,
            /* [out] */ UINT *puArgErr);
        
        /* [helpstring][id] */ HRESULT ( STDMETHODCALLTYPE *GetDFreeSpace )( 
            IDisk * This,
            /* [in] */ BSTR drive,
            /* [retval][out] */ __int64 *size);
        
        END_INTERFACE
    } IDiskVtbl;

    interface IDisk
    {
        CONST_VTBL struct IDiskVtbl *lpVtbl;
    };

    

#ifdef COBJMACROS


#define IDisk_QueryInterface(This,riid,ppvObject)	\
    (This)->lpVtbl -> QueryInterface(This,riid,ppvObject)

#define IDisk_AddRef(This)	\
    (This)->lpVtbl -> AddRef(This)

#define IDisk_Release(This)	\
    (This)->lpVtbl -> Release(This)


#define IDisk_GetTypeInfoCount(This,pctinfo)	\
    (This)->lpVtbl -> GetTypeInfoCount(This,pctinfo)

#define IDisk_GetTypeInfo(This,iTInfo,lcid,ppTInfo)	\
    (This)->lpVtbl -> GetTypeInfo(This,iTInfo,lcid,ppTInfo)

#define IDisk_GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)	\
    (This)->lpVtbl -> GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)

#define IDisk_Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)	\
    (This)->lpVtbl -> Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)


#define IDisk_GetDFreeSpace(This,drive,size)	\
    (This)->lpVtbl -> GetDFreeSpace(This,drive,size)

#endif /* COBJMACROS */


#endif 	/* C style interface */



/* [helpstring][id] */ HRESULT STDMETHODCALLTYPE IDisk_GetDFreeSpace_Proxy( 
    IDisk * This,
    /* [in] */ BSTR drive,
    /* [retval][out] */ __int64 *size);


void __RPC_STUB IDisk_GetDFreeSpace_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);



#endif 	/* __IDisk_INTERFACE_DEFINED__ */



#ifndef __DCOMSERVERLib_LIBRARY_DEFINED__
#define __DCOMSERVERLib_LIBRARY_DEFINED__

/* library DCOMSERVERLib */
/* [helpstring][version][uuid] */ 


EXTERN_C const IID LIBID_DCOMSERVERLib;

EXTERN_C const CLSID CLSID_Disk;

#ifdef __cplusplus

class DECLSPEC_UUID("E55F07E6-6548-4FA4-8D91-F818D28BA78D")
Disk;
#endif
#endif /* __DCOMSERVERLib_LIBRARY_DEFINED__ */

/* Additional Prototypes for ALL interfaces */

unsigned long             __RPC_USER  BSTR_UserSize(     unsigned long *, unsigned long            , BSTR * ); 
unsigned char * __RPC_USER  BSTR_UserMarshal(  unsigned long *, unsigned char *, BSTR * ); 
unsigned char * __RPC_USER  BSTR_UserUnmarshal(unsigned long *, unsigned char *, BSTR * ); 
void                      __RPC_USER  BSTR_UserFree(     unsigned long *, BSTR * ); 

/* end of Additional Prototypes */

#ifdef __cplusplus
}
#endif

#endif


