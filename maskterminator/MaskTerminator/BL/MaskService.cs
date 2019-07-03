using System;
using System.Collections.Generic;
using System.Text;
using System.Web;
using MaskTerminator.Util;
using System.IO;

namespace MaskTerminator.BL
{
    class MaskService
    {
        public static byte[] GetMask(string username,string token)
        {
            Random rand = new Random();

            string strUrl = "http://" + SysConfig.GetInstance().LoginAddr + "/optverify.do";
            byte[] bytes = Util.Util.GetInstance().DownloadHttpStreamPost(strUrl,"action=find&rand=" + rand.NextDouble().ToString() + "&username=" + username + "&token=" + token,Encoding.UTF8,5);



            return bytes;
        }

        public static String SendMaskRslt(string strMask,string username)
        {
            string strUrl = "http://" + SysConfig.GetInstance().LoginAddr + "/optverify.do";
            string strResp = Util.Util.GetInstance().DownloadHttpFilePost(strUrl,"action=update&verifycode=" + HttpUtility.UrlEncode(strMask,UTF8Encoding.UTF8) + "&username=" + username,Encoding.UTF8,5,false);

            return strResp;
        }

        public static bool Login(string userName, string password)
        {
            string strUrl = "http://" + SysConfig.GetInstance().LoginAddr + "/login.do";
            string strResp = Util.Util.GetInstance().DownloadHttpFilePost(strUrl, "username=" + userName + "&pwd=" + password + "&codeversion=2.4", Encoding.UTF8, 5, true);
            string[] resp = strResp.Split(new char[] { '*' });
            if(resp.Length > 0 && resp[0]=="true")
            {
                SysConfig.GetInstance().Token = resp[resp.Length - 1];
                return true;
            }
            else
            {
                Close(userName);
            }

            return false;

        }

        public static string StopInput(string username)
        {
            string strUrl = "http://" + SysConfig.GetInstance().LoginAddr + "/optverify.do?action=stop&username=" + username;
            string strResp = Util.Util.GetInstance().DownloadHttpFileGet(strUrl, Encoding.UTF8, 5);
            return strResp;
        }


        public static string JiaoZhun(string username)
        {
            string strUrl = "http://" + SysConfig.GetInstance().LoginAddr + "/optverify.do?action=calibration&username=" + username;
            string strResp = Util.Util.GetInstance().DownloadHttpFileGet(strUrl, Encoding.UTF8, 5);
            return strResp;
        }


        public static string Close(string userName)
        {
            ////optverify.do?action=close&username=%s
            string strUrl = "http://" + SysConfig.GetInstance().LoginAddr + "/optverify.do?action=close&username=" + userName;
            string strResp = Util.Util.GetInstance().DownloadHttpFileGet(strUrl, Encoding.UTF8, 5);
            return strResp;
        }

    }
}
