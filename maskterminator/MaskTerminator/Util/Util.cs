using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Net;

namespace MaskTerminator.Util
{
    class Util
    {
        private Util()
        {
            _cookieContainner = null;
        }
        private static Util _util = null;
        public static Util GetInstance()
        {
            if(_util == null)
            {
                _util = new Util();
            }

            return _util;
        }
        CookieContainer _cookieContainner;





        public string DownloadHttpFileGet(string strURL, System.Text.Encoding encoding, int nTimeOut)
        {
            string url = strURL;
            HttpClient client = new HttpClient(_cookieContainner);
            //client.Headers.Add("Content-Type", "application/x-www-form-urlencoded");
            //byte[] postData = encoding.GetBytes(strPost);
            string errMsg;
            string strRet = client.GetSrc(url, "utf-8", out errMsg);
            _cookieContainner = client.Cookies;
            return strRet;


            /*
            WebRequest req = HttpWebRequest.Create(strURL);
            req.Method = "GET";

            //WebProxy proxy = new WebProxy();
            //Uri u = new Uri("http://192.168.61.19:808");
            //proxy.Address = u;
            //req.Proxy = proxy;

            req.Timeout = nTimeOut * 1000;

            using (WebResponse rsp = req.GetResponse())
            {
                StreamReader reader = new StreamReader(rsp.GetResponseStream(), encoding);
                string strResult = reader.ReadToEnd();
                return strResult;
            }*/
        }




        public string DownloadHttpFilePost(string strURL, string strPost, System.Text.Encoding encoding, int nTimeOut, bool isNewSession)
        {
            string url = strURL;
            HttpClient client ;
            if(isNewSession)
                client = new HttpClient();
            else
                client = new HttpClient(_cookieContainner);
            client.Headers.Add("Content-Type", "application/x-www-form-urlencoded");
            byte[] postData = encoding.GetBytes(strPost);
            byte[] responseData = client.UploadData(url, "POST", postData);
            _cookieContainner = client.Cookies;
            string srcString = Encoding.Default.GetString(responseData);
            return srcString;
        }


        public byte[] DownloadHttpStreamPost(string strURL, string strPost, System.Text.Encoding encoding, int nTimeOut)
        {
            string url = strURL;
            HttpClient client = new HttpClient(_cookieContainner);
            client.Headers.Add("Content-Type", "application/x-www-form-urlencoded");

            byte[] postData = encoding.GetBytes(strPost);
            byte[] responseData = client.UploadData(url, "POST", postData);
            _cookieContainner = client.Cookies;
            return responseData;
            /*
            WebRequest req = HttpWebRequest.Create(strURL);
            if (null != strPost && strPost.Length > 0)
            {
                req.Method = "POST";
                byte[] byData = encoding.GetBytes(strPost);
                req.ContentLength = byData.Length;
                req.Timeout = nTimeOut * 1000;
                req.ContentType = "application/x-www-form-urlencoded";
                Stream streamRequest = req.GetRequestStream();
                streamRequest.Write(byData, 0, byData.Length);
            }

            using (WebResponse rsp = req.GetResponse())
            {
                BinaryReader br = new BinaryReader(rsp.GetResponseStream());
                
                byte[] byteReturn = new byte[204800];
                int nIndex = 0;
                while(true)
                {
                    int nReaded = br.Read(byteReturn, nIndex, 2048);
                    if (nReaded == 0)
                    {
                        Array.Resize<byte>(ref byteReturn, nIndex);
                        break;
                    }
                    else
                    {
                        nIndex += nReaded;
                    }



                    
                }
                //int nReaded = br.Read(bytes, 0, 1024);
                return byteReturn;
            }
             */
        }
    }
}
