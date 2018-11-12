package com.acq;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.acq.web.dao.Acq_Dao;

public class test extends Acq_Dao {
	@Autowired
	private SessionFactory sessionFactory;

	Session session;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session createNewSession() {
		session = sessionFactory.openSession();
		return session;
	}
	Session s = createNewSession();
	
	public static void main(String[] args) {
		
//		test obj = new test();
//		obj.logISOData(null);
		
		 
		String a = " ";//"{\"a\":345}";
		
		if(a==" ")
			System.out.println("Aaaaaaaaaaaaa");
		else
			System.out.println("bbbbbbbbbbb");
		
		//test A = new test();
		//Acq_Sms_Sender.sendRouteSMS("ewfwefewf", "8527195300");
		String queryStr = "select m.id,m.txnType from acq_transaction_summary m, acq_transaction_card_summary c where m.id=c.transactionId and m.cardPanNo='"+"123"+"' and m.txnType in('VOID','CARD','CASHATPOS','CVOID','CASHBACK','CBVOID') and m.status=505 and m.payoutStatus=700 and m.otpdatetime between '2017-06-07 00:00:00' and '2017-06-07 23:23:59' and m.amount not in (select amount from acq_transaction_summary where amount like '-%') and m.amount not in (select REPLACE(amount,'-','') from acq_transaction_summary where amount like '-%') order by m.id desc";
		//SQLQuery query = (SQLQuery)s.createSQLQuery(queryStr);
		//List txnList = query.list();
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
		Date txnDate = new Date();
		String transmissionDatetime = dateFormatGmt.format(txnDate);
		System.out.println("wefewfwefwef::::::::::::::::::::"+queryStr);
		
	}
	public static String generateRRN(String Stan, String tDate)
	{
		int hourOfDay = Integer.valueOf(tDate.substring(4, 6));
		String strHourOfDay="";
		System.out.println("hourOfDay:"+hourOfDay);
		if (hourOfDay < 10)	strHourOfDay = "0" + hourOfDay;
		else strHourOfDay = "" + hourOfDay;
		System.out.println("hourOfDay2:"+hourOfDay);
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
		System.out.println("dayOfYear:"+dayOfYear);
		return lastCharYear + strDayOfYear + hourOfDay + Stan;
	}
	

}
