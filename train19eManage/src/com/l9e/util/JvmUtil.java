package com.l9e.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * @author meizs
 * @create 2018-03-15 16:17
 **/
public class JvmUtil {
    public static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    /**
     * 初始堆大小  Mb
     *
     * @return
     */
    public static long getINTHEAP() {
        MemoryUsage usage = memoryMXBean.getHeapMemoryUsage();
        return usage.getInit() / 1024 / 1024;
    }

    /**
     * 堆大小最大可用 Mb
     *
     * @return
     */
    public static long getMAXHEAP() {
        MemoryUsage usage = memoryMXBean.getHeapMemoryUsage();
        return usage.getMax() / 1024 / 1024;
    }

    /**
     * 堆大小已使用  Mb
     *
     * @return
     */
    public static long getUSEDHEAP() {
        MemoryUsage usage = memoryMXBean.getHeapMemoryUsage();
        return usage.getUsed() / 1024 / 1024;
    }

    /**
     * Java 虚拟机中的内存总量 MB
     *
     * @return
     */
    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory() / 1024 / 1024;
    }

    /**
     * Java 虚拟机中的空闲内存量 MB
     *
     * @return
     */
    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory() / 1024 / 1024;
    }

    /**
     * Java 虚拟机中 最大可用内存量 MB
     *
     * @return
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory() / 1024 / 1024;
    }

    /**
     * 获取实际jvm内存占用比率
     *
     * @return
     */
    public static float getUseRateMemoryMXBean() {
        float a = getUSEDHEAP() * 1f / getMAXHEAP() * 1f;
        float b = (float) (Math.round(a * 1000)) / 1000;
        return b;
    }


    public static void main(String[] args) {

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage usage = memoryMXBean.getHeapMemoryUsage();
        System.out.println("INT HEAP:" + usage.getInit() / 1024 / 1024 + "Mb");
        System.out.println("MAX HEAP:" + usage.getMax() / 1024 / 1024 + "Mb");
        System.out.println("USED HEAP:" + usage.getUsed() / 1024 / 1024 + "Mb");


        System.out.println("\nFull Information:");
        System.out.println("Heap Memory Usage:" + memoryMXBean.getHeapMemoryUsage());
        System.out.println("Non-Heap Memory Usage:" + memoryMXBean.getNonHeapMemoryUsage());


        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        System.out.println("=====================java options==================");
        System.out.println(inputArguments);


        System.out.println("=====================通过java来获取相关系统状态====================");
        long i = Runtime.getRuntime().totalMemory() / 1024 / 1024;//Java 虚拟机中的内存总量，以字节为单位
        System.out.println("总的内存量为:" + i + "Mb");
        long j = Runtime.getRuntime().freeMemory() / 1024 / 1024;//Java 虚拟机中的空闲内存量
        System.out.println("空闲内存量:" + j + "Mb");
        long k = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        System.out.println("最大可用内存量:" + k + "Mb");


        System.out.println(getUseRateMemoryMXBean());
        System.out.println(getUseRateMemoryMXBean());

    }
}
