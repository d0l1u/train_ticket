using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using MaskTerminator.BL;
using MaskTerminator.Util;
using MaskTerminator.EL;

namespace MaskTerminator
{
    public partial class MainDlg : Form
    {

        private static int dotNum = 0;
        public MainDlg()
        {

            InitializeComponent();
        }


        private void btnStart_Click(object sender, EventArgs e)
        {
            MaskController.GetInstance().StartGetMask();
        }

        private void MainDlg_Load(object sender, EventArgs e)
        {
            this.pbImg.Visible = false;
            this.Visible = false;
            MaskController.GetInstance().CurState = OptStatus.NOT_LOGIN;
            MaskController.GetInstance().HWnd = this.Handle;
            MaskController.GetInstance().Start();


            this.pic1.Parent = this.pbImg;
            this.pic2.Parent = this.pbImg;
            this.pic3.Parent = this.pbImg;
            this.pic4.Parent = this.pbImg;
            this.pic5.Parent = this.pbImg;
            this.pic6.Parent = this.pbImg;
            this.pic7.Parent = this.pbImg;
            this.pic8.Parent = this.pbImg;
            //Win32API.SetForegroundWindow(this.Handle);
            //this.TopMost = true;
            //this.Activate();
        }


        protected override void DefWndProc(ref Message m)
        {
            switch (m.Msg)
            {
                //接收自定义消息MYMESSAGE，并显示其参数
                case MsgDef.CLOSE_MSG:
                    {
                        MaskController.GetInstance().WaitStop(1000);
                        System.Environment.Exit(0);
                    }
                    break;
                case MsgDef.UPDATE_IMG:
                    {
                        this.pbImg.Visible = true;
                        //if(cbAutoShow.Checked)
                        this.btnSend.Enabled = true;
                        this.tbMaskCode.Focus();
                        this.tbMaskCode.Text = "";
                        this.tbMaskCode.Enabled = true;
                        this.pbImg.Image = ShowData.GetInstance().Pic;
                        this.Show();

                        if (cbBeep.Checked)
                            Win32API.MessageBeep(Win32API.MessageBeepType.Error);

                        notifyIcon1.Visible = false;
                        if (cbAutoShow.Checked)
                        {
                            this.WindowState = FormWindowState.Normal;
                            Win32API.SetForegroundWindow(this.Handle);
                            this.TopMost = true;
                            this.Activate();
                        }
                        
                    }
                    break;
                case MsgDef.CNTR_MSG:
                    {
                        this.tbTodayCntr.Text = ShowData.GetInstance().TodayCntr.ToString();
                        this.tbRightCntr.Text = ShowData.GetInstance().SuccCntr.ToString();
                        this.tbErrorCntr.Text = ShowData.GetInstance().FailCntr.ToString();
                        this.tbInputCntr.Text = ShowData.GetInstance().TyperCntr.ToString();
                        this.tbWaitCntr.Text = ShowData.GetInstance().RemainCntr.ToString();
                        this.tbInfor.Text = ShowData.GetInstance().WarnMsg.ToString();
                    }
               
                    break;
                case MsgDef.REMAIN_SECONDS_MSG:
                    {
                        this.tbSeconds.Text = m.WParam.ToString();
                        this.labelInfo.Text = "请在" + tbSeconds.Text + "秒内输入结果";
                        //this.Text = "请在" + tbSeconds.Text + "秒内输入结果";

                        if ((int)m.WParam == 0)
                        {
                            if(tbMaskCode.Text.Trim()!="")
                            {
                                SendMask();
                            }
                        }

                    }
                    break;
                case MsgDef.START_BTN_MSG:
                    {
                        if (m.WParam.ToString()=="0")
                        {
                            this.btnStart.Text = "暂停";
                            this.labelInfo.Text = "欢迎使用19e打码器!";
                            this.Text = "欢迎使用19e打码器!";
                            this.tbMaskCode.Focus();
                        }
                        else
                        {
                            if (m.LParam.ToString() == "1")
                            {
                                this.tbInfor.Text = "3次没有输入,暂停获取验证码";
                            }

                            this.btnStart.Text = "开始";
                            this.labelInfo.Text = "欢迎使用19e打码器!";
                            this.Text = "欢迎使用19e打码器!";
                        }
                    }
                    break;
                case MsgDef.HEART_BEAT_MSG:
                    {
                        this.TopMost = false;
                        dotNum = (dotNum + 1) % 10;
                        string str = "";
                        for(int i=0; i<dotNum; i++)
                        {
                            str += ".";
                        }
                        this.labelInfo.Text = "正在获取验证码" + str;
                        //this.Text = "正在获取验证码" + str;

                    }
                    break;
                case MsgDef.LOGIN_MSG:
                    {
                        if (m.WParam.ToString() == "0")
                        {
                            
                            this.Hide();
                        }
                        else
                        {
                            this.lbLoginInfo.Text = "欢迎你," + SysConfig.GetInstance().UserName;
                            this.Show();
                        }
                    }
                    break;
                default:
                    base.DefWndProc(ref m);
                    break;
            }
        }

