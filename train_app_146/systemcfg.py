#coding=utf-8
import json
import sys
import urllib

jfile = file("./ini/systemcfg.json")
jdata = json.load(jfile)
jfile.close()


#error infomation
seat_map = jdata["seat_map"]
card_map = jdata["card_map"]
ticket_map = jdata["ticket_map"]
