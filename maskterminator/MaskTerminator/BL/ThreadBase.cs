using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;

namespace MaskTerminator.BL
{

    public abstract class ThreadBase
    {
        public ThreadBase()
        {
        }

        protected abstract void Run();

        public void Start()
        {
            _tr = new Thread(new ThreadStart(ThreadProc));
            _tr.Start();
        }

        public void Stop()
        {
            if (_tr.IsAlive)
            {
                _tr.Abort();
            }
        }

        public virtual void WaitStop(int nMiliSeconds)
        {
            if(_tr==null)
                return;

            bRunning = false;
            if (_tr.IsAlive)
            {
                _tr.Join(nMiliSeconds);
                _tr.Abort();
            }
        }

        public bool IsAlive()
        {
            if(null != _tr)
                return _tr.IsAlive;
            return false;
        }


        void ThreadProc()
        {
            try
            {
                Run();
            }
            catch (Exception ex)
            {
                //Log.CSLog.GetInstance().Error(_strModule, "线程中出现异常:" + ex.Message);
            }
        }

        //running
        protected bool bRunning;

        //线程
        protected Thread _tr = null;
        //模块
        protected string _strModule;
    }
}