package com.cqz.utils;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.InetAddress;  
import java.net.NetworkInterface;  
import java.net.SocketException;  
import java.net.UnknownHostException;  

import org.apache.log4j.Logger;

import com.l9e.transaction.job.LZErrorSentJob;
  
public class GetMacAddress
{  
	
	
    public static void main(String[] args)  
    {  
        String address = "";  
//        String host = "*.*.*.*";  
          
          
        address = getMacAddress();  
        System.out.println("Physical Address is : " + address);  
          
//        address = test.getMacAddress(host);  
//        System.out.println("Physical Address is : " + address);  
    }  
      
    public static String getMacAddress()  
    {  
        String mac = "";    
        String line = "";  
          
        String os = System.getProperty("os.name");    
          
        if (os != null && os.startsWith("Windows"))   
        {     
            try   
            {     
                String command = "cmd.exe /c ipconfig /all";     
                Process p = Runtime.getRuntime().exec(command);     
                  
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));     
                  
                while((line = br.readLine()) != null)  
                {     
//                	System.out.println(line);
                    if (line.indexOf("Physical Address") > 0||line.indexOf("物理地址") > 0)  
                    {     
                        int index = line.indexOf(":") + 2;  
                          
                        mac = line.substring(index);  
                          
                        break;     
                    }     
                }     
                  
                br.close();     
                  
            } catch (IOException e) {}     
        }     
          
        return mac;     
    }  
      
    public static String getMacAddress(String host)  
    {  
        String mac = "";  
        StringBuffer sb = new StringBuffer();  
          
        try   
        {  
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName(host));  
              
            byte[] macs = ni.getHardwareAddress();  
              
            for(int i=0; i<macs.length; i++)  
            {  
                mac = Integer.toHexString(macs[i] & 0xFF);   
                  
                 if (mac.length() == 1)   
                 {   
                     mac = '0' + mac;   
                 }   
  
                sb.append(mac + "-");  
            }  
                          
        } catch (SocketException e) {  
            e.printStackTrace();  
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        }  
          
        mac = sb.toString();  
        mac = mac.substring(0, mac.length()-1);  
          
        return mac;  
    }  
    
    
    
    
    /**  
     * 获取unix网卡的mac地址. 非windows的系统默认调用本方法获取.  
     * 如果有特殊系统请继续扩充新的取mac地址方法.  
     * @author liuyi02
     * @return mac地址  
     */  
    public static String getUnixMACAddress() {   
        String mac = null;   
        BufferedReader bufferedReader = null;   
        Process process = null;   
        try {   
            // linux下的命令，一般取eth0作为本地主网卡   
        	
        	/**火车票系统  eth1作为本地主网卡     坑爹呢!*/
            process = Runtime.getRuntime().exec("ifconfig eth1");   
            // 显示信息中包含有mac地址信息   
            bufferedReader = new BufferedReader(new InputStreamReader(   
                    process.getInputStream()));   
            String line = null;   
            int index = -1;   
            while ((line = bufferedReader.readLine()) != null) {   
                // 寻找标示字符串[hwaddr]   
                index = line.toLowerCase().indexOf("hwaddr");   
                if (index >= 0) {// 找到了   
                    // 取出mac地址并去除2边空格   
                    mac = line.substring(index + "hwaddr".length() + 1).trim();   
                    break;   
                }   
            }   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {   
            try {   
                if (bufferedReader != null) {   
                    bufferedReader.close();   
                }   
            } catch (IOException e1) {   
                e1.printStackTrace();   
            }   
            bufferedReader = null;   
            process = null;   
        }   
        return mac;   
    }   

    
    /**
     * 获取 请求优优云的MAC地址
     * 此处不支持特殊字符
     * 
     * */
    public static String getUUYUnixMACAddress() {   
    	String mac=getMacAddress()  ;
    	if("".equals(mac)){
    		mac=getUnixMACAddress();
    	}
    	return mac.replaceAll("-", "").replace(":", "");
    }
}  