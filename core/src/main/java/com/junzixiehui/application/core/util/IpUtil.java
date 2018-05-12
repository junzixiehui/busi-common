package com.junzixiehui.application.core.util;

import com.dangdang.ddframe.job.util.env.IpUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * <p>Description: </p>
 *
 * @author: by qulibin
 * @date: 2018/3/8  14:12
 * @version: 1.0
 */
public class IpUtil {

    public static String getRemoteIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                }
                ipAddress = inet.getHostAddress();
            }

        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }


    /**
     * 取得本机的IP.
     *
     * @return 如果有多个IP地址返回外网的IP，多个外网IP返回第一个IP（在多网管等特殊情况下）
     * @throws SocketException
     */
    public static String getRealIp() throws SocketException {
        String localIp = null; // 本地IP，如果没有配置外网IP则返回它
        String netIp = null; // 外网IP

        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean isFind = false; // 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !isFind) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) { // 外网IP
                    netIp = ip.getHostAddress();
                    isFind = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) { // 内网IP
                    localIp = ip.getHostAddress();
                }
            }
        }
        if (netIp != null && !"".equals(netIp)) {
            return netIp;
        } else {
            return localIp;
        }
    }

    public static String getHostName() {

        try {
           return IpUtils.getHostName();
        } catch (Exception e){
            return "";
        }
    }


    public static String getRealIpNoException() {

        try {
           return getRealIp();
        } catch (Exception e){
            return "";
        }
    }



}