        private void btnSend_Click(object sender, EventArgs e)
        {
            SendMask();
        }

        private void MainDlg_FormClosing(object sender, FormClosingEventArgs e)
        {
            notifyIcon1.Visible = false;
            MaskController.GetInstance().WaitStop(1000);
        }

        protected override bool ProcessCmdKey(ref Message msg, Keys keyData)
        {
            if (keyData == Keys.Escape && cbEscMin.Checked)
            {
                this.WindowState = FormWindowState.Minimized;
            }
            if (keyData == Keys.Space && cbBlackSend.Checked)
            {
                if (this.btnSend.Enabled)
                {
                    SendMask();
                }
            }
            if (keyData == Keys.Enter)
            {
                if (this.btnSend.Enabled)
                {
                    SendMask();
                }
            }
            //return true;
           return base.ProcessCmdKey(ref msg, keyData);
        }



        private void SendMask()
        {
            if (tbMaskCode.Text.Trim() == "")
                return;

            for(int i=1;i<=8;i++)
            {
                System.Windows.Forms.Control ctrl = FindControl(this, "pic" + i.ToString());
                if (ctrl != null)
                    ctrl.Visible = false;
            }

            MaskController.GetInstance().SendMask(tbMaskCode.Text);
            this.pbImg.Visible = false;
            this.btnSend.Enabled = false;
            this.tbSeconds.Text = "";
            this.tbMaskCode.Enabled = false;
            this.tbMaskCode.Text = "";
          
            this.labelInfo.Text = "正在获取验证码";
        }


        private void pbImg_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                if(!this.pbImg.Visible)
                {
	                return;
                }

                int x = e.X;
                int y = e.Y;

                int picWidth = this.pbImg.Right-this.pbImg.Left;
                int picHeight = this.pbImg.Bottom - this.pbImg.Top;
                int singleWidth = picWidth/4;
                int heightStart = 40;
                int singleHeight = 70;

                int index = x/singleWidth;
                if(index>=0&&index<=3)
                {
	                index +=1;
                }
                else
                {
	                return;
                }

                if((y-heightStart)/singleHeight == 1)
	                index+=4;
                else if((y-heightStart)/singleHeight ==0)
	                ;
                else 
                    return;

                string strMask = this.tbMaskCode.Text;

                if (strMask.IndexOf(index.ToString())>=0)
                {
                    this.tbMaskCode.Text = this.tbMaskCode.Text.Replace(index.ToString(), "");
                    System.Windows.Forms.Control ctrl = FindControl(this, "pic" + index.ToString());
                    if(ctrl != null)
                        ctrl.Visible = false;
                }
                else
                {
                    System.Windows.Forms.Control ctrl = FindControl(this, "pic" + index.ToString());
                    if (ctrl != null)
                        ctrl.Visible = true;
                    this.tbMaskCode.Text += index.ToString();
                }

                btnSend.Focus();

            }
            else if(e.Button == MouseButtons.Right)
            {
                if(tbMaskCode.Text.Trim().Length >0)
                    SendMask();
            }
        }


        private System.Windows.Forms.Control FindControl(System.Windows.Forms.Control control, string controlName)
        {
            Control c1;
            foreach (Control c in control.Controls)
            {
                if (c.Name == controlName)
                {
                    return c;
                }
                else if (c.Controls.Count > 0)
                {
                    c1 = FindControl(c, controlName);
                    if (c1 != null)
                    {
                        return c1;
                    }
                }
            }
            return null;
        } 


        private void notifyIcon1_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            notifyIcon1.Visible = false;
            this.Show();
            this.WindowState = FormWindowState.Normal;
        }

        private void MainDlg_SizeChanged(object sender, EventArgs e)
        {
            if (this.WindowState == FormWindowState.Minimized)
            {
                this.Hide();
                notifyIcon1.Visible = true;
            }
        }



    }
}