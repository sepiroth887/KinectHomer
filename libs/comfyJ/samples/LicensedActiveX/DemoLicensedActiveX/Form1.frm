VERSION 5.00
Object = "{08D8537D-8E2E-4A6C-BED4-C815460D1B76}#5.0#0"; "testLicensedActiveX.ocx"
Begin VB.Form Form1 
   Caption         =   "Licensed ActiveX Demo"
   ClientHeight    =   5145
   ClientLeft      =   60
   ClientTop       =   450
   ClientWidth     =   4875
   LinkTopic       =   "Form1"
   ScaleHeight     =   5145
   ScaleWidth      =   4875
   StartUpPosition =   3  'Windows Default
   Begin testLicensedActiveX.LicensedActiveX LicensedActiveX1 
      Height          =   4575
      Left            =   240
      TabIndex        =   0
      Top             =   240
      Width           =   4455
      _ExtentX        =   7858
      _ExtentY        =   8070
      BackColor       =   -2147483624
      BeginProperty Font {0BE35203-8F91-11CE-9DE3-00AA004BB851} 
         Name            =   "MS Sans Serif"
         Size            =   12
         Charset         =   204
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      BorderStyle     =   1
      Caption         =   "JNIWrapper + ComfyJ"
      TimerOn         =   -1  'True
   End
End
Attribute VB_Name = "Form1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub Command1_Click()
LicensedActiveX1.TimerOn = Not LicensedActiveX1.TimerOn
End Sub
