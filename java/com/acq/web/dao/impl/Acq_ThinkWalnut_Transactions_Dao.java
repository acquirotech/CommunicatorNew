package com.acq.web.dao.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.acq.Acq_Status_Definations;
import com.acq.Acq_ThinkWalnut_Utilities;
import com.acq.users.entity.Acq_TerminalInfo_Entity;
import com.acq.users.entity.Acq_SysParams_Entity;
import com.acq.users.entity.Acq_Tw_Recharge_Entity;
import com.acq.web.controller.model.Acq_TW_OperatorList_Model;
import com.acq.web.controller.model.Acq_TW_DoRecharge_Model;
import com.acq.web.controller.model.Acq_TW_TxnStatus_Model;
import com.acq.web.dao.Acq_Dao;
import com.acq.web.dao.Acq_ThinkWalnut_Transactions_Dao_Inf;
import com.acq.web.dto.impl.DbDto2;
import com.acq.web.threads.Acq_TW_Txn_Status_Thread;



	@Repository
	public class Acq_ThinkWalnut_Transactions_Dao extends Acq_Dao implements Acq_ThinkWalnut_Transactions_Dao_Inf {

		
		final static Logger logger = Logger.getLogger(Acq_ThinkWalnut_Transactions_Dao.class);
		Acq_ThinkWalnut_Utilities twRechargeApi = new Acq_ThinkWalnut_Utilities();
		
		
		@Override
		public DbDto2<Object> getOperators(Acq_TW_OperatorList_Model model) {

			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "DAO" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			try{				
				JSONObject operator1 = new JSONObject();
				operator1.put("operatorId", "1");
				operator1.put("operatorCode", "VODAFONE");
				operator1.put("operatorName", "VODAFONE");
				JSONObject operator2 = new JSONObject();
				operator2.put("operatorId", "2");
				operator2.put("operatorCode", "AIRTEL");
				operator2.put("operatorName", "AIRTEL");

				JSONObject operator3 = new JSONObject();
				operator3.put("operatorId", "3");
				operator3.put("operatorCode", "IDEA");
				operator3.put("operatorName", "IDEA");
				JSONObject operator4 = new JSONObject();
				operator4.put("operatorId", "4");
				operator4.put("operatorCode", "AIRCEL");
				operator4.put("operatorName", "AIRCEL");
				JSONObject operator5 = new JSONObject();
				operator5.put("operatorId", "5");
				operator5.put("operatorCode", "BSNL");
				operator5.put("operatorName", "BSNL");
				JSONObject operator6 = new JSONObject();
				operator6.put("operatorId", "6");
				operator6.put("operatorCode", "DOCOMO");
				operator6.put("operatorName", "DOCOMO");
				JSONObject operator7 = new JSONObject();
				operator7.put("operatorId", "7");
				operator7.put("operatorCode", "MTS");
				operator7.put("operatorName", "MTS");
				JSONObject operator8 = new JSONObject();
				operator8.put("operatorId", "8");
				operator8.put("operatorCode", "RELIANCE");
				operator8.put("operatorName", "RELIANCE");
				JSONObject operator9 = new JSONObject();
				operator9.put("operatorId", "9");
				operator9.put("operatorCode", "RELIANCECDMA");
				operator9.put("operatorName", "RELIANCECDMA");
				JSONObject operator10 = new JSONObject();
				operator10.put("operatorId", "10");
				operator10.put("operatorCode", "JIO");
				operator10.put("operatorName", "JIO");
				JSONObject operator11 = new JSONObject();
				operator11.put("operatorId", "11");
				operator11.put("operatorCode", "TATACDMA");
				operator11.put("operatorName", "TATACDMA");
				JSONObject operator12 = new JSONObject();
				operator12.put("operatorId", "12");
				operator12.put("operatorCode", "TELENOR");
				operator12.put("operatorName", "TELENOR");
				JSONObject operator13 = new JSONObject();
				operator13.put("operatorId", "13");
				operator13.put("operatorCode", "MTNL");
				operator13.put("operatorName", "MTNL");
				JSONObject operator14 = new JSONObject();
				operator14.put("operatorId", "20");
				operator14.put("operatorCode", "DOCOMOSPL");
				operator14.put("operatorName", "DOCOMO SPECIAL");
				JSONObject operator15 = new JSONObject();
				operator15.put("operatorId", "21");
				operator15.put("operatorCode", "TELENORSPL");
				operator15.put("operatorName", "TELENOR SPECIAL");
				JSONObject operator16 = new JSONObject();
				operator16.put("operatorId", "25");
				operator16.put("operatorCode", "BSNLSPL");
				operator16.put("operatorName", "BSNL STV");
				JSONObject circleCode1 = new JSONObject();
				circleCode1.put("circleId", "1");
				circleCode1.put("circleCode", "AP");
				circleCode1.put("circleName", "Andhra Pradesh");
				JSONObject circleCode2 = new JSONObject();
				circleCode2.put("circleId", "2");
				circleCode2.put("circleCode", "CH");
				circleCode2.put("circleName", "Chennai");
				JSONObject circleCode3 = new JSONObject();
				circleCode3.put("circleId", "3");
				circleCode3.put("circleCode", "DH");
				circleCode3.put("circleName", "Delhi");
				JSONObject circleCode4 = new JSONObject();
				circleCode4.put("circleId", "4");
				circleCode4.put("circleCode", "GJ");
				circleCode4.put("circleName", "Gujarat");
				JSONObject circleCode5 = new JSONObject();
				circleCode5.put("circleId", "5");
				circleCode5.put("circleCode", "HR");
				circleCode5.put("circleName", "Haryana");
				JSONObject circleCode6 = new JSONObject();
				circleCode6.put("circleId", "6");
				circleCode6.put("circleCode", "KA");
				circleCode6.put("circleName", "Karnataka");
				JSONObject circleCode7 = new JSONObject();
				circleCode7.put("circleId", "7");
				circleCode7.put("circleCode", "KE");
				circleCode7.put("circleName", "Kerla");
				JSONObject circleCode8 = new JSONObject();
				circleCode8.put("circleId", "8");
				circleCode8.put("circleCode", "KO");
				circleCode8.put("circleName", "Kolkata");
				JSONObject circleCode9 = new JSONObject();
				circleCode9.put("circleId", "9");
				circleCode9.put("circleCode", "MH");
				circleCode9.put("circleName", "Maharashtra Goa");
				JSONObject circleCode10 = new JSONObject();
				circleCode10.put("circleId", "10");
				circleCode10.put("circleCode", "MU");
				circleCode10.put("circleName", "Mumbai");
				JSONObject circleCode11 = new JSONObject();
				circleCode11.put("circleId", "11");
				circleCode11.put("circleCode", "PB");
				circleCode11.put("circleName", "Punjab");
				JSONObject circleCode12 = new JSONObject();
				circleCode12.put("circleId", "12");
				circleCode12.put("circleCode", "RJ");
				circleCode12.put("circleName", "Rajasthan");
				JSONObject circleCode13 = new JSONObject();
				circleCode13.put("circleId", "13");
				circleCode13.put("circleCode", "WB");
				circleCode13.put("circleName", "West Bengal");
				JSONObject circleCode14 = new JSONObject();
				circleCode14.put("circleId", "14");
				circleCode14.put("circleCode", "TN");
				circleCode14.put("circleName", "Tamil Nadu");
				JSONObject circleCode15 = new JSONObject();
				circleCode15.put("circleId", "15");
				circleCode15.put("circleCode", "UE");
				circleCode15.put("circleName", "UP East");
				JSONObject circleCode16 = new JSONObject();
				circleCode16.put("circleId", "16");
				circleCode16.put("circleCode", "UW");
				circleCode16.put("circleName", "UP West");
				JSONObject circleCode17 = new JSONObject();
				circleCode17.put("circleId", "17");
				circleCode17.put("circleCode", "HP");
				circleCode17.put("circleName", "Himachal Pradesh");
				JSONObject circleCode18 = new JSONObject();
				circleCode18.put("circleId", "18");
				circleCode18.put("circleCode", "MP");
				circleCode18.put("circleName", "Madhya Pradesh");
				JSONObject circleCode19 = new JSONObject();
				circleCode19.put("circleId", "19");
				circleCode19.put("circleCode", "AS");
				circleCode19.put("circleName", "Assam");
				JSONObject circleCode20 = new JSONObject();
				circleCode20.put("circleId", "20");
				circleCode20.put("circleCode", "BR");
				circleCode20.put("circleName", "Bihar");
				JSONObject circleCode21 = new JSONObject();
				circleCode21.put("circleId", "21");
				circleCode21.put("circleCode", "JK");
				circleCode21.put("circleName", "Jammu Kashmir");
				JSONObject circleCode22 = new JSONObject();
				circleCode22.put("circleId", "22");
				circleCode22.put("circleCode", "NE");
				circleCode22.put("circleName", "North East");
				JSONObject circleCode23 = new JSONObject();
				circleCode23.put("circleId", "23");
				circleCode23.put("circleCode", "OR");
				circleCode23.put("circleName", "Orissa");
				JSONObject circleCode24 = new JSONObject();
				circleCode24.put("circleId", "51");
				circleCode24.put("circleCode", "UN");
				circleCode24.put("circleName", "Unknown");
				JSONObject dth1 = new JSONObject();
				dth1.put("dthCode", "14");
				dth1.put("provider", "Airtel DTH");
				JSONObject dth2 = new JSONObject();
				dth2.put("dthCode", "15");
				dth2.put("provider", "BigTV");
				JSONObject dth3 = new JSONObject();
				dth3.put("dthCode", "16");
				dth3.put("provider", "DishTV");
				JSONObject dth4 = new JSONObject();
				dth4.put("dthCode", "17");
				dth4.put("provider", "SunTV");
				JSONObject dth5 = new JSONObject();
				dth5.put("dthCode", "18");
				dth5.put("provider", "VideoconDTH");
				JSONObject dth6 = new JSONObject();
				dth6.put("dthCode", "19");
				dth6.put("provider", "TataSky");

				JSONArray operatorList = new JSONArray();
				JSONArray circleCodeList = new JSONArray();
				JSONArray dthList = new JSONArray();
				operatorList.put(operator1);operatorList.put(operator2);operatorList.put(operator3);operatorList.put(operator4);operatorList.put(operator5);operatorList.put(operator6);
				operatorList.put(operator7);operatorList.put(operator8);operatorList.put(operator9);operatorList.put(operator10);operatorList.put(operator11);operatorList.put(operator12);
				operatorList.put(operator13);operatorList.put(operator14);operatorList.put(operator15);operatorList.put(operator15);operatorList.put(operator16);
				circleCodeList.put(circleCode1);circleCodeList.put(circleCode2);circleCodeList.put(circleCode3);circleCodeList.put(circleCode4);circleCodeList.put(circleCode5);circleCodeList.put(circleCode6);circleCodeList.put(circleCode7);circleCodeList.put(circleCode8);
				circleCodeList.put(circleCode9);circleCodeList.put(circleCode10);circleCodeList.put(circleCode11);circleCodeList.put(circleCode12);circleCodeList.put(circleCode13);circleCodeList.put(circleCode14);
				circleCodeList.put(circleCode15);circleCodeList.put(circleCode16);circleCodeList.put(circleCode17);circleCodeList.put(circleCode18);circleCodeList.put(circleCode19);circleCodeList.put(circleCode20);
				circleCodeList.put(circleCode21);circleCodeList.put(circleCode22);circleCodeList.put(circleCode23);circleCodeList.put(circleCode24);
				dthList.put(dth1);dthList.put(dth2);dthList.put(dth3);dthList.put(dth4);dthList.put(dth5);dthList.put(dth6);
				JSONObject responseJson = new JSONObject();
				responseJson.put("Operators", operatorList);
				responseJson.put("circleCodes", circleCodeList);
				responseJson.put("dth", dthList);

				daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setBody(responseJson);		
				logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "DAO" + "::" + "Returning To Handler");		
			}
			catch(Exception e)
			{
				logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
				e.printStackTrace();
				daoResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
				daoResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
				daoResponse.setBody(null);
			}
			return daoResponse;
		}
	
		@Override
		public DbDto2<Object> doRecharge(Acq_TW_DoRecharge_Model model) {
			
			logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			Session daoSession = null;
			Transaction dbTransaction = null;
			
			try{
				String dbId = null;
				String twId = null;
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String txnDatetime = dateFormat.format(new Date());
				String twMobileUrl = "http://api.twd.bz/wallet/api/mobile.php";
				String twDthUrl = "http://api.twd.bz/wallet/api/dth.php";
				
				Acq_TerminalInfo_Entity terminalInfo = null;
				Acq_SysParams_Entity systemSettings  = null;
				try 
				{
					//Fetching Terminal Info and System Parameters
					
					daoSession = createNewSession();
					terminalInfo = (Acq_TerminalInfo_Entity)daoSession.createCriteria(Acq_TerminalInfo_Entity.class)
							.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();

					if (terminalInfo == null || terminalInfo + "" == "") {
						logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Terminal Info Not Found");
						daoResponse.setStatusCode(Acq_Status_Definations.TerminalInfoNotFound.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.TerminalInfoNotFound.getDescription());
						daoResponse.setBody(null);
						return daoResponse;
					}

					logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Terminal Info Found");

					systemSettings = (Acq_SysParams_Entity)daoSession.createCriteria(Acq_SysParams_Entity.class).add(Restrictions.eq("id", Long.valueOf("1"))).uniqueResult();

					if (systemSettings == null || systemSettings + "" == "") {
						logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "System Settings Not Found");
						daoResponse.setStatusCode(Acq_Status_Definations.ExceptionInSystemParams.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.ExceptionInSystemParams.getDescription());
						daoResponse.setBody(null);
						return daoResponse;
					}

					logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "System Settings Found");

				} catch (Exception e) {
					logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::"
							+ "Exception in Validating User Details");
					e.printStackTrace();
					daoResponse.setStatusCode(Acq_Status_Definations.ExceptionInMerchantInfo.getId());
					daoResponse.setStatusMessage(Acq_Status_Definations.ExceptionInMerchantInfo.getDescription());
					daoResponse.setBody(null);
					return daoResponse;
				} finally {
					if (daoSession.isOpen() == true || daoSession.isConnected())
						daoSession.close();
				}
				
					
				if(Double.parseDouble(terminalInfo.getRechargeBal())<Double.parseDouble(model.getAmount()))
				{
					logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::"
							+ "Insufficient Recharge Balance");
					daoResponse.setStatusCode(Acq_Status_Definations.InsufficientRechargeBalance.getId());
					daoResponse.setStatusMessage(Acq_Status_Definations.InsufficientRechargeBalance.getDescription());
					daoResponse.setBody(null);
					return daoResponse;
				}
				else
				{
					// Saving Transaction Data
					logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Database Transaction Begin");
					
					JSONObject resultJson = new JSONObject();
					try {
						daoSession = createNewSession();
						dbTransaction = daoSession.beginTransaction();
						
						Acq_Tw_Recharge_Entity rechargeTxn = new Acq_Tw_Recharge_Entity();
						rechargeTxn.setAmount(new BigDecimal(model.getAmount()));
						rechargeTxn.setCardTransactionId(model.getCardTransactionId());
						rechargeTxn.setCircle(model.getCircle());
						rechargeTxn.setOperator(model.getOperator());
						rechargeTxn.setRechargeType(model.getRechargeType());
						rechargeTxn.setServiceType(model.getServiceType());
						rechargeTxn.setSessionId(model.getSessionId());
						rechargeTxn.setSubscriberId(model.getSubscriberId());
						rechargeTxn.setStatus("Initiated");
						rechargeTxn.setTwId("0");
						rechargeTxn.setDateTime(txnDatetime);
						rechargeTxn.setMessage("NA");
						rechargeTxn.setServiceTax(systemSettings.getServiceTax());
						rechargeTxn.setPaymentMode(model.getPaymentMode());
						daoSession.save(rechargeTxn);
						//Transaction Saved

						if(rechargeTxn.getServiceType().equalsIgnoreCase("Mobile"))
						{
							logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Mobile Recharge Api Begin");
							
							String requestParams = "&to="+rechargeTxn.getSubscriberId()+"&transId="+""+rechargeTxn.getId()+"&amount="+""+rechargeTxn.getAmount()+"&type="+rechargeTxn.getRechargeType()+"&operator="+rechargeTxn.getOperator()+"&circle="+rechargeTxn.getCircle();
							Map<String, String> twResponseMap = twRechargeApi.tw_Port_Connector(twMobileUrl,requestParams,"/tw/tpos/billpay/doRecharge/v1");
							
							if(twResponseMap == null)
							{
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Connection Failed");
								
								//Updating Transaction Info
								Acq_Tw_Recharge_Entity  txn2Update = (Acq_Tw_Recharge_Entity) daoSession.createCriteria(Acq_Tw_Recharge_Entity.class).add(Restrictions.eq("id",rechargeTxn.getId())).uniqueResult();	
								txn2Update.setMessage("Connection Failed");
								txn2Update.setStatus("Failed");
								txn2Update.setTwId("0");
								txn2Update.setTwStatusCode("2");
								daoSession.update(txn2Update);
								dbTransaction.commit();
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Mobile Recharge Failed - DB Updated");

								daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
								daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
								daoResponse.setBody(null);
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Mobile Recharge Failed - Returning To Handler");
								return daoResponse;
							}
							else if(twResponseMap.get("errCode").equalsIgnoreCase("-1"))
							{
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Mobile Recharge Done - Status Pending");
								
								//Updating Transaction Info
								Acq_Tw_Recharge_Entity  txn2Update = (Acq_Tw_Recharge_Entity) daoSession.createCriteria(Acq_Tw_Recharge_Entity.class).add(Restrictions.eq("id",rechargeTxn.getId())).uniqueResult();	
								txn2Update.setMessage(twResponseMap.get("msg"));
								txn2Update.setStatus("Pending");
								txn2Update.setTwId(twResponseMap.get("txnId"));
								txn2Update.setTwStatusCode(twResponseMap.get("errCode"));
								daoSession.update(txn2Update);
								
								//Updating Balance in Terminal Info
								Double newBalance = Double.parseDouble(terminalInfo.getRechargeBal()) - Double.parseDouble(model.getAmount());
								terminalInfo.setRechargeBal(new DecimalFormat("#.###").format(Double.parseDouble(newBalance.toString())));
								daoSession.update(terminalInfo);

								twId = txn2Update.getTwId();
								dbId = txn2Update.getId();
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Mobile Recharge Done - DB Updated");
							}
							else
							{
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Mobile Recharge Done - Status Failed");
								
								//Updating Transaction Info
								Acq_Tw_Recharge_Entity  txn2Update = (Acq_Tw_Recharge_Entity) daoSession.createCriteria(Acq_Tw_Recharge_Entity.class).add(Restrictions.eq("id",rechargeTxn.getId())).uniqueResult();	
								txn2Update.setMessage(twResponseMap.get("msg"));
								txn2Update.setStatus("Failed");
								txn2Update.setTwId(twResponseMap.get("txnId"));
								txn2Update.setTwStatusCode(twResponseMap.get("errCode"));
								daoSession.update(txn2Update);
								dbTransaction.commit();
								
								twId = txn2Update.getTwId();
								dbId = txn2Update.getId();
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Mobile Recharge Failed - DB Updated");

								resultJson.put("rechargeStatus", rechargeTxn.getTwStatusCode());
								resultJson.put("twId", twResponseMap.get("txnId"));
								resultJson.put("rechargeId", rechargeTxn.getId());
								resultJson.put("rechargeMessage", twResponseMap.get("msg"));
								
								daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
								daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
								daoResponse.setBody(resultJson);
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Mobile Recharge Failed - Returning To Handler");
								return daoResponse;
							}
						}
						else if(rechargeTxn.getServiceType().equalsIgnoreCase("DTH"))
						{
							logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "DTH Recharge Api Begin");

							String requestParams = "&to="+rechargeTxn.getSubscriberId()+"&transId="+""+rechargeTxn.getId()+"&amount="+""+rechargeTxn.getAmount()+"&type="+rechargeTxn.getRechargeType()+"&operator="+rechargeTxn.getOperator();
							Map<String, String> twResponseMap = twRechargeApi.tw_Port_Connector(twDthUrl,requestParams,"/tw/tpos/billpay/doRecharge/v1");

							if(twResponseMap == null)
							{
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Connection Failed");
								
								//Updating Transaction Info
								Acq_Tw_Recharge_Entity  txn2Update = (Acq_Tw_Recharge_Entity) daoSession.createCriteria(Acq_Tw_Recharge_Entity.class).add(Restrictions.eq("id",rechargeTxn.getId())).uniqueResult();	
								txn2Update.setMessage("Connection Failed");
								txn2Update.setStatus("Failed");
								txn2Update.setTwId("0");
								txn2Update.setTwStatusCode("2");
								daoSession.update(txn2Update);
								dbTransaction.commit();
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "DTH Recharge Failed - DB Updated");

								
								daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
								daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
								daoResponse.setBody(null);
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "DTH Recharge Failed - Returning To Handler");
								return daoResponse;
								
							}
							else if(twResponseMap.get("errCode").equalsIgnoreCase("-1")){
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "DTH Recharge Done - Status Pending");
								
								//Updating Transaction Info
								Acq_Tw_Recharge_Entity  txn2Update = (Acq_Tw_Recharge_Entity) daoSession.createCriteria(Acq_Tw_Recharge_Entity.class).add(Restrictions.eq("id",rechargeTxn.getId())).uniqueResult();	
								txn2Update.setMessage(twResponseMap.get("msg"));
								txn2Update.setStatus("Pending");
								txn2Update.setTwId(twResponseMap.get("txnId"));
								txn2Update.setTwStatusCode(twResponseMap.get("errCode"));
								daoSession.update(txn2Update);
								
								//Updating Balance in Terminal Info
								Double newBalance = Double.parseDouble(terminalInfo.getRechargeBal()) - Double.parseDouble(model.getAmount());
								terminalInfo.setRechargeBal(new DecimalFormat("#.###").format(Double.parseDouble(newBalance.toString())));
								daoSession.update(terminalInfo);
								
								twId = txn2Update.getTwId();
								dbId = txn2Update.getId();
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "DTH Recharge Done - DB Updated");
							
							}else{
								
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "DTH Recharge Done - Status Failed");
								
								//Updating Transaction Info
								Acq_Tw_Recharge_Entity  txn2Update = (Acq_Tw_Recharge_Entity) daoSession.createCriteria(Acq_Tw_Recharge_Entity.class).add(Restrictions.eq("id",rechargeTxn.getId())).uniqueResult();	
								txn2Update.setMessage(twResponseMap.get("msg"));
								txn2Update.setStatus("Failed");
								txn2Update.setTwId(twResponseMap.get("txnId"));
								txn2Update.setTwStatusCode(twResponseMap.get("errCode"));
								daoSession.update(txn2Update);
								dbTransaction.commit();

								twId = txn2Update.getTwId();
								dbId = txn2Update.getId();
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "DTH Recharge Failed - DB Updated");

								resultJson.put("rechargeStatus", twResponseMap.get("errCode"));
								resultJson.put("twId", twResponseMap.get("txnId"));
								resultJson.put("rechargeId", rechargeTxn.getId());
								resultJson.put("rechargeMessage", twResponseMap.get("msg"));
								
								daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
								daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
								daoResponse.setBody(resultJson);
								logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "DTH Recharge Failed - Returning To Handler");
								return daoResponse;
							}
						}
						
						resultJson.put("rechargeStatus", rechargeTxn.getTwStatusCode());
						resultJson.put("twId", rechargeTxn.getTwId());
						resultJson.put("rechargeId", rechargeTxn.getId());
						resultJson.put("rechargeMessage", rechargeTxn.getMessage());
						dbTransaction.commit();
						daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
						daoResponse.setBody(resultJson);
					}
					catch (Exception e) 
					{
						logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Exception in Handling DB");
						e.printStackTrace();
						if(dbTransaction.isActive()){dbTransaction.rollback();} 
						daoResponse.setStatusCode(Acq_Status_Definations.DataPersistanceError.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.DataPersistanceError.getDescription());
						daoResponse.setBody(null);
						return daoResponse;
					}
					finally 
					{
						if (daoSession.isOpen() == true || daoSession.isConnected() == true) {
							daoSession.close();
						}
					}

					logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Recharge Complete, Starting Status Thread");
						
					try{
						if(twId!=null && twId!="" && dbId!=null && dbId!="")
						{
							Acq_TW_Txn_Status_Thread checkStatus = new Acq_TW_Txn_Status_Thread();
							checkStatus.setSession(createNewSession());
							checkStatus.setCallFrom("/tw/tpos/billpay/doRecharge/v1");
							checkStatus.setRefId(twId);
							checkStatus.setId(dbId);
							Thread checkStatusTd = new Thread(checkStatus);	
							checkStatusTd.start();
						}
					}
					catch(Exception e)
					{
						logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Status Thread Failed to Start");
						e.printStackTrace();
					}
				}
			}catch(Exception e){
				logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
				e.printStackTrace();
				daoResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
				return daoResponse;
			}
			return daoResponse;
		}		

		@Override
		public DbDto2<Object> getBalance(Acq_TW_OperatorList_Model model) 
		{
			logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "DAO" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			Session daoSession = null;

			try {
				daoSession = createNewSession();
				Acq_TerminalInfo_Entity terminalInfo = (Acq_TerminalInfo_Entity)daoSession.createCriteria(Acq_TerminalInfo_Entity.class).add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();				
				String availableBalance = new DecimalFormat("#.###").format(Double.parseDouble(terminalInfo.getRechargeBal()));
				JSONObject resultJson = new JSONObject();
				resultJson.put("availableBalance", availableBalance);

				daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setBody(resultJson);
				logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "DAO" + "::"+ "User Balance Fetched");		
			} 
			catch (Exception e) 
			{
				logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "DAO" + "::"+ "Exception in Validating User Details");
				e.printStackTrace();
				daoResponse.setStatusCode(Acq_Status_Definations.TerminalInfoNotFound.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.TerminalInfoNotFound.getDescription());
				daoResponse.setBody(null);
			}			
			finally
			{
				if (daoSession.isOpen() == true || daoSession.isConnected() == true) {
					daoSession.close();
				}		
			}
			logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "DAO" + "::"+ "Returning to Handler");
			return daoResponse;
		}

		@Override
		public DbDto2<Object> getTxnStatus(Acq_TW_TxnStatus_Model model) 
		{
			logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			Session daoSession = null;
			Transaction dbTransaction = null;
			String checkStatusURL = "http://api.twd.bz/wallet/api/checkStatus.php";
			try {
					daoSession = createNewSession();
					Acq_Tw_Recharge_Entity  acq_Tw_Recharge_Entity = (Acq_Tw_Recharge_Entity) daoSession.createCriteria(Acq_Tw_Recharge_Entity.class).add(Restrictions.eq("sessionId",model.getSessionId())).add(Restrictions.eq("subscriberId",model.getSubscriberId())).add(Restrictions.eq("id",model.getRechargeId())).uniqueResult();	
					if(acq_Tw_Recharge_Entity==null||acq_Tw_Recharge_Entity+""=="")
					{
						logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Transaction Not Found");
						daoResponse.setStatusCode(Acq_Status_Definations.TransactionSearchFailed.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.TransactionSearchFailed.getDescription());
						daoResponse.setBody(null); 
					}
					else
					{
						logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Transaction Found");
						if(!acq_Tw_Recharge_Entity.getTwStatusCode().equals("0"))
						{
							logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Transaction Status "+acq_Tw_Recharge_Entity.getTwStatusCode());
							
							String requestParams = "&refId="+acq_Tw_Recharge_Entity.getId();
							Map<String, String> twResponseMap = twRechargeApi.tw_Port_Connector(checkStatusURL,requestParams,"/tw/tpos/billpay/getTxnStatus/v1");
							
							if(twResponseMap.get("errCode").equals("0"))
							{
								logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Transaction Status - Success ");
								acq_Tw_Recharge_Entity.setStatus("Success");
							}
							else
							{
								logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Transaction Status "+twResponseMap.get("msg"));
								acq_Tw_Recharge_Entity.setStatus(twResponseMap.get("msg"));
							}
							
							dbTransaction = daoSession.beginTransaction();
							
							acq_Tw_Recharge_Entity.setMessage(twResponseMap.get("msg"));
							acq_Tw_Recharge_Entity.setTwStatusCode(twResponseMap.get("errCode"));
							acq_Tw_Recharge_Entity.setOptId(twResponseMap.get("optId"));
							daoSession.update(acq_Tw_Recharge_Entity);
							
							logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Transaction Status Updated in DB");
							
							JSONObject resultJson = new JSONObject();
							resultJson.put("rechargeStatus", acq_Tw_Recharge_Entity.getTwStatusCode());
							resultJson.put("rechargeMessage", acq_Tw_Recharge_Entity.getMessage());
							resultJson.put("rechargeId", acq_Tw_Recharge_Entity.getId());
							
							daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
							daoResponse.setBody(resultJson);
							dbTransaction.commit();
						}
						else
						{
							logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Transaction Status Already Success ");
							JSONObject resultJson = new JSONObject();
							resultJson.put("rechargeStatus", acq_Tw_Recharge_Entity.getTwStatusCode());
							resultJson.put("rechargeMessage", acq_Tw_Recharge_Entity.getMessage());
							resultJson.put("rechargeId", acq_Tw_Recharge_Entity.getId());
							
							daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
							daoResponse.setBody(resultJson);	
						}
					}
				logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Returning To Handler");
				}
				catch (Exception e) 
				{
					logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
					e.printStackTrace();
					if(dbTransaction.isActive()){dbTransaction.rollback();} 
					daoResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
					daoResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
					daoResponse.setBody(null);
				}			
				finally
				{
					if (daoSession.isOpen() == true || daoSession.isConnected() == true) {
						daoSession.close();
					}			
				}
			return daoResponse;
		}
	
	
	@Override
	public DbDto2<Object> getTxnList(Acq_TW_OperatorList_Model model) 
	{
		logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Begin");
		DbDto2<Object> daoResponse = new DbDto2<Object>();
		Session daoSession = null;
		
		try 
		{
			daoSession = createNewSession();
			SimpleDateFormat dbDateFormat = new SimpleDateFormat("YYYY-MM-dd");
			String fromDate = null;
			String toDate = null; 
			
			if(model.getDate() !=null && model.getDate()!="" && model.getDate().length()>1)
			{
				SimpleDateFormat terminalDateFormat = new SimpleDateFormat("ddMMyyyy");
				Date givenDate =terminalDateFormat.parse(model.getDate());
				fromDate = dbDateFormat.format(givenDate) + " 00:00:00";
				toDate= dbDateFormat.format(givenDate) + " 23:59:59";
			}
			else
			{
				Date dateToday = new Date();
				fromDate = dbDateFormat.format(dateToday) + " 00:00:00";
				toDate= dbDateFormat.format(dateToday) + " 23:59:59";
			}
			
			List<Acq_Tw_Recharge_Entity> txnList = (List<Acq_Tw_Recharge_Entity>) daoSession.createCriteria(Acq_Tw_Recharge_Entity.class).addOrder(Order.desc("id")).add(Restrictions.eq("sessionId",model.getSessionId())).add(Restrictions.between("dateTime",fromDate, toDate)).list();
			
			if (txnList == null || txnList.isEmpty()) 
			{
				logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "No Tranasctions Found");
				daoResponse.setStatusCode(Acq_Status_Definations.TransactionsNotFound.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.TransactionsNotFound.getDescription());
				daoResponse.setBody(null);
			} 
			else 
			{
				Iterator<Acq_Tw_Recharge_Entity> txnListItr = txnList.iterator();
				JSONArray txnJsonArray = new JSONArray();
				JSONObject singleTxnJson = new JSONObject();
				long txnCount = 0;
				while (txnListItr.hasNext()) {
					Acq_Tw_Recharge_Entity singleTxn = (Acq_Tw_Recharge_Entity) txnListItr.next();
					singleTxnJson.put("subscriberId", singleTxn.getSubscriberId());
					singleTxnJson.put("rechargeId", singleTxn.getId());
					singleTxnJson.put("rechargeStatusCode", singleTxn.getTwStatusCode());
					singleTxnJson.put("rechargeStatusMessage", singleTxn.getMessage());
					singleTxnJson.put("rechargeAmount", singleTxn.getAmount());
					singleTxnJson.put("operator", singleTxn.getOperator());
					singleTxnJson.put("circle", singleTxn.getCircle());
					singleTxnJson.put("rechargeType", singleTxn.getRechargeType());
					singleTxnJson.put("serviceType", singleTxn.getServiceType());
					singleTxnJson.put("cardTransactionId", singleTxn.getCardTransactionId());
					txnCount++;
					txnJsonArray.put(singleTxnJson);					
				}
				JSONObject resultJson = new JSONObject();
				resultJson.put("rechargeList", txnJsonArray);
				logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Total Txns - " + txnCount);
				
				daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setBody(resultJson);
				logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Returning To Handler");			
			}
			
		}
		catch (Exception e) 
		{
			logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
			e.printStackTrace();
			daoResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
		}			
		finally
		{
			if (daoSession.isOpen() == true || daoSession.isConnected() == true) {
				daoSession.close();
			}		
		}
		return daoResponse;
	}
}
