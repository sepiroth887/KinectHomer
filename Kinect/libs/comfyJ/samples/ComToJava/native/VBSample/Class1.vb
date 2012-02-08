Imports System

'author: Alexey Razoryonov
'This is the sample of the Java Com Server usage.
'It creates an instance of the COM server using Automation technology and calls its methods.
Public Class Sample

    <STAThread()> _
    Public Shared Sub Main()
        'Creating Java Com Server object by its ProgID
        Dim JavaServer As Object
        JavaServer = CreateObject("jniwrapper.comtojavasample.1")
        'Calling the server's methods

        Console.WriteLine("ProgID = " + JavaServer.getProgId)
        Console.WriteLine("VersionIndependentProgID = " + JavaServer.getVersionIndependentProgId)
        Console.WriteLine("ComServerDescription = " + JavaServer.getComServerDescription)

        Dim Left = 5.0
        Dim Right = 2.0
        Console.WriteLine(Left.ToString + " + " + Right.ToString + " = " + JavaServer.add(Left, Right).ToString)


        Dim nIter As Integer
        Dim sStringArray() As String

        sStringArray = JavaServer.getSafeArray()
        Console.WriteLine("SafeArray(0) = " & sStringArray(0))
        Console.WriteLine("SafeArray(1) = " & sStringArray(1))
        Console.WriteLine("SafeArray(2) = " & sStringArray(2))

        sStringArray = Nothing

        Console.WriteLine("getEmptyVariant = " & JavaServer.getEmptyVariant())
        Console.WriteLine("getIntegerVariant = " & JavaServer.getIntegerVariant().ToString)

        JavaServer = Nothing

    End Sub 'Main

End Class
