using System;
using System.Collections.Generic;
using System.Text;

namespace MaskTerminator.Util
{
    class OptStatus
    {
        public static readonly string NOT_LOGIN = "1";
        public static readonly string NOT_START = "2";
        public static readonly string RUNNING_NOMASK = "3";
        public static readonly string RUNNING_HAVEMASK = "4";
    }

}
