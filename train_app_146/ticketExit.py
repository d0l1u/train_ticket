# coding=utf-8
# author=Frank

import base64
import binascii
import hashlib
import urllib2
import json
import time
import urllib
import datetime
import struct
import random
import hmac
import math
import requests
import sys
#import paraCryptexit
from logger import *
import unit
import dama

BS = 16
pad = lambda s: s + (BS - len(s) % BS) * chr(BS - len(s) % BS)
unpad = lambda s: s[0:-ord(s[-1])]
#g_url="http://192.168.65.227:18100/so/soCall.htm?"
#g_dll_url = "http://192.168.8.4:7777"
g_ip = "114.215.28.129"
g_url="http://%s:17080/so/soCall.htm?"%(g_ip)
g_dll_url = "http://%s:7777"%(g_ip)
#g_version_no = "3.0.0.12121430"
g_version_no = "3.0.1.01221000"


def writeBin (filename,binCon):
    fp=open(filename,'wb')
    fp.write(binCon)
    fp.close()

def readBin (filename):
    fp=open(filename,'rb')
    data = fp.read()
    fp.close()
    return data

def java_hashcode(_str):
    tmpret = 0
    for i in range(0, len(_str)):
        tmpret = ((tmpret * 31 & 0xFFFFFFFF) + ord(_str[i])) & 0xFFFFFFFF
    return tmpret

def buildUtdid(_imei):
    tmp_utdid = binascii.hexlify(struct.pack('>I', int(time.time())))
    tmp_utdid += binascii.hexlify(struct.pack('>I', random.randint(0, 100000000)))
    tmp_utdid += "0300"
    tmp_utdid += binascii.hexlify(struct.pack('>I', java_hashcode(_imei)))
    myhmac = hmac.new("d6fc3a4a06adbde89223bvefedc24fecde188aaa9161", digestmod=hashlib.sha1)
    myhmac.update(binascii.unhexlify('57b47f0a1b8a35a00300fbe94bcf'))
    tmp_utdid += binascii.hexlify(struct.pack('>I', java_hashcode(base64.b64encode(myhmac.digest()))))
    print tmp_utdid
    return base64.b64encode(binascii.unhexlify(tmp_utdid))

tmpimei = random.choice('123456789')
for tmpi in range(0, 14):
    tmpimei += random.choice('0123456789')
g_imei = tmpimei
g_device_no = buildUtdid(g_imei)
g_clientId = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S%f')[2:17] + "|" + g_imei

#getSign func in plug
def getsign(op_type, data, ts):
    tmpdata = 'Operation-Type=%s&Request-Data=%s&Ts=%s'%(op_type, base64.b64encode(data), ts)
    logging.info("sign:%s"%(tmpdata))
    #print "please input Sign:"
    #return raw_input(tmpdata + "\n").strip()
    #content = "operationType=com.cars.otsmobile.queryLeftTicketO&requestData=[{'to_station': 'AOH', 'dfpStr': '', 'baseDTO': {'device_no': 'Wl3sBQXHXQwDAKVXOf4uTSZd', 'version_no': '3.0.0.12121430', 'mobile_no': '', 'check_code': '5a470eadfb8e267eca4eae0ec8a4748d', 'os_type': 'a', 'user_name': '', 'time_str': '20180116201216'}, 'start_time_begin': '0000', 'seatBack_Type': '', 'ticket_num': '', 'train_headers': 'QB#', 'station_train_code': '', 'from_station': 'VNP', 'secret_str': '\u540e\u53f0\u5f00\u5173\u83b7\u53d6\u5931\u8d25\uff0c\u6216\u914d\u7f6e\u5f00\u5173\u4e3afalse.null', 'seat_type': '0', 'start_time_end': '2400', 'train_flag': '', 'train_date': '20171222', 'purpose_codes': '00'}]&ts=1516104736185"
    #test_data=urllib.urlencode(test_data)
    new_data = {}
    new_data['data'] =  tmpdata
    new_data['type'] = "native"
    count = 0
    sign = ""
    while count<5:
        try:
            r = requests.post(url=g_url,data=new_data)
            if(r.status_code == 200):
                jdata = json.loads(urllib.unquote(r.text).decode('GBK'))
                sign = jdata.get('data')
                logging.info(sign)
                return sign.strip()
        except Exception as e:
            logging.exception(e)
        count+=1
        time.sleep(0.5)
    return sign.strip()



#getSign func in plug
def getsign1(op_type, data, ts):
    tmpdata = 'operationType=%s&requestData=%s&ts=%s'%(op_type, data, ts)
    logging.info("sign1:%s"%(tmpdata))
    new_data = {}
    new_data['data'] =  tmpdata
    new_data['type'] = "native"
    count = 0
    sign = ""
    while count<5:
        try:
            r = requests.post(url=g_url,data=new_data)
            if(r.status_code == 200):
                jdata = json.loads(urllib.unquote(r.text).decode('GBK'))
                sign = jdata.get('data')
                logging.info(sign)
                return sign.strip()
        except Exception as e:
            logging.exception(e)
        count+=1
        time.sleep(0.5)
    return sign.strip()
#invokeAVMP func in plug
def getsign10(op_type, data, ts):
    tmpdata = 'Operation-Type=%s&Request-Data=%s&Ts=%s'%(op_type, base64.b64encode(data), ts)
    logging.info("sign10:%s"%(tmpdata))
    new_data = {}
    new_data['data'] =  tmpdata
    new_data['type'] = "nativeavmp"
    count = 0
    sign = ""
    while count<5:
        try:
            r = requests.post(url=g_url,data=new_data)
            if(r.status_code == 200):
                jdata = json.loads(urllib.unquote(r.text).decode('GBK'))
                sign = jdata.get('data')
                logging.info(sign)
                return sign.strip()
        except Exception as e:
            logging.exception(e)
        count+=1
        time.sleep(0.5)
    return sign.strip()

def getcheckcode(time_str, device_no):
    return hashlib.md5('F%s%s'%(time_str, device_no)).hexdigest()

def encode_b64(n):
    table = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+/'
    tmplen = int(math.pow(2, 6))
    tmpbytes = [0] * tmplen
    tmpj = tmplen
    while n != 0:
        tmpj -= 1
        tmpbytes[tmpj] = table[63 & n]
        n >>= 6
    return ''.join(tmpbytes[tmpj:])

# def unwrap_resp_data(_data, is_need_decrypt):
#     if not is_need_decrypt:
#         return _data
#     else:
#         return paraCryptexit.decode(_data)

# def wrap_req_data(_data):
#     return paraCryptexit.encode(_data)

def unwrap_resp_data(_data, is_need_decrypt):
    if not is_need_decrypt:
        return _data
    else:
        data = {"data": base64.b64encode(_data)}
        headers = {
            'Content-Type': 'application/json'
        }
        url = "%s/decode"%(g_dll_url)
        count = 0
        content = ""
        if _data == None or len(_data)==0:
            return content
        while count<5:
            try:
                content =  requests.session().post(url=url, data=json.dumps(data), headers=headers,verify=False).content
            except Exception as e:
                logging.exception(e)
            if len(content) > 0:
                return content
            else:
                count+=1
                time.sleep(0.5)
        return content
        # return paraCryptexit.decode(_data)
#
def wrap_req_data(_data):
    data = {"data": _data}
    headers = {
        'Content-Type': 'application/json'
    }
    url = "%s/encode"%(g_dll_url)
    print url
    content = None
    count = 0
    while count<5:
        try:
            content = requests.session().post(url=url, data=json.dumps(data), headers=headers, verify=False).content
        except Exception as e:
            logging.exception(e)
        if content<> None and len(content)>0:
            return bytearray(content)
        else:
            count+=1
            time.sleep(0.5)
    return bytearray("")
    # return paraCryptexit.encode(_data)

def before_login():
    pass

