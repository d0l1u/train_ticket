# coding=utf-8
from flask import Flask
app = Flask(__name__)
#import ticketExit
import requests
from flask import request
import json
import paraCryptexit
import base64

@app.route('/encode', methods=['post'])
def encode():
    data = str(request.json.get('data'))
    print("encode:%s"%(data))
    return paraCryptexit.encode(data)

@app.route('/decode', methods=['post'])
def decode():
    data = base64.b64decode(request.json.get('data'))
    resp = paraCryptexit.decode(data)
    print("decode:%s"%(resp))
    return resp



if __name__ == '__main__':
    #app.run()
	app.run("0.0.0.0", 7777, debug=True)