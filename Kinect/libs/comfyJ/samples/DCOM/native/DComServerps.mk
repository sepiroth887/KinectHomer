
DComServerps.dll: dlldata.obj DComServer_p.obj DComServer_i.obj
	link /dll /out:DComServerps.dll /def:DComServerps.def /entry:DllMain dlldata.obj DComServer_p.obj DComServer_i.obj \
		kernel32.lib rpcndr.lib rpcns4.lib rpcrt4.lib oleaut32.lib uuid.lib \

.c.obj:
	cl /c /Ox /DWIN32 /D_WIN32_WINNT=0x0400 /DREGISTER_PROXY_DLL \
		$<

clean:
	@del DComServerps.dll
	@del DComServerps.lib
	@del DComServerps.exp
	@del dlldata.obj
	@del DComServer_p.obj
	@del DComServer_i.obj