def do_login(wbs, un, pwd):
    result = {}
    _device_no = g_device_no
    logging.info("device_no:%s",_device_no)
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    _check_code = getcheckcode(_time_str, _device_no)

    _data = '[{"_requestBody":{"baseDTO":{"check_code":"%s","device_no":"%s","mobile_no":"","os_type":"a","time_str":"%s","user_name":"%s","version_no":"%s"},"password":"%s","user_name":"%s"}}]'%(_check_code, _device_no, _time_str, un,g_version_no, hashlib.md5(pwd).hexdigest(), un)
    logging.info(_data)

    _op_type = 'com.cars.otsmobile.login'
    _ts = encode_b64(int(time.time()*1000))
    time.sleep(1.5)
    _pagets = "-__" + encode_b64(int(time.time()*1000))
    _sign = getsign(_op_type, _data, _ts)
    if _sign=="":
        logging.info("get sign fail")
        result['status'] = '0001'
        result['message'] = 'getsign失败'
        return result
    # print _ts, _pagets

    headers = {
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'Operation-Type': _op_type,
        'Ts': _ts,
        'Content-Type': 'application/json',
        'signType': '0',
        'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'Sign': _sign,
        'Accept-Encoding': 'gzip'
    }

    logging.info('login requet:%s'%(_data))
    print 'login requet:%s'%(_data)
    data=wrap_req_data(_data)
    print "login len:%d"%(len(data))
    if len(data) == 0:
        result['status'] = '0001'
        result['message'] = '加密失败'
    count = 0
    while count<5:
        try:
            response = wbs.post('https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm',data , headers=headers, verify=False)
            if(len(response.content) == 0):
                time.sleep(0.5)
                logging.info(" login again:%d "%(count))
                result['status'] = '0001'
                result['message'] = '登录无返回'
                continue
            logging.info("resp len:%d"%len(response.content))
            unwrapRet = unwrap_resp_data(response.content, True)
            if len(unwrapRet) == 0:
                result['status'] = '0001'
                result['message'] = '解密失败'
                continue
            result['status'] = '0000'
            result['message'] = 'success'
            result['data'] = unwrapRet
            return result
        except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
            logging.exception(e)
            result['message'] = '登录网络异常'
        except Exception as e:
            logging.exception(e)
            result['message'] = '登录异常'
        finally:
            count = count + 1
    result['status'] = '4001'
    return result
def queryLeftTicket(wbs,bookInfo):
    result = {}
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    _check_code = getcheckcode(_time_str, _device_no)

    # '[{"train_date":"20171220","purpose_codes":"00","from_station":"BJP","to_station":"SHH","station_train_code":"","start_time_begin":"0000","start_time_end":"2400","train_headers":"QB#","train_flag":"","seat_type":"0","seatBack_Type":"","ticket_num":"","dfpStr":"","secret_str":"后台开关获取失败，或配置开关为false.null","baseDTO":{"check_code":"%s","device_no":"%s","mobile_no":"","os_type":"a","time_str":"%s","user_name":"","version_no":"3.0.0.12121430"}}]'%(_check_code, _device_no, _time_str)
    _op_type = 'com.cars.otsmobile.queryLeftTicketZ'
    # _dataobj = [{
    #     "train_date": bookInfo['train_date'],
    #     "purpose_codes": bookInfo['purpose_codes'],
    #     "from_station": bookInfo['from_station'],
    #     "to_station": bookInfo['to_station'],
    #     "station_train_code": "",
    #     "start_time_begin": "0000",
    #     "start_time_end": "2400",
    #     "train_headers": "QB#",
    #     "train_flag": "",
    #     "seat_type": bookInfo['seat_type'],
    #     "seatBack_Type": "",
    #     "ticket_num": "",
    #     "dfpStr": "",
    #    # "secret_str": "后台开关获取失败，或配置开关为false.null",
    #     "baseDTO": {
    #         "check_code": _check_code,
    #         "device_no": _device_no,
    #         "os_type": "a",
    #         "time_str": _time_str,
    #         "version_no": g_version_no
    #     }
    # }]
    #_data = json.dumps(_dataobj)
    #dftStr=RM9C1cdw2uyIW3Gw_DtYTC5Y8PATdz-t-Ap9A4TXdUaMGKu6FR4ur-aYTSvUMRNNx-Et2KrOUU2ueUkcSo1FUUmOjdN7WTMbKP9AqHqGXw1L1adG7ji1umz7rN-N418vrc9ZYOU85hufE-GoNjkA_qRQqW6n3H_E
    _data = '[{"train_date":"%s","purpose_codes":"%s","from_station":"%s","to_station":"%s","station_train_code":"","start_time_begin":"0000","start_time_end":"2400","train_headers":"QB#","train_flag":"","seat_type":"%s","seatBack_Type":"","ticket_num":"","dfpStr":"","baseDTO":{"check_code":"%s","device_no":"%s","mobile_no":"%s","os_type":"a","time_str":"%s","user_name":"%s","version_no":"%s"}}]'%\
     ( bookInfo['train_date'],bookInfo['purpose_codes'],bookInfo['from_station'],bookInfo['to_station'],bookInfo['seat_type'],\
       _check_code, _device_no,bookInfo["mobile_no"], _time_str,bookInfo["user_name"],g_version_no)

    _ts = int(time.time()*1000)
    _pagets = "-__" + encode_b64(int(time.time()*1000))

    _sign = getsign1(_op_type, _data, _ts)
    if _sign == "":
        logging.info("get sign fail")
        result['status'] = "0001"
        result['message'] = "get sign fail"
        return result
    headers = {
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'signType': '0',
        'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'clientId': g_clientId,
        'Accept-Encoding': 'gzip',
        'TRACKERID': '',
    }

    _url = "https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm?operationType=%s&requestData=%s&ts=%s&sign=%s"%(_op_type, urllib.quote_plus(_data), _ts, _sign)
    logging.info("%s:%s"%( _op_type,_url))

    for i in range(5):
        try:
            time.sleep(random.randint(2,8))
            resp = wbs.get(url=_url, headers=headers, verify=False)
            if resp.status_code <> 200:
                result['status'] = "0001"
                result['message'] = "查询余票报错:%d"%(resp.status_code)
                continue
            resp_data = unwrap_resp_data(resp.content, False)
            print "[%s] query left ticket:%s"%(bookInfo['orderId'],resp_data)
            logging.info("[%s] query left ticket:%s"%(bookInfo['orderId'],resp_data))
            if resp_data.find("Forbidden")>=0:
                result['status'] = "0001"
                result['message'] = "查询余票报错:403 Forbidden"
                continue
            resp_data_js = json.loads(resp_data)
            if (resp_data_js.has_key('resultStatus') and resp_data_js['resultStatus'] <> 1000):
                result['status'] = "0001"
                result['message'] = "查询余票报错:%s"%(resp_data_js['tips'])
                return result
            if(resp_data_js.has_key('resultStatus') and resp_data_js['resultStatus'] == 1000 and resp_data_js['result']['succ_flag'] == '1'):
                print 'query left success'
                result['status'] = "0000"
                result['message'] = "success"
                result['data'] = resp_data
                return result
        except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
            logging.exception(e)
            result['status'] = "0001"
            result['message'] = "查询余票网络超时"
        except Exception,e:
            logging.exception(e)
            result['status'] = "0001"
            result['message'] = "查询余票其它异常"
    logging.info(" query max times , no right return")
    return result

def queryPassenger(wbs, loginInfo):
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    _check_code = getcheckcode(_time_str, _device_no)
    _op_type = 'com.cars.otsmobile.queryPassenger'
    _dataobj = [{
        "_requestBody": {
            "baseDTO": {
                "check_code": _check_code,
                "device_no": _device_no,
                "mobile_no": loginInfo["mobileNo"],
                "os_type": "a",
                "time_str": _time_str,
                "user_name": loginInfo["user_name"],
                "version_no": g_version_no
            }
        }
    }]
    _data = json.dumps(_dataobj)
    _ts = encode_b64(int(time.time()*1000))
    _pagets = "-__" + encode_b64(int(time.time()*1000))
    _sign = getsign(_op_type, _data, _ts)
    if _sign == "":
        logging.info("get sign fail")
        return ""
    _headers = {
        'tk': loginInfo["tk"],
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'Operation-Type': _op_type,
        'Ts': _ts,
        'Content-Type': 'application/json',
        'signType': '0',
        'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'Sign': _sign,
        'Accept-Encoding': 'gzip'
    }

    try:
        response = wbs.post('https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm', data=wrap_req_data(_data), headers = _headers, verify=False)
        return unwrap_resp_data(response.content, True)
    except Exception as e:    #HTTPError必须排在URLError的前面
        logging.exception(e)
        return ""
