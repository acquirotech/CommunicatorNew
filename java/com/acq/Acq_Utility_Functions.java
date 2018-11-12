package com.acq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Random;

import sun.misc.BASE64Decoder;

public class Acq_Utility_Functions {
	
	public static String RandomNumberGenerator() {
	    String chars = "0123456789";

        final int PW_LENGTH = 15;
        Random rnd = new SecureRandom();
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < PW_LENGTH; i++)
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        return pass.toString();
    
}
	
	public static String getSHA256(String param){
		MessageDigest md;
		StringBuffer sbstr = new StringBuffer();
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(param.getBytes());	 
		    byte byteData[] = md.digest();	 
		    for (int i = 0; i < byteData.length; i++) {
		        sbstr.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		    }	
		    return sbstr.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}      
	}
	
	public static boolean Convert64AndSaveImg(String base64Image,String location)
	{
		BASE64Decoder decoder= new BASE64Decoder();
		FileOutputStream out=null ;
		try
		{
		byte[] image = decoder.decodeBuffer(base64Image);
		File imageFile = new File(location);	
		if(imageFile.exists())
		imageFile.delete();
		imageFile.createNewFile();
		out= new FileOutputStream(imageFile);
		out.write(image);
		out.close();
		return true;
		}
		catch(IOException ioException)
		{
			return false;			
		}
		
	}
	
	public static String otpGenerator(int otpLength) {
        String chars = "123456789";
        Random rnd = new SecureRandom();
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < otpLength; i++)
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        return pass.toString();
    }
	
	
	public static String generateRRN(String Stan, String tDate)
	{
		int hourOfDay = Integer.valueOf(tDate.substring(4, 6));
		String strHourOfDay="";
		
		if (hourOfDay < 10)	strHourOfDay = "0" + hourOfDay;
		else strHourOfDay = "" + hourOfDay;

		Calendar calObj = Calendar.getInstance();

		String currentYear = Integer.toString(calObj.get(Calendar.YEAR));
		String lastCharYear = currentYear.substring(currentYear.length()-1);
		int dayOfYear = calObj.get(Calendar.DAY_OF_YEAR);
		String strDayOfYear;
		
		if (dayOfYear < 10) {
			strDayOfYear = "00" + dayOfYear;
		} else if (dayOfYear > 10 && dayOfYear < 100) {
			strDayOfYear = "0" + dayOfYear;
		} else {
			strDayOfYear =  Integer.toString(dayOfYear);
		}	
		return lastCharYear + strDayOfYear + hourOfDay + Stan;
	}
	
	
	
}
