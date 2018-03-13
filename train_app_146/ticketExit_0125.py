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
import paraCryptexit
from logger import *

BS = 16
pad = lambda s: s + (BS - len(s) % BS) * chr(BS - len(s) % BS)
unpad = lambda s: s[0:-ord(s[-1])]
#g_url="http://192.168.65.227:17080/so/soCall.htm?"
g_url="http://localhost:17080/so/soCall.htm?"
#g_version_no = "3.0.0.12121430"
g_version_no = "3.0.0.12312300"
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
    r = requests.post(url=g_url,data=new_data)
    if(r.status_code == 200):
        jdata = json.loads(urllib.unquote(r.text).decode('GBK'))
        sign = jdata.get('data')
    else:
        sign = ""
    logging.info(sign)
    return sign.strip()
#getSign func in plug
def getsign1(op_type, data, ts):
    tmpdata = 'operationType=%s&requestData=%s&ts=%s'%(op_type, data, ts)
    logging.info("sign1:%s"%(tmpdata))
    new_data = {}
    new_data['data'] =  tmpdata
    new_data['type'] = "native"
    r = requests.post(url=g_url,data=new_data)
    if(r.status_code == 200):
        jdata = json.loads(urllib.unquote(r.text).decode('GBK'))
        sign = jdata.get('data')
    else:
        sign = ""
    print sign
    return sign.strip()

#invokeAVMP func in plug
def getsign10(op_type, data, ts):
    tmpdata = 'Operation-Type=%s&Request-Data=%s&Ts=%s'%(op_type, base64.b64encode(data), ts)
    logging.info("sign10:%s"%(tmpdata))
    new_data = {}
    new_data['data'] =  tmpdata
    new_data['type'] = "nativeavmp"
    r = requests.post(url=g_url,data=new_data)
    if(r.status_code == 200):
        jdata = json.loads(urllib.unquote(r.text).decode('GBK'))
        sign = jdata.get('data')
    else:
        sign = ""
    logging.info(sign)
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

def unwrap_resp_data(_data, is_need_decrypt):
    if not is_need_decrypt:
        return _data
    else:
        return paraCryptexit.decode(_data)

def wrap_req_data(_data):
    return paraCryptexit.encode(_data)


def before_login():
    pass

def do_login(wbs, un, pwd):

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
    count = 0
    while count<5:
        try:
            response = wbs.post('https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm',data , headers=headers, verify=False)
            if(len(response.content) == 0):
                time.sleep(0.5)
                logging.info(" login again:%d "%(count))
                continue
            logging.info("resp len:%d"%len(response.content))
            writeBin("response.bin",response.content)
            unwrapRet = unwrap_resp_data(response.content, True)
            print unwrapRet
            return unwrapRet
        except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
            logging.exception(e)
        except Exception as e:
            logging.exception(e)
        finally:
            count = count + 1
    return ""
