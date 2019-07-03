using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;
using System.Threading;
using MaskTerminator.Util;
using System.IO;
using System.Windows.Forms;
using MaskTerminator.EL;

namespace MaskTerminator.BL
{
    public class MaskController :ThreadBase
    {
        private MaskController()
        {
            _notInputCntr = 0;
            _curState = OptStatus.NOT_LOGIN;
        }


        private AutoResetEvent resetEvent = new AutoResetEvent(false);

        public AutoResetEvent ResetEvent
        {
            get { return resetEvent; }
            set { resetEvent = value; }
        }

        public static MaskController _maskController;

        public static MaskController GetInstance()
        {
            if(_maskController == null)
            {
                _maskController = new MaskController();
                return _maskController;
            }
            return _maskController;
        }

        private IntPtr _hWnd;

        public IntPtr HWnd
        {
            get { return _hWnd; }
            set { _hWnd = value; }
        }

        private string _curState = OptStatus.NOT_LOGIN;

        public string CurState
        {
            get { return _curState; }
            set { _curState = value; }
        }
        private DateTime _inputMaskStartTime;
        
        private int _loginFailCntr = 0;
        private int _notInputCntr;
        private bool _isStoped;
        private string _userName;

        public string UserName
        {
            get { return _userName; }
            set { _userName = value; }
        }
        private bool _isLogin;
        private int _scanIntervalSecond = 100;
        private int _inputMaskSecond = 15;


