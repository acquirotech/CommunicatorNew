package com.acq.web.threads;

import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.acq.Acq_ThinkWalnut_Utilities;
import com.acq.users.entity.Acq_Tw_Recharge_Entity;

@Repository
public class Acq_TW_Txn_Status_Thread implements Runnable {
	private String refId;
	private Session dbSession;
	private String id;
	private String callFrom;

	public String getCallFrom() {
		return callFrom;
	}

	public void setCallFrom(String callFrom) {
		this.callFrom = callFrom;
	}

	final static Logger logger = Logger.getLogger(Acq_TW_Txn_Status_Thread.class);

	Acq_ThinkWalnut_Utilities  twRechargeApi = new Acq_ThinkWalnut_Utilities();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Session getSession() {
		return dbSession;
	}

	public void setSession(Session session) {
		this.dbSession = session;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public Acq_TW_Txn_Status_Thread(){}

	public void run(){
		try{
			logger.info(callFrom + "::" + "Acq_TW_Txn_Status_Thread" + "::" + "Begin");

			if(refId!=null || refId+"" != ""){
				String requestParams = "&refId="+id;
				Map<String, String> twResponseMap = twRechargeApi.tw_Port_Connector("http://api.twd.bz/wallet/api/checkStatus.php",requestParams, "Acq_TW_Txn_Status_Thread" );
				Acq_Tw_Recharge_Entity  txn2Update = (Acq_Tw_Recharge_Entity) dbSession.createCriteria(Acq_Tw_Recharge_Entity.class).add(Restrictions.eq("id",id)).uniqueResult();	
				
				if(twResponseMap.get("errCode").equals("0")){
					txn2Update.setStatus("Success");
				}else{
					txn2Update.setStatus(twResponseMap.get("msg"));
				}
				txn2Update.setMessage(twResponseMap.get("msg"));
				txn2Update.setTwStatusCode(twResponseMap.get("errCode"));
				txn2Update.setOptId(twResponseMap.get("optId"));
				dbSession.update(txn2Update);
				Transaction dbTransaction = dbSession.beginTransaction();
				dbSession.update(txn2Update);
				dbTransaction.commit();
				logger.info(callFrom + "::" + "Acq_TW_Txn_Status_Thread" + "::" + "End");
			}
			else{
				logger.info(callFrom + "::" + "Acq_TW_Txn_Status_Thread" + "::" + "Reference Id Not Found");
			}
		}catch(Exception e){
			logger.info(callFrom + "::" + "Acq_TW_Txn_Status_Thread" + "::" + "Thread Failed");
			e.printStackTrace();
		}finally{
			if (dbSession.isOpen() == true || dbSession.isConnected() == true) {
				dbSession.close();
			}
		} 


	}

}