def queryLeftTicket(wbs,bookInfo):
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
    _data = '[{"train_date":"%s","purpose_codes":"%s","from_station":"%s","to_station":"%s","station_train_code":"","start_time_begin":"0000","start_time_end":"2400","train_headers":"QB#","train_flag":"","seat_type":"%s","seatBack_Type":"","ticket_num":"","dfpStr":"RM9C1cdw2uyIW3Gw_DtYTC5Y8PATdz-t-Ap9A4TXdUaMGKu6FR4ur-aYTSvUMRNNx-Et2KrOUU2ueUkcSo1FUUmOjdN7WTMbKP9AqHqGXw1L1adG7ji1umz7rN-N418vrc9ZYOU85hufE-GoNjkA_qRQqW6n3H_E","baseDTO":{"check_code":"%s","device_no":"%s","os_type":"a","time_str":"%s","version_no":"%s"}}]'%\
     ( bookInfo['train_date'],bookInfo['purpose_codes'],bookInfo['from_station'],bookInfo['to_station'],bookInfo['seat_type'],\
       _check_code, _device_no, _time_str,g_version_no)

    _ts = int(time.time()*1000)
    _pagets = "-__" + encode_b64(int(time.time()*1000))

    _sign = getsign1(_op_type, _data, _ts)
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
            resp_data = unwrap_resp_data(wbs.get(url=_url, headers=headers, verify=False).content, False)
            print "query left ticket:%s"%(resp_data)
            logging.info("query left ticket:%s"%(resp_data))
            resp_data_js = json.loads(resp_data)
            if(resp_data_js.has_key('resultStatus') and resp_data_js['resultStatus'] == 1000 and resp_data_js['result']['succ_flag'] == '1'):
                print 'query left success'
                return resp_data
        except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
            logging.exception(e)
        except Exception,e:
            logging.exception(e)
    logging.info(" query max times , no right return")
    return ""

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
def addPassenger(wbs, loginInfo,name,id,type):
    _device_no = g_device_no
    _time_str = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
    enter_year = datetime.datetime.strftime(datetime.datetime.now(),'%Y')
    sex = getSex(id)
    _check_code = getcheckcode(_time_str, _device_no)
    _op_type = 'com.cars.otsmobile.addPassenger'
    borndate = id[6:14]
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
                      "passenger_type": type,
                      "phone_no": "",
                      "postalcode": "",
                      "preference_card_no": "",
                      "preference_from_station_code": "",
                      "preference_from_station_name": "",
                      "preference_to_station_code": "",
                      "preference_to_station_name": "",
                      "province_code": "",
                      "province_name": "",
                      "school_class": "",
                      "school_code": "",
                      "school_name": "",
                      "school_system": "4",
                      "sex_code": sex,
                      "student_no": "",
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
        ret_flag = json.loads(addResp)["succ_flag"]
        if( ret_flag == "1"):
            return True
        else:
            return False
    except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
        logging.exception(e)
        print e.read
        return False
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
            "dynamicProp": orderInfo["dynamicProp"],
            "from_station": _ticket_from_station_telecode,
            "from_station_telecode": _ticket_from_station_telecode,
            "location_code": _ticket_location_code,
            "mobile_nos": _passenger_phone_no,
            "p_str": "",
            "pass_code": "",
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
    try:
        tmpcontent = wbs.post(url='https://mobile.12306.cn/otsmobile/app/mgs/mgw.htm', data=wrap_req_data(_data), headers=_headers, verify=False).content
        orderResult =  unwrap_resp_data(tmpcontent, True)
        logging.info( 'order result:%s'% (orderResult))
        print 'order result:%s'% (orderResult)
        return "0"
    except urllib2.HTTPError,e:    #HTTPError必须排在URLError的前面
        return e.read()

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
def queryOrderInfo(wbs,loginInfo,bookinfo,orderId):
    logging.info( "queryOrderInfo")
    result = {}
    queryRet = queryOrder(wbs, loginInfo)
    print "queryOrderInfo:%s"%queryRet
    logging.info("queryOrderInfo:%s"%queryRet)
    orderInfo = json.loads(queryRet)
    if orderInfo.has_key('orderList'):
        orderList  = orderInfo['orderList']
    else:
        result['error_code'] = "4001"
        result['error_desc'] = "query fail"
        return  result
    if len(orderList) == 0:
        result['error_code'] = "4001"
        result['error_desc'] = "order is zero "
        return  result
    for i in range(len(orderList)):
        myOrder = orderList[i]
        #and myOrder['sequence_no'] == orderId
        #if myOrder['order_flag'] == '2':
        if myOrder['sequence_no'] == orderId:
            result['error_code'] = "0000"
            result['error_desc'] = "success"
            body = {}
            body["sequence"]=orderId
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
    logging.info( "print order")
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
    logging.info("login:%s,%s"%(username,password))
    loginRetStr = do_login(wbs, username, password)
    logging.info("logretstr:%s"%(loginRetStr))
    try:
        loginInfo = json.loads(loginRetStr)
    except Exception,e:
        logging.exception(e)
        return "login fail"
    if not loginInfo.has_key('name'):
        logging.info('login fail')
        return ""
    name = loginInfo["name"]
    mobileNo = str(loginInfo["mobileNo"])
    user_id = loginInfo["user_id"]
    login_token = loginInfo["tk"]

    logging.info("login success:%s,%s,%s,%s"%( name, mobileNo, user_id, login_token))
    return loginInfo

