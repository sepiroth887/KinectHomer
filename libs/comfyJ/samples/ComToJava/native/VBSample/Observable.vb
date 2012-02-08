'author: Serge Piletsky
'
'This example demonstrates how to create the Observable Java COM server and how to add VB event handlers to it.
Public Class ObservableJavaCOMServer

    <STAThread()> _
    Public Shared Sub Main()
        'Creating Java Com Server object by its ProgID
        Dim JavaServer = CreateObject("comfyj.observable.1")

        'Set VB observers
        Dim myEventHandler1 = New MyVBEventHandler
        Dim myEventHandler2 = New MyVBEventHandler

        JavaServer.addObserver(myEventHandler1)
        JavaServer.addObserver(myEventHandler2)

        'Print number of observers of our Java COM server
        Console.WriteLine("countObservers = " + JavaServer.countObservers().ToString)

        'Notify all attached observers from VB client via Java COM server
        JavaServer.notifyObservers("This a string from VB client")

        'Make Java COM server to fire the 'update' event, so our VB handlers are notified
        JavaServer.simulateEventsFromJava()
    End Sub 'Main
End Class

' Event handler that allows to listen to Java COM server events
Public Class MyVBEventHandler
    Public Sub update(ByVal value As Object)
        Console.WriteLine("VB: MyObservableHandler.update(" + value.ToString + ")")
    End Sub
End Class
