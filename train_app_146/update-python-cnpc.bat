@echo off

set RSYNC_HOME=C:\Program Files\cwRsync\bin
set HOST_VERSION=C:\python-cnpc-version.txt
set SERVER_VERSION=C:\data\train_app\python-cnpc-version.txt
set TOMCAT_PATH=C:\data\train_app\python-order-restart.bat
set LOG_DIR=C:\log\python-log
set LOG_DAY=35

cd /d %RSYNC_HOME%

rsync -av --delete 139.129.246.143::train_app_129 /cygdrive/C/data/train_app/

ping -n 2 127.1 >nul
if not exist %HOST_VERSION% echo.>%HOST_VERSION%
echo.
echo ******* python预定 train_app_129 *******
echo 对比python预定版本号...
echo n|comp %HOST_VERSION% %SERVER_VERSION% >nul 2>&1
if errorlevel 1 (
	start %TOMCAT_PATH%
	ping -n 2 127.1 >nul
	echo 更新本地改签版本号...
	copy /y %SERVER_VERSION% %HOST_VERSION%
) else (
	echo 改签版本号一致,不作处理...
	ping -n 2 127.1 >nul
)

echo ******* 删除日志文件 *******
forfiles /p %LOG_DIR% /S /M *.* /D -%LOG_DAY% /C "cmd /c echo 正在删除@relpath 文件… & echo. & del @file"

echo.
echo ======= SUCCESS !!!! =======
ping -n 5 127.1 >nul
