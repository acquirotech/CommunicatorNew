package com.acq.web.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.acq.Acq_Status_Definations;
import com.acq.Acq_Utility_Functions;
import com.acq.users.entity.Acq_KeyExchange_Entity;
import com.acq.users.entity.Acq_Login_Entity;
import com.acq.users.entity.Acq_TerminalInfo_Entity;
import com.acq.users.entity.Acq_Merchant_Entity;
import com.acq.users.entity.Acq_Store_Entity;
import com.acq.web.controller.model.Acq_TposLogin_Model;
import com.acq.web.dao.Acq_Dao;
import com.acq.web.dao.LoginDaoInf;
import com.acq.web.dto.impl.DbDto2;

@Repository
public class Acq_Login_Dao extends Acq_Dao implements LoginDaoInf {
	final static Logger logger = Logger.getLogger(Acq_Login_Dao.class);

	@Autowired
	MessageSource resource;

	@Value("#{acqConfig['loggers.location']}")
	private String loginLocation;


	
	
	@Override
	public DbDto2<Object> loginTposV1(Acq_TposLogin_Model model) {
		logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Begin");
		DbDto2<Object> daoResponse = new DbDto2<Object>();
		Map<String,String> resultMap = new HashMap<String,String>();
		Session daoSession = null;
		try {
			daoSession = createNewSession();
			logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Hibernate Session Created");
			
			Acq_Login_Entity terminalUserInfo = (Acq_Login_Entity) daoSession.createCriteria(Acq_Login_Entity.class)
					.add(Restrictions.eq("username", model.getLoginId()))
					.add(Restrictions.eq("password",Acq_Utility_Functions.getSHA256(model.getPassword())))
					.uniqueResult();
			
			if (terminalUserInfo == null) {
				logger.info("tpos/login/v1"+"::"+"DAO"+"::"+ "Terminal User Not Found");
				daoResponse.setStatusCode(Acq_Status_Definations.InvalidCrediential.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.InvalidCrediential.getDescription());
				daoResponse.setBody(resultMap);
			} 
			else 
			{
				logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Terminal User Found");
				
				String bankTid=null;
				String serialNo=null;
				String merchantBankMID = "";	
				resultMap.put("sessionId", terminalUserInfo.getUserId().toString());
				resultMap.put("firstLogin",terminalUserInfo.getStatus());
				resultMap.put("userStatus",terminalUserInfo.getUserStatus());
					
						try // Fetching Bank TID
						{
							bankTid = (String)daoSession.createCriteria(Acq_TerminalInfo_Entity.class)
							      .setProjection(Projections.property("bankTid"))
							      .add(Restrictions.eq("userId", terminalUserInfo.getUserId())).uniqueResult();
							serialNo = (String)daoSession.createCriteria(Acq_TerminalInfo_Entity.class)
								      .setProjection(Projections.property("bankTid"))
								      .add(Restrictions.eq("userId", terminalUserInfo.getUserId())).uniqueResult();
							if(bankTid == null || bankTid+""== "")
							{
								logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Bank Tid Not Found");
								daoResponse.setStatusCode(Acq_Status_Definations.TidNotFound.getId());
								daoResponse.setStatusMessage(Acq_Status_Definations.TidNotFound.getDescription());
								daoResponse.setBody(resultMap);
								return daoResponse;
							}
							else
							{
								resultMap.put("banktid",bankTid);
								logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Successfully Fetched Bank Tid");
							}
							
						}
						catch (Exception e) 
						{
							logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Error In Fetching Bank Tid");
							e.printStackTrace();
							daoResponse.setStatusCode(Acq_Status_Definations.TidNotFound.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.TidNotFound.getDescription());
							daoResponse.setBody(resultMap);
							return daoResponse;
						}
						
						try // Fetching Bank MID
						{
							Long merchantid = (Long)daoSession.createCriteria(Acq_Store_Entity.class)
								      .setProjection(Projections.property("merchantId"))
								      .add(Restrictions.eq("id", Long.valueOf(terminalUserInfo.getOrgId()))).uniqueResult();
							merchantBankMID = (String)daoSession.createCriteria(Acq_Merchant_Entity.class)
								      .setProjection(Projections.property("merchantTID"))
								      .add(Restrictions.eq("merchantId",""+ merchantid)).uniqueResult();
							
							if(merchantBankMID == null || merchantBankMID.isEmpty())
							{
								logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Bank Mid Not Found");
								daoResponse.setStatusCode(Acq_Status_Definations.MidNotFound.getId());
								daoResponse.setStatusMessage(Acq_Status_Definations.MidNotFound.getDescription());
								daoResponse.setBody(resultMap);
								return daoResponse;
								
							}
							else
							{
								resultMap.put("merchantBankMID",merchantBankMID);
								logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Successfully Fetched Bank Mid");
							}
						}
						catch (Exception e) {
							logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Error In Fetching Bank Mid");
							e.printStackTrace();
							daoResponse.setStatusCode(Acq_Status_Definations.MidNotFound.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.MidNotFound.getDescription());
							daoResponse.setBody(resultMap);
							return daoResponse;
						}						
						try 
						{
							Acq_KeyExchange_Entity keyEnt = (Acq_KeyExchange_Entity)daoSession.createCriteria(Acq_KeyExchange_Entity.class).add(Restrictions.eq("tid",serialNo)).add(Restrictions.eq("userId",terminalUserInfo.getUserId())).uniqueResult();
							resultMap.put("keyExchange", keyEnt.getStatus());
						}catch(Exception e){
							logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Error in Key Exchange Entity");
						}
						
					daoResponse.setBody(resultMap);
					daoResponse.setStatusCode(Acq_Status_Definations.Authenticated.getId());
					daoResponse.setStatusMessage( Acq_Status_Definations.Authenticated.getDescription());
					logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"User Details Fetched Successfully");
				} 
		} 
		catch (Exception e) 
		{
			logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Unexpected Server Error");
			e.printStackTrace();
			daoResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setBody(resultMap);
			return daoResponse;
		} 
		finally {
			if (daoSession.isOpen() == true || daoSession.isConnected() == true) {
				daoSession.close();
			}
		}	
		logger.info("tpos/login/v1"+"::"+"DAO"+"::"+"Returning Successfully");
		return daoResponse;
	}
		
}