def checkPassage(name,id,type,contactlist):
    for i in range(0,len(contactlist)):
        data = contactlist[i]
        if (data['id_no'] == id and data['user_name'].decode('utf-8') == name):
            return True
    return False
def getSex(a):
    if len(a)==15 :
        if int(a[14:])%2 == 0 :
            return 'F'
        else:
            return 'M'
    if len(a) == 18:
        if int(a[16:17])%2 == 0:
            return 'F'
        else:
            return 'M'
def addPassenger(wbs, loginInfo,name,id,type,ticketType,province_name,province_code,student_no,system,enter_year,limit_beginName,limit_beginCode,limit_endName,limit_endCode):
    result = {}
    result['status'] = '4001'
    result['message'] = ''
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    #enter_year = datetime.datetime.strftime(datetime.datetime.now(),'%Y')
    if type == '1':
        sex = getSex(id)
    else:
        sex = 'M'
    _check_code = getcheckcode(_time_str, _device_no)
    _op_type = 'com.cars.otsmobile.addPassenger'
    if type == '1':
        borndate = id[6:14]
    else:
        borndate = ''
    #[{"_requestBody":{"address":"","auditStatus":"","baseDTO":{"check_code":"e51faca911e0651421d439650ab5267f","device_no":"Wl8fjoNHlboDAOHmwyq0EwRR","mobile_no":"17079590702","os_type":"a","time_str":"20180122163149","user_name":"li04jzq36","version_no":"3.0.0.12312300"},"born_date":"19801127","card_no":"342622198011274590","card_type":"1","country_code":"CN","department":"","email":"","enter_year":"2018","mobile_no":"","name":"孙俊峰","old_card_no":"","old_card_type":"","old_name":"","passenger_type":"1","phone_no":"","postalcode":"","preference_card_no":"","preference_from_station_code":"","preference_from_station_name":"","preference_to_station_code":"","preference_to_station_name":"","province_code":"","province_name":"","school_class":"","school_code":"","school_name":"","school_system":"4","sex_code":"M","student_no":"","userCountry":"中国CHINA"}}]
    _dataobj = [
                  {
                    "_requestBody": {
                      "address": "",
                      "auditStatus": "",
                      "baseDTO": {
                        "check_code": _check_code,
                        "device_no": _device_no,
                        "mobile_no": loginInfo["mobileNo"],
                        "os_type": "a",
                        "time_str": _time_str,
                        "user_name": loginInfo["user_name"],
                        "version_no": g_version_no
                      },
                      "born_date": borndate,
                      "card_no": id,
                      "card_type": type,
                      "country_code": "CN",
                      "department": "",
                      "email": "",
                      "enter_year": enter_year,
                      "mobile_no": "",
                      "name": name,
                      "old_card_no": "",
                      "old_card_type": "",
                      "old_name": "",
                      "passenger_type": ticketType,
                      "phone_no": "",
                      "postalcode": "",
                      "preference_card_no": "",
                      "preference_from_station_code": limit_beginCode,
                      "preference_from_station_name": limit_beginName,
                      "preference_to_station_code": limit_endCode,
                      "preference_to_station_name": limit_endName,
                      "province_code": province_code,
                      "province_name": province_name,
                      "school_class": "",
                      "school_code": "",
                      "school_name": "",
                      "school_system": system,
                      "sex_code": sex,
                      "student_no": student_no,
                      "userCountry": "中国CHINA"
                    }
                  }
                ]
    _data = json.dumps(_dataobj)
    logging.info("add passager:%s"%(_data))
    print _data
    _ts = encode_b64(int(time.time()*1000))
    _pagets = "-__" + encode_b64(int(time.time()*1000))
    _sign = getsign(_op_type, _data, _ts)
    if _sign == "":
        result['status'] = "4001"
        result['message']= "get sign fail"
        logging.info("get sign fail")
        return result
    _headers = {
        'tk': loginInfo["tk"],
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'Operation-Type': _op_type,
        'Ts': _ts,
        'Content-Type': 'application/json',
        'signType': '0',
        #'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'Sign': _sign,
        'Accept-Encoding': 'gzip'
    }

    try:
        response = wbs.post('https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm', data=wrap_req_data(_data), headers = _headers, verify=False)
        addResp = unwrap_resp_data(response.content, True)
        # {"succ_flag":"0","error_msg":"您的常用联系人数量已超过上限，详见《铁路互联网购票身份核验须知》。"}
        # {"succ_flag":"1","error_msg":""}
        logging.info(addResp)
        print addResp
        js_addResp = json.loads(addResp)
        ret_flag = js_addResp["succ_flag"]
        if( ret_flag == "1"):
            result['status'] = '0000'
            result['message'] = '添加联系人成功'
        else:
             result['status'] = '0001'
             result['message'] = js_addResp['error_msg']
        return result
    except Exception as e:    #HTTPError必须排在URLError的前面
        logging.exception(e)
        result['status'] = '4001'
        result['message'] = '添加联系人网络异常'
        return result
def orderTicket(wbs, loginInfo, passengerInfo, ticketInfo, orderInfo,bookInfo):
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    _check_code = getcheckcode(_time_str, _device_no)

    _login_token = loginInfo["tk"]
    _user_name = loginInfo["user_name"]

    _passenger_phone_no = passengerInfo["mobile_no"]
    if not _passenger_phone_no:
        _passenger_phone_no = loginInfo["mobileNo"]
    _passenger_name = passengerInfo["passenger_names"]
    _passenger_id_no = passengerInfo["passenger_id_no"]
    _passenger_id_type = passengerInfo["passenger_id_type"]

    _ticket_from_station_telecode = ticketInfo["from_station_telecode"]
    _ticket_to_station_telecode = ticketInfo["to_station_telecode"]
    _ticket_location_code = orderInfo["location_code"]
    _ticket_start_time = ticketInfo["start_time"]
    _ticket_station_train_code = ticketInfo["station_train_code"]
    _ticket_train_date = ticketInfo["start_train_date"]
    _ticket_train_no = ticketInfo["train_no"]
    _ticket_yp_info = ticketInfo["yp_info"]
    _ticket_pass_code = ticketInfo["pass_code"]

    logging.info( "-"*120)
    print "-"*120
    logging.info( "订票信息如下")
    print "订票信息如下"
    logging.info(  "名字：%s"%( _passenger_name))
    print "名字：%s"%( _passenger_name)
    logging.info(  "电话：%s"%( _passenger_phone_no))
    print "电话：%s"%( _passenger_phone_no)
    logging.info(  "证据号码：%s"%(_passenger_id_no))
    print "证据号码：%s"%(_passenger_id_no)

    logging.info(  "from_station_telecode:%s"%( _ticket_from_station_telecode))
    logging.info(  "to_station_telecode:%s"%( _ticket_to_station_telecode))
    logging.info(  "location_code:%s"%( _ticket_location_code))
    logging.info(  "start_time:%s"%(_ticket_start_time))
    logging.info(  "station_train_code:%s"%( _ticket_station_train_code))
    logging.info(  "train_date:%s"%( _ticket_train_date))
    logging.info(  "train_no:%s"%( _ticket_train_no))
    logging.info(  "yp_info:%s"%( _ticket_yp_info))

    _op_type = 'com.cars.otsmobile.confirmPassengerInfoSingle'
    _dataobj = [{
        "_requestBody": {
            "baseDTO": {
                "check_code": _check_code,
                "device_no": _device_no,
                "mobile_no": _passenger_phone_no,
                "os_type": "a",
                "time_str": _time_str,
                "user_name": _user_name,
                "version_no": g_version_no
            },
            "bed_level": "",
            "bed_level_order_num": "0",
            "choose_seats": bookInfo['choose_seats'],
            "dfpStr":"",
            "dynamicProp": orderInfo["dynamicProp"],
            "from_station": _ticket_from_station_telecode,
            "from_station_telecode": _ticket_from_station_telecode,
            "location_code": _ticket_location_code,
            "mobile_nos": _passenger_phone_no,
            "p_str": "",
            "pass_code": _ticket_pass_code,
            "passenger_flag": "1",
            "passenger_id_nos": _passenger_id_no,
            "passenger_id_types": _passenger_id_type,
            "passenger_names": _passenger_name,
            "purpose_codes": "00",
            "save_passenger_flag": "Y",
            "seat_type_codes": bookInfo['seat_type_codes'],
            "start_time": _ticket_start_time,
            "station_train_code": _ticket_station_train_code,
            "ticket_type_order_num": bookInfo['ticket_type_order_num'],
            "ticket_types": bookInfo['ticket_types'],
            "to_station": _ticket_to_station_telecode,
            "to_station_telecode": _ticket_to_station_telecode,
            "tour_flag": "dc",
            #"train_date": _ticket_train_date,
            "train_date": bookInfo['train_date'],
            "train_no": _ticket_train_no,
            "yp_info": _ticket_yp_info
        }
    }]
    _data = json.dumps(_dataobj)
    logging.info( _data)
    _ts = encode_b64(int(time.time()*1000))
    _pagets = "-__" + encode_b64(int(time.time()*1000))

    _sign = getsign10(_op_type, _data, _ts)
    if _sign == "":
        logging.info("get sign fail")
        return ""
    _headers = {
        'tk': _login_token,
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'Operation-Type': _op_type,
        'Ts': _ts,
        'Content-Type': 'application/json',
        'signType': '10',
        'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'Sign': _sign,
        'Accept-Encoding': 'gzip',
    }
    for i in range(3):
        time.sleep(random.randint(3,8))
        try:
            tmpcontent = wbs.post(url='https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm', data=wrap_req_data(_data), headers=_headers, verify=False).content
            if len(tmpcontent) > 0:
                orderResult =  unwrap_resp_data(tmpcontent, True)
                logging.info( '[%s] order result:%s'% (bookInfo['orderId'],orderResult))
                print '[%s] order result:%s'% (bookInfo['orderId'],orderResult)
                return orderResult
        except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
            logging.exception(e)
        except Exception as e:
            logging.exception(e)
    return ""

def queryOrder(wbs, loginInfo):
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    _check_code = getcheckcode(_time_str, _device_no)
    # print _data
    _op_type = 'com.cars.otsmobile.queryOrder'

    _user_name = loginInfo["user_name"]

    #'[{"_requestBody":{"baseDTO":{"check_code":"%s","device_no":"%s","mobile_no":"%s","os_type":"a","time_str":"%s","user_name":"%s","version_no":"3.0.0.12121430"}}}]'%(_check_code, _device_no, mobile_no, _time_str, un)
    _dataobj = [{
        "_requestBody": {
            "baseDTO": {
                "check_code": _check_code,
                "device_no": _device_no,
                "mobile_no": loginInfo["mobileNo"],
                "os_type": "a",
                "time_str": _time_str,
                "user_name": _user_name,
                "version_no": g_version_no
            },
            "query_class": "1"
        }
    }]
    _data = json.dumps(_dataobj)
    _ts = encode_b64(int(time.time()*1000))
    _pagets = "-__" + encode_b64(int(time.time()*1000))
    _sign = getsign(_op_type, _data, _ts)
    if _sign == "":
        logging.info("get sign fail")
        return ""
    _headers = {
        'tk': loginInfo["tk"],
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'Operation-Type': _op_type,
        'Ts': _ts,
        'Content-Type': 'application/json',
        'signType': '0',
        'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'Sign': _sign,
        'Accept-Encoding': 'gzip'
    }

    try:
        response = wbs.post('https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm', data=wrap_req_data(_data), headers = _headers, verify=False)
        return unwrap_resp_data(response.content, True)
    except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
        return e.read()

def checkOrderInfo(wbs, loginInfo, ticketInfo):
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    _check_code = getcheckcode(_time_str, _device_no)
    # print _data
    _op_type = 'com.cars.otsmobile.checkOrderInfo'

    _user_name = loginInfo["user_name"]


    #'[{"_requestBody":{"baseDTO":{"check_code":"%s","device_no":"%s","mobile_no":"%s","os_type":"a","time_str":"%s","user_name":"%s","version_no":"3.0.0.12121430"}}}]'%(_check_code, _device_no, mobile_no, _time_str, un)
    _dataobj = [{
        "_requestBody": {
            "baseDTO": {
                "check_code": _check_code,
                "device_no": _device_no,
                "mobile_no": loginInfo["mobileNo"],
                "os_type": "a",
                "time_str": _time_str,
                "user_name": _user_name,
                "version_no": g_version_no
            },
            "secret_str": ticketInfo["location_code"],
            "tour_flag": "dc"
        }
    }]
    _data = json.dumps(_dataobj)
    _ts = encode_b64(int(time.time()*1000))
    _pagets = "-__" + encode_b64(int(time.time()*1000))
    _sign = getsign(_op_type, _data, _ts)
    if _sign == "":
        logging.info("get sign fail")
        return ""
    _headers = {
        'tk': loginInfo["tk"],
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'Operation-Type': _op_type,
        'Ts': _ts,
        'Content-Type': 'application/json',
        'signType': '0',
        'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'Sign': _sign,
        'Accept-Encoding': 'gzip'
    }

    count = 0
    while count < 5:
        time.sleep(random.randint(1,5))
        try:
            response = wbs.post('https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm', data=wrap_req_data(_data), headers = _headers, verify=False)
            if(len(response.content) == 0):
                count+=1
                continue
            else:
                return unwrap_resp_data(response.content, True)
        except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
            logging.exception(e)
        except Exception as e:
            logging.exception(e)
        finally:
            count+=1
    return ""
def getWaitTime(wbs, loginInfo):
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    _check_code = getcheckcode(_time_str, _device_no)
    # print _data
    _op_type = 'com.cars.otsmobile.getWaitTime'

    _user_name = loginInfo["user_name"]

    #[{"_requestBody":{"baseDTO":{"check_code":"8fa9c37a9aa0088e974531a39d7c1714","device_no":"Wl8fjoNHlboDAOHmwyq0EwRR","mobile_no":"17079590702","os_type":"a","time_str":"20180123104736","user_name":"li04jzq36","version_no":"3.0.0.12312300"},"tourFlag":"dc"}}]
    _dataobj =[
          {
            "_requestBody": {
              "baseDTO": {
                "check_code": _check_code,
                "device_no": _device_no,
                "mobile_no":  loginInfo["mobileNo"],
                "os_type": "a",
                "time_str": _time_str,
                "user_name": _user_name,
                "version_no": g_version_no
              },
              "tourFlag": "dc"
            }
          }
        ]
    _data = json.dumps(_dataobj)
    _ts = encode_b64(int(time.time()*1000))
    _pagets = "-__" + encode_b64(int(time.time()*1000))
    _sign = getsign(_op_type, _data, _ts)
    if _sign == "":
            logging.info("get sign fail")
            return ""
    _headers = {
        'tk': loginInfo["tk"],
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'Operation-Type': _op_type,
        'Ts': _ts,
        'Content-Type': 'application/json',
        'signType': '0',
        'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'Sign': _sign,
        'Accept-Encoding': 'gzip'
    }

    try:
        response = wbs.post('https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm', data=wrap_req_data(_data), headers = _headers, verify=False)
        return unwrap_resp_data(response.content, True)
    except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
        return e.read()
