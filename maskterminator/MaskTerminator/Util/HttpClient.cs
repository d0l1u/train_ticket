using System;
using System.Collections.Generic;
using System.Text;
using System.Net;

namespace MaskTerminator.Util
{
    /// <summary>
    /// ֧�� Session �� Cookie �� WebClient��
    /// </summary>
    public class HttpClient : WebClient
    {
        // Cookie ����
        private CookieContainer cookieContainer;

        /// <summary>
        /// ����һ���µ� WebClient ʵ����
        /// </summary>
        public HttpClient()
        {
            this.cookieContainer = new CookieContainer();
        }

        /// <summary>
        /// ����һ���µ� WebClient ʵ����
        /// </summary>
        /// <param name="cookie">Cookie ����</param>
        public HttpClient(CookieContainer cookies)
        {
            this.cookieContainer = cookies;
        }

        /// <summary>
        /// Cookie ����
        /// </summary>
        public CookieContainer Cookies
        {
            get { return this.cookieContainer; }
            set { this.cookieContainer = value; }
        }

        /// <summary>
        /// ���ش��� Cookie �� HttpWebRequest��
        /// </summary>
        /// <param name="address"></param>
        /// <returns></returns>
        protected override WebRequest GetWebRequest(Uri address)
        {
            WebRequest request = base.GetWebRequest(address);
            if (request is HttpWebRequest)
            {
                HttpWebRequest httpRequest = request as HttpWebRequest;
                httpRequest.CookieContainer = cookieContainer;
            }
            return request;
        }

        #region ��װ��PostData, GetSrc �� GetFile ����
        /// <summary>
        /// ��ָ���� URL POST ���ݣ�������ҳ��
        /// </summary>
        /// <param name="uriString">POST URL</param>
        /// <param name="postString">POST �� ����</param>
        /// <param name="postStringEncoding">POST ���ݵ� CharSet</param>
        /// <param name="dataEncoding">ҳ��� CharSet</param>
        /// <returns>ҳ���Դ�ļ�</returns>
        public string PostData(string uriString, string postString, string postStringEncoding, string dataEncoding, out string msg)
        {
            try
            {
                // �� Post �ַ���ת�����ֽ�����
                byte[] postData = Encoding.GetEncoding(postStringEncoding).GetBytes(postString);
                this.Headers.Add("Content-Type", "application/x-www-form-urlencoded");
                // �ϴ����ݣ�����ҳ����ֽ�����
                byte[] responseData = this.UploadData(uriString, "POST", postData);
                // �����صĽ��ֽ�����ת�����ַ���(HTML);
                string srcString = Encoding.GetEncoding(dataEncoding).GetString(responseData);
                srcString = srcString.Replace("\t", "");
                srcString = srcString.Replace("\r", "");
                srcString = srcString.Replace("\n", "");
                msg = string.Empty;
                return srcString;
            }
            catch (WebException we)
            {
                msg = we.Message;
                return string.Empty;
            }
        }

        /// <summary>
        /// ���ָ�� URL ��Դ�ļ�
        /// </summary>
        /// <param name="uriString">ҳ�� URL</param>
        /// <param name="dataEncoding">ҳ��� CharSet</param>
        /// <returns>ҳ���Դ�ļ�</returns>
        public string GetSrc(string uriString, string dataEncoding, out string msg)
        {
            try
            {
                // ����ҳ����ֽ�����
                byte[] responseData = this.DownloadData(uriString);
                // �����صĽ��ֽ�����ת�����ַ���(HTML);
                string srcString = Encoding.GetEncoding(dataEncoding).GetString(responseData);
                srcString = srcString.Replace("\t", "");
                srcString = srcString.Replace("\r", "");
                srcString = srcString.Replace("\n", "");
                msg = string.Empty;
                return srcString;
            }
            catch (WebException we)
            {
                msg = we.Message;
                return string.Empty;
            }
        }

        /// <summary>
        /// ��ָ���� URL �����ļ�������
        /// </summary>
        /// <param name="uriString">�ļ� URL</param>
        /// <param name="fileName">�����ļ������·��</param>
        /// <returns></returns>
        public bool GetFile(string urlString, string fileName, out string msg)
        {
            try
            {
                this.DownloadFile(urlString, fileName);
                msg = string.Empty;
                return true;
            }
            catch (WebException we)
            {
                msg = we.Message;
                return false;
            }
        }
        #endregion
    }
}
