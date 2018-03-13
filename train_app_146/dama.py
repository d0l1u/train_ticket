# coding=utf-8

from poster.encode import multipart_encode
from poster.streaminghttp import register_openers
import urllib2
import requests
import json
import time
import logging


#获取打码结果路径
KYFW_SEND_URL = "http://219.238.151.222:18010/partner/sendCode.do";
ENG_SEND_URL = "http://219.238.151.236:18000/partner/sendCode.do";

KYFW_GET_URL = "http://219.238.151.222:18010/partner/requestResult.do";
ENG_GET_URL = "http://219.238.151.236:18000/partner/requestResult.do";
# 在 urllib2 上注册 http 流处理句柄


# 开始对文件 "DSC0001.jpg" 的 multiart/form-data 编码
# "image1" 是参数的名字，一般通过 HTML 中的 <input> 标签的 name 参数设置

# headers 包含必须的 Content-Type 和 Content-Length
# datagen 是一个生成器对象，返回编码过后的参数
def UploadImag(fileName):
    datagen, headers = multipart_encode({"name":"pic_id","pic1": open(fileName, "rb")})
    # 创建请求对象
    request = urllib2.Request(KYFW_SEND_URL,datagen, headers)
    # 实际执行请求并取得返回
    #{"pic_id":"PIC1801242127186324385","ret":"success","verify_code":""}
    ret = urllib2.urlopen(request).read()
    try:
        jsRet = json.loads(ret)
        logging.info(jsRet)
        if jsRet['ret'] == 'success':
            return jsRet['pic_id']
        else:
            return ""
    except Exception as e:
        logging.exception(e)
        return ""

def GetCode(picId):
    s = requests.Session()
    r = s.get(url=KYFW_GET_URL+"?pic_id="+picId)
    #{"pic_id":"PIC1801242127186324385","ret":"success","verify_code":"34,42,183,41"}
    print r.text
    logging.info(r.text)
    try:
        jsRet = json.loads(r.text)
        if jsRet['ret'] == 'success':
            return jsRet['verify_code']
        else:
            return ""
    except Exception as e:
        logging.exception(e)
        return ""
def dama(filePath):
    register_openers()
    code = ""
    pic_id = UploadImag(filePath)
    if pic_id == "":
        return ""
    for i in range(10):
        time.sleep(1.5)
        code = GetCode(pic_id)
        if(code <> '' ):
            break
        else:
            continue
    return code
#dama(".\\captcha\\2018\\01\\25\\GTGJ180112153746431.jpg")
#UploadImag("E:\\project\\train_app\\c.jpg")
#GetCode("PIC1801242127186324385")