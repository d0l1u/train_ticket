#coding=utf-8
import sys

# author:qiancheng
# time:2016-12-02
# 在http://www.sinopecsales.com/网站对中石油加油卡进行自动充值

import tornado.ioloop
import tornado.web
import urllib
import json
import systemcfg
import random
from logger import *
from tornado import gen
import ticketExit
import time

from concurrent.futures import ThreadPoolExecutor
from tornado.concurrent import run_on_executor

VERSION = '1.0.0.0'

MAX_WORKERS = 2
PROXY_PERCENT = 100

class MainHandler(tornado.web.RequestHandler):
    executor = ThreadPoolExecutor(max_workers=MAX_WORKERS)

    @run_on_executor
    def post(self, *args, **kwargs):
        values = urllib.unquote(self.request.body)
        bookInfo = dict([(kv.split('=')[0].strip(), kv.split('=')[1].strip()) \
                       for kv in values.split('&')])
        logging.info('[%s] request [%s] from %s',bookInfo['orderId'], self.request.body, self.request.remote_ip)
        #
        #
        # res_data = {}
        # res_data['status'] = '2'
        # res_data['error_code'] = '0'
        # res_data['error_desc'] = ''
        #res_data['face_value'] = req_params['username']
        #res_data['account_no'] = req_params['password']
        #param = self.request.body
        #print param
        #bookInfo = json.loads(param)
        t = time.time()
        res_data = {}
        res_data['status'] = '0'
        res_data['message'] = ''
        res_data['startMillis']=int(round(t * 1000))
        keyList = ['orderId','seat_type','user_name','password','train_date','purpose_codes','from_station','to_station','seat_type','passenger_names',\
                   'passenger_id_no','passenger_id_type','seat_type_codes','ticket_type_order_num','ticket_types','station_train_code','choose_seats',\
                   'province_name','province_code','student_no','system','enter_year','limit_beginName','limit_beginCode','limit_endName','limit_endCode']
        for i,val in enumerate(keyList):
            if(not bookInfo.has_key(val)):
               logging.info('%s param is needed!'% val)
               resu_str = urllib.urlencode(res_data)
               self.write(resu_str)
               return
        # bookInfo = {}
        # bookInfo["user_name"] = js.get('user_name')
        # bookInfo['password'] =  js.get('password')
        # bookInfo['train_date'] =  js.get('train_date')
        # bookInfo['purpose_codes'] = js.get('purpose_codes')
        # bookInfo['from_station'] = js.get('from_station')
        # bookInfo['to_station'] = js.get('to_station')
        # bookInfo['seat_type'] = systemcfg.seat_map[bookInfo['seat_type']]
        # bookInfo['passenger_names'] = urllib.unquote(bookInfo['passenger_names'])
        # # bookInfo['passenger_id_no'] = js.get('passenger_id_no')
        # bookInfo['passenger_id_type'] = systemcfg.card_map[ bookInfo['passenger_id_type']]
        # # bookInfo['choose_seats'] = js.get('choose_seats')
        # bookInfo['seatchoose_seats_type_codes'] = systemcfg.seat_map[bookInfo['seat_type_codes']]
        # # bookInfo['ticket_type_order_num'] = js.get('ticket_type_order_num')
        # bookInfo['ticket_types'] = systemcfg.ticket_map[bookInfo['ticket_types']]
        # #bookInfo['train_no'] = js.get('train_no')
        # bookInfo['station_train_code'] = js.get('station_train_code')
        logging.info(bookInfo)
        print bookInfo
        index = random.randint(0,100)
        if(index < PROXY_PERCENT and bookInfo.has_key('proxy') and  bookInfo['proxy'] <> None and bookInfo['proxy']<>''):
            logging.info('[%s] 使用代理：%s',bookInfo['orderId'],bookInfo['proxy'])
        else:
            bookInfo['proxy'] = ''
        resp = ticketExit.bookTickt(bookInfo)
        if(resp['error_code'] == '0000'):
            res_data['status'] = '0000'
            res_data['message'] = 'success'
            res_data['body'] = resp['data']
        else:
             res_data['status'] = resp['error_code']
             res_data['message'] =  resp['error_desc']
        t = time.time()
        res_data['endMillis']=int(round(t * 1000))
        resu_str = urllib.urlencode(res_data)
        logging.info('[%s] response [%s]',bookInfo['orderId'],resu_str)
        self.write(resu_str)
    def get(self):
        data = "your ip address:%s - " % self.request.remote_ip
        data += "Book ticket service version:%s" % VERSION
        self.write(data)
application = tornado.web.Application([
    (r"/bookticket", MainHandler)
])
if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding("utf-8")
    setup_logging()
    logging.info("Book ticket service start.version:%s",VERSION)
    application.listen(18884)
    #收单使用
    tornado.ioloop.IOLoop.instance().start()
