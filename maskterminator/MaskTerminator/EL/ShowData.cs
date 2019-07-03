using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace MaskTerminator.EL
{
    public class ShowData
    {
        private ShowData()
        {

        }
        private static ShowData _showData;
        public static ShowData GetInstance()
        {
            if (_showData == null)
                _showData = new ShowData();
            return _showData;
        }

        //private string msg;
        private Image pic;

        public Image Pic
        {
            get { return pic; }
            set { pic = value; }
        }

        private int curSeconds = 0;

        public int CurSeconds
        {
            get { return curSeconds; }
            set { curSeconds = value; }
        }
        private int todayCntr = 0;

        public int TodayCntr
        {
            get { return todayCntr; }
            set { todayCntr = value; }
        }
        private int succCntr = 0;

        public int SuccCntr
        {
            get { return succCntr; }
            set { succCntr = value; }
        }
        private int failCntr = 0;

        public int FailCntr
        {
            get { return failCntr; }
            set { failCntr = value; }
        }
        private int typerCntr = 0;

        public int TyperCntr
        {
            get { return typerCntr; }
            set { typerCntr = value; }
        }
        private int remainCntr = 0;

        public int RemainCntr
        {
            get { return remainCntr; }
            set { remainCntr = value; }
        }
        private string warnMsg = string.Empty;

        public string WarnMsg
        {
            get { return warnMsg; }
            set { warnMsg = value; }
        }


        private string maskCode;

        public string MaskCode
        {
            get { return maskCode; }
            set { maskCode = value; }
        }

        private string userName;

        public string UserName
        {
            get { return userName; }
            set { userName = value; }
        }


    }
}
