package com.kuyou.train.common.util;

import org.apache.commons.lang3.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * IpUtil
 *
 * @author taokai3
 * @date 2018/11/14
 */
public class IpUtil {

    public static String getPublicIip() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress()
                            //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && ip.getHostAddress().indexOf(":") == -1) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getInnetIp() {
        try {
            Enumeration<NetworkInterface> netInterfaces;
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        String localip = ip.getHostAddress();
                        return localip;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.err.println(IpUtil.getPublicIip());
        System.err.println(IpUtil.getInnetIp());
        System.err.println(IpUtil.getHostAddress());
    }

    public static String getHostAddress() {
        Enumeration<NetworkInterface> allNetInterfaces;
        String ip = StringUtils.EMPTY;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    inetAddress = addresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }
}