def confirmSingleSuc(wbs, loginInfo,orderId):
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    _check_code = getcheckcode(_time_str, _device_no)
    # print _data
    _op_type = 'com.cars.otsmobile.getWaitTime'

    _user_name = loginInfo["user_name"]

    #[{"_requestBody":{"baseDTO":{"check_code":"8fa9c37a9aa0088e974531a39d7c1714","device_no":"Wl8fjoNHlboDAOHmwyq0EwRR","mobile_no":"17079590702","os_type":"a","time_str":"20180123104736","user_name":"li04jzq36","version_no":"3.0.0.12312300"},"tourFlag":"dc"}}]
    _dataobj =[
          {
            "_requestBody": {
              "baseDTO": {
                "check_code": _check_code,
                "device_no": _device_no,
                "mobile_no":  loginInfo["mobileNo"],
                "os_type": "a",
                "time_str": _time_str,
                "user_name": _user_name,
                "version_no": g_version_no
              },
              "orderId": orderId
            }
          }
        ]
    _data = json.dumps(_dataobj)
    _ts = encode_b64(int(time.time()*1000))
    _pagets = "-__" + encode_b64(int(time.time()*1000))
    _sign = getsign(_op_type, _data, _ts)
    if _sign == "":
        logging.info("get sign fail")
        return ""
    _headers = {
        'tk': loginInfo["tk"],
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'Operation-Type': _op_type,
        'Ts': _ts,
        'Content-Type': 'application/json',
        'signType': '0',
        'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'Sign': _sign,
        'Accept-Encoding': 'gzip'
    }

    try:
        response = wbs.post('https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm', data=wrap_req_data(_data), headers = _headers, verify=False)
        return unwrap_resp_data(response.content, True)
    except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
        return e.read()
# 0000,4002返回
# 其它继续
def queryOrderBeforeBook(wbs,loginInfo,bookinfo):
    logging.info( "[%s] queryOrderBeforeBook"%bookinfo['orderId'])
    result = {}
    queryRet = queryOrder(wbs, loginInfo)
    print "[%s] queryOrderBeforeBook:%s"%(bookinfo['orderId'],queryRet)
    logging.info("[%s] queryOrderBeforeBook:%s"%(bookinfo['orderId'],queryRet))
    try:
        orderInfo = json.loads(queryRet)
    except Exception as e:
        logging.info(e)
        result['error_code'] = "0001"
        result['error_desc'] = "query fail"
        return result
    if orderInfo.has_key('orderList'):
        orderList  = orderInfo['orderList']
        if len(orderList) > 0:
            myOrder = orderList[0]
            if myOrder['order_flag'] <> '2':
                result['error_code'] = "0001"
                result['error_desc'] = "order is false"
                return result
        else:
            result['error_code'] = "0001"
            result['error_desc'] = "order is zero "
            return  result
    else:
        result['error_code'] = "0001"
        result['error_desc'] = "query fail"
        return  result
    if len(orderList) == 0:
        result['error_code'] = "0001"
        result['error_desc'] = "order is zero "
        return  result
    #车次，时间，身份证号，姓名，座位类型，票类型一致
    return checkUnfinishOrder(wbs,bookinfo,orderList)
def checkUnfinishOrder(wbs,bookinfo,orderList):
    train_date = bookinfo['train_date']
    station_train_code = bookinfo['station_train_code']
    result = {}
    #最多就会有1条
    myOrder = orderList[0]
    if not (train_date == myOrder['myTicketList'][0]['train_date']\
                and station_train_code == myOrder['myTicketList'][0]['station_train_code']\
                and myOrder['order_flag'] == '2'):
        result['error_code'] = "4001"
        result['error_desc'] = "有未完成订单"
        return result
    #判断乘客是否一致
    ids = bookinfo['passenger_id_no'].split(",")
    names = bookinfo['passenger_names'].split(",")
    seatTyps = bookinfo['seat_type_codes'].split(",")
    ticketTypes = bookinfo['ticket_types'].split(",")
    tickResult = myOrder['myTicketList']
    nFount = 0
    for i in range(len(tickResult)):
        for j in range(len(ids)):
            if(ids[j] == tickResult[i]['passenger_id_no']\
                    #and names[j] ==  tickResult[i]['passenger_name']\
                    and ticketTypes[j] == tickResult[i]['ticket_type_code']):
                nFount = nFount + 1
                break
    if nFount < len(tickResult):
        result['error_code'] = "4001"
        result['error_desc'] = "有未完成订单1"
        return result
    #查询车票时间信息
    queryResp = queryLeftTicket(wbs,bookinfo)
    if queryResp['status'] == "0000":
         try:
             queryRet_js = json.loads(queryResp['data'])
             if(queryRet_js.has_key('resultStatus') and queryRet_js['resultStatus'] == 1000 and queryRet_js['result']['succ_flag'] == '1'):
                    ticketResult = queryRet_js["result"]["ticketResult"]
                    for i in range(0, len(ticketResult)):
                        if ticketResult[i]["station_train_code"] == bookinfo['station_train_code']:
                            ticketOne = ticketResult[i]
                            bookinfo["start_time"] = ticketOne["start_time"]
                            bookinfo["lishi"] = ticketOne["lishi"]
                            bookinfo["arrive_time"] =ticketOne["arrive_time"]
                            break
         except Exception as e:
             logging.exception(e)
             bookinfo["start_time"] = ""
             bookinfo["lishi"] = ""
             bookinfo["arrive_time"] =""

    else:
        bookinfo["start_time"] = ""
        bookinfo["lishi"] = ""
        bookinfo["arrive_time"] =""

    #设定结果
    result['error_code'] = "0000"
    result['error_desc'] = "success"
    body = {}
    body["sequence"]=myOrder['sequence_no']
    body["trainCode"]= myOrder['myTicketList'][0]['station_train_code']
    body["departureDate"]=myOrder['myTicketList'][0]['train_date']
    body["fromStationName"]=myOrder['myTicketList'][0]['from_station_name']
    body["fromStationCode"]=myOrder['myTicketList'][0]['from_station_telecode']
    body["toStationName"]= myOrder['myTicketList'][0]['to_station_name']
    body["toStationCode"]= myOrder['myTicketList'][0]['to_station_telecode']

    if  bookinfo["start_time"] == "":
        body['start_time'] = myOrder['myTicketList'][0]['start_time']
    else:
        body['start_time'] = bookinfo["start_time"]
    body['lishi'] = bookinfo["lishi"]
    if bookinfo["arrive_time"] == "":
        body['arrive_time'] = myOrder['myTicketList'][0]['arrive_time']
    else:
        body['arrive_time'] =  bookinfo["arrive_time"]
    passengers = []
    for i in range(0,len(tickResult)):
        passenger = {}
        r = tickResult[i]
        passenger['name'] = r['passenger_name']
        passenger['subSequence'] =r['sequence_no']
        passenger['subOutTicketBillNo'] =r['sequence_no']
        passenger['cardType'] = r['passenger_id_type_code']
        passenger['cardNo'] = r['passenger_id_no']
        passenger['ticketType']  = r['ticket_type_code']

        passenger['seatType']  = r['seat_type_code']
        passenger['boxNo']  = r['coach_no']
        passenger['boxName']  = r['coach_name']
        passenger['seatNo']  = r['seat_no']
        passenger['seatName']  = r['seat_name']
        passenger['price']  = float(r['ticket_price'])
        passenger['loseTime']  = ''
        passengers.append(passenger)
    body['passengers'] = passengers
    result['data'] = body
    return result