        protected override void Run()
        {
            bRunning = true;
            _isLogin = false;

            DateTime lastAdjustTime = DateTime.Now - new TimeSpan(1, 0, 0, 0) ;

            while (bRunning)
            {
                try
                {
                    if ((DateTime.Now - lastAdjustTime).TotalSeconds > 30 && _curState != OptStatus.NOT_LOGIN)
                    {
                        lastAdjustTime = DateTime.Now;
                        string strAdjustResp = MaskService.JiaoZhun(SysConfig.GetInstance().UserName);
                        if(!string.IsNullOrEmpty(strAdjustResp))
                        {
                            string[] vecAdjust = strAdjustResp.Split(new char[] { '*'});
                            if (vecAdjust.Length < 3)
                            {
                                continue;
                            }
                            else
                            {
                                int todayCntr = 0;
                                bool bSucc = int.TryParse(vecAdjust[0], out todayCntr);
                                if (bSucc)
                                    ShowData.GetInstance().TodayCntr = todayCntr;
                                else
                                    ShowData.GetInstance().TodayCntr = 0;


                                int rightCntr = 0;
                                bSucc = int.TryParse(vecAdjust[1], out rightCntr);
                                if (bSucc)
                                    ShowData.GetInstance().SuccCntr = rightCntr;
                                else
                                    ShowData.GetInstance().SuccCntr = 0;



                                int errorCntr = 0;
                                bSucc = int.TryParse(vecAdjust[2], out errorCntr);
                                if (bSucc)
                                    ShowData.GetInstance().FailCntr = errorCntr;
                                else
                                    ShowData.GetInstance().FailCntr = 0;

                                //发送消息
                                Win32API.PostMessage(HWnd, MsgDef.CNTR_MSG, 0, 0);

                                if (vecAdjust.Length < 5)
                                {
                                    continue;
                                }

                                //扫描间隔
                                int scanIntervalSecond = 1000;
                                bSucc = int.TryParse(vecAdjust[3], out scanIntervalSecond);

                                if (bSucc)
                                {
                                    _scanIntervalSecond = scanIntervalSecond;
                                }
                                else
                                {
                                    _scanIntervalSecond = 1000;
                                }

                                //打码时常
                                int inputMaskSecond = 15;
                                bSucc = int.TryParse(vecAdjust[4], out inputMaskSecond);
                                if(bSucc)
                                {
                                    _inputMaskSecond = inputMaskSecond;
                                }
                                else
                                {
                                    _inputMaskSecond = 15;
                                }
                            }
                        }
                    }



                    if (_curState == OptStatus.RUNNING_NOMASK)
                    {
                        //没有码, 每隔1秒拉一次码,拉倒码, 展示,变化状态为有码,启动计时器
                        byte[] arrRslt = MaskService.GetMask(SysConfig.GetInstance().UserName,SysConfig.GetInstance().Token);
                        if (arrRslt.Length< 800)
                        {
                            try
                            {
                                string str = System.Text.Encoding.Default.GetString(arrRslt);
                                if(str == "false")
                                {
                                    Win32API.PostMessage(HWnd, MsgDef.HEART_BEAT_MSG, 0, 0);
                                    Thread.Sleep(_scanIntervalSecond );
                                }
                                else if (str == "relogin")
                                {
                                    _curState = OptStatus.NOT_LOGIN;
                                }
                                else
                                {
                                    _curState = OptStatus.NOT_START;
                                }
                            }
                            catch(Exception ex)
                            {
                                _curState = OptStatus.NOT_START;
                            }
                        }
                        else
                        {
                            try
                            {
                                Stream stream = new MemoryStream(arrRslt);
                                Image img = Image.FromStream(stream);

                                _inputMaskStartTime = DateTime.Now;
                                _curState = OptStatus.RUNNING_HAVEMASK;

                                ShowData.GetInstance().Pic = img;
                                Win32API.PostMessage(HWnd, MsgDef.UPDATE_IMG, 0, 0);

                            }
                            catch(Exception ex)
                            {
                                Thread.Sleep(_scanIntervalSecond);
                            }
                        }
                        

                    }
                    else if (_curState == OptStatus.RUNNING_HAVEMASK)
                    {
                        TimeSpan span = DateTime.Now - _inputMaskStartTime;
                        int curSecond = (int)span.TotalSeconds;
                        if (curSecond > _inputMaskSecond)
                        {
                            _curState = OptStatus.RUNNING_NOMASK;
                            if (++_notInputCntr >= 2)
                            {
                                //停止扫描
                                Win32API.PostMessage(HWnd, MsgDef.START_BTN_MSG, 1, 1);
                                return;
                            }
                        }
                        else
                        {
                            bool bWaitEvent = ResetEvent.WaitOne(1000);
                            if(bWaitEvent)
                            {
                                _notInputCntr = 0;
                                //发送码
                                string strResp = MaskService.SendMaskRslt(ShowData.GetInstance().MaskCode,SysConfig.GetInstance().UserName);
                                if(!string.IsNullOrEmpty(strResp))
                                {
                                    string[] vec = strResp.Split(new char[] { '*' });
                                    if(vec.Length<=3)
                                    {
                                        _curState = OptStatus.RUNNING_NOMASK;
                                        continue;
                                    }
                                    else
                                    {
                                        if(vec[0]== "true")
                                        {
                                             ShowData.GetInstance().SuccCntr ++;
                                        }
                                        else
                                        {
                                             ShowData.GetInstance().FailCntr ++;
                                        }
                                        ShowData.GetInstance().TodayCntr++;

                                        SysConfig.GetInstance().Token = vec[vec.Length - 1];
                                        SysConfig.GetInstance().SaveToken();

                                        int typeCntr = 0;
                                        bool bSucc = int.TryParse(vec[1], out typeCntr);
                                        if (bSucc)
                                            ShowData.GetInstance().TyperCntr = typeCntr;
                                        else
                                            ShowData.GetInstance().TyperCntr = 0;

                                        int remainCntr=0;
                                        bSucc = int.TryParse(vec[2], out remainCntr);
                                        if (bSucc)
                                            ShowData.GetInstance().RemainCntr = remainCntr;
                                        else
                                            ShowData.GetInstance().RemainCntr = 0;

                                        if (remainCntr / typeCntr > 4 && typeCntr>0)
                                        {
                                            ShowData.GetInstance().WarnMsg = "请增加打码人员！";
                                        }
                                        else
                                        {
                                            ShowData.GetInstance().WarnMsg = "";
                                        }
                                        

                                        //发送消息
                                        Win32API.PostMessage(HWnd, MsgDef.CNTR_MSG, 0, 0);                                        
                                    }
                                }

                                _curState = OptStatus.RUNNING_NOMASK;
                            }
                            else
                            {
                                ShowData.GetInstance().CurSeconds = curSecond;
                                Win32API.PostMessage(HWnd, MsgDef.REMAIN_SECONDS_MSG, _inputMaskSecond - curSecond - 1, 0);
                            }
                        }
                    }
                    else if (_curState == OptStatus.NOT_LOGIN)
                    {
                        Win32API.PostMessage(HWnd, MsgDef.LOGIN_MSG, 0, 0);
                        LoginDlg dlg = new LoginDlg();

                        DialogResult rst = dlg.ShowDialog();
                        if (rst == DialogResult.OK)
                        {
                            SysConfig.GetInstance().LoginAddr = dlg.ServerIp.Trim();
                            bool bLoginSucc = MaskService.Login(dlg.UserName.Trim(), dlg.Pwd.Trim());
                            if (bLoginSucc)
                            {
                                Win32API.PostMessage(HWnd, MsgDef.LOGIN_MSG, 1, 0);
                                SysConfig.GetInstance().UserName = dlg.UserName.Trim();
                                

                                SysConfig.GetInstance().PassWord = dlg.Pwd.Trim();
                                SysConfig.GetInstance().SaveLoginInfo();
                                _userName = dlg.UserName;
                                _curState = OptStatus.NOT_START;
                                _isLogin = true;
                            }
                            else
                            {
                                _isLogin = false;
                                if (++_loginFailCntr > 2)
                                {

                                    CurState = OptStatus.NOT_START;
                                    Win32API.PostMessage(HWnd, MsgDef.CLOSE_MSG, 0, 0);
                                    return;
                                }

                            }
                        }
                        else
                        {
                            Win32API.PostMessage(HWnd, MsgDef.CLOSE_MSG, 0, 0);
                            //退出程序
                            return;
                        }


                    }
                    else if (_curState == OptStatus.NOT_START)
                    {
                        if (!_isStoped)
                        {
                            _isStoped = true;
                            MaskService.StopInput(SysConfig.GetInstance().UserName);
                        }
                        Thread.Sleep(1000);
                    }
                }
                catch(Exception ex)
                {
                    
                }

            }

            if (_isLogin)
                MaskService.Close(_userName);

        }

        public void SendMask(string strMask)
        {
            if(CurState == OptStatus.RUNNING_HAVEMASK)
            {
                ShowData.GetInstance().MaskCode = strMask;
                ResetEvent.Set();
            }

        }

        internal void StartGetMask()
        {
            lock (this)
            {
                if(CurState == OptStatus.NOT_START)
                {
                    CurState = OptStatus.RUNNING_NOMASK;
                    _isStoped = false;
                    Win32API.PostMessage(HWnd, MsgDef.START_BTN_MSG, 0, 0);
                }
                else if(CurState != OptStatus.NOT_LOGIN)
                {
                    CurState = OptStatus.NOT_START;
                    Win32API.PostMessage(HWnd, MsgDef.START_BTN_MSG, 1, 0);
                }
            }
            

        }
    }
}
