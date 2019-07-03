using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.InteropServices;

namespace MaskTerminator.Util
{

    public class Win32API
    {

        [DllImport("User32.dll", EntryPoint = "FindWindow")]
        public static extern IntPtr FindWindow(string lpClassName, string lpWindowName);

        [DllImport("User32.dll", EntryPoint = "FindWindowEx")]
        public static extern IntPtr FindWindowEx(IntPtr hwndParent, IntPtr hwndChildAfter, string lpClassName, string lpWindowName);

        [DllImport("user32.dll")]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool SetForegroundWindow(IntPtr hWnd);

        /// <summary>
        /// �Զ���Ľṹ
        /// </summary>
        public struct My_lParam
        {
            public int i;
            public string s;
        }
        /// <summary>
        /// ʹ��COPYDATASTRUCT�������ַ���
        /// </summary>
        [StructLayout(LayoutKind.Sequential)]
        public struct COPYDATASTRUCT
        {
            public IntPtr dwData;
            public int cbData;
            [MarshalAs(UnmanagedType.LPStr)]
            public string lpData;
        }
        //��Ϣ����API
        [DllImport("User32.dll", EntryPoint = "SendMessage")]
        public static extern int SendMessage(
            IntPtr hWnd,        // ��Ϣ�����Ĵ��ڵľ��
            int Msg,            // ��ϢID
            int wParam,         // ����1
            int lParam          //����2
        );


        //��Ϣ����API
        [DllImport("User32.dll", EntryPoint = "SendMessage")]
        public static extern int SendMessage(
            IntPtr hWnd,        // ��Ϣ�����Ĵ��ڵľ��
            int Msg,            // ��ϢID
            int wParam,         // ����1
            ref My_lParam lParam //����2
        );

        //��Ϣ����API
        [DllImport("User32.dll", EntryPoint = "SendMessage")]
        public static extern int SendMessage(
            IntPtr hWnd,        // ��Ϣ�����Ĵ��ڵľ��
            int Msg,            // ��ϢID
            int wParam,         // ����1
            ref  COPYDATASTRUCT lParam  //����2
        );

        //��Ϣ����API
        [DllImport("User32.dll", EntryPoint = "PostMessage")]
        public static extern int PostMessage(
            IntPtr hWnd,        // ��Ϣ�����Ĵ��ڵľ��
            int Msg,            // ��ϢID
            int wParam,         // ����1
            int lParam            // ����2
        );



        //��Ϣ����API
        [DllImport("User32.dll", EntryPoint = "PostMessage")]
        public static extern int PostMessage(
            IntPtr hWnd,        // ��Ϣ�����Ĵ��ڵľ��
            int Msg,            // ��ϢID
            int wParam,         // ����1
            ref My_lParam lParam //����2
        );

        //�첽��Ϣ����API
        [DllImport("User32.dll", EntryPoint = "PostMessage")]
        public static extern int PostMessage(
            IntPtr hWnd,        // ��Ϣ�����Ĵ��ڵľ��
            int Msg,            // ��ϢID
            int wParam,         // ����1
            ref  COPYDATASTRUCT lParam  // ����2
        );


        public enum MessageBeepType
        {
            Default = -1,
            Ok = 0x00000000,
            Error = 0x00000010,
            Question = 0x00000020,
            Warning = 0x00000030,
            Information = 0x00000040
        }

        [DllImport("user32.dll", SetLastError = true)]
        public static extern bool MessageBeep(MessageBeepType type);
    }

}
