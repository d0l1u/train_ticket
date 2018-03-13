#-*-coding:utf8-*-
import requests

for i in range(100):
    print requests.session().get(url="http://101.200.80.146:8000/async")
    
