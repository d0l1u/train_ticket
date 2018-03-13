@echo off

set CATALINA_HOME=C:\data\train_app

cd %CATALINA_HOME%
start python-cnpc.bat

ping -n 6 127.1 >nul
tskill python
ping -n 3 127.1 >nul

cd %CATALINA_HOME%
start python-cnpc.bat
exit
