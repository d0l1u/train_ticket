using System;
using System.Collections.Generic;
using System.Text;

namespace MaskTerminator.Util
{
    public class SysConfig
    {
        private static SysConfig _sysConfig;
        private IniVisiter _ini;

        private String _userName;

        public String UserName
        {
            get { return _userName; }
            set { _userName = value; }
        }
        private String _passWord;

        public String PassWord
        {
            get { return _passWord; }
            set { _passWord = value; }
        }
        private String _loginAddr;

        public String LoginAddr
        {
            get { return _loginAddr; }
            set { _loginAddr = value; }
        }

        private String _token;
        public String Token {
            get {
                return _token;
            }
            set {
                _token = value;
            }
        }

        public static SysConfig GetInstance()
        {
            if(_sysConfig == null)
            {
                _sysConfig = new  SysConfig();
            }

            return _sysConfig;
        }

        private SysConfig()
        {
            string strExePath = System.Environment.CurrentDirectory;
            _ini = new IniVisiter(strExePath + "\\UserInfo.ini");
            _userName = _ini.ReadValue("Information", "UserName");
            _passWord = _ini.ReadValue("Information", "Password");
            _loginAddr = _ini.ReadValue("Information", "ServerIP");
            _token = _ini.ReadValue("Information","Token");
        }


        public void SaveLoginInfo()
        {
            _ini.Writue("Information", "UserName", _userName);
            _ini.Writue("Information", "Password", _passWord);
            _ini.Writue("Information", "ServerIP", _loginAddr);
            _ini.Writue("Information","Token",_token);
        }

        public void SaveAddr()
        {
            _ini.Writue("Information", "ServerIP", _loginAddr);
        }

        public void SaveToken() {
            _ini.Writue("Information","Token",_token);
        }

        public void ReadToken() {
            _token = _ini.ReadValue("Information","Token");
        }
    }
}
