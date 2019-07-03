#!/bin/bash
cd `dirname $0`
dir=`pwd`
pid=`ps -ef | grep $dir | grep -v grep | awk '{print $2}'`

while [[ -n "$pid" ]];do
	kill -9  $pid
	echo -e  "\033[32m********* Server Stop**********\033[0m"
    break
done

sh startup.sh > /dev/null
if [[ $? -eq 0 ]];then
	echo -e  "\033[32m********* Server Start**********\033[0m"
else
	echo -e  "\033[32m*********Server Fail!!!**********\033[0m"
fi
sleep 2
pid2=`ps -ef | grep $dir | grep -v grep | awk '{print $2}'`
echo "$pid2"
