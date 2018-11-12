package com.acq;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Acq_TransTrac_IsoValidator {


	private static Pattern pattern;
	private static Matcher matcher;
	
	private static final String de02Pattern="^[0-9A-Z]{1,60}$";
	private static final String de04Pattern="^[0-9]{1,20}$";
	private static final String de14Pattern="^[0-9]{0,6}$";	
	private static final String de23Pattern="^[0-9]{0,2}$";
	private static final String de35Pattern="^[0-9A-Z\\=]{3,99}$";
	private static final String de39Pattern="^[0-9A-Z]{2}$";	
	private static final String de40Pattern="^[0-9]{3}$";
	private static final String de52Pattern="^[0-9A-Z]{2,50}$";
	private static final String de55Pattern="^[0-9A-Z]{0,999}$";
	private static final String de106Pattern="^[0-9\\=\\.]{0,50}$";
	private static final String ksnPattern="^[0-9A-Z]{1,50}$";
	
	public static boolean isDE02(String de02) {
		 pattern = Pattern.compile(de02Pattern);
	     matcher = pattern.matcher(de02);
	     return matcher.matches(); 
	}


	public static boolean isKsn(String ksn) {
		 pattern = Pattern.compile(ksnPattern);
	     matcher = pattern.matcher(ksn);
	     return matcher.matches(); 
	}

	public static boolean isDE11(String de11) {
		pattern = Pattern.compile(de04Pattern);
	     matcher = pattern.matcher(de11);
	     return matcher.matches(); 
	}public static boolean isDE14(String de14) {
		pattern = Pattern.compile(de14Pattern);
	     matcher = pattern.matcher(de14);
	     return matcher.matches(); 
	}
	public static boolean isDE04(String de04) {
		pattern = Pattern.compile(de04Pattern);
	     matcher = pattern.matcher(de04);
	     return matcher.matches(); 
	}public static boolean isDE23(String de23) {
		pattern = Pattern.compile(de23Pattern);
	     matcher = pattern.matcher(de23);
	     return matcher.matches(); 
	}public static boolean isDE35(String de35) {
		pattern = Pattern.compile(de35Pattern);
	     matcher = pattern.matcher(de35);
	     return matcher.matches(); 
	}public static boolean isDE40(String de40) {
		pattern = Pattern.compile(de40Pattern);
	     matcher = pattern.matcher(de40);
	     return matcher.matches(); 
	}public static boolean isDE52(String de52) {
		pattern = Pattern.compile(de52Pattern);
	     matcher = pattern.matcher(de52);
	     return matcher.matches(); 
	}public static boolean isDE55(String de55) {
		pattern = Pattern.compile(de55Pattern);
	     matcher = pattern.matcher(de55);
	     return matcher.matches(); 
	}public static boolean isDE106(String de106) {
		pattern = Pattern.compile(de106Pattern);
	     matcher = pattern.matcher(de106);
	     return matcher.matches(); 
	}

	public static boolean isDE39(String de39) {
		pattern = Pattern.compile(de39Pattern);
	     matcher = pattern.matcher(de39);
	     return matcher.matches(); 
	}
}
