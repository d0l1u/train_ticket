using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using MaskTerminator.Util;

namespace MaskTerminator
{
    public partial class LoginDlg : Form
    {
        public LoginDlg()
        {
            InitializeComponent();
        }

        private string _userName;

        public string UserName
        {
            get { return _userName; }
            set { _userName = value; }
        }
        private string _pwd;

        public string Pwd
        {
            get { return _pwd; }
            set { _pwd = value; }
        }

        private string _serverIp;

        public string ServerIp
        {
            get { return _serverIp; }
            set { _serverIp = value; }
        }


        private void btnLogin_Click(object sender, EventArgs e)
        {
            _userName = this.tbUserName.Text;
            _pwd = this.tbPwd.Text;
            _serverIp = this.tbServerAddr.Text;
        }

        private void LoginDlg_Load(object sender, EventArgs e)
        {
            this.tbUserName.Text = SysConfig.GetInstance().UserName;
            this.tbPwd.Text = SysConfig.GetInstance().PassWord;
            this.tbServerAddr.Text = SysConfig.GetInstance().LoginAddr;
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {

        }
    }
}