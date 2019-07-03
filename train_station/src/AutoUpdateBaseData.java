import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用于定时更新基础数据，[站名，途径站，票价数据],也可以手动执行指定的功能数据更新
 * @author meizs
 * @create 2018-05-29 10:12
 *
 **/
public class AutoUpdateBaseData {

    private static final long delayTime = 1000 * 60 * 60 * 24 * 15;//15天执行一次
    private static Logger logger = Logger.getLogger(AutoUpdateBaseData.class);

    public static void main(String[] args) {

        logger.info("更新票价数据");
        new Main().updateCtripPrice();  //先前执行这个方法，[定期更新站名后，查站名自动更新票价]

        upateDataFrom12306();//更新途经站数据from 12306
        upateDataFromCtrip();//更新票价数据 from ctrip

    }

    public static void upateDataFromCtrip() {

        final Timer timer = new Timer();
        //timer.scheduleAtFixedRate(new TimerTask() {//超时累计任务
        timer.schedule(new TimerTask() {//超时丢弃任务

            @Override
            public void run() {
                SimpleDateFormat sf = new SimpleDateFormat(
                        "yyyy MM dd HH:mm:ss");

                logger.info("1.upateDataFromCtrip当前时间："
                        + sf.format(System.currentTimeMillis()) + "计划时间："
                        + sf.format(scheduledExecutionTime()));

                logger.info("1.更新t_ctrip_zm1站名数据");
                new CtripStationManager().updateCtripStation();

                logger.info("携程站名数据更新完成");


                logger.info("2.upateDataFromCtrip当前时间："
                        + sf.format(System.currentTimeMillis()) + "计划时间："
                        + sf.format(scheduledExecutionTime()));
            }
        }, 10000, delayTime);//启动之后，10s开始执行任务，之后每隔15天执行一次


    }


    public static void upateDataFrom12306() {

        final Timer timer = new Timer();
        //timer.scheduleAtFixedRate(new TimerTask() {//超时累计任务
        timer.schedule(new TimerTask() {//超时丢弃任务

            @Override
            public void run() {
                SimpleDateFormat sf = new SimpleDateFormat(
                        "yyyy MM dd HH:mm:ss");

                logger.info("1.upateDataFrom12306当前时间："
                        + sf.format(System.currentTimeMillis()) + "计划时间："
                        + sf.format(scheduledExecutionTime()));

                logger.info("1.更新站名数据");
                new ZmManager().updateStation();
                logger.info("2.更新车次数据");
                new LcccManager().updateTrain();
                logger.info("3.更新途经站数据");
                new MidwayManager().updateMidwayOfLccc();

                logger.info("途经站数据更新完成");


                logger.info("2.upateDataFrom12306当前时间："
                        + sf.format(System.currentTimeMillis()) + "计划时间："
                        + sf.format(scheduledExecutionTime()));
            }
        }, 10000, delayTime);//启动之后，10s开始执行任务，之后每隔15天执行一次
    }


}
