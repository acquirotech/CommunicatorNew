package com.acq.web.dao.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.acq.Acq_Status_Definations;
import com.acq.Acq_Utility_Functions;
import com.acq.Acq_Bankit_Transactions_Utilities;
import com.acq.users.entity.Acq_BankIt_Persist_Entity;
import com.acq.users.entity.Acq_BankIt_Transaction_Entity;
import com.acq.users.entity.Acq_TerminalInfo_Entity;
import com.acq.users.entity.Acq_Store_Entity;
import com.acq.users.entity.Acq_TerminalUser_Entity;
import com.acq.web.controller.model.Acq_Bankit_PrepaidLoad_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_TxnList_Model;
import com.acq.web.controller.model.Acq_BankitTxns_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_AddBene_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_AddSender_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_BnkList_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_TxnStatus_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_Neft_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_sendOTP_Model;
import com.acq.web.dao.Acq_Dao;
import com.acq.web.dao.Acq_Bankit_Transactions_DAO_Inf;
import com.acq.web.dto.impl.DbDto2;

	@Repository
	public class Acq_Bankit_Transactions_Dao extends Acq_Dao implements Acq_Bankit_Transactions_DAO_Inf {
		
		final static Logger logger = Logger.getLogger(Acq_Bankit_Transactions_Dao.class);
		
		@Override
		public DbDto2<Object> addSender(Acq_Bankit_DMT_AddSender_Model model) 
		{
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "DAO" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			
			Acq_Bankit_Transactions_Utilities dmtUtilities = new Acq_Bankit_Transactions_Utilities();
	
			String requestParams ="{\"agentCode\":\""+model.getSessionId()+"\",\n\"customerId\":\""+model.getCustomerId()+"\",\n\"name\":\""+model.getName()+"\",\n\"address\":\""+model.getAddress()+"\",\n\"dateOfBirth\":\""+model.getDateOfBirth()+"\",\n\"otp\":\""+model.getOtp()+"\"\n}";
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "DAO" + "::" + "Request Params Ready");
			
			JSONObject resultJson = new JSONObject();
			resultJson = dmtUtilities.bankitDMTConnector(requestParams,"customer/create","/bankit/tpos/dmt/addSender/v1");
			
			//Saving request/response params
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dtNow = dateFormat.format(new Date());
			
			if(resultJson != null && resultJson.toString().length()>2) //empty Json length is 2 {}
			{
				logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "DAO" + "::" + "Bankit Response - Saving and Returning");
				saveBankitApiCalls("/bankit/tpos/dmt/addSender/v1", model.getSessionId(), requestParams, resultJson.toString(), model.getDateTime(),dtNow);			
				daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setBody(resultJson);
			}
			else
			{
				logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "DAO" + "::" + "Bankit Response Null Saving and Returning");
				saveBankitApiCalls("/bankit/tpos/dmt/addSender/v1", model.getSessionId(), requestParams, "", model.getDateTime(),dtNow);			
				daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setBody(null);
			}
			return daoResponse;
		}

		
		@Override
		public DbDto2<Object> addBene(Acq_Bankit_DMT_AddBene_Model model) 
		{
			logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();

			Acq_Bankit_Transactions_Utilities dmtUtilities = new Acq_Bankit_Transactions_Utilities();
		
			String requestParams ="{\"agentCode\":\""+model.getSessionId()+"\",\n\"customerId\":\""+model.getCustomerId()+"\",\n\"bankName\":\""+model.getBankName()+"\",\n\"accountNo\":\""+model.getAccountNo()+"\",\n\"ifsc\":\""+model.getIfsc()+"\",\n\"mobileNo\":\""+model.getMobileNo()+"\",\n\"recipientName\":\""+model.getRecipientName()+"\"\n}";
			logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Request Params Ready");
			
			JSONObject resultJson = new JSONObject();
			resultJson = dmtUtilities.bankitDMTConnector(requestParams,"recipient/add","/bankit/tpos/dmt/addBene/v1");
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dtNow = dateFormat.format(new Date());
			
			if(resultJson != null && resultJson.toString().length()>2) //empty Json length is 2 {}
			{
				if(resultJson.get("errorCode").equals("00"))
				{
					logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Bankit Response - Success");
					JSONObject responseDataParams = (JSONObject) resultJson.get("data");
					
					JSONObject dataParams = new JSONObject();
					dataParams.put("walletbal", responseDataParams.get("walletbal").toString());
					dataParams.put("customerId", responseDataParams.get("customerId"));
					dataParams.put("name", responseDataParams.get("name"));
					dataParams.put("kycstatus", responseDataParams.get("kycstatus").toString());
					dataParams.put("recipientId", responseDataParams.get("recipientId"));
					dataParams.put("recipientName", responseDataParams.get("recipientName"));
					dataParams.put("dateOfBirth", responseDataParams.get("dateOfBirth"));
					dataParams.put("mobileNo", responseDataParams.get("mobileNo"));
					
					JSONObject responseJson = new JSONObject();
					responseJson.put("errorCode", resultJson.get("errorCode"));
					responseJson.put("errorMsg", resultJson.get("errorMsg"));
					responseJson.put("data", dataParams);
					daoResponse.setBody(responseJson);
				}
				else
				{
					logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Bankit Response - "+resultJson.get("errorCode"));
					daoResponse.setBody(resultJson);
				}
				logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Bankit Response - Saving and Returning");
				daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				saveBankitApiCalls("/bankit/tpos/dmt/addBene/v1", model.getSessionId(), requestParams, resultJson.toString(), model.getDateTime(),dtNow);			
			}
			else
			{
				logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Bankit Response Null Saving and Returning");
				saveBankitApiCalls("/bankit/tpos/dmt/addBene/v1", model.getSessionId(), requestParams, "", model.getDateTime(), dtNow);			
				daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setBody(null);
			}
			return daoResponse;
		}		

		
		@Override
		public DbDto2<Object> sendOtp(Acq_Bankit_DMT_sendOTP_Model model)
		{
			logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Handler" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			Acq_Bankit_Transactions_Utilities dmtUtilities = new Acq_Bankit_Transactions_Utilities();

			String requestParams ="{\"agentCode\":\""+model.getSessionId()+"\",\n\"customerId\":\""+model.getCustomerId()+"\"\n}";
			JSONObject resultJson = dmtUtilities.bankitDMTConnector(requestParams,"generic/otp","/bankit/tpos/dmt/sendOtp/v1");
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dtNow = dateFormat.format(new Date());

			if(resultJson != null && resultJson.toString().length()>2) //empty Json length is 2 {}
			{
				logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "DAO" + "::" + "Bankit Response - Saving and Returning");
				saveBankitApiCalls("/bankit/tpos/dmt/sendOtp/v1", model.getSessionId(), requestParams, resultJson.toString(), model.getDateTime(),dtNow);
				daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setBody(resultJson);
			}
			else
			{
				logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "DAO" + "::" + "Bankit Response Null Saving and Returning");
				saveBankitApiCalls("/bankit/tpos/dmt/sendOtp/v1", model.getSessionId(), requestParams, "", model.getDateTime(), dtNow);			
				daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setBody(null);
			}
			return daoResponse;
		}
		
		
		@Override
		public DbDto2<Object> getBeneList(Acq_Bankit_DMT_sendOTP_Model model) 
		{
			logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Handler" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();

			Acq_Bankit_Transactions_Utilities dmtUtilities = new Acq_Bankit_Transactions_Utilities();

			String requestParams ="{\"agentCode\":\""+model.getSessionId()+"\",\n\"customerId\":\""+model.getCustomerId()+"\"\n}";
			logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "DAO" + "::" + "Request Params Ready");

			JSONObject resultJson = dmtUtilities.bankitDMTConnector(requestParams,"recipient/fetchAll","/bankit/tpos/dmt/getBeneList/v1");

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dtNow = dateFormat.format(new Date());

			if(resultJson != null && resultJson.toString().length()>2) //empty Json length is 2 {}
			{
				logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "DAO" + "::" + "Bankit Response - Saving and Returning");
				saveBankitApiCalls("/bankit/tpos/dmt/getBeneList/v1", model.getSessionId(), requestParams, resultJson.toString(), model.getDateTime(),dtNow);
				daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setBody(resultJson);
			}
			else
			{
				logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "DAO" + "::" + "Bankit Response Null Saving and Returning");
				saveBankitApiCalls("/bankit/tpos/dmt/getBeneList/v1", model.getSessionId(), requestParams, "", model.getDateTime(), dtNow);			
				daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setBody(null);
			}
			return daoResponse;
		}


		@Override
		public DbDto2<Object> deleteBene(Acq_Bankit_DMT_Neft_Model model) {
			
			logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "Handler" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			
			Acq_Bankit_Transactions_Utilities dmtUtilities = new Acq_Bankit_Transactions_Utilities();
			String requestParams ="{\"agentCode\":\""+model.getSessionId()+"\",\n\"customerId\":\""+model.getCustomerId()+"\",\n\"recipientId\":\""+model.getRecipientId()+"\"\n}";
			
			JSONObject resultJson = dmtUtilities.bankitDMTConnector(requestParams,"recipient/delete","/bankit/tpos/dmt/deleteBene/v1");

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dtNow = dateFormat.format(new Date());
			
			if(resultJson != null && resultJson.toString().length()>2) //empty Json length is 2 {}
			{
				logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "DAO" + "::" + "Bankit Response Received");
				saveBankitApiCalls("/bankit/tpos/dmt/getBeneList/v1", model.getSessionId(), requestParams, resultJson.toString(), model.getDateTime(),dtNow);
				daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setBody(resultJson);
			}
			else
			{
				logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "DAO" + "::" + "Bankit Response Null");
				saveBankitApiCalls("/bankit/tpos/dmt/getBeneList/v1", model.getSessionId(), requestParams, "", model.getDateTime(), dtNow);			
				daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setBody(null);
			}
			return daoResponse;
		}
		
		
		@Override
		public DbDto2<Object> doNeft(Acq_Bankit_DMT_Neft_Model model) 
		{
			
			logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Handler" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			
			Session daoSession = null;
			Transaction dbTransaction = null;
			
			try {
				
				daoSession = createNewSession();
				Acq_Bankit_Transactions_Utilities dmtUtilities = new Acq_Bankit_Transactions_Utilities();
				Acq_TerminalInfo_Entity terminalInfo = null;
				JSONObject beneDetails = null;
				JSONObject resultJsonNeft = null;
				
				try
				{
					//Fetching Terminal Info and System Parameters
					daoSession = createNewSession();
					terminalInfo = (Acq_TerminalInfo_Entity)daoSession.createCriteria(Acq_TerminalInfo_Entity.class)
							.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();

					if (terminalInfo == null || terminalInfo + "" == "") {
						logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Terminal Info Not Found");
						daoResponse.setStatusCode(Acq_Status_Definations.TerminalInfoNotFound.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.TerminalInfoNotFound.getDescription());
						daoResponse.setBody(null);
						return daoResponse;
					}
					logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Terminal Info Found");
				}
				catch (Exception e) 
				{
						logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::"
								+ "Exception in Validating User Details");
						e.printStackTrace();
						daoResponse.setStatusCode(Acq_Status_Definations.ExceptionInMerchantInfo.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.ExceptionInMerchantInfo.getDescription());
						daoResponse.setBody(null);
						return daoResponse;
				} 
				finally 
				{
						if (daoSession.isOpen() == true || daoSession.isConnected())
							daoSession.close();
				}
				
				
				if(Double.parseDouble(terminalInfo.getRechargeBal())<Double.parseDouble(model.getAmount()))
				{
					logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::"
							+ "Insufficient Recharge Balance");
					daoResponse.setStatusCode(Acq_Status_Definations.InsufficientDMTBalance.getId());
					daoResponse.setStatusMessage(Acq_Status_Definations.InsufficientDMTBalance.getDescription());
					daoResponse.setBody(null);
					return daoResponse;
				}
				else
				{
					logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "DMT Api Begin");
					
					String requestParams = "{\"agentCode\":\""+model.getSessionId()+"\",\n\"customerId\":\""+model.getRecipientId()+"\",\n\"recipientId\":\""+model.getCustomerId()+"\"\n}";
					logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Request Params Ready");
					
					JSONObject resultJson= dmtUtilities.bankitDMTConnector(requestParams,"recipient/fetch","/bankit/tpos/dmt/doNeft/v1");
					
					if(resultJson == null || resultJson.isEmpty())
					{
						//Connection Failed
						logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Bankit Response Null");
						daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
						daoResponse.setBody(null);
					}
					else if(resultJson.get("errorCode").equals("00")) 
					{
						logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Beneficiary Validated");
						beneDetails = (JSONObject) resultJson.get("data");
						
						logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "DMT Api Begin");
						String clientRefId = "10037"+Acq_Utility_Functions.RandomNumberGenerator().substring(0, Acq_Utility_Functions.RandomNumberGenerator().length() - model.getSessionId().length())+model.getSessionId();
						String requestParamsNeft = "{\"agentCode\":\""+model.getSessionId()+"\",\n\"customerId\":\""+model.getRecipientId()+"\",\n\"recipientId\":\""+model.getCustomerId()+"\",\n\"amount\":\""+model.getAmount()+"\",\n\"clientRefId\":\""+clientRefId+"\"\n}";
						logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Neft Request Params Ready");
						
						resultJsonNeft= dmtUtilities.bankitDMTConnector(requestParamsNeft,"transact/NEFT/remit","/bankit/tpos/dmt/doNeft/v1");
						
						if(resultJsonNeft == null || resultJsonNeft.isEmpty()) 
						{
							//Connection Failed
							logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Bankit Response Null");
							daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
							daoResponse.setBody(null);
						}
						else if (resultJsonNeft.get("errorCode").equals("00") || resultJsonNeft.get("errorCode").equals("01"))
						{
							//NEFT success
							
							//Updating Balance in Terminal Info
							logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Updating Balance in Terminal Info");
							Double newBalance = Double.parseDouble(terminalInfo.getRechargeBal()) - Double.parseDouble(model.getAmount());
							terminalInfo.setRechargeBal(new DecimalFormat("#.###").format(Double.parseDouble(newBalance.toString())));
							
							JSONObject neftDetails = (JSONObject) resultJsonNeft.get("data");
							Acq_BankitTxns_Model txnModel = new Acq_BankitTxns_Model();
							txnModel.setAmount(model.getAmount());
							txnModel.setSessionId(model.getSessionId());
							txnModel.setClientRefId(""+neftDetails.get("clientRefId"));
							txnModel.setRecipientId(model.getRecipientId());
							txnModel.setStatus(""+resultJsonNeft.get("errorCode"));
							txnModel.setTransactionType("NEFT");
							txnModel.setTxnId(""+neftDetails.get("txnId"));
							txnModel.setCustomerId(model.getCustomerId());
							txnModel.setMessage(""+resultJsonNeft.get("errorMsg"));
							txnModel.setIfsc(""+beneDetails.get("udf2"));
							txnModel.setMobileNo(""+beneDetails.get("mobileNo"));
							txnModel.setAccountNo(""+beneDetails.get("udf1"));
							txnModel.setRecipientName(""+beneDetails.get("recipientName"));
							txnModel.setBankName(model.getBankName());
							saveBankitTxns(txnModel,"/bankit/tpos/dmt/doNeft/v1");
							
							daoSession = createNewSession();
							dbTransaction = daoSession.beginTransaction();
							daoSession.update(terminalInfo);
							dbTransaction.commit();
							logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Balance in Terminal Info Updated");
							
							daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
							daoResponse.setBody(null);
						}
						else 
						{
							//NEFT Failed
							logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Neft Failed, Code - "+resultJsonNeft.get("errorCode"));
							daoResponse.setStatusCode(Acq_Status_Definations.NeftFailed.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.NeftFailed.getDescription());
							daoResponse.setBody(null);
						}
					}
					else
					{
						//Bene Not Found
						logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Bene Not Found");
						daoResponse.setStatusCode(Acq_Status_Definations.BeneNotFound.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.BeneNotFound.getDescription());
						daoResponse.setBody(null);
					}
				}
			}
			catch (Exception e) 
			{
				logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
				e.printStackTrace();
				daoResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
				daoResponse.setBody(null);
			}
			finally 
			{
				if (daoSession.isOpen() == true || daoSession.isConnected() == true) 
				{
					daoSession.close();
				}
			}
			return daoResponse;
		}
	
		
		@Override
		public DbDto2<Object> doImps(Acq_Bankit_DMT_Neft_Model model) 
		{
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Handler" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			
			Session daoSession = null;
			Transaction dbTransaction = null;
			try {
				
				daoSession = createNewSession();
				Acq_Bankit_Transactions_Utilities dmtUtilities = new Acq_Bankit_Transactions_Utilities();
				Acq_TerminalInfo_Entity terminalInfo = null;
				JSONObject beneDetails = null;
				JSONObject resultJsonImps = null;
				
				try
				{
					//Fetching Terminal Info and System Parameters
					daoSession = createNewSession();
					terminalInfo = (Acq_TerminalInfo_Entity)daoSession.createCriteria(Acq_TerminalInfo_Entity.class)
							.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();

					if (terminalInfo == null || terminalInfo + "" == "") {
						logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Terminal Info Not Found");
						daoResponse.setStatusCode(Acq_Status_Definations.TerminalInfoNotFound.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.TerminalInfoNotFound.getDescription());
						daoResponse.setBody(null);
						return daoResponse;
					}
					logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Terminal Info Found");
				}
				catch (Exception e) 
				{
						logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::"
								+ "Exception in Validating User Details");
						e.printStackTrace();
						daoResponse.setStatusCode(Acq_Status_Definations.ExceptionInMerchantInfo.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.ExceptionInMerchantInfo.getDescription());
						daoResponse.setBody(null);
						return daoResponse;
				} 
				finally 
				{
						if (daoSession.isOpen() == true || daoSession.isConnected())
							daoSession.close();
				}
				
				if(Double.parseDouble(terminalInfo.getRechargeBal())<Double.parseDouble(model.getAmount()))
				{
					logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::"
							+ "Insufficient Recharge Balance");
					daoResponse.setStatusCode(Acq_Status_Definations.InsufficientDMTBalance.getId());
					daoResponse.setStatusMessage(Acq_Status_Definations.InsufficientDMTBalance.getDescription());
					daoResponse.setBody(null);
					return daoResponse;
				}
				else
				{
					logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "DMT Api Begin");
					
					String requestParams = "{\"agentCode\":\""+model.getSessionId()+"\",\n\"customerId\":\""+model.getRecipientId()+"\",\n\"recipientId\":\""+model.getCustomerId()+"\"\n}";
					logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Request Params Ready");
					
					JSONObject resultJson= dmtUtilities.bankitDMTConnector(requestParams,"recipient/fetch","/bankit/tpos/dmt/doImps/v1");
					
					if(resultJson == null || resultJson.isEmpty())
					{
						//Connection Failed
						logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Bankit Response Null");
						daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
						daoResponse.setBody(null);
					}
					else if(resultJson.get("errorCode").equals("00")) 
					{
						logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Beneficiary Validated");
						beneDetails = (JSONObject) resultJson.get("data");
						
						logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "DMT Api Begin");
						
						String clientRefId = "10037"+Acq_Utility_Functions.RandomNumberGenerator().substring(0, Acq_Utility_Functions.RandomNumberGenerator().length() - model.getSessionId().length())+model.getSessionId();
						String requestParamsNeft = "{\"agentCode\":\""+model.getSessionId()+"\",\n\"customerId\":\""+model.getRecipientId()+"\",\n\"recipientId\":\""+model.getCustomerId()+"\",\n\"amount\":\""+model.getAmount()+"\",\n\"clientRefId\":\""+clientRefId+"\"\n}";
						
						logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Neft Request Params Ready");
						
						resultJsonImps= dmtUtilities.bankitDMTConnector(requestParamsNeft,"transact/IMPS/remit","/bankit/tpos/dmt/doImps/v1");
						
						if(resultJsonImps == null || resultJsonImps.isEmpty()) 
						{
							//Connection Failed
							logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Bankit Response Null");
							daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
							daoResponse.setBody(null);
						}
						else if (resultJsonImps.get("errorCode").equals("00") || resultJsonImps.get("errorCode").equals("01"))
						{
							//NEFT success
							
							//Updating Balance in Terminal Info
							logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Updating Balance in Terminal Info");
							Double newBalance = Double.parseDouble(terminalInfo.getRechargeBal()) - Double.parseDouble(model.getAmount());
							terminalInfo.setRechargeBal(new DecimalFormat("#.###").format(Double.parseDouble(newBalance.toString())));
							
							JSONObject impsDetails = (JSONObject) resultJsonImps.get("data");
							Acq_BankitTxns_Model txnModel = new Acq_BankitTxns_Model();
							txnModel.setAmount(model.getAmount());
							txnModel.setSessionId(model.getSessionId());
							txnModel.setClientRefId(""+impsDetails.get("clientRefId"));
							txnModel.setRecipientId(model.getRecipientId());
							txnModel.setStatus(""+resultJsonImps.get("errorCode"));
							txnModel.setTransactionType("IMPS");
							txnModel.setTxnId(""+impsDetails.get("txnId"));
							txnModel.setCustomerId(model.getCustomerId());
							txnModel.setMessage(""+resultJsonImps.get("errorMsg"));
							txnModel.setIfsc(""+beneDetails.get("udf2"));
							txnModel.setMobileNo(""+beneDetails.get("mobileNo"));
							txnModel.setAccountNo(""+beneDetails.get("udf1"));
							txnModel.setRecipientName(""+beneDetails.get("recipientName"));
							txnModel.setBankName(model.getBankName());
							saveBankitTxns(txnModel,"/bankit/tpos/dmt/doImps/v1");
							
							daoSession = createNewSession();
							dbTransaction = daoSession.beginTransaction();
							daoSession.update(terminalInfo);
							dbTransaction.commit();
							logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Balance in Terminal Info Updated");
							
							daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
							daoResponse.setBody(null);
						}
						else 
						{
							//NEFT Failed
							logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Neft Failed, Code - "+resultJsonImps.get("errorCode"));
							daoResponse.setStatusCode(Acq_Status_Definations.NeftFailed.getId());
							daoResponse.setStatusMessage(Acq_Status_Definations.NeftFailed.getDescription());
							daoResponse.setBody(null);
						}
					}
				else
				{
					//Bene Not Found
					logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Bene Not Found");
					daoResponse.setStatusCode(Acq_Status_Definations.BeneNotFound.getId());
					daoResponse.setStatusMessage(Acq_Status_Definations.BeneNotFound.getDescription());
					daoResponse.setBody(null);
				}
			}
		}
		catch (Exception e) 
		{
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
			e.printStackTrace();
			daoResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setBody(null);
		}
		finally 
		{
			if (daoSession.isOpen() == true || daoSession.isConnected() == true) 
			{
				daoSession.close();
			}
		}
		return daoResponse;
	}

		
		@Override
		public DbDto2<Object> getTxnList(Acq_Bankit_DMT_TxnList_Model model) 
		{
			logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			Session daoSession = null;
			JSONObject resultJson = new JSONObject();
			
			try 
			{
				daoSession = createNewSession();

				//Creating criteria to fetch transactions
				Criteria txnListParams = daoSession.createCriteria(Acq_BankIt_Transaction_Entity.class).
						addOrder(Order.desc("id")).add(Restrictions.eq("sessionId",model.getSessionId()));

				SimpleDateFormat dbDateFormat = new SimpleDateFormat("YYYY-MM-dd");
				SimpleDateFormat terminalDateFormat = new SimpleDateFormat("ddMMyyyy");
				String fromDate = null;
				String toDate = null; 

				if(model.getFromDate()!= null && !model.getFromDate().isEmpty())
				{
					fromDate= dbDateFormat.format(terminalDateFormat.parse(model.getFromDate())) + " 00:00:00";
				}
				else
				{
					fromDate= dbDateFormat.format(new Date()) + " 00:00:00";
				}
				logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "fromDate : "+fromDate);

				if(model.getToDate()!= null && !model.getToDate().isEmpty())
				{
					toDate= dbDateFormat.format(terminalDateFormat.parse(model.getToDate())) + " 23:59:59";
				}
				else
				{
					toDate= dbDateFormat.format(new Date()) + " 23:59:59";
				}
				logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "toDate : "+toDate);

				txnListParams.add(Restrictions.between("dateTime",fromDate, toDate));

				
				//Adding conditional parameter of customer Id
				if(model.getCustomerId()!=null && !model.getCustomerId().isEmpty())
				{
					logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "customerId Set");
					txnListParams.add(Restrictions.eq("customerId",model.getCustomerId()));
				}
				
				//Adding conditional parameter of txn Status
				if(model.getTxnStatus().equals("0"))
				{
					txnListParams.add(Restrictions.eq("status","00"));
					logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Txn Status 0 Set");
				}
				else if(model.getTxnStatus().equals("1"))
				{
					txnListParams.add(Restrictions.eq("status","01"));
					logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Txn Status 1 Set");
				}
				else if(model.getTxnStatus().equals("2"))
				{
					txnListParams.add(Restrictions.eq("status","02"));
					logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Txn Status 2 Set");
				}
				

				List<Acq_BankIt_Transaction_Entity> txnList = null;
				try 
				{
					txnList = txnListParams.list();
					logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Transactions Fetched From DB");
				} 
				catch (Exception e) 
				{
					txnList = null;
					logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Exception in Fetching Transactions From DB");
					e.printStackTrace();
				}

				if(txnList != null)
				{
					if(!txnList.isEmpty())
					{
						logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Txn List Not Empty");
						Iterator<Acq_BankIt_Transaction_Entity> txnIterator = txnList.iterator();
						JSONArray allTxnJson =  new JSONArray();
						JSONObject txnDetailsJson = new JSONObject();
						int txnCount = 0;
						logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Txn List Loop Begin");
						while (txnIterator.hasNext()) 
						{
							txnCount++;
							Acq_BankIt_Transaction_Entity singleTxn = (Acq_BankIt_Transaction_Entity) txnIterator.next();
							txnDetailsJson.put("id", singleTxn.getId());
							txnDetailsJson.put("customerId", singleTxn.getCustomerId());
							txnDetailsJson.put("clientRefId", singleTxn.getClientRefId());
							txnDetailsJson.put("ifsc", singleTxn.getIfsc());
							txnDetailsJson.put("accountNo", singleTxn.getAccountNo());
							txnDetailsJson.put("amount", singleTxn.getAmount());
							txnDetailsJson.put("dateTime", singleTxn.getDateTime());
							txnDetailsJson.put("bankName", singleTxn.getBankName());
							txnDetailsJson.put("mobileNo", singleTxn.getMobileNo());
	
							if(singleTxn.getStatus().equals("00"))
								txnDetailsJson.put("status", "Success");	
							else if(singleTxn.getStatus().equals("01"))
								txnDetailsJson.put("status", "Pending");
							else if(singleTxn.getStatus().equals("02"))
								txnDetailsJson.put("status", "Failed");
							else
								txnDetailsJson.put("status", singleTxn.getStatus());
	
							allTxnJson.put(txnDetailsJson);					
						}
						logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Txn List Loop End");
						resultJson.put("transferList", allTxnJson);
						logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Total Txn Found : "+txnCount);
						daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
						daoResponse.setBody(resultJson);
						logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Returning To Handler");
					}
					else
					{
						//txn list empty
						logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Txn List Empty");
						daoResponse.setStatusCode(Acq_Status_Definations.TransactionsNotFound.getId());
						daoResponse.setStatusMessage(Acq_Status_Definations.TransactionsNotFound.getDescription());
						daoResponse.setBody(null);
						logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Returning To Handler");
					}		
				}
				else
				{
					//Null Result From DB
					logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Transaction Search Failed");
					daoResponse.setStatusCode(Acq_Status_Definations.TransactionSearchFailed.getId());
					daoResponse.setStatusMessage(Acq_Status_Definations.TransactionSearchFailed.getDescription());
					daoResponse.setBody(null);
					logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Returning To Handler");
				}
			}
			catch (Exception e) 
			{
				logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
				e.printStackTrace();
				daoResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
				daoResponse.setBody(null);
				logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "DAO" + "::" + "Returning To Handler");
			}			
			finally
			{
				if (daoSession.isOpen() == true || daoSession.isConnected() == true) 
				{
					daoSession.close();
				}		
			}
			return daoResponse;
		}
		
		
		@Override
		public DbDto2<Object> getTxnStatus(Acq_Bankit_DMT_TxnStatus_Model model) 
		{
			logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Handler" + "::" + "Begin");
			
			DbDto2<Object> daoResponse = new DbDto2<Object>();
				
			Acq_Bankit_Transactions_Utilities dmtUtilities = new Acq_Bankit_Transactions_Utilities();
				
			String requestParams ="{\"agentCode\":\""+model.getSessionId()+"\",\n\"clientRefId\":\""+model.getClientRefId()+"\"\n}";
			JSONObject resultJson = dmtUtilities.bankitDMTConnector(requestParams,"transact/searchtxn","/bankit/tpos/dmt/getTxnStatus/v1");
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dtNow = dateFormat.format(new Date());
			
			if(resultJson != null && !resultJson.isEmpty()) 
			{
				if(resultJson.get("errorCode").equals("00"))
				{
					logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Bankit Response - Success");
					JSONObject responseDataParams = (JSONObject) resultJson.get("data");
					JSONObject dataParams = new JSONObject();
					dataParams.put("clientRefId", responseDataParams.get("clientRefId"));
					dataParams.put("txnId", responseDataParams.get("txnId"));
					dataParams.put("transactionDate", responseDataParams.get("transactionDate"));
					dataParams.put("amount", responseDataParams.get("amount").toString());
					
					JSONObject responseJson = new JSONObject();
					responseJson.put("errorCode", resultJson.get("errorCode"));
					responseJson.put("errorMsg", resultJson.get("errorMsg"));
					responseJson.put("data", dataParams);
					daoResponse.setBody(responseJson);
					
				}
				else
				{
					logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Bankit Response - "+resultJson.get("errorCode"));
					daoResponse.setBody(resultJson);
				}
				logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Bankit Response - Saving and Returning");
				daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				saveBankitApiCalls("/bankit/tpos/dmt/getTxnStatus/v1", model.getSessionId(), requestParams, resultJson.toString(), ""+model.getDateTime(),dtNow);
			}
			else
			{
				logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "DAO" + "::" + "Bankit Response Null Saving and Returning");
				saveBankitApiCalls("/bankit/tpos/dmt/getTxnStatus/v1", model.getSessionId(), requestParams, "", ""+model.getDateTime(),dtNow);
				daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setBody(null);
			}
			return daoResponse;
		}
		

		@Override
		public DbDto2<Object> getBankList(Acq_Bankit_DMT_BnkList_Model model)
		{
			logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "DAO" + "::" + "Begin");
			DbDto2<Object> daoResponse = new DbDto2<Object>();
			try
			{
				Acq_Bankit_Transactions_Utilities bankitDMTConnector = new Acq_Bankit_Transactions_Utilities();
				JSONObject resultJson = bankitDMTConnector.bankitDMTConnector("","generic/bankList","/bankit/tpos/dmt/getBankList/v1");
				
				if(resultJson != null && !resultJson.isEmpty()) 
				{
					logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "DAO" + "::" + "Bankit Response Recieved");
					daoResponse.setStatusCode(Acq_Status_Definations.OK.getId());
					daoResponse.setStatusMessage(Acq_Status_Definations.OK.getDescription());
					daoResponse.setBody(resultJson);
				}
				else
				{
					logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "DAO" + "::" + "Bankit Response Null");
					daoResponse.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
					daoResponse.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
					daoResponse.setBody(null);
					
				}
			} 
			catch (Exception e) 
			{
				logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
				e.printStackTrace();
				daoResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
				daoResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
				daoResponse.setBody(null);
			}			
				
			return daoResponse;
		}

		
		public void saveBankitTxns(Acq_BankitTxns_Model model,String callFrom) 
		{
			logger.info(callFrom + "::" + "saveBankitTxns" + "::" + "Begin");
			Session daoSession = null;
			
			try
			{		
				daoSession = createNewSession();
				String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(new Date());
				String merchantId = null;

				try 
				{
					Acq_TerminalUser_Entity terminalInfo = (Acq_TerminalUser_Entity)daoSession.createCriteria(Acq_TerminalUser_Entity.class).add(Restrictions.eq("id", Long.valueOf(model.getSessionId()))).uniqueResult();
					
					if(terminalInfo != null && !terminalInfo.toString().isEmpty())
					{
						Acq_Store_Entity storeInfo = (Acq_Store_Entity)daoSession.createCriteria(Acq_Store_Entity.class)
								.add(Restrictions.eq("id", Long.valueOf(terminalInfo.getOrgId()))).uniqueResult();
						
						if(storeInfo!=null && !storeInfo.toString().isEmpty())
						{
							merchantId = storeInfo.getMerchantId().toString();
						}
					}
				} 
				catch (Exception e) 
				{
					logger.info(callFrom + "::" + "saveBankitTxns" + "::" + "Exception in Finding Merchant Details");
					e.printStackTrace();
				}
				
				if(merchantId != null && !merchantId.isEmpty())
				{
						Acq_BankIt_Transaction_Entity txnDetails = new Acq_BankIt_Transaction_Entity();
						txnDetails.setMerchantId(merchantId);
						txnDetails.setSessionId(model.getSessionId());
						txnDetails.setAccountNo(model.getAccountNo());
						txnDetails.setAmount(model.getAmount());
						txnDetails.setClientRefId(model.getClientRefId());
						txnDetails.setCustomerId(model.getCustomerId());
						txnDetails.setIfsc(model.getIfsc());
						txnDetails.setRecipientId(model.getRecipientId());
						txnDetails.setRecipientName(model.getRecipientName());
						txnDetails.setStatus(model.getStatus());
						txnDetails.setTransactionType(model.getTransactionType());
						txnDetails.setTxnId(model.getTxnId());
						txnDetails.setDateTime(dateTime);
						txnDetails.setMessage(model.getMessage());
						txnDetails.setBankName(model.getBankName());
						txnDetails.setMobileNo(model.getMobileNo());
						
						Transaction dbTransaction = daoSession.beginTransaction();
						daoSession.save(txnDetails);
						dbTransaction.commit();	
				}
				else
				{
					logger.info(callFrom + "::" + "saveBankitTxns" + "::" + "Merchant Details Not Found");
				}
			}
			catch (Exception e) 
			{
				logger.info(callFrom + "::" + "saveBankitTxns" + "::" + "Exception in Saving Transactions");
				e.printStackTrace();
			}
			finally 
			{
				if (daoSession.isOpen() == true || daoSession.isConnected() == true) 
				{
					daoSession.close();
				}
			}
		}
	
		
		public boolean saveBankitApiCalls(String serviceName,String sessionId,String request, String response, String requestDateTime,String responseDateTime) {
			logger.info(serviceName + "::" + "saveBankitApiCallDetails" + "::" + "Begin");
			Session daoSession = null;
			try
			{		
				daoSession = createNewSession();
				

				Acq_TerminalUser_Entity storeInfo = (Acq_TerminalUser_Entity)daoSession.createCriteria(Acq_TerminalUser_Entity.class).add(Restrictions.eq("id", Long.valueOf(sessionId))).uniqueResult();

				if(storeInfo != null)
				{	
					logger.info(serviceName + "::" + "saveBankitApiCallDetails" + "::" + "Store Details Found");
					Acq_Store_Entity merchantInfo = (Acq_Store_Entity)daoSession.createCriteria(Acq_Store_Entity.class).add(Restrictions.eq("id", Long.valueOf(storeInfo.getOrgId()))).uniqueResult();
					
					if(merchantInfo!=null )
					{
						logger.info(serviceName + "::" + "saveBankitApiCallDetails" + "::" + "Merchant Details Found");
						Transaction dbTransaction = daoSession.beginTransaction();;
						Acq_BankIt_Persist_Entity apiCallDetails = new Acq_BankIt_Persist_Entity();
						apiCallDetails.setMerchantId(merchantInfo.getMerchantId().toString());
						apiCallDetails.setTerminalId(sessionId);
						apiCallDetails.setRequest(request);
						apiCallDetails.setResponse(response);
						apiCallDetails.setRequestTime(requestDateTime);
						apiCallDetails.setServiceName(serviceName);
						apiCallDetails.setResponseTime(responseDateTime);
						daoSession.save(apiCallDetails);
						dbTransaction.commit();
						logger.info(serviceName + "::" + "saveBankitApiCallDetails" + "::" + "Db Updated");
						return true;
					}
					else
					{
						logger.info(serviceName + "::" + "saveBankitApiCallDetails" + "::" + "Merchant Details Not Found");
						return false;
					}
				}
				else
				{
					logger.info(serviceName + "::" + "saveBankitApiCallDetails" + "::" + "Store Details Not Found");
					return false;
				}
			}
			catch(Exception e)
			{		
				logger.info(serviceName + "::" + "saveBankitApiCallDetails" + "::" + "Error To Save Api Params");
				return false;

			}
			finally
			{
				if (daoSession.isOpen() == true || daoSession.isConnected() == true) {
					daoSession.close();
					logger.info(serviceName + "::" + "saveBankitApiCallDetails" + "::" + "Session Closed");
				}		
			}			
		}
		
		
	@Override
		public DbDto2<Object> cardLoad(Acq_Bankit_PrepaidLoad_Model model) {
			logger.info("request in card load Request dao");
			DbDto2<Object> response = new DbDto2<Object>();
			JSONObject resultJson = new JSONObject();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Acq_TerminalInfo_Entity terminalInfo = null;
			Session daoSession = createNewSession();
			Transaction dbTransaction = null;
			try {
				
				terminalInfo = (Acq_TerminalInfo_Entity)daoSession.createCriteria(Acq_TerminalInfo_Entity.class).add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				if (terminalInfo == null || terminalInfo + "" == "") {
					logger.info("/bankit/tpos/dmt/CardLoad/v1" + "::" + "DAO" + "::" + "Terminal Info Not Found");
					response.setStatusCode(Acq_Status_Definations.TerminalInfoNotFound.getId());
					response.setStatusMessage(Acq_Status_Definations.TerminalInfoNotFound.getDescription());
					response.setBody(null);
					return response;
				}

				if(Double.parseDouble(terminalInfo.getRechargeBal())<Double.parseDouble(model.getAmount()))
				{
					logger.info("/bankit/tpos/dmt/CardLoad/v1" + "::" + "DAO" + "::"+ "Insufficient Recharge Balance");
					response.setStatusCode(Acq_Status_Definations.InsufficientDMTBalance.getId());
					response.setStatusMessage(Acq_Status_Definations.InsufficientDMTBalance.getDescription());
					response.setBody(null);
					return response;
				}
				else
				{
					logger.info("/bankit/tpos/dmt/CardLoad/v1" + "::" + "DAO" + "::" + "DMT Api Begin");
					String clientRefId = Acq_Utility_Functions.RandomNumberGenerator().substring(0, Acq_Utility_Functions.RandomNumberGenerator().length() - model.getSessionId().length())+model.getSessionId();
					model.setTransactionId(clientRefId);
					String request ="{\"cardNumber\":\""+model.getCardNumber()+"\",\n\"amount\":\""+model.getAmount()+"\",\n\"mobile\":\""+model.getMobile()+"\",\n\"transactionId\":\""+model.getTransactionId()+"\",\n\"InstitutionKey\":\""+"8860180472AG21961D4019"+"\",\n\"AgentID\":\""+"21961"+"\"\n}";					
					Acq_Bankit_Transactions_Utilities moneyApis = new Acq_Bankit_Transactions_Utilities();
					resultJson = moneyApis.callPrepaidApi(request,"InstitutionLoadCard","/tpos/dmt/doCardLoad/v1");
					if(resultJson == null || resultJson.isEmpty())
					{
						//Connection Failed
						logger.info("/bankit/tpos/dmt/CardLoad/v1" + "::" + "DAO" + "::" + "Bankit Response Null");
						response.setStatusCode(Acq_Status_Definations.SwitchConnectionFailed.getId());
						response.setStatusMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
						response.setBody(null);
					}
					else if(resultJson.get("errorCode").equals("0")) 
					{
							//CardLoad success
							//Updating Balance in Terminal Info
							logger.info("/bankit/tpos/dmt/CardLoad/v1" + "::" + "DAO" + "::" + "Updating Balance in Terminal Info");
							Double newBalance = Double.parseDouble(terminalInfo.getRechargeBal()) - Double.parseDouble(model.getAmount());
							terminalInfo.setRechargeBal(new DecimalFormat("#.###").format(Double.parseDouble(newBalance.toString())));							
							Acq_BankitTxns_Model txnModel = new Acq_BankitTxns_Model();
							txnModel.setAmount(model.getAmount());
							txnModel.setSessionId(model.getSessionId());
							txnModel.setClientRefId(clientRefId);
							txnModel.setRecipientId("NA");
							txnModel.setStatus(""+resultJson.get("errorCode"));
							txnModel.setTransactionType("CardLoad");
							txnModel.setTxnId(""+resultJson.get("transactionId"));
							txnModel.setCustomerId(model.getCustomerId());
							txnModel.setMessage(""+resultJson.get("message"));
							txnModel.setIfsc("NA");
							txnModel.setMobileNo(model.getMobile());
							txnModel.setAccountNo(model.getCardNumber());
							txnModel.setRecipientName("NA");
							txnModel.setBankName("NA");
							saveBankitTxns(txnModel,"/tpos/dmt/doCardLoad/v1");							
							daoSession = createNewSession();
							dbTransaction = daoSession.beginTransaction();
							daoSession.update(terminalInfo);
							dbTransaction.commit();
							logger.info("/bankit/tpos/dmt/CardLoad/v1" + "::" + "DAO" + "::" + "Balance in Terminal Info Updated");
							
							response.setStatusCode(Acq_Status_Definations.OK.getId());
							response.setStatusMessage(Acq_Status_Definations.OK.getDescription());
							response.setBody(null);
						}
						else 
						{
							logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "DAO" + "::" + "Neft Failed, Code - ");
							response.setStatusCode(Acq_Status_Definations.CardLoadFailed.getId());
							response.setStatusMessage(Acq_Status_Definations.CardLoadFailed.getDescription());
							response.setBody(null);
						}
			//return response;
		}}catch (Exception e) {
			response.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			response.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			logger.info("Error to card load Status Request Dao " + e);
		}
			return response;			
		
		}	
	
	
	
	
		@Override
		public DbDto2<Object> cardLoadStatus(Acq_Bankit_PrepaidLoad_Model model) {
			logger.info("request in card load Request dao");
			DbDto2<Object> response = new DbDto2<Object>();
			JSONObject json = new JSONObject();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Acq_Bankit_Transactions_Utilities moneyApis = new Acq_Bankit_Transactions_Utilities();
				String request ="{\"transactionId\":\""+model.getTransactionId()+"\",\n\"InstitutionKey\":\""+"8860180472AG21961D4019"+"\",\n\"AgentID\":\""+"21961"+"\"\n}";
				json = moneyApis.callPrepaidApi(request,"InstitutionLoadCardStatus","/dmt/cardLoadStatus/version1");
				Date date = new Date();
				String dateTime = format.format(date);
				try{
					System.out.println("wedwed");
					saveBankitApiCalls("CardLoadStatus", model.getSessionId(), request, json.toString(), ""+model.getDateTime(),dateTime);
					}catch(Exception e){
						
					}
				response.setStatusCode(Acq_Status_Definations.OK.getId());
				response.setStatusMessage(Acq_Status_Definations.OK.getDescription());
				response.setBody(json);
				logger.info("Fetch dmt card load Status Succesfuly Done::");	
				return response;
				//return response;
			 }catch (Exception e) {
					response.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
					response.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
					logger.info("Error to card load Status Request Dao " + e);
				}			
				
		/*finally{
				session.close();			
			}*/
			return response;
		}
		
		
		@Override
		public DbDto2<Object> getCardBal(Acq_Bankit_PrepaidLoad_Model model) {
		logger.info("request in get CardBal Request dao");
		DbDto2<Object> response = new DbDto2<Object>();
		JSONObject json = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Acq_Bankit_Transactions_Utilities moneyApis = new Acq_Bankit_Transactions_Utilities();
			String request ="{\"cardNumber\":\""+model.getCardNumber()+"\",\n\"mobile\":\""+model.getMobile()+"\",\n\"InstitutionKey\":\""+"8860180472AG21961D4019"+"\",\n\"AgentID\":\""+"21961"+"\"\n}";
			json = moneyApis.callPrepaidApi(request,"InstitutionCardBalance","/dmt/getCardBal/version1");
			Date date = new Date();
			String dateTime = format.format(date);
			try{
				System.out.println("wedwed");
				saveBankitApiCalls("CardLoadBal", model.getSessionId(), request, json.toString(), ""+model.getDateTime(),dateTime);
				}catch(Exception e){
					
				}
			response.setStatusCode(Acq_Status_Definations.OK.getId());
			response.setStatusMessage(Acq_Status_Definations.OK.getDescription());
			response.setBody(json);
			logger.info("Fetch dmt get CardBal Succesfuly Done::"+json);	
			return response;
			//return response;
		 }catch (Exception e) {
				response.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
				response.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
				logger.info("Error to get CardBal Request Dao " + e);
			}			
			
	/*finally{
			session.close();			
		}*/
		return response;
	}
	
	
	
	
	
	
	
	
	
}
