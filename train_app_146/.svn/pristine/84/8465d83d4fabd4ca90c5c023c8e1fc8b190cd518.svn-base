#coding=utf-8
import json
import binascii
import urllib

# 仿照js的cryptToHex实现python版本
def cryptToHex(str,key=158):
    val = ""
    for i in range(len(str)):
        temp = hex(ord(str[i]) ^ key)
        temp = temp[2:]
        if len(temp) == 1:
            val = val + "0"
        val = val + temp
    return val.lower()

# 仿照js的stringify实现python版本
def stringify(str):
    return '[\"' + str + '\"]'
def mkdir(path):
    import os
    path = path.strip()
    path = path.rstrip("\\")
    path = path.rstrip("/")
    isExists = os.path.exists(path)

    if not isExists:
        os.makedirs(path)
        return True
    else:
        return False

if __name__ == "__main__":
    var = cryptToHex('13842337599461740672',158)
    print var
    str = stringify(var)
    print str
    data = {
        'str':str
    }
    str = urllib.urlencode(data)
    print binascii.a2b_hex('5B')
    print binascii.a2b_hex('22')
    print binascii.a2b_hex('22')
    print binascii.a2b_hex('5D')
