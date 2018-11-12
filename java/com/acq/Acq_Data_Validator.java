package com.acq;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Acq_Data_Validator {
	
	private static Pattern pattern;
	private static Matcher matcher;
	
	private static final String NumberPattern="^[0-9]{1,20}$";		
	private static final String phonePattern="^[0-9]{10}$";		
	private static final String receiptNoPattern="^[0-9\\-]{1,30}$";
	

	
	public static boolean mobileSign(String mobileNo) {
		 if(mobileNo==""||mobileNo==null||mobileNo.equals("")){
		      return true;
		     }
		 pattern = Pattern.compile(phonePattern);
	     matcher = pattern.matcher(mobileNo);
	     return matcher.matches(); 
	}
	public static boolean isValidReceiptNo(String receiptNo) {  
		  if(receiptNo==""||receiptNo==null){
		   return false;
		  }
		  pattern = Pattern.compile(receiptNoPattern);
		  matcher = pattern.matcher(receiptNo);
		  return matcher.matches();
		  
		 }
	
	public static boolean isId(final String id) { 
		if(id==""||id==null){
			return false;
		}
		pattern = Pattern.compile(NumberPattern);
		matcher = pattern.matcher(id);
		return matcher.matches();

	}
	
	public static boolean isPhoneNo(final String phoneNo) { 
		if(phoneNo==""||phoneNo==null){
			return false;
		}
		pattern = Pattern.compile(phonePattern);
		matcher = pattern.matcher(phoneNo);
		return matcher.matches();

	}
	private static final String daysPattern="^(1)$|(7)$|(30)$|(90)$|(365)$";
	public static boolean isValidDays(String days) {
		if(days==""||days==null){
			return false;
		}
		pattern = Pattern.compile(daysPattern);
		matcher = pattern.matcher(days);
		return matcher.matches();
	}
	
	
	
	private static final String multiPhonePattern="^(\\d{10}(,\\d{10})*)?$"; 
	 public static boolean isValidMultiPhone(String phoneNo) {
	  if(phoneNo==""||phoneNo==null){
	   return false;
	  }
	  pattern = Pattern.compile(multiPhonePattern);
	  matcher = pattern.matcher(phoneNo);
	  return matcher.matches();
	 }
	 
	
	public static boolean isFrequency(String frequency) {
		 if(frequency==""||frequency==null||frequency.equals("")){
		      return false;
		 }else{
			 if(frequency.equals("today")||frequency.equals("week")||frequency.equals("month"))
				 return true;
			 else
				 return false;
		 }
	}
	public static boolean dateValidation(String fromDate, String toDate)
	{ if (fromDate.matches("^((20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$")&&toDate.matches("^((20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$")) {
	     
		  return true;
		  }
		  else 
		  return false;
	}

	public static boolean isDateValidation(String fromDate, String toDate)
	{
		if (fromDate.compareTo(toDate) <= 0) {
			return true;
		}
		else
			return false;
	}
	
	
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
 
	public Acq_Data_Validator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	
	
}
