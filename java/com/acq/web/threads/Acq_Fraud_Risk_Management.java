package com.acq.web.threads;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.acq.users.entity.Acq_Terminal_Info;
import com.acq.users.entity.Acq_CardTransaction_Entity;
import com.acq.users.entity.Acq_RiskRules_Entity;

@Repository
public class Acq_Fraud_Risk_Management implements Runnable {
	 private int txnId;
	 private String description;
	 private Session session;	
	 private String carPanNo;
	 private String amount;
	 private String terminalId;
	 private String otpDateTime;
	 private String userId;
	 private String status;
	 final static Logger logger = Logger.getLogger(Acq_Fraud_Risk_Management.class);
	 
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getOtpDateTime() {
		return otpDateTime;
	}

	public void setOtpDateTime(String otpDateTime) {
		this.otpDateTime = otpDateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public String getCarPanNo() {
		return carPanNo;
	}

	public void setCarPanNo(String carPanNo) {
		this.carPanNo = carPanNo;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTxnId() {
		return txnId;
	}

	public void setTxnId(int txnId) {
		this.txnId = txnId;
	}

	public Acq_Fraud_Risk_Management(){}
	public void run(){
		try{
			if(Integer.valueOf(status)==505){
				Calendar calendar2 = Calendar.getInstance();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now = sdf1.parse(otpDateTime);
				calendar2.setTime(now);
				calendar2.add(Calendar.MINUTE, -3);
				String newTime = sdf1.format(calendar2.getTime());
				Date before3Min = sdf1.parse(newTime);
				Timestamp befor3MinTimestamp= new Timestamp(before3Min.getTime());
				Timestamp currentTimestamp= new Timestamp(now.getTime());
				HashMap<String,String> deviceMap = null;
				
				List<Acq_CardTransaction_Entity> mobiEntity = (List<Acq_CardTransaction_Entity>)session.createCriteria(Acq_CardTransaction_Entity.class).add(Restrictions.eq("carPanNo",carPanNo)).add(Restrictions.eq("amount",amount)).add(Restrictions.eq("status",505)).add(Restrictions.between("otpDateTime", befor3MinTimestamp, currentTimestamp)).list();
				ProjectionList proList = Projections.projectionList();
				proList.add(Projections.property("tid"));
				proList.add(Projections.property("avgTxnAmount"));
				Criteria deviceCriteria = (Criteria)session.createCriteria(Acq_Terminal_Info.class).setProjection(proList).add(Restrictions.eq("userId",userId));
				List ezDevice = deviceCriteria.list();
				deviceMap= new HashMap<String,String>();
				for(Object obj:ezDevice){
					Object[] ob=(Object[])obj;
					deviceMap.put("tid",""+ob[0]); 
					deviceMap.put("avgTxnAmount",""+ob[1]); 
				}	
				String avgTxnAmt =  (String) deviceMap.get("avgTxnAmount");
				Acq_RiskRules_Entity riskEnt = new Acq_RiskRules_Entity();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String d1 = sdf.format(now);
				Timestamp fromDate = Timestamp.valueOf(d1+" 23:00:00.000");
				Timestamp toDate = Timestamp.valueOf(d1+" 08:00:00.000");		   
				if((currentTimestamp.before(toDate)|| currentTimestamp.after(fromDate))){ 
					riskEnt.setTxnId(txnId);
					riskEnt.setDescription("23:00 PM to 08:00 AM");
					riskEnt.setDateTime(otpDateTime);
					riskEnt.setStatus("1");
					Transaction tx = session.beginTransaction();
					session.save(riskEnt);
					tx.commit();
				}else if(mobiEntity.size()>1&&terminalId.equals(deviceMap.get("tid"))){
					riskEnt.setTxnId(txnId);
					riskEnt.setDescription("With in 3min same card,amount and terminal id");
					riskEnt.setDateTime(otpDateTime);
					riskEnt.setStatus("1");
					Transaction tx = session.beginTransaction();
					session.save(riskEnt);
					tx.commit();
				}else if(Double.valueOf(amount)>Double.valueOf(avgTxnAmt)){
					riskEnt.setTxnId(txnId);
					riskEnt.setDescription("greater than average"); 
					riskEnt.setDateTime(otpDateTime);
					riskEnt.setStatus("1");
					Transaction tx = session.beginTransaction();
					session.save(riskEnt);
					tx.commit();
				}
			 }else{
			 }
		  }catch(Exception e){
			  
		  }finally{
			  session.close();
		  }  
	}
	

}