def queryOrderInfo(wbs,loginInfo,bookinfo,orderId):
    logging.info( "[%s] queryOrderInfo"%bookinfo['orderId'])
    result = {}
    count = 0
    while count<5:
        count = count + 1
        time.sleep(1.5)
        queryRet = queryOrder(wbs, loginInfo)
        print "[%s] queryOrderInfo:%s"%(bookinfo['orderId'],queryRet)
        logging.info("[%s] queryOrderInfo:%s"%(bookinfo['orderId'],queryRet))
        try:
            orderInfo = json.loads(queryRet)
        except Exception as e:
            logging.exception(e)
            result['error_code'] = "4001"
            result['error_desc'] = "query fail"
            continue
        if orderInfo.has_key('succ_flag') and orderInfo['succ_flag'] <> '1':
            result['error_code'] = "4001"
            result['error_desc'] = orderInfo['error_msg']
            return result
        if orderInfo.has_key('orderList'):
            orderList  = orderInfo['orderList']
        else:
            result['error_code'] = "4001"
            result['error_desc'] = "query fail"
        if len(orderList) == 0:
            result['error_code'] = "4001"
            result['error_desc'] = "order is zero "
            continue
        else:
            if orderList[0]['order_flag'] == '2':
                break
    if count == 5:
        return result
    for i in range(len(orderList)):
        myOrder = orderList[i]
        if orderId is None or orderId =='' or myOrder['sequence_no'] == orderId:
            result['error_code'] = "0000"
            result['error_desc'] = "success"
            body = {}
            body["sequence"]=myOrder['sequence_no']
            body["trainCode"]= myOrder['myTicketList'][0]['station_train_code']
            body["departureDate"]=myOrder['myTicketList'][0]['train_date']
            body["fromStationName"]=myOrder['myTicketList'][0]['from_station_name']
            body["fromStationCode"]=myOrder['myTicketList'][0]['from_station_telecode']
            body["toStationName"]= myOrder['myTicketList'][0]['to_station_name']
            body["toStationCode"]= myOrder['myTicketList'][0]['to_station_telecode']
            body['start_time'] = bookinfo["start_time"]
            body['lishi'] = bookinfo["lishi"]
            body['arrive_time'] = bookinfo["arrive_time"]
            passengers = []
            tickResult = myOrder['myTicketList']
            for i in range(0,len(tickResult)):
                passenger = {}
                r = tickResult[i]
                passenger['name'] = r['passenger_name']
                passenger['subSequence'] =r['sequence_no']
                passenger['subOutTicketBillNo'] =r['sequence_no']
                passenger['cardType'] = r['passenger_id_type_code']
                passenger['cardNo'] = r['passenger_id_no']
                passenger['ticketType']  = r['ticket_type_code']

                passenger['seatType']  = r['seat_type_code']
                passenger['boxNo']  = r['coach_no']
                passenger['boxName']  = r['coach_name']
                passenger['seatNo']  = r['seat_no']
                passenger['seatName']  = r['seat_name']
                passenger['price']  = float(r['ticket_price'])
                passenger['loseTime']  = ''
                passengers.append(passenger)
            body['passengers'] = passengers
            result['data'] = body
            return result
    result['error_code'] = "4001"
    result['error_desc'] = "orderId not find"
    return result

def printOrderInfo(wbs, loginInfo,bookinfo):
    result = {}
    logging.info( "[%s] print order" %bookInfo['orderId'])
    queryRet = queryOrder(wbs, loginInfo)
    orderList = json.loads(queryRet)["orderList"]
    if len(orderList) > 0:
        print "查询到%d个订单"%(len(orderList))
        logging.info("查询到%d个订单"%(len(orderList)))
        for i in range(0, len(orderList)):
            logging.info( "dingdan%d"%(i))
            logging.info( u"\tding dan biao shi:%s"%(orderList[i]["order_flag"]))
            logging.info( u"\tdui lie xing xi:%s"%(orderList[i]["queue_message"]))
            logging.info( u"\tding dan xing xi:%s"%(orderList[i]))

    else:
        logging.info("\t无订单信息")
        print "\t无订单信息"
        result['error_code'] = '4001'
        result['error_desc'] = 'no order'
        return result

def login(wbs, username, password):
    result = {}
    logging.info("login:%s,%s"%(username,password))
    loginResult = do_login(wbs, username, password)
    if loginResult['status'] <> '0000':
        return loginResult
    loginRetStr = loginResult['data']
    logging.info("logretstr:%s"%(loginRetStr))
    try:
        loginInfo = json.loads(loginRetStr)
    except Exception,e:
        logging.exception(e)
        result['status'] = '0001'
        result['message'] = 'json格式错误'
        return result
    if loginInfo.has_key('succ_flag') and loginInfo['succ_flag']<>'1':
        result['status'] = '0001'
        result['message'] = loginInfo['error_msg']
        return result
    if not loginInfo.has_key('name'):
        logging.info('login fail')
        result['status'] = '0001'
        result['message'] = 'json格式错误，无name'
        return result
    name = loginInfo["name"]
    mobileNo = str(loginInfo["mobileNo"])
    user_id = loginInfo["user_id"]
    login_token = loginInfo["tk"]

    logging.info("login success:%s,%s,%s,%s"%( name, mobileNo, user_id, login_token))
    result['status'] = '0000'
    result['message'] = 'success'
    result['data'] = loginInfo
    return result

