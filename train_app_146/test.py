#-*-coding:utf8-*-

import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
from tornado import httpclient

import time
import os
import requests
from tornado.options import define,options
define("port",default=8000,help="run on the given port",type=int)


class Async1Handler(tornado.web.RequestHandler):
    @tornado.web.asynchronous
    @tornado.gen.coroutine
    def get(self,*args,**kwargs):
        cur = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
        print("%s:begin!"%(cur))
        #response = (requests.session().get(url="http://www.github.com").content)
        #print(response)
        #cur = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
        #print("%s:response"%(cur))
        #self.write("finish")
        #self.finish()
        http = httpclient.AsyncHTTPClient()
        yield http.fetch("http://http://www.github.com", self.done,)

    def done(self, response):
        #print(response)
        cur = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
        #print("%s:response"%(cur))
        self.write("finish")
        self.finish()
    #@tornado.gen.coroutine
    def open(self):
        return (requests.session().get(url="http://www.baidu.com").content)
        #os.system("ping -c 5 127.0.0.1")

if __name__ == "__main__":
    tornado.options.parse_command_line()
    print("****listening 127.0.0.1:8000****")
    app = tornado.web.Application(
                handlers=[(r'/async',Async1Handler)],
                debug="True")
    http_server = tornado.httpserver.HTTPServer(app)
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.instance().start()
