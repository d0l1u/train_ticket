#!/bin/sh
APP_HOME=/app/ctripdatainterface
LANG=zh_CN.UTF-8
cd $APP_HOME
CLASSPATH=.:$CLASSPATH
for jar in `ls $APP_HOME/lib/*.jar`
do
    CLASSPATH=$CLASSPATH:$jar
done
CLASSPATH=$CLASSPATH:$APP_HOME/bin/

echo $CLASSPATH
/usr/local/java/jdk1.8.0_91/bin/java -Dnetworkaddress.cache.ttl="3600" -classpath $CLASSPATH com.l9e.Main $1 1>/dev/null 2>error$1.txt &
echo -e " "$! >pid$1.txt
