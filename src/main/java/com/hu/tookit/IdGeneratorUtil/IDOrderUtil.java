package com.hu.tookit.IdGeneratorUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.UUID;

public class IDOrderUtil {

	public static void main(String[] args) {
//		getMACAddress(InetAddress.getLocalHost());
	}

	public static String getUUIDOrder() {
		return UUID.randomUUID().toString();
	}

	public static long getSnowFlakeOrder() {
		int workId = 0;//获取当前的机器ID散列值
		return new SnowFlakeIdWorker(workId).nextId();
	}

	// 获取MAC地址的方法
	private static String getMACAddress(InetAddress ia) throws Exception {
		// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString().toUpperCase();
	}

	private static void getBatchMACAddress() {
		try {
		 Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
	      StringBuilder builder = new StringBuilder();
	      while (el.hasMoreElements()) {
		      byte[] mac = el.nextElement().getHardwareAddress();
		      if (mac == null){
		         continue;
		      }
		      if(builder.length() > 0){
		    	  builder.append(",");
		      }
		      for (byte b : mac) {
		    	 //convert to hex string.
		    	 String hex = Integer.toHexString(0xff & b).toUpperCase();
		    	 if(hex.length() == 1){
		    		 hex  = "0" + hex;
		    	 }
		         builder.append(hex);
		         builder.append("-");
		      }
		      builder.deleteCharAt(builder.length() - 1);
	     }
	     if(builder.length() == 0){
	    	 System.out.println("Sorry, can't find your MAC Address.");
	     }else{
	    	 System.out.println("Your MAC Address is " + builder.toString());
	     }
	 }catch (Exception exception) {
	     exception.printStackTrace();
	 }
	}
}