def test_full(wbs, loginInfo,bookInfo):

    result = {}
    result['status'] = '1'
    result['message'] = ''
    logging.info("查询余票")
    print "查询余票"
    queryResp = queryLeftTicket(wbs,bookInfo)
    if queryResp['status'] <> "0000":
         result['status'] = '1'
         result['message'] = queryResp['message']
         return result
    #logging.info(queryRet)
    try:
        queryRet_js = json.loads(queryResp['data'])
    except Exception as e:
        logging.exception(e)
        result['status'] = '1'
        result['message'] = '查询余票失败'
        return result
    if(queryRet_js.has_key('resultStatus') and queryRet_js['resultStatus'] == 1000 and queryRet_js['result']['succ_flag'] == '1'):
        ticketResult = queryRet_js["result"]["ticketResult"]
    else:
        print "查询余票失败"
        result['status'] = '1'
        if queryRet_js.has_key('result'):
            result['message'] =  queryRet_js['result']['error_msg']
        else:
           result['message'] = '查询余票失败'
        return result
    ticketOne = {}
    for i in range(0, len(ticketResult)):
        if ticketResult[i]["station_train_code"] == bookInfo['station_train_code']:
            ticketOne = ticketResult[i]
            break
    if not ticketOne:
        logging.info("余票没有%s次列车"%(bookInfo['station_train_code']))
        result['status'] = '1'
        result['message'] = "余票没有%s次列车"%(bookInfo['station_train_code'])
        return result
    logging.info("选择一个班次")
    print "选择一个班次"
    logging.info("出发站：%s"%(ticketOne["from_station_name"]))
    print "出发站：%s"%(ticketOne["from_station_name"])
    logging.info("目的站：%s"%(ticketOne["to_station_name"]))
    print "目的站：%s"%(ticketOne["to_station_name"])
    logging.info("火车号码：%s"%(ticketOne["train_no"]))
    print "火车号码：%s"%(ticketOne["train_no"])
    logging.info("出发日期：%s"%(ticketOne["start_train_date"]))
    print "出发日期：%s"%(ticketOne["start_train_date"])
    logging.info("出发时间：%s"%(ticketOne["start_time"]))
    print "出发时间：%s"%(ticketOne["start_time"])
    logging.info("位置代码：%s"%(ticketOne["location_code"]))
    print "位置代码：%s"%(ticketOne["location_code"])
    logging.info("余票信息：%s"%(ticketOne["yp_info"]))
    print "余票信息：%s"%(ticketOne["yp_info"])

    bookInfo['fromStationName'] = ticketOne["from_station_name"]
    bookInfo['fromStationCode'] = ticketOne["from_station_telecode"]
    bookInfo['toStationName'] = ticketOne["to_station_name"]
    bookInfo['toStationCode'] = ticketOne["to_station_telecode"]
    bookInfo['start_time'] = ticketOne["start_time"]
    bookInfo['lishi'] = ticketOne["lishi"]
    bookInfo['arrive_time'] = ticketOne["arrive_time"]

    #确认坐席
    if not bookInfo['station_train_code'][0] in ('G','D','C'):
        seattypes = bookInfo['seat_type_codes'].split(',')
        newseattypes = ""
        for i in range(len(seattypes)):
            if seattypes[i]=='O':
                newseattypes = "%s,1_none"%(newseattypes)
            else:
                newseattypes = "%s,%s"%(newseattypes,seattypes[i])
        bookInfo['seat_type_codes'] = newseattypes[1:]
        logging.info("change seat_type:%s"%( bookInfo['seat_type_codes']))

    time.sleep(random.randint(2,8))
    logging.info( "确认订单信息")
    print "确认订单信息"
    queryRet = checkOrderInfo(wbs, loginInfo, ticketOne)
    logging.info( queryRet)
    if queryRet == "":
        result['status'] = '6'
        result['message'] = "确认订单网络异常"
        return result
    try:
        orderInfo = json.loads(queryRet)
    except Exception as e:
        logging.exception(e)
        result['status'] = '6'
        result['message'] = "确认订单返回数据格式错误"
        return result
    print orderInfo
    if(orderInfo["succ_flag"] == 0 ):
        result['status'] = '1'
        result['message'] = orderInfo["error_msg"]
        return result
    if(orderInfo["is_support_chooseSeats"] == 'N'):
        bookInfo['choose_seats'] = ""
    logging.info("查询常用联系人")
    print "查询常用联系人:"
    count = 0
    flag = False
    while count<5:
        time.sleep(random.randint(2,8))
        queryRet = queryPassenger(wbs, loginInfo)
        if len(queryRet) == 0:
            count+=1
            continue
        logging.info(queryRet)
        try:
            js_queryRet = json.loads(queryRet)
            passengerResult = js_queryRet["passengerResult"]
            if js_queryRet['succ_flag'] == '1':
                flag = True
                break
            else:
                result['status'] = '7'
                result['message'] = js_queryRet["error_msg"]
                count+=1
                continue
        except urllib2.HTTPError as e:    #HTTPError必须排在URLError的前面
            logging.exception(e)
            result['status'] = '1'
            result['message'] =  "查询常用联系人网络超时"
        except Exception as e:
            logging.exception(e)
            result['status'] = '1'
            result['message'] =  "查询常用联系人异常"
        finally:
            count+=1
    if not flag:
        result['message'] =  "%s，多次查询联系人失败"%(result['message'])
        return result
    passager_names = bookInfo['passenger_names']
    name_array = passager_names.split(',')
    passager_id_types = bookInfo["passenger_id_type"].split(',')
    passager_ids = bookInfo["passenger_id_no"].split(',')
    ticketTypes = bookInfo['ticket_types'].split(",")
    #学生票信息
    province_names = bookInfo["province_name"].split(',')
    province_codes = bookInfo["province_code"].split(',')
    student_nos = bookInfo["student_no"].split(',')
    systems = bookInfo["system"].split(',')
    enter_years = bookInfo["enter_year"].split(',')
    limit_beginNames = bookInfo["limit_beginName"].split(',')
    limit_beginCodes = bookInfo["limit_beginCode"].split(',')
    limit_endNames = bookInfo["limit_endName"].split(',')
    limit_endCodes = bookInfo["limit_endCode"].split(',')

    for ind in range(0,len(name_array)):
        checkRet = checkPassage(name_array[ind],passager_ids[ind],passager_id_types[ind],passengerResult)
        if(not checkRet):
            #已经添加
            fFind = False
            for index in range(0,ind):
                if passager_ids[index] == passager_ids[ind] and name_array[index] == name_array[ind]:
                    fFind = True
                    break
            if fFind:
                #已经添加，继续判断下一个
                continue
            count = 0
            while count < 5:
                time.sleep(random.randint(1,6))
                addRet = addPassenger(wbs,loginInfo,name_array[ind],passager_ids[ind],passager_id_types[ind],ticketTypes[ind],province_names[ind],province_codes[ind],student_nos[ind],systems[ind],enter_years[ind],limit_beginNames[ind],limit_beginCodes[ind],limit_endNames[ind],limit_endCodes[ind])
                if addRet['status'] == '0000':
                    break
                elif addRet['status'] == '0001':
                    break
                else:
                    count+=1
            if addRet['status'] <> '0000':
                logging.info("添加联系人失败:%s"%(name_array[ind]))
                print ("添加联系人失败:%s"%(name_array[ind]))
                result['status'] = '2'
                result['message'] = "添加联系人失败:%s,%s"%(name_array[ind],addRet['message'])
                return result
    # passengerOne = passengerResult[random.randint(0, len(passengerResult))]
    #passengerOne = passengerResult[0]

    logging.info( "设置乘客")
    print  "设置乘客"
    logging.info( "乘客姓名：%s"%(bookInfo["passenger_names"]))
    print "乘客姓名：%s"%(bookInfo["passenger_names"])
    logging.info( "证件类型：%s"%(bookInfo["passenger_id_type"]))
    print "证件类型：%s"%(bookInfo["passenger_id_type"])
    logging.info( "证件号码：%s"%(bookInfo["passenger_id_no"]))
    print "证件号码：%s"%(bookInfo["passenger_id_no"])
    #print "手机号码：", bookInfo["mobile_no"]

    passengerOne = {}
    passengerOne['passenger_names'] =  bookInfo["passenger_names"]
    passengerOne['passenger_id_type'] =  bookInfo["passenger_id_type"]
    passengerOne['passenger_id_no'] =  bookInfo["passenger_id_no"]
    if(bookInfo.has_key('mobile_no')):
        passengerOne['mobile_no'] =  bookInfo["mobile_no"]
    else:
        passengerOne['mobile_no'] = ''

    logging.info(  "开始订票")
    print "开始订票"
    logging.info(  "验证码检查")
    pcType = orderInfo['pc_types']
    isNeedCheck = False
    if(pcType.find('Y') >= 0 ):
        temp = pcType.split("*")[1]
        logging.info("需要验证码:%s,本次预定:%s"%(temp,bookInfo['seat_type_codes']),)
        if(bookInfo['seat_type_codes'].find("1")>=0 and temp.find("1_none")):
            isNeedCheck = True
            logging.info(  "需要验证码1")
        elif (temp.find(bookInfo['seat_type_codes'].split(",")[0])>=0):
            isNeedCheck = True
            logging.info(  "需要验证码2")
        else:
            isNeedCheck = False
            logging.info(  "不需要验证码1")
    else:
        isNeedCheck = False
        logging.info(  "不需要验证码2")
    if isNeedCheck:
        ticketOne['pass_code'] = ""
        count = 0
        while count<5:
            #get code
            ret = getPassCodeNew(wbs,loginInfo,ticketOne,bookInfo)
            logging.info("get pass dode:%s"%ret)
            if ret['status'] <> '0':
                count = count + 1
                continue;
            res = dama.dama(ticketOne['code_path'])
            logging.info("da ma:%s"%(res))
            if res == '':
                count = count + 1
                continue
            ticketOne['pass_code'] = res
            time.sleep(random.randint(2,8))
            ret = orderTicket(wbs, loginInfo, passengerOne, ticketOne, orderInfo,bookInfo)
            try:
                js_ret = json.loads(ret)
                if(js_ret['succ_flag'] == '0'):
                    result['status'] = '4'
                    result['message'] = js_ret['error_msg']
                    error_msg = js_ret['error_msg']
                    if error_msg.find("验证码不正确".decode('utf-8'))>=0:
                        logging.info("验证码不正确:%d"%(count))
                        continue
                    else:
                        return result
                else:
                    result['status'] = '0'
                    result['message'] = js_ret['queueMessage']
                    return result
            except Exception as e:
                logging.exception(e)
            finally:
                count = count + 1
        result['status'] = '5'
        result['message'] = "验证码尝试次数过多"
    else:
        ticketOne['pass_code'] = ""
        time.sleep(random.randint(2,8))
        ret = orderTicket(wbs, loginInfo, passengerOne, ticketOne, orderInfo,bookInfo)
        try:
            if ret <> "":
                js_ret = json.loads(ret)
                if(js_ret['succ_flag'] == '0'):
                    result['status'] = '4'
                    result['message'] = js_ret['error_msg']
                else:
                    result['status'] = '0'
                    result['message'] = js_ret['queueMessage']
            else:
                result['status'] = '4'
                result['message'] = "订票异常"
        except Exception as e:
            logging.exception(e)
            result['status'] = '3'
            result['message'] = "订票异常"
    logging.info("结束订票:%s,%s"%(bookInfo['orderId'],result))
    return result
#error_code="0"
#error_desc=""
#data={}
def getPassCodeNew(wbs, loginInfo, ticketInfo,bookInfo):
    result = {}
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    _check_code = getcheckcode(_time_str, _device_no)
    # print _data
    _op_type = 'com.cars.otsmobile.getPassCodeNew'

    _user_name = loginInfo["user_name"]

    _dataobj = [{
        "_requestBody": {
            "baseDTO": {
                "check_code": _check_code,
                "device_no": _device_no,
                "mobile_no": loginInfo["mobileNo"],
                "os_type": "a",
                "time_str": _time_str,
                "user_name": _user_name,
                "version_no": g_version_no
            },
            "location_code": ticketInfo["location_code"],
            "module": "passenger",
            "rand": "randp"
        }
    }]
    _data = json.dumps(_dataobj)
    _ts = encode_b64(int(time.time()*1000))
    _pagets = "-__" + encode_b64(int(time.time()*1000))
    _sign = getsign(_op_type, _data, _ts)
    if _sign == "":
        result['status'] = "1"
        result['message']= "get sign fail"
        logging.info("get sign fail")
        return result

    _headers = {
        'tk': loginInfo["tk"],
        'pagets': _pagets,
        'AppId': '9101430221728',
        'Platform': 'ANDROID',
        'WorkspaceId': 'product',
        'Version': '2',
        'Did': _device_no,
        'Operation-Type': _op_type,
        'Ts': _ts,
        'Content-Type': 'application/json',
        'signType': '0',
        'Accept-Language': 'zh-Hans',
        'Host': 'mobile.12306.cn',
        'User-Agent': 'android',
        'Connection': 'close',
        'Sign': _sign,
        'Accept-Encoding': 'gzip'
    }

    try:
        response = wbs.post('https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm', data=wrap_req_data(_data), headers = _headers, verify=False)
        resp = unwrap_resp_data(response.content, True)
        logging.info("[%s] getPassCodeNew:%s"%(bookInfo['orderId'],resp))
        js_resp = json.loads(resp)
        if(js_resp['succ_flag'] <> '1'):
            result['status'] = "1"
            result['message']= js_resp['error_msg']
            return result
        img = js_resp['passcode']
        #datatime = time.strftime('%Y/%m/%d/', time.localtime(time.time()))
        #path = './captcha/'+ datatime
        path = './captcha/'
        unit.mkdir(path)
        filename = path + bookInfo['orderId'] +'.jpg'
        logging.info('[%s] sava captcha:%s'%(bookInfo['orderId'],filename))
        ticketInfo['code_path'] = filename
        writeBin(filename,base64.b64decode(img))
        result['status'] = "0"
        result['message']= "success"
        return result
    except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
        logging.exception(e)
        result['status'] = "1"
        result['message']= "exception1"
        return result
    except Exception as e:
        logging.exception(e)
        result['status'] = "1"
        result['message']= "exception2"
        return result

def bookTickt(bookInfo):
    wbs = requests.session()
    if bookInfo['proxy'] <> '':
        proxy = {}
        proxy['http'] = bookInfo['proxy']
        wbs.proxies = proxy
    result = {}
    loginResult = login(wbs,bookInfo['user_name'],bookInfo['password'])
    if loginResult['status'] <> "0000":
        result["error_code"] = "4001"
        result["error_desc"] = "登录失败:%s"%(loginResult['message'])
        return result
    loginInfo = loginResult['data']
    try:
        #查询是否有未完成订单
        bookInfo['mobile_no']=loginInfo['mobileNo']
        qrt = queryOrderBeforeBook(wbs,loginInfo,bookInfo)
        print(qrt)
        logging.info("[%s]查询未完成订单:%s"%(bookInfo['orderId'],qrt))
        if(qrt['error_code'] == "0000" or qrt['error_code'] == "4001"):
            logging.info("查询有未完成订单，直接返回")
            return qrt
        logging.info("查询无未完成订单")
        ret = test_full(wbs, loginInfo,bookInfo)
        logging.info("[%s] test_full:%s"%(bookInfo['orderId'],ret))
        if ret['status'] <> '0':
            result["error_code"] = "4001"
            result["error_desc"] = ret['message']
            return result
    except Exception,e:
       logging.exception(e)
       result["error_code"] = "4001"
       result["error_desc"] = "book exception"
       return result
    orderStr = getWaitTime(wbs,loginInfo)
    print "getWaitTime:%s"%(orderStr)
    logging.info("getWaitTime:%s"%(orderStr))
    newOrderId = ""
    if(orderStr == ""):
        time.sleep(3)
        return queryOrderInfo(wbs,loginInfo,bookInfo,newOrderId)
    jsOrder = {}
    try:
        jsOrder = json.loads(orderStr)
    except Exception,e:
        logging.exception(e)
        logging.info("analyze query waittime exception")
        result["error_code"] = "4001"
        result["error_desc"] = "query waittime fail"
        return result
    if jsOrder['succ_flag'] <> '1':
        logging.info('get waittime fail')
        result["error_code"] = "4001"
        result["error_desc"] = jsOrder['error_msg']
        return result
    newOrderId =  jsOrder['orderId']
    print "orderid:%s"%(newOrderId)
    logging.info("orderid:%s"%(newOrderId))
    if(orderStr.find('返回未完成订单'.decode('utf-8'))>0):
        time.sleep(3)
        return queryOrderInfo(wbs,loginInfo,bookInfo,newOrderId)
    else:
       return queryOrderConfirmSingle(wbs,loginInfo,newOrderId,bookInfo)
    #printOrderInfo(wbs, loginInfo)
    #time.sleep(3)
    #printOrderInfo(wbs, loginInfo)
def queryOrderConfirmSingle(wbs,loginInfo,newOrderId,bookInfo):
    result = {}
    confirmRet = confirmSingleSuc(wbs,loginInfo,newOrderId)
    print "confirmSingleSuc:%s"%(confirmRet)
    logging.info("[%s] confirmSingleSuc:%s"%(bookInfo['orderId'],confirmRet))
    try:
        jsConfirm = json.loads(confirmRet)
    except Exception as e:
        logging.info(e)
        result["error_code"] = "4001"
        result["error_desc"] = "查询订单确认失败"
        return result
    if not jsConfirm.has_key('ticketResult'):
        result["error_code"] = "4001"
        result["error_desc"] = "ticketResult is empty"
        return result
    if jsConfirm['succ_flag'] == '1':
        result['error_code'] = "0000"
        result['error_desc'] = "success"
        body = {}
        body["sequence"]=newOrderId
        body["trainCode"]= jsConfirm['ticketResult'][0]['station_train_code']
        body["departureDate"]=bookInfo['train_date']
        body["fromStationName"]=bookInfo['fromStationName']
        body["fromStationCode"]=bookInfo['fromStationCode']
        body["toStationName"]= bookInfo['toStationName']
        body["toStationCode"]= bookInfo['toStationCode']
        body['start_time'] = bookInfo["start_time"]
        body['lishi'] = bookInfo["lishi"]
        body['arrive_time'] = bookInfo["arrive_time"]
        passengers = []
        tickResult = jsConfirm['ticketResult']
        for i in range(0,len(tickResult)):
            passenger = {}
            r = tickResult[i]
            passenger['name'] = r['passenger_name']
            passenger['subSequence'] =r['sequence_no']
            passenger['subOutTicketBillNo'] = r['sequence_no']
            passenger['cardType'] = r['passenger_id_type_code']
            passenger['cardNo'] = r['passenger_id_no']
            passenger['ticketType']  = r['ticket_type_code']

            passenger['seatType']  = r['seat_type_code']
            passenger['boxNo']  = r['coach_no']
            passenger['boxName']  = r['coach_name']
            passenger['seatNo']  = r['seat_no']
            passenger['seatName']  = r['seat_name']
            passenger['price']  = float(r['ticket_price'])
            timestr = r['lose_time']
            y = timestr[0:4]
            m = timestr[4:6]
            d = timestr[6:8]
            h = timestr[8:10]
            i = timestr[10:12]
            s = timestr[12:14]
            passenger['loseTime']  = '%s-%s-%s %s:%s:%s'%(y,m,d,h,i,s)
            passengers.append(passenger)
        body['passengers'] = passengers
        result['data'] = body
    else:
        result['error_code'] = "4001"
        result['error_desc'] = jsConfirm['error_msg']
    return result

if __name__ == "__main__":

#    print getsign("login", "sdfsfdsfsdf", "sdfsfsdf")
##   print buildUtdid('12345678901234')
    #print base64.b64encode("skie[;s'skf']")
    #print base64.b64decode()
    wbs = requests.session()
    username = 'li04jzq36'
    password = 'hzflzk34'
    bookInfo = {}
    bookInfo['user_name'] = username
    bookInfo['password'] = password
    bookTickt(bookInfo)
#    loginInfo = login(wbs, username, password)
#    test_full(wbs, loginInfo)
#   printOrderInfo(wbs, loginInfo)
#   time.sleep(3)
#   printOrderInfo(wbs, loginInfo)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  