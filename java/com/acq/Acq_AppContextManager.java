package com.acq;

import java.security.MessageDigest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Acq_AppContextManager {
	static ApplicationContext appContext;
	public static ApplicationContext getAppContext(){
		appContext = new ClassPathXmlApplicationContext("spring-database.xml"); 
		//appContext = new ClassPathXmlApplicationContext("file:WEB-INF/spring-database.xml");
		return appContext;
	}
	
	
	public static String getGenerateSHA(String password){
	try{
		StringBuffer sb;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
	    md.update(password.getBytes());
	    byte byteData[] = md.digest();
	    sb = new StringBuffer();
	    for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	    }return sb.toString();
		}catch(Exception e){
			return null;
		}	
	}
}
