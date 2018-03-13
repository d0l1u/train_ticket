#!/usr/bin/python2.7
# -*- coding: utf-8 -*-  

import os
import shutil
import sys
import ctypes
from ctypes import *
import urllib2
import httplib

 
Objdll = ctypes.CDLL("NEWDLL") 

#shareBuffer = create_string_buffer('/0'*0x100000)
#shareRef = byref(shareBuffer)

def writeBin (filename,binCon):
    fp=open(filename,'wb')
    fp.write(binCon)
    fp.close()

def readBin (filename):
    fp=open(filename,'rb')
    data = fp.read()
    fp.close()
    return data

def encode (inputCode):    
    buf_size = 0x1000
    raw_memory = bytearray(buf_size)  
    ctypes_raw_type = (ctypes.c_char * buf_size)  
    ctypes_raw_memory = ctypes_raw_type.from_buffer(raw_memory)  
    encLen = Objdll.encode(byref(ctypes_raw_memory), buf_size,inputCode,len(inputCode))
    #encLen = Objdll.checkcode(byref(ctypes_raw_memory), buf_size,inputCode,len(inputCode))
    print "len:",encLen
#   print "ret len:",len(szPara.value)
    return raw_memory[:encLen]

def decode (decodeInput):    
    buf_size = 0x10000
#   raw_memory = bytearray(buf_size)
#   ctypes_raw_type = (ctypes.c_char * buf_size)
#   ctypes_raw_memory = ctypes_raw_type.from_buffer(raw_memory)
    szPara = create_string_buffer('/0'*buf_size) 
    decLen = Objdll.decode(byref(szPara), buf_size,decodeInput,len(decodeInput))
#   decLen = Objdll.decode(byref(ctypes_raw_memory), buf_size,decodeInput,len(decodeInput))
#   decodeStr = "".join(raw_memory)
#   return decodeStr
    print "inlen:%d decode len:%d "%(len(decodeInput),decLen)+szPara.value[:decLen]
    return szPara.value[:decLen]

def writeBin (filename,binCon):
    fp=open(filename,'wb')
    fp.write(binCon)
    fp.close()

def readBin (filename):
    fp=open(filename,'rb')
    data = fp.read()
    fp.close()
    return data

def main(argv):
  """
  A command line google safe browsing client. Usage:
    client.py <APIKey> [check <URLs>]
  """  
# signinput = 'Operation-Type=com.cars.otsmobile.login&Request-Data=W3siX3JlcXVlc3RCb2R5Ijp7ImJhc2VEVE8iOnsiY2hlY2tfY29kZSI6IjY2NzFiMTkwOTZkNmFmNzE5NGY3ZTJiZmE5NWIzZWUxIiwiZGV2aWNlX25vIjoiV2p5anNnS05uWU1EQU9LdmpYRXVUU1pkIiwibW9iaWxlX25vIjoiIiwib3NfdHlwZSI6ImEiLCJ0aW1lX3N0ciI6IjIwMTcxMjIyMTQyMTQ5IiwidXNlcl9uYW1lIjoiaGJzcHdhbmciLCJ2ZXJzaW9uX25vIjoiMy4wLjAuMTIxMjE0MzAifSwicGFzc3dvcmQiOiI3MGJmYTc1NTY4Y2U0MGM4YTI3NmI4NWFkY2E1NDQ1NyIsInVzZXJfbmFtZSI6Imhic3B3YW5nIn19XQ==&Ts=M1yu55A'
# print getSign(signinput)
# encodeinput='[{"_requestBody":{"baseDTO":{"check_code":"7b3ef46a9b0709d3819af4e7c795537c","device_no":"WjyjsgKNnYMDAOKvjXEuTSZd","mobile_no":"","os_type":"a","time_str":"20171222144030","user_name":"hbspwang","version_no":"3.0.0.12121430"},"password":"70bfa75568ce40c8a276b85adca54457","user_name":"hbspwang"}}]'
# encodeinput='abcd'
# encodedata = encode(encodeinput)
# writeBin('encode.bin',encodedata)
  readData = readBin('before')
  decodeData = encode(readData)
  writeBin('after-test3',decodeData)
  




if __name__ == '__main__':
  main(sys.argv)