def test_full(wbs, loginInfo,bookInfo):

    logging.info("查询余票")
    print "查询余票"
    queryRet = queryLeftTicket(wbs,bookInfo)
    #logging.info(queryRet)
    logging.info("*****1")
    queryRet_js = json.loads(queryRet)
    if(queryRet_js.has_key('resultStatus') and queryRet_js['resultStatus'] == 1000 and queryRet_js['result']['succ_flag'] == '1'):
        ticketResult = queryRet_js["result"]["ticketResult"]
        logging.info("*****2")
    else:
        print "查询余票失败"
        return "1"
    for i in range(0, len(ticketResult)):
        if ticketResult[i]["station_train_code"] == bookInfo['station_train_code']:
            ticketOne = ticketResult[i]
            break

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

    # time.sleep(3)
    #

    logging.info( "确认订单信息")
    print "确认订单信息"
    queryRet = checkOrderInfo(wbs, loginInfo, ticketOne)
    orderInfo = json.loads(queryRet)
    logging.info( orderInfo)
    print orderInfo


    logging.info("查询常用联系人")
    print "查询常用联系人:"
    queryRet = queryPassenger(wbs, loginInfo)
    logging.info(queryRet)
    passengerResult = json.loads(queryRet)["passengerResult"]
    passager_names = bookInfo['passenger_names']
    name_array = passager_names.split(',')
    passager_id_types = bookInfo["passenger_id_type"].split(',')
    passager_ids = bookInfo["passenger_id_no"].split(',')
    for ind in range(0,len(name_array)):
        checkRet = checkPassage(name_array[ind],passager_ids[ind],passager_id_types[ind],passengerResult)
        if(not checkRet):
            addRet = addPassenger(wbs,loginInfo,name_array[ind],passager_ids[ind],passager_id_types[ind])
            if not addRet:
                logging.info("添加联系人失败:%s"%(name_array[ind]))
                print ("添加联系人失败:%s"%(name_array[ind]))
                return "2"
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
    ret = orderTicket(wbs, loginInfo, passengerOne, ticketOne, orderInfo,bookInfo)
    logging.info("结束订票:%s"%(bookInfo['orderId']))
    print "结束订票"
    return ret
#error_code="0"
#error_desc=""
#data={}
def bookTickt(bookInfo):
    wbs = requests.session()
    result = {}
    loginInfo = login(wbs,bookInfo['user_name'],bookInfo['password'])
    if loginInfo == "":
        result["error_code"] = "4001"
        result["error_desc"] = "login fail"
        return result
    try:
        ret = test_full(wbs, loginInfo,bookInfo)
        if ret == '0':
            pass
        elif ret == '1':
            result["error_code"] = "4001"
            result["error_desc"] = "query left ticket fail"
            return result
        elif ret == '2':
            result["error_code"] = "4001"
            result["error_desc"] = "add passager fail"
            return result
        else:
            result["error_code"] = "4001"
            result["error_desc"] = "unknown fail"
            return result
    except Exception,e:
       result["error_code"] = "4001"
       result["error_desc"] = "book exception"
       return result
    orderStr = getWaitTime(wbs,loginInfo)
    print "getWaitTime:%s"%(orderStr)
    logging.info("getWaitTime:%s"%(orderStr))
    newOrderId = ""
    try:
        jsOrder = json.loads(orderStr)
        if not jsOrder.has_key('orderId'):
            logging.info('get orderid fail')
            result["error_code"] = "4001"
            result["error_desc"] = "get orderid fail"
            return result
        newOrderId =   jsOrder['orderId']
        print "orderid:%s"%(newOrderId)
        logging.info("orderid:%s"%(newOrderId))
        if(orderStr.find('返回未完成订单'.decode('utf-8'))>0):
            time.sleep(3)
            return queryOrderInfo(wbs,loginInfo,bookInfo,newOrderId)
        else:
           return queryOrderConfirmSingle(wbs,loginInfo,newOrderId,bookInfo)
    except Exception,e:
        logging.exception(e)
        logging.info("analyze query order exception")
        result["error_code"] = "4001"
        result["error_desc"] = "query orderid fail"
        return result


    except Exception,e:
        logging.exception(e)
        logging.info("analyze confirmsinglesucc to json exception")
        result["error_code"] = "4001"
        result["error_desc"] = "confirmsinglesucc fail"
        return result
    #printOrderInfo(wbs, loginInfo)
    #time.sleep(3)
    #printOrderInfo(wbs, loginInfo)
def queryOrderConfirmSingle(wbs,loginInfo,newOrderId,bookInfo):
    result = {}
    confirmRet = confirmSingleSuc(wbs,loginInfo,newOrderId)
    print "confirmSingleSuc:%s"%(confirmRet)
    logging.info("confirmSingleSuc:%s"%(confirmRet))
    jsConfirm = json.loads(confirmRet)
    if not jsConfirm.has_key('ticketResult'):
        result["error_code"] = "4001"
        result["error_desc"] = "ticketResult is empty"
        return result
    if jsConfirm['succ_flag'] == '0':
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
        result['error_desc'] = "fail"
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
#   printOrderInfo(wbs, loginInfo)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  