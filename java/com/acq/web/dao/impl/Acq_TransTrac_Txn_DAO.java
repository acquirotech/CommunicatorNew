package com.acq.web.dao.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.acq.Acq_TransTrac_Constants;
import com.acq.Acq_Status_Definations;
import com.acq.Acq_Utility_Functions;
import com.acq.users.entity.Acq_Card_Bin_Details;
import com.acq.users.entity.Acq_Card_Details;
import com.acq.users.entity.Acq_KeyExchange_Entity;
import com.acq.users.entity.Acq_Terminal_Info;
import com.acq.users.entity.Acq_TerminalInfo_Entity;
import com.acq.users.entity.Acq_Merchant_Entity;
import com.acq.users.entity.Acq_CardTransaction_Entity;
import com.acq.users.entity.Acq_TTIso_Entity;
import com.acq.users.entity.Acq_Store_Entity;
import com.acq.users.entity.Acq_RiskRules_Entity;
import com.acq.users.entity.Acq_SysParams_Entity;
import com.acq.users.entity.Acq_TUser_Entity;
import com.acq.web.controller.model.Acq_TransTrac_GetTMK_Model;
import com.acq.web.controller.model.Acq_TransTrac_Reversal_Model;
import com.acq.web.controller.model.Acq_PurchaseAck_Model;
import com.acq.web.controller.model.Acq_TransTrac_Purchase_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxnList_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxn_Model;
import com.acq.web.dao.Acq_Dao;
import com.acq.web.dao.Acq_TransTrac_Txn_DAO_Inf;
import com.acq.web.dto.impl.DbDto;

@Repository
public class Acq_TransTrac_Txn_DAO extends Acq_Dao implements Acq_TransTrac_Txn_DAO_Inf {

	final static Logger logger = Logger.getLogger(Acq_TransTrac_Txn_DAO.class);
	
	// DAO For Get DUKPT Service
	@Override
	public DbDto<Object> getTMK(Acq_TransTrac_GetTMK_Model model) {
		logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Begin");

		DbDto<Object> daoResponse = new DbDto<Object>();
		GenericPackager isoPackager;
		Session daoSession = null;

		try {
			try {
				daoSession = createNewSession();
				Acq_Terminal_Info acqDevice = (Acq_Terminal_Info) daoSession.createCriteria(Acq_Terminal_Info.class)
						.add(Restrictions.eq("deviceSerialNo", model.getDeviceId()))
						.add(Restrictions.eq("userId", model.getSessionId())).uniqueResult();


				if (acqDevice == null) {
					logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Terminal Not Found");
					daoResponse.setStatus(Acq_Status_Definations.SerialNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.SerialNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				daoSession.close();
			} catch (Exception e) {
				logger.info(
						"/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Exception in Finding Terminal Details");
				e.printStackTrace();
				daoSession.close();
				daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
				daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			}

			isoPackager = new GenericPackager("keyExchange.xml");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss"); // -----------------
			String transmissionDatetime = dateFormatGmt.format(new Date());

			ISOMsg isoObj = new ISOMsg();
			isoObj.setPackager(isoPackager);
			isoObj.setMTI(Acq_TransTrac_Constants.KeyExchangeMTI.getId());
			isoObj.set(7, transmissionDatetime); // -----------------
			isoObj.set(11, model.getStan());
			isoObj.set(37, Acq_Utility_Functions.generateRRN(model.getStan(), transmissionDatetime));
			isoObj.set(61, model.getDeviceId());
			isoObj.set(70, "181");
			logISOData(isoObj, "/TransTrac/tpos/getTMK/v1");

			byte[] isoBytes = isoObj.pack();
			String portResponse = null;

			portResponse = Acq_TransTrac_Port_Connector.portConnectorForKeyExchange(new String(isoBytes), model.getUat(),
					"/TransTrac/tpos/getTMK/v1", 1);

			if (portResponse.isEmpty() || portResponse.equals("null")) { // Blank Response From Switch
				daoResponse.setStatus(Acq_Status_Definations.NoResponseFromSwitch.getId());
				daoResponse.setMessage(Acq_Status_Definations.NoResponseFromSwitch.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} else if (portResponse.equals("Connection Timeout")) { // Connection Timed Out
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionTimeOut.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionTimeOut.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} else if (portResponse.equalsIgnoreCase("Connection Failed")) { // Connection Failed Due To Unexpected Error
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			}

			String lengthRemoved = portResponse.substring(2); // Remove First 2 Characters
			String firstBitmap = lengthRemoved.substring(4, 20); // Extracting Bitmap
			String bitmap1Binary = convertHexToBinary(firstBitmap);
			String secondBitmap = "";
			if (bitmap1Binary.startsWith("1")) { // Secondary Bitmap Present
				secondBitmap = lengthRemoved.substring(4, 36); // Extracting Secondary Bitmap
				secondBitmap = convertHexToBinary(secondBitmap);
			}
			logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "secondBitmap" + "::" + secondBitmap);

			try { // Unpacking Response ISO

				GenericPackager isoUnpackager = new GenericPackager("key_response.xml");
				ISOMsg isoResultObj = new ISOMsg();
				isoResultObj.setPackager(isoUnpackager);
				isoResultObj.unpack(lengthRemoved.getBytes()); // NEED TO CHECK
				logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Printing Response ISO");

				logISOData(isoResultObj, "/TransTrac/tpos/getTMK/v1");
				JSONObject IsoResultJson = new JSONObject();
				IsoResultJson = convertIsoToJson(isoResultObj, "KeyEx");   //Handle Empty Json Response
				daoResponse.setMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.OK.getId());
				daoResponse.setResult(IsoResultJson);
			} catch (Exception e) {
				logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Error In Unpacking Response ISO");
				e.printStackTrace();
				daoResponse.setMessage(Acq_Status_Definations.InvalidResponseFromSwitch.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.InvalidResponseFromSwitch.getId());
				daoResponse.setResult(null);
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
			e.printStackTrace();
			daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setResult(null);
		}
		logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Returning To Handler");
		return daoResponse;
	}

	// DAO For Send TMK Acknowledgement Service
	@Override
	public DbDto<Object> sendTMKACK(Acq_TransTrac_GetTMK_Model model) {

		logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "DAO" + "::" + "Begin");

		DbDto<Object> daoResponse = new DbDto<Object>();
		GenericPackager isoPackager;

		if (model.getDe44() == null) {
			daoResponse.setMessage(Acq_Status_Definations.InvalidDe44.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.InvalidDe44.getId());
			daoResponse.setResult(null);
			return daoResponse;
		}

		try {
			isoPackager = new GenericPackager("keyExchange.xml");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			String transmissionDatetime = dateFormatGmt.format(new Date());

			ISOMsg isoObj = new ISOMsg();
			isoObj.setPackager(isoPackager);
			isoObj.setMTI(Acq_TransTrac_Constants.KeyExchangeMTI.getId());
			isoObj.set(7, transmissionDatetime);
			isoObj.set(11, model.getStan());
			isoObj.set(37, Acq_Utility_Functions.generateRRN(model.getStan(), transmissionDatetime));
			isoObj.set(44, model.getDe44());
			isoObj.set(61, model.getDeviceId());
			isoObj.set(70, "183");
			logISOData(isoObj, "/TransTrac/tpos/sendTMKACK/v1");

			byte[] isoBytes = isoObj.pack();
			String portResponse = null;

			portResponse = Acq_TransTrac_Port_Connector.portConnectorForKeyExchange(new String(isoBytes), model.getUat(),
					"/TransTrac/tpos/sendTMKACK/v1", 1);
			if (portResponse.isEmpty() || portResponse.equals("null")) { // Blank Response From Switch
				daoResponse.setStatus(Acq_Status_Definations.NoResponseFromSwitch.getId());
				daoResponse.setMessage(Acq_Status_Definations.NoResponseFromSwitch.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} else if (portResponse.equals("Connection Timeout")) { // Connection Timed Out
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionTimeOut.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionTimeOut.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} else if (portResponse.equalsIgnoreCase("Connection Failed")) { // Connection Failed Due To Unexpected Error
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			}

			String lengthRemoved = portResponse.substring(2); // Remove First 2 Characters
			String firstBitmap = lengthRemoved.substring(4, 20); // Extracting Bitmap
			String bitmap1Binary = convertHexToBinary(firstBitmap);
			String secondBitmap = "";
			if (bitmap1Binary.startsWith("1")) { // Secondary Bitmap Present
				secondBitmap = lengthRemoved.substring(4, 36); // Extracting Secondary Bitmap
				secondBitmap = convertHexToBinary(secondBitmap);
			}
			logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "DAO" + "::" + "secondBitmap" + "::" + secondBitmap);

			try { // Unpacking Response ISO

				GenericPackager isoUnpackager = new GenericPackager("key_response.xml");
				ISOMsg isoResultObj = new ISOMsg();
				isoResultObj.setPackager(isoUnpackager);
				isoResultObj.unpack(lengthRemoved.getBytes()); // NEED TO CHECK
				logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "DAO" + "::" + "Printing Response ISO");

				logISOData(isoResultObj, "/TransTrac/tpos/sendTMKACK/v1");
				JSONObject IsoResultJson = new JSONObject();
				IsoResultJson = convertIsoToJson(isoResultObj, "KeyAck"); //Handle Empty Json Response
				daoResponse.setMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.OK.getId());
				daoResponse.setResult(IsoResultJson);
			} catch (Exception e) {
				logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "DAO" + "::" + "Error In Unpacking Response ISO");
				e.printStackTrace();
				daoResponse.setMessage(Acq_Status_Definations.InvalidResponseFromSwitch.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.InvalidResponseFromSwitch.getId());
				daoResponse.setResult(null);
			}

		} catch (Exception e) {
			logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
			e.printStackTrace();
			daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setResult(null);
		}
		logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Returning To Handler");
		return daoResponse;
	}
	
	// DAO For Get DUKPT Service
	@Override
	public DbDto<Object> getDUKPT(Acq_TransTrac_GetTMK_Model model) {

		logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Begin");

		DbDto<Object> daoResponse = new DbDto<Object>();
		GenericPackager isoPackager;

		Acq_Merchant_Entity merchantInfo = null;
		Session daoSession = null;

		try { // Try to Fetch Merchant Info

			logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Fetching Merchant Info Begin");

			daoSession = createNewSession();

			Acq_TUser_Entity tUserInfo = (Acq_TUser_Entity) daoSession.createCriteria(Acq_TUser_Entity.class)
					.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
			if (tUserInfo == null) {
				daoSession.close();
				daoResponse.setStatus(Acq_Status_Definations.TerminalUserNotFound.getId());
				daoResponse.setMessage(Acq_Status_Definations.TerminalUserNotFound.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			}

			Acq_Store_Entity storeInfo = (Acq_Store_Entity) daoSession.createCriteria(Acq_Store_Entity.class)
					.add(Restrictions.eq("id", Long.valueOf(tUserInfo.getOrgId()))).uniqueResult();
			if (storeInfo == null) {
				daoSession.close();
				daoResponse.setStatus(Acq_Status_Definations.TerminalStoreNotFound.getId());
				daoResponse.setMessage(Acq_Status_Definations.TerminalStoreNotFound.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			}

			merchantInfo = (Acq_Merchant_Entity) daoSession.createCriteria(Acq_Merchant_Entity.class)
					.add(Restrictions.eq("merchantId", "" + storeInfo.getMerchantId())).uniqueResult();
			if (merchantInfo == null) {
				daoSession.close();
				daoResponse.setStatus(Acq_Status_Definations.TerminalMerchantNotFound.getId());
				daoResponse.setMessage(Acq_Status_Definations.TerminalMerchantNotFound.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			}

		} catch (Exception e) {

			logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Exception In Fetching Merchant Info");
			e.printStackTrace();
			daoSession.close();
			daoResponse.setStatus(Acq_Status_Definations.ExceptionInMerchantInfo.getId());
			daoResponse.setMessage(Acq_Status_Definations.ExceptionInMerchantInfo.getDescription());
			daoResponse.setResult(null);
			return daoResponse;

		} finally {
			daoSession.close();
		}

		logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Fetching Merchant Info Success");

		try { //Preparing and Sending ISO Message 

			logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Preparing ISO Message");
			isoPackager = new GenericPackager("keyExchange.xml");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			String transmissionDatetime = dateFormatGmt.format(new Date());

			ISOMsg isoObj = new ISOMsg();
			isoObj.setPackager(isoPackager);
			isoObj.setMTI(Acq_TransTrac_Constants.KeyExchangeMTI.getId());
			isoObj.set(7, transmissionDatetime);
			isoObj.set(11, model.getStan());
			isoObj.set(37, Acq_Utility_Functions.generateRRN(model.getStan(), transmissionDatetime));
			isoObj.set(42, merchantInfo.getMerchantTID());
			isoObj.set(61, model.getDeviceId());
			isoObj.set(70, "191");
			logISOData(isoObj, "/TransTrac/tpos/getDUKPT/v1");

			byte[] isoBytes = isoObj.pack();
			String portResponse = null;

			portResponse = Acq_TransTrac_Port_Connector.portConnectorForKeyExchange(new String(isoBytes), model.getUat(),"/TransTrac/tpos/getDUKPT/v1",1);

			if (portResponse.isEmpty() || portResponse.equals("null")) { // Blank Response From Switch
				daoResponse.setStatus(Acq_Status_Definations.NoResponseFromSwitch.getId());
				daoResponse.setMessage(Acq_Status_Definations.NoResponseFromSwitch.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} else if (portResponse.equals("Connection Timeout")) { // Connection Timed Out
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionTimeOut.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionTimeOut.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} else if (portResponse.equalsIgnoreCase("Connection Failed")) { // Connection Failed Due To Unexpected Error
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			}

			String lengthRemoved = portResponse.substring(2); // Remove First 2 Characters
			String firstBitmap = lengthRemoved.substring(4, 20); // Extracting Bitmap
			String bitmap1Binary = convertHexToBinary(firstBitmap);
			String secondBitmap = "";
			if (bitmap1Binary.startsWith("1")) { // Secondary Bitmap Present
				secondBitmap = lengthRemoved.substring(4, 36); // Extracting Secondary Bitmap
				secondBitmap = convertHexToBinary(secondBitmap);
			}
			logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "secondBitmap" + "::" + secondBitmap);

			try { // Unpacking Response ISO

				GenericPackager isoUnpackager = new GenericPackager("key_response.xml");
				ISOMsg isoResultObj = new ISOMsg();
				isoResultObj.setPackager(isoUnpackager);
				isoResultObj.unpack(lengthRemoved.getBytes()); // NEED TO CHECK
				logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Printing Response ISO");
				logISOData(isoResultObj, "/TransTrac/tpos/getDUKPT/v1");

				JSONObject IsoResultJson = new JSONObject();				
				IsoResultJson = convertIsoToJson(isoResultObj, "Dukpt"); //Handle Empty Json Response

				if (IsoResultJson.containsKey("DE63")) {
					logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "DE63 Present");
					try { //Saving DE63 data to Database

						String[] merchantDataArray = IsoResultJson.get("DE63").toString().split("\\|");  // Merchant Data from key exchange response
						String mcc = merchantDataArray[4];
						String acquirerId = merchantDataArray[3];

						daoSession = createNewSession();

						Acq_KeyExchange_Entity dukptDetails = (Acq_KeyExchange_Entity) daoSession.createCriteria(Acq_KeyExchange_Entity.class)
								.add(Restrictions.eq("tid", model.getDeviceId()))
								.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();

						if (dukptDetails == null) { // New Entry To Be Done
							logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "New Merchant Data Entry Begin");
							Acq_KeyExchange_Entity newDukptDetails = new Acq_KeyExchange_Entity();
							dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							newDukptDetails.setUserId(Long.valueOf(model.getSessionId()));
							newDukptDetails.setDateTime(dateFormatGmt.format(new Date()));
							newDukptDetails.setTid(model.getDeviceId());
							newDukptDetails.setMcc(mcc);
							newDukptDetails.setAcquirerId(acquirerId);
							newDukptDetails.setStatus("True");
							daoSession.save(newDukptDetails);
							daoSession.close();
							logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "New Merchant Data Entry End");

						} else {// Update Existing Entry
							logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Update Merchant Data Begin");
							dukptDetails.setMcc(mcc);
							dukptDetails.setAcquirerId(acquirerId);
							dukptDetails.setStatus("True");
							daoSession.update(dukptDetails);
							daoSession.close();
							logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Update Merchant Data Begin");
						}
					} catch (Exception e) {
						logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Error Saving Merchant Data");
						e.printStackTrace();
						daoResponse.setMessage(Acq_Status_Definations.DataPersistanceError.getDescription());
						daoResponse.setStatus(Acq_Status_Definations.DataPersistanceError.getId());
						daoResponse.setResult(null);
					} finally {
						daoSession.close();
					}
				}
				else{
					logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "DE63 Missing");
					daoResponse.setMessage(Acq_Status_Definations.DataElementMissing.getDescription());
					daoResponse.setStatus(Acq_Status_Definations.DataElementMissing.getId());
					daoResponse.setResult(null);
				}

				daoResponse.setMessage(Acq_Status_Definations.OK.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.OK.getId());
				daoResponse.setResult(IsoResultJson);
			} catch (Exception e) {
				logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Error In Unpacking Response ISO");
				e.printStackTrace();
				daoResponse.setMessage(Acq_Status_Definations.InvalidResponseFromSwitch.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.InvalidResponseFromSwitch.getId());
				daoResponse.setResult(null);
			}	

		} catch (Exception e) {
			logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
			e.printStackTrace();
			daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setResult(null);
		}
		logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "DAO" + "::" + "Returning to Handler Successfully");
		return daoResponse;
	}

	// DAO For Send DUKPT Service
	@Override
	public DbDto<Object> sendDUKPTACK(Acq_TransTrac_GetTMK_Model model) {
		
		logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "Begin");
		
		DbDto<Object> daoResponse = new DbDto<Object>();
		GenericPackager isoPackager;
		
		try { //Preparing and Sending ISO Message 
			
			logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "Preparing ISO Message");
			isoPackager = new GenericPackager("keyExchange.xml");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			String transmissionDatetime = dateFormatGmt.format(new Date());
			System.out.println("Date:"+transmissionDatetime);
			ISOMsg isoObj = new ISOMsg();
			isoObj.setPackager(isoPackager);
			isoObj.setMTI(Acq_TransTrac_Constants.KeyExchangeMTI.getId());
			isoObj.set(7, transmissionDatetime);
			isoObj.set(11, model.getStan());
			isoObj.set(37, Acq_Utility_Functions.generateRRN(model.getStan(), transmissionDatetime));
			isoObj.set(44, model.getDe44());
			isoObj.set(61, model.getDeviceId());
			isoObj.set(70, "191");
			logISOData(isoObj, "/TransTrac/tpos/sendDUKPTACK/v1");
			System.out.println("121211111111111111111111111111");
			byte[] isoBytes = isoObj.pack();
			System.out.println("33333333333333");
			String portResponse = null;
			System.out.println("wwwwwwwwwwwwwwwwwwww");
			portResponse = Acq_TransTrac_Port_Connector.portConnectorForKeyExchange(new String(isoBytes), model.getUat(), "/TransTrac/tpos/sendDUKPTACK/v1",1);
				if (portResponse.isEmpty() || portResponse.equals("null")) { // Blank Response From Switch
					daoResponse.setStatus(Acq_Status_Definations.NoResponseFromSwitch.getId());
					daoResponse.setMessage(Acq_Status_Definations.NoResponseFromSwitch.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				} else if (portResponse.equals("Connection Timeout")) { // Connection Timed Out
					daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionTimeOut.getId());
					daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionTimeOut.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				} else if (portResponse.equalsIgnoreCase("Connection Failed")) { // Connection Failed Due To Unexpected Error
					daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionFailed.getId());
					daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				System.out.println("portResponse::"+portResponse);
				String lengthRemoved = portResponse.substring(2); // Remove First 2 Characters i.e. length
				String firstBitmap = lengthRemoved.substring(4, 20); // Excluding MTI, Extracting Bitmap
				String bitmap1Binary = convertHexToBinary(firstBitmap); //Converting Bitmap to Binary
				String secondBitmap = "";
				if (bitmap1Binary.startsWith("1")) { // Secondary Bitmap Present
					secondBitmap = lengthRemoved.substring(4, 36); // Extracting Secondary Bitmap
					secondBitmap = convertHexToBinary(secondBitmap); //Converting Bitmap to Binary
				}
				logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "secondBitmap" + "::" + secondBitmap);
			
			try {  // Unpacking Response ISO

				GenericPackager isoUnpackager = new GenericPackager("key_response.xml");
				ISOMsg isoResultObj = new ISOMsg();
				isoResultObj.setPackager(isoUnpackager);
				isoResultObj.unpack(lengthRemoved.getBytes());				
				
				logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "Printing Response ISO");
				try{
					System.out.println("eeeeeeeeeeeeee:");
				logISOData(isoResultObj, "/TransTrac/tpos/getDUKPT/v1");
				System.out.println("ddddddddddddddddddddddddddddd:");
				}catch(Exception e){
					System.out.println("sefffffffff:"+e);
				}
				JSONObject IsoResultJson = new JSONObject();				
				IsoResultJson = convertIsoToJson(isoResultObj, "DukptAck"); //Handle Empty Json Response
				System.out.println("qqqqqqqqqqqqqqqqqqqqqqq:"+IsoResultJson);
				if(IsoResultJson ==null || IsoResultJson.isEmpty()){
					logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "Result Json is Empty");
					daoResponse.setMessage(Acq_Status_Definations.NoResponseFromSwitch.getDescription());
					daoResponse.setStatus(Acq_Status_Definations.NoResponseFromSwitch.getId());
					daoResponse.setResult(null);
				}
				else if (IsoResultJson.containsKey("DE39") && IsoResultJson.get("DE39").toString().equals("00")) {
					logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "DE39 Present");
					
					Session daoSession = null;
					Transaction dbTransaction = null;
					try {
						daoSession = createNewSession();
						Acq_KeyExchange_Entity dukptDetails = (Acq_KeyExchange_Entity) daoSession
								.createCriteria(Acq_KeyExchange_Entity.class)
								.add(Restrictions.eq("tid", model.getDeviceId()))
								.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
						
						if (dukptDetails == null) {
							logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "DUKPT Details Not Present");
							daoResponse.setMessage(Acq_Status_Definations.DataElementMissing.getDescription());
							daoResponse.setStatus(Acq_Status_Definations.DataElementMissing.getId());
							daoResponse.setResult(null);
						
						} else {
							logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "DUKPT Details Present");
							dukptDetails.setStatus("False");   //Key Exchange Not Needed in Future
							dbTransaction = daoSession.beginTransaction();
							daoSession.update(dukptDetails);
							dbTransaction.commit();
							daoResponse.setMessage(Acq_Status_Definations.OK.getDescription());
							daoResponse.setStatus(Acq_Status_Definations.OK.getId());
							daoResponse.setResult(IsoResultJson);
							logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "DUKPT Status Updated");
						}
					} catch (Exception e) {
						logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "Error Saving Merchant Data");
						e.printStackTrace();
						if(dbTransaction.isActive()){dbTransaction.rollback();}
						daoResponse.setMessage(Acq_Status_Definations.DataPersistanceError.getDescription());
						daoResponse.setStatus(Acq_Status_Definations.DataPersistanceError.getId());
						daoResponse.setResult(null);
						
					} finally {
						daoSession.close();
					}
				}

				
			} catch (Exception e) {
				logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "Error In Unpacking Response ISO");
				e.printStackTrace();
				daoResponse.setMessage(Acq_Status_Definations.InvalidResponseFromSwitch.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.InvalidResponseFromSwitch.getId());
				daoResponse.setResult(null);
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "Unexpected Server Error+"+e);
			daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setResult(null);
		}
		logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "DAO" + "::" + "Returning to Handler Successfully");
		return daoResponse;
	}
	
	// DAO For Purchase Service
	@Override
	public DbDto<Object> doPurchase(Acq_TransTrac_Purchase_Model model) {

		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Begin");

		DbDto<Object> daoResponse = new DbDto<Object>();
		int txnId = 0;
		Date txnDate = new Date();
		Session daoSession = null;
		Transaction dbTransaction = null;

		try {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Saving Transaction Begin");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			String transmissionDatetime = dateFormatGmt.format(txnDate);
			
			SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
			model.setDe12(timeFormat.format(txnDate)); // Local Time
			
			String MMdd = transmissionDatetime.substring(0, 4);
			model.setDe13(MMdd);  //Local Transaction Date
			
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			String gmtDatetime = dateFormatGmt.format(txnDate);
			model.setGmtDateTime(gmtDatetime);
			
			String rrNo = Acq_Utility_Functions.generateRRN(model.getStan(), transmissionDatetime);
			model.setRrNo(rrNo);

			Acq_TUser_Entity terminalUser = null;
			Acq_TerminalInfo_Entity terminalInfo = null;
			Acq_Store_Entity storeInfo = null;
			Acq_SysParams_Entity systemSetting = null;

			try { // checking user details and  key details 
				
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Validating User Details");
				daoSession = createNewSession();
				// Fetching User Details
				terminalUser = (Acq_TUser_Entity) daoSession.get(Acq_TUser_Entity.class,
						Long.valueOf(model.getSessionId()));
				if (terminalUser == null) {
					logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Terminal User Not Found");
					daoResponse.setStatus(Acq_Status_Definations.TerminalUserNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalUserNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Terminal User Found");

				// Fetching Terminal Info
				terminalInfo = (Acq_TerminalInfo_Entity) daoSession.createCriteria(Acq_TerminalInfo_Entity.class)
						.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				if (terminalInfo == null || terminalInfo + "" == "") {
					daoResponse.setStatus(Acq_Status_Definations.TerminalInfoNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalInfoNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				model.setTid(terminalInfo.getBankTid());
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Terminal Info Found");

				// Fetching Merchant Details
				storeInfo = (Acq_Store_Entity) daoSession.get(Acq_Store_Entity.class, terminalUser.getOrgId());
				Acq_Merchant_Entity merchantInfo = (Acq_Merchant_Entity) daoSession
						.createCriteria(Acq_Merchant_Entity.class)
						.add(Restrictions.eq("merchantId", "" + storeInfo.getMerchantId())).uniqueResult();
				if (merchantInfo == null) {
					logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Terminal Merchant Not Found");
					daoResponse.setStatus(Acq_Status_Definations.TerminalMerchantNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalMerchantNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				model.setMid(merchantInfo.getMerchantTID());
				model.setMerchantAddress(merchantInfo.getAddress1());
				model.setMerchantPinCode(merchantInfo.getMerchantPinCode());
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Merchant Details Found");

				// Fetching System Settings
				systemSetting = (Acq_SysParams_Entity) daoSession.createCriteria(Acq_SysParams_Entity.class)
						.add(Restrictions.eq("id", 1l)).uniqueResult();
				if (systemSetting.getMaintenanceMode().equals("1")
						&& !systemSetting.getMaintenanceReason().equalsIgnoreCase("NA")) {
					logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Maintenance Mode On");
					daoResponse.setStatus(Acq_Status_Definations.MaintenanceMode.getId());
					daoResponse.setMessage(Acq_Status_Definations.MaintenanceMode.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Maintenance Mode Off");

				// Fetching Dukpt Key Details
				Acq_KeyExchange_Entity dukptDetails = (Acq_KeyExchange_Entity) daoSession
						.createCriteria(Acq_KeyExchange_Entity.class).add(Restrictions.eq("tid", model.getDeviceId()))
						.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();

				if (dukptDetails == null) {
					logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Dukpt Keys Missing");
					daoResponse.setStatus(Acq_Status_Definations.DukptMissing.getId());
					daoResponse.setMessage(Acq_Status_Definations.DukptMissing.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}

				model.setMcc(dukptDetails.getMcc());
				model.setAcquirerId(dukptDetails.getAcquirerId());
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Dukpt Keys Set");

			} catch (Exception e) {

				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::"
						+ "Exception in Validating User Details");
				e.printStackTrace();
				daoResponse.setStatus(Acq_Status_Definations.ExceptionInMerchantInfo.getId());
				daoResponse.setMessage(Acq_Status_Definations.ExceptionInMerchantInfo.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} finally {
				if (daoSession.isOpen() == true || daoSession.isConnected())
					daoSession.close();
			}
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "User Details Validated ");

			try { // Saving Transaction Data
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Database Transaction Begin");
				
				daoSession = createNewSession();
				dbTransaction = daoSession.beginTransaction();

				Acq_Card_Bin_Details cardBinInfo = new Acq_Card_Bin_Details();
				
				cardBinInfo.setCardType(translateCardType(model.getMaskPan())); // Finding Card Interchange
				model.setCardType(cardBinInfo.getCardType());

				Boolean pinEntered = Boolean.valueOf(model.getPinEntered()); // If Pin or Not
				Boolean isEmv = Boolean.valueOf(model.getEmv()); // If Card is Chip
				String de52 = model.getIsoData().get("DE52").toString(); //If Pin Block Present
				String merchantAddress = model.getMerchantAddress();
				String merchantPincode = model.getMerchantPinCode();

				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Preparing DE61");
				String de61 = Acq_TransTrac_Port_Connector.de61Builder(pinEntered, isEmv, de52, merchantAddress,
						merchantPincode);   // creating de61
				model.setDe61(de61);

				Timestamp currentTimestamp = new Timestamp(txnDate.getTime());
				SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Double txnAmt = Double.valueOf(model.getIsoData().get("DE04").toString()) / 100;				

				Acq_CardTransaction_Entity txnData = new Acq_CardTransaction_Entity();
				txnData.setUserid(terminalUser.getUserId().intValue());
				txnData.setOrgId(terminalUser.getOrgId().intValue());
				txnData.setMerchantId(storeInfo.getMerchantId().intValue());
				txnData.setMobile("0000000000");
				txnData.setAmount(txnAmt.toString());
				txnData.setEzMdr("0.0");
				txnData.setBankMdr("0.0");
				txnData.setAcquirerMdr("0.0");
				txnData.setServiceTax(systemSetting.getServiceTax());
				
				//TODO      txnData.setSystemUtilityFee(settingEnt.getSystmUtltyFee()); 
				// need to set base value
				txnData.setDate(currentTimestamp);
				txnData.setOtpDateTime(Timestamp.valueOf(dbDateFormat.format(txnDate))); // To be removed
				txnData.setStatus(503);
				txnData.setMobile("NA");
				txnData.setEmail("NA");
				txnData.setTxnType("CARD");
				txnData.setDescription("Transaction Initiated");
				txnData.setCarPanNo(model.getMaskPan());
				txnData.setCustomerId("0");
				txnData.setPayoutStatus(700);
				txnData.setPayoutDateTime(currentTimestamp);
				txnData.setAcquirerCode(terminalUser.getAcquirerCode());
				txnData.setCashBackAmt("0.0");
				model.setRequestTime(currentTimestamp.toString());

				if (model.getApplicationCertificate() == null || model.getApplicationCertificate() == "")
					txnData.setAppCertificate("NA");
				else
					txnData.setAppCertificate(model.getApplicationCertificate());

				if (model.getAid() == null || model.getAid() == "")
					txnData.setAid("NA");
				else
					txnData.setAid(model.getAid());

				txnData.setScriptResult("NA");

				daoSession.save(txnData);
				logger.info(
						"/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Entry Created in Transaction Table:"+txnData.getId());

				Acq_Card_Details cardEntity = new Acq_Card_Details();
				cardEntity.setTransactionId(txnData.getId());
				cardEntity.setStan(model.getStan());
				cardEntity.setTerminalId(terminalInfo.getBankTid());
				cardEntity.setRrNo(rrNo);
				cardEntity.setAuthCode("NA");
				cardEntity.setBatchNo("0");
				if (model.getCardHolderName().isEmpty()) {
					cardEntity.setCardHolderName("NA");
				} else {
					cardEntity.setCardHolderName(model.getCardHolderName());
				}
				if (model.getEmv().equalsIgnoreCase("true"))
					cardEntity.setCardType(model.getCardType() + "|Chip");
				else
					cardEntity.setCardType(model.getCardType() + "|Swipe");
				cardEntity.setIpAddress(model.getIpAddress());
				cardEntity.setImeiNo("000000");
				cardEntity.setLatitude(model.getLatitude());
				cardEntity.setLongitude(model.getLongitude());
				cardEntity.setDe61(model.getDe61());
				cardEntity.setGmtDateTime(gmtDatetime);
				cardEntity.setPinEntered(model.getPinEntered());
				cardEntity.setApplicationName("NA");
				/*if (model.getApplicationName() == null || model.getApplicationName() == "") {
					cardEntity.setApplicationName("NA");
				} else {
					cardEntity.setApplicationName(model.getApplicationName());
				}*/
				daoSession.save(cardEntity);
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::"
						+ "Entry Created in Transaction Card Details");
				dbTransaction.commit();

				txnId = txnData.getId();
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Database Transaction End");
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Transaction ID " + txnId);

			} catch (Exception e) {
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Database Transaction Failed:"+e);
				e.printStackTrace();
				if(dbTransaction.isActive()){dbTransaction.rollback();}
				daoResponse.setStatus(Acq_Status_Definations.DataPersistanceError.getId());
				daoResponse.setMessage(Acq_Status_Definations.DataPersistanceError.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} finally {
				if (daoSession.isOpen() || daoSession.isConnected())
					daoSession.close();
			}

			JSONObject IsoResultJson = new JSONObject();
			IsoResultJson.put("transactionId", txnId + "");
			IsoResultJson.put("gmtDateTime", model.getGmtDateTime());

			
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Preparing ISO Message");
			String purchaseRequestIso = purchaseISOBuilder(model,"doPurchase");
			model.setRequestIso(model.getIsoData().get("ksn") + purchaseRequestIso);

			String portResponse = null;
			portResponse = Acq_TransTrac_Port_Connector.portConnectorForKeyExchange(
					model.getIsoData().get("ksn") + purchaseRequestIso, model.getUat(), "/TransTrac/tpos/doPurchase/v1",0);
			System.out.println("portResponse::"+portResponse);
			if (portResponse.isEmpty() || portResponse.equals("null")) { // Blank Response From Switch
				daoResponse.setStatus(Acq_Status_Definations.NoResponseFromSwitch.getId());
				daoResponse.setMessage(Acq_Status_Definations.NoResponseFromSwitch.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else if (portResponse.equals("Connection Timeout")) { // Connection Timed Out
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionTimeOut.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionTimeOut.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else if (portResponse.equalsIgnoreCase("Connection Failed")) { // Connection Failed Due To Unexpected Error
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			}

			model.setRespnseIso(portResponse);

			String responseISO = portResponse.substring(22); // Removing KSN and KSN Length
			IsoResultJson = translateResponseISO(responseISO, "doPurchase", "/TransTrac/tpos/doPurchase/v1");

			if (IsoResultJson == null) {
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Invalid Response From Switch");
				IsoResultJson = new JSONObject();
				IsoResultJson.put("transactionId", txnId + "");
				IsoResultJson.put("gmtDateTime", model.getGmtDateTime());
				daoResponse.setMessage(Acq_Status_Definations.InvalidResponseFromSwitch.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.InvalidResponseFromSwitch.getId());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else {
				IsoResultJson.put("transactionId", txnId + "");
				IsoResultJson.put("gmtDateTime", model.getGmtDateTime());
				daoSession = createNewSession();
				dbTransaction = daoSession.beginTransaction();
				logger.info(
						"/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Database Transaction Update Begin");
				try {

					// Fetch Transaction Details
					Acq_CardTransaction_Entity txnDetails = (Acq_CardTransaction_Entity) daoSession
							.createCriteria(Acq_CardTransaction_Entity.class)
							.add(Restrictions.eq("id", Integer.valueOf(txnId))).uniqueResult();
					if (IsoResultJson.containsKey("responseMessage"))
						txnDetails.setDescription(IsoResultJson.get("responseMessage").toString());
					daoSession.update(txnDetails);

					// Fetching Card Transaction Details
					Acq_Card_Details txnCardDetails = (Acq_Card_Details) daoSession
							.createCriteria(Acq_Card_Details.class)
							.add(Restrictions.eq("transactionId", Integer.valueOf(txnId))).uniqueResult();

					if (IsoResultJson.containsKey("DE38"))
						txnCardDetails.setAuthCode(IsoResultJson.get("DE38").toString());

					if (IsoResultJson.containsKey("DE12"))
						txnCardDetails.setBatchNo(IsoResultJson.get("DE12").toString());

					if (model.getGmtDateTime() != null)
						txnCardDetails.setGmtDateTime(model.getGmtDateTime());

					daoSession.update(txnCardDetails);

					Timestamp currentTimestamp = new Timestamp(new Date().getTime());
					Acq_TTIso_Entity isoEnt = new Acq_TTIso_Entity();
					isoEnt.setTransactionId(txnId + "");
					isoEnt.setIsoRequest(model.getRequestIso());
					isoEnt.setIsoResponse(model.getRespnseIso());
					isoEnt.setSwitchLab("TransTrac");
					isoEnt.setRequestTime(model.getRequestTime());
					isoEnt.setResponseTime(currentTimestamp.toString());
					daoSession.save(isoEnt);
					dbTransaction.commit();
					logger.info(
							"/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Database Transaction Update Begin");
				} catch (Exception e) {
					logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Exception in Saving Data ");
					e.printStackTrace();
					if(dbTransaction.isActive()){dbTransaction.rollback();}
					daoResponse.setStatus(Acq_Status_Definations.DataPersistanceError.getId());
					daoResponse.setMessage(Acq_Status_Definations.DataPersistanceError.getDescription());
					daoResponse.setResult(IsoResultJson);
					
				} finally {
					
					if (daoSession!=null &&  (daoSession.isOpen() == true || daoSession.isConnected() == true)) {
						daoSession.close();
					}
				}
				daoResponse.setResult(IsoResultJson);
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Returning to Handler Successfully");
				return daoResponse;
			}

		} catch (Exception e) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "DAO" + "::" + "Unexpected Server Error:");
			e.printStackTrace();
			daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setResult(null);
			return daoResponse;
		}
	}

	
	// DAO For Cash At POS Service
	@Override
	public DbDto<Object> doCashAtPos(Acq_TransTrac_Purchase_Model model) {

		logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Begin");

		DbDto<Object> daoResponse = new DbDto<Object>();
		int txnId = 0;
		Date txnDate = new Date();
		Session daoSession = null;
		Transaction dbTransaction = null;

		try {
			logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Saving Transaction Begin");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			String transmissionDatetime = dateFormatGmt.format(txnDate);
			
			SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
			model.setDe12(timeFormat.format(txnDate)); // Local Time
			
			String MMdd = transmissionDatetime.substring(0, 4);
			model.setDe13(MMdd);  //Local Transaction Date
			
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			String gmtDatetime = dateFormatGmt.format(txnDate);
			model.setGmtDateTime(gmtDatetime);
			
			String rrNo = Acq_Utility_Functions.generateRRN(model.getStan(), transmissionDatetime);
			model.setRrNo(rrNo);

			Acq_TUser_Entity terminalUser = null;
			Acq_TerminalInfo_Entity terminalInfo = null;
			Acq_Store_Entity storeInfo = null;
			Acq_SysParams_Entity systemSetting = null;

			try { // checking user details and  key details 
				
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Validating User Details");
				daoSession = createNewSession();
				// Fetching User Details
				terminalUser = (Acq_TUser_Entity) daoSession.get(Acq_TUser_Entity.class,
						Long.valueOf(model.getSessionId()));
				if (terminalUser == null) {
					logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Terminal User Not Found");
					daoResponse.setStatus(Acq_Status_Definations.TerminalUserNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalUserNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Terminal User Found");

				// Fetching Terminal Info
				terminalInfo = (Acq_TerminalInfo_Entity) daoSession.createCriteria(Acq_TerminalInfo_Entity.class)
						.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				if (terminalInfo == null || terminalInfo + "" == "") {
					daoResponse.setStatus(Acq_Status_Definations.TerminalInfoNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalInfoNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				model.setTid(terminalInfo.getBankTid());
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Terminal Info Found");

				// Fetching Merchant Details
				storeInfo = (Acq_Store_Entity) daoSession.get(Acq_Store_Entity.class, terminalUser.getOrgId());
				Acq_Merchant_Entity merchantInfo = (Acq_Merchant_Entity) daoSession
						.createCriteria(Acq_Merchant_Entity.class)
						.add(Restrictions.eq("merchantId", "" + storeInfo.getMerchantId())).uniqueResult();
				if (merchantInfo == null) {
					logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Terminal Merchant Not Found");
					daoResponse.setStatus(Acq_Status_Definations.TerminalMerchantNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalMerchantNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				model.setMid(merchantInfo.getMerchantTID());
				model.setMerchantAddress(merchantInfo.getAddress1());
				model.setMerchantPinCode(merchantInfo.getMerchantPinCode());
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Merchant Details Found");

				// Fetching System Settings
				systemSetting = (Acq_SysParams_Entity) daoSession.createCriteria(Acq_SysParams_Entity.class)
						.add(Restrictions.eq("id", 1l)).uniqueResult();
				if (systemSetting.getMaintenanceMode().equals("1")
						&& !systemSetting.getMaintenanceReason().equalsIgnoreCase("NA")) {
					logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Maintenance Mode On");
					daoResponse.setStatus(Acq_Status_Definations.MaintenanceMode.getId());
					daoResponse.setMessage(Acq_Status_Definations.MaintenanceMode.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Maintenance Mode Off");

				// Fetching Dukpt Key Details
				Acq_KeyExchange_Entity dukptDetails = (Acq_KeyExchange_Entity) daoSession
						.createCriteria(Acq_KeyExchange_Entity.class).add(Restrictions.eq("tid", model.getDeviceId()))
						.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();

				if (dukptDetails == null) {
					logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Dukpt Keys Missing");
					daoResponse.setStatus(Acq_Status_Definations.DukptMissing.getId());
					daoResponse.setMessage(Acq_Status_Definations.DukptMissing.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}

				model.setMcc(dukptDetails.getMcc());
				model.setAcquirerId(dukptDetails.getAcquirerId());
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Dukpt Keys Set");

			} catch (Exception e) {

				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::"
						+ "Exception in Validating User Details");
				e.printStackTrace();
				daoResponse.setStatus(Acq_Status_Definations.ExceptionInMerchantInfo.getId());
				daoResponse.setMessage(Acq_Status_Definations.ExceptionInMerchantInfo.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} finally {
				if (daoSession.isOpen() == true || daoSession.isConnected())
					daoSession.close();
			}
			logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "User Details Validated ");

			try { // Saving Transaction Data
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Database Transaction Begin");
				
				daoSession = createNewSession();
				dbTransaction = daoSession.beginTransaction();

				Acq_Card_Bin_Details cardBinInfo = new Acq_Card_Bin_Details();
				
				cardBinInfo.setCardType(translateCardType(model.getMaskPan())); // Finding Card Interchange
				model.setCardType(cardBinInfo.getCardType());

				Boolean pinEntered = Boolean.valueOf(model.getPinEntered()); // If Pin or Not
				Boolean isEmv = Boolean.valueOf(model.getEmv()); // If Card is Chip
				String de52 = model.getIsoData().get("DE52").toString(); //If Pin Block Present
				String merchantAddress = model.getMerchantAddress();
				String merchantPincode = model.getMerchantPinCode();

				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Preparing DE61");
				String de61 = Acq_TransTrac_Port_Connector.de61Builder(pinEntered, isEmv, de52, merchantAddress,
						merchantPincode);   // creating de61
				model.setDe61(de61);

				Timestamp currentTimestamp = new Timestamp(txnDate.getTime());
				SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Double txnAmt = Double.valueOf(model.getIsoData().get("DE04").toString()) / 100;				

				Acq_CardTransaction_Entity txnData = new Acq_CardTransaction_Entity();
				txnData.setUserid(terminalUser.getUserId().intValue());
				txnData.setOrgId(terminalUser.getOrgId().intValue());
				txnData.setMerchantId(storeInfo.getMerchantId().intValue());
				txnData.setMobile("0000000000");
				txnData.setAmount(txnAmt.toString());
				txnData.setEzMdr("0.0");
				txnData.setBankMdr("0.0");
				txnData.setAcquirerMdr("0.0");
				txnData.setServiceTax(systemSetting.getServiceTax());
				// txnData.setSystemUtilityFee(settingEnt.getSystmUtltyFee());
				// need to set base value
				txnData.setDate(currentTimestamp); 
				txnData.setOtpDateTime(Timestamp.valueOf(dbDateFormat.format(txnDate))); 
				txnData.setStatus(503);
				txnData.setMobile("NA");
				txnData.setEmail("NA");
				txnData.setTxnType("CARD");
				txnData.setDescription("Transaction Initiated");
				txnData.setCarPanNo(model.getMaskPan());
				txnData.setCustomerId("0");
				txnData.setPayoutStatus(700);
				txnData.setPayoutDateTime(currentTimestamp);
				txnData.setAcquirerCode(terminalUser.getAcquirerCode());
				txnData.setCashBackAmt("0.0");
				model.setRequestTime(currentTimestamp.toString());

				if (model.getApplicationCertificate() == null || model.getApplicationCertificate() == "")
					txnData.setAppCertificate("NA");
				else
					txnData.setAppCertificate(model.getApplicationCertificate());

				if (model.getAid() == null || model.getAid() == "")
					txnData.setAid("NA");
				else
					txnData.setAid(model.getAid());

				txnData.setScriptResult("NA");

				daoSession.save(txnData);
				logger.info(
						"/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Entry Created in Transaction Table");

				Acq_Card_Details cardEntity = new Acq_Card_Details();
				cardEntity.setTransactionId(txnData.getId());
				cardEntity.setStan(model.getStan());
				cardEntity.setTerminalId(terminalInfo.getBankTid());
				cardEntity.setRrNo(rrNo);
				cardEntity.setAuthCode("NA");
				cardEntity.setBatchNo("0");
				cardEntity.setCardHolderName("NA");
				/*if (model.getCardHolderName().isEmpty()) {
					cardEntity.setCardHolderName("NA");
				} else {
					cardEntity.setCardHolderName(model.getCardHolderName());
				}*/
				if (model.getEmv().equalsIgnoreCase("true"))
					cardEntity.setCardType(model.getCardType() + "|Chip");
				else
					cardEntity.setCardType(model.getCardType() + "|Swipe");

				cardEntity.setIpAddress(model.getIpAddress());
				cardEntity.setImeiNo("000000");
				cardEntity.setLatitude(model.getLatitude());
				cardEntity.setLongitude(model.getLongitude());
				cardEntity.setDe61(model.getDe61());
				cardEntity.setGmtDateTime(gmtDatetime);
				cardEntity.setPinEntered(model.getPinEntered());
				
				if (model.getApplicationName() == null || model.getApplicationName() == "") {
					cardEntity.setApplicationName("NA");
				} else {
					cardEntity.setApplicationName(model.getApplicationName());
				}
				daoSession.save(cardEntity);
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::"
						+ "Entry Created in Transaction Card Details");
				dbTransaction.commit();

				txnId = txnData.getId();
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Database Transaction End");
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Transaction ID " + txnId);

			} catch (Exception e) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Database Transaction Failed");
				e.printStackTrace();
				if(dbTransaction.isActive()){dbTransaction.rollback();}
				daoResponse.setStatus(Acq_Status_Definations.DataPersistanceError.getId());
				daoResponse.setMessage(Acq_Status_Definations.DataPersistanceError.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} finally {
				if (daoSession.isOpen() || daoSession.isConnected())
					daoSession.close();
			}

			JSONObject IsoResultJson = new JSONObject();
			IsoResultJson.put("transactionId", txnId + "");
			IsoResultJson.put("gmtDateTime", model.getGmtDateTime());

			
			logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Preparing ISO Message");
			String capRequestIso = purchaseISOBuilder(model,"doCashAtPos");
			model.setRequestIso(model.getIsoData().get("ksn") + capRequestIso);

			String portResponse = null;
			portResponse = Acq_TransTrac_Port_Connector.portConnectorForKeyExchange(
					model.getIsoData().get("ksn") + capRequestIso, model.getUat(), "/TransTrac/tpos/doCashAtPos/v1",0);

			if (portResponse.isEmpty() || portResponse.equals("null")) { // Blank Response From Switch
				daoResponse.setStatus(Acq_Status_Definations.NoResponseFromSwitch.getId());
				daoResponse.setMessage(Acq_Status_Definations.NoResponseFromSwitch.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else if (portResponse.equals("Connection Timeout")) { // Connection Timed Out
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionTimeOut.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionTimeOut.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else if (portResponse.equalsIgnoreCase("Connection Failed")) { // Connection Failed Due To Unexpected Error
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			}

			model.setRespnseIso(portResponse);

			String responseISO = portResponse.substring(22); // Removing KSN and KSN Length
			IsoResultJson = translateResponseISO(responseISO, "doCashAtPos", "/TransTrac/tpos/doCashAtPos/v1");

			if (IsoResultJson == null) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Invalid Response From Switch");
				IsoResultJson = new JSONObject();
				IsoResultJson.put("transactionId", txnId + "");
				IsoResultJson.put("gmtDateTime", model.getGmtDateTime());
				daoResponse.setMessage(Acq_Status_Definations.InvalidResponseFromSwitch.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.InvalidResponseFromSwitch.getId());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else {
				IsoResultJson.put("transactionId", txnId + "");
				IsoResultJson.put("gmtDateTime", model.getGmtDateTime());
				daoSession = createNewSession();
				dbTransaction = daoSession.beginTransaction();
				logger.info(
						"/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Database Transaction Update Begin");
				try {

					// Fetch Transaction Details
					Acq_CardTransaction_Entity txnDetails = (Acq_CardTransaction_Entity) daoSession
							.createCriteria(Acq_CardTransaction_Entity.class)
							.add(Restrictions.eq("id", Integer.valueOf(txnId))).uniqueResult();
					if (IsoResultJson.containsKey("responseMessage"))
						txnDetails.setDescription(IsoResultJson.get("responseMessage").toString());
					daoSession.update(txnDetails);

					// Fetching Card Transaction Details
					Acq_Card_Details txnCardDetails = (Acq_Card_Details) daoSession
							.createCriteria(Acq_Card_Details.class)
							.add(Restrictions.eq("transactionId", Integer.valueOf(txnId))).uniqueResult();

					if (IsoResultJson.containsKey("DE38"))
						txnCardDetails.setAuthCode(IsoResultJson.get("DE38").toString());

					if (IsoResultJson.containsKey("DE12"))
						txnCardDetails.setBatchNo(IsoResultJson.get("DE12").toString());

					if (model.getGmtDateTime() != null)
						txnCardDetails.setGmtDateTime(model.getGmtDateTime());

					daoSession.update(txnCardDetails);

					Timestamp currentTimestamp = new Timestamp(new Date().getTime());
					Acq_TTIso_Entity isoEnt = new Acq_TTIso_Entity();
					isoEnt.setTransactionId(txnId + "");
					isoEnt.setIsoRequest(model.getRequestIso());
					isoEnt.setIsoResponse(model.getRespnseIso());
					isoEnt.setSwitchLab("TransTrac");
					isoEnt.setRequestTime(model.getRequestTime());
					isoEnt.setResponseTime(currentTimestamp.toString());
					daoSession.save(isoEnt);
					dbTransaction.commit();
					logger.info(
							"/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Database Transaction Update Begin");
				} catch (Exception e) {
					logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Exception in Saving Data ");
					e.printStackTrace();
					if(dbTransaction.isActive()){dbTransaction.rollback();}
					daoResponse.setStatus(Acq_Status_Definations.DataPersistanceError.getId());
					daoResponse.setMessage(Acq_Status_Definations.DataPersistanceError.getDescription());
					
				} finally {
					if (daoSession.isOpen() == true || daoSession.isConnected() == true) {
						daoSession.close();
					}
				}
				daoResponse.setResult(IsoResultJson);
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Returning to Handler Successfully");
				return daoResponse;
			}

		} catch (Exception e) {
			logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
			daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setResult(null);
			return daoResponse;
		}
	}
	
	// DAO For Txn Void Service
	@Override
	public DbDto<Object> doVoid(Acq_TransTrac_VoidTxn_Model model) {
		logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Begin");
		DbDto<Object> daoResponse = new DbDto<Object>();
		
		Date txnDate = new Date();
		Session daoSession = null;
		try {
			Acq_TUser_Entity terminalUser = null;
			Acq_TerminalInfo_Entity terminalInfo = null;
			Acq_Store_Entity storeInfo = null;
			
			try {// checking user details and key details 
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Validating User Details");
				daoSession = createNewSession();
				
				// Fetching User Details
				System.out.println("000000000000");
				terminalUser = (Acq_TUser_Entity) daoSession.get(Acq_TUser_Entity.class,Long.valueOf(model.getSessionId()));
				System.out.println("11111111111111");
				if (terminalUser == null) {
					logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Terminal User Not Found");
					daoResponse.setStatus(Acq_Status_Definations.TerminalUserNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalUserNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Terminal User Found");
				
				// Fetching Terminal Info
				terminalInfo = (Acq_TerminalInfo_Entity) daoSession.createCriteria(Acq_TerminalInfo_Entity.class)
						.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				if (terminalInfo == null || terminalInfo + "" == "") {
					daoResponse.setStatus(Acq_Status_Definations.TerminalInfoNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalInfoNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				System.out.println("222222222222222222");
				model.setTid(terminalInfo.getBankTid());
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Terminal Info Found");
				
				// Fetching Merchant Details
				storeInfo = (Acq_Store_Entity) daoSession.get(Acq_Store_Entity.class, terminalUser.getOrgId());
				Acq_Merchant_Entity merchantInfo = (Acq_Merchant_Entity) daoSession
						.createCriteria(Acq_Merchant_Entity.class)
						.add(Restrictions.eq("merchantId", "" + storeInfo.getMerchantId())).uniqueResult();
				System.out.println("33333333333333333333");
				if (merchantInfo == null) {
					logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Terminal Merchant Not Found");
					daoResponse.setStatus(Acq_Status_Definations.TerminalMerchantNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalMerchantNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				model.setMid(merchantInfo.getMerchantTID());

				// Fetching Dukpt Key Details
				Acq_KeyExchange_Entity dukptDetails = (Acq_KeyExchange_Entity) daoSession
						.createCriteria(Acq_KeyExchange_Entity.class).add(Restrictions.eq("tid", model.getDeviceId()))
						.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				System.out.println("4444444444444444444444");
				if (dukptDetails == null) {
					logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Dukpt Keys Missing");
					daoResponse.setStatus(Acq_Status_Definations.DukptMissing.getId());
					daoResponse.setMessage(Acq_Status_Definations.DukptMissing.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				model.setAcquirerId(dukptDetails.getAcquirerId());
				
			}
			catch (Exception e) {

				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::"
						+ "Exception in Validating User Details");
				e.printStackTrace();
				daoResponse.setStatus(Acq_Status_Definations.ExceptionInMerchantInfo.getId());
				daoResponse.setMessage(Acq_Status_Definations.ExceptionInMerchantInfo.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} finally {
				if (daoSession.isOpen() == true || daoSession.isConnected())
					daoSession.close();
			}
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "User Details Validated ");
			
			try{ //Searching Txn to be Voided
				
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Trying to Find Txn");
				daoSession = createNewSession();
				System.out.println("5555555555555555");
				Acq_Card_Details txn2Void = (Acq_Card_Details) daoSession.createCriteria(Acq_Card_Details.class)
						.add(Restrictions.eq("transactionId", Integer.valueOf(model.getTransactionId())))
						.uniqueResult();
				System.out.println("6666666666666666");
				if (txn2Void == null) {
					logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "No Transaction For Txn Id "+model.getTransactionId());
					daoResponse.setStatus(Acq_Status_Definations.TransactionsNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TransactionsNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				} else {
					logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Transaction Found For Txn Id "+model.getTransactionId());
					model.setDe61(txn2Void.getDe61());
					model.setGmtDateTime(txn2Void.getGmtDateTime());  // Original Txn Date Time
					model.setStan(txn2Void.getStan());  //Original Txn Stan
					model.setRrNo(txn2Void.getRrNo()); //Original Txn Rrno
				}				
				Acq_CardTransaction_Entity txnEnt = (Acq_CardTransaction_Entity) daoSession 
						.createCriteria(Acq_CardTransaction_Entity.class)
						.add(Restrictions.eq("id", Integer.valueOf(model.getTransactionId())))
						.add(Restrictions.eq("userid", Integer.valueOf(model.getSessionId()))).uniqueResult();  //TODO Add restriction of payout status
				System.out.println("77777777777777");
				Date txn2VoidDate = new Date(txnEnt.getOtpDateTime().getTime());
				SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");  //Original Txn Date Time
				String MMdd = dateFormatGmt.format(txn2VoidDate).substring(0, 4);
				String HHmmss = dateFormatGmt.format(txn2VoidDate).substring(4, 10); 
				
				model.setDe13(MMdd);  
				model.setLocalTime(HHmmss);
				model.setAddtionalAmount(txnEnt.getCashBackAmt());
				model.setTxnType(txnEnt.getTxnType());
			} catch (Exception e) {

				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::"
						+ "Exception in Finding Transaction Details");
				e.printStackTrace();
				daoResponse.setStatus(Acq_Status_Definations.TransactionSearchFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.TransactionSearchFailed.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} finally {
				if (daoSession.isOpen() == true || daoSession.isConnected())
					daoSession.close();
			}
			
			
			JSONObject IsoResultJson = new JSONObject();
			
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Preparing ISO Message");
			String voidRequestIso = voidISOBuilder(model,"/TransTrac/tpos/doVoid/v1");

			String portResponse = null;
			portResponse = Acq_TransTrac_Port_Connector
					.portConnectorForKeyExchange(model.getIsoData().get("ksn") + voidRequestIso, model.getUat(),"/TransTrac/tpos/doVoid/v1",0);
			
			if (portResponse.isEmpty() || portResponse.equals("null")) { // Blank Response From Switch
				daoResponse.setStatus(Acq_Status_Definations.NoResponseFromSwitch.getId());
				daoResponse.setMessage(Acq_Status_Definations.NoResponseFromSwitch.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else if (portResponse.equals("Connection Timeout")) { // Connection Timed Out
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionTimeOut.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionTimeOut.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else if (portResponse.equalsIgnoreCase("Connection Failed")) { // Connection Failed Due To Unexpected Error
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			}
			
			String subStr = portResponse.substring(22);  
			IsoResultJson = translateResponseISO(subStr, "doVoid","/TransTrac/tpos/doVoid/v1");

			if (IsoResultJson == null) {
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Invalid Response From Switch");
				daoResponse.setMessage(Acq_Status_Definations.InvalidResponseFromSwitch.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.InvalidResponseFromSwitch.getId());
				daoResponse.setResult(null);
				return daoResponse;
			}
			
			if (IsoResultJson.containsKey("DE39") && IsoResultJson.get("DE39").equals("00")) 
			{ 
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Void Success, Updating DB");
				Transaction dbTransaction = null;
				try {
						daoSession = createNewSession();
						dbTransaction = daoSession.beginTransaction();
						
						Acq_CardTransaction_Entity txn2VoidUpdate = (Acq_CardTransaction_Entity) daoSession
								.createCriteria(Acq_CardTransaction_Entity.class)
								.add(Restrictions.eq("id", Integer.valueOf(model.getTransactionId())))
								.add(Restrictions.eq("userid", Integer.valueOf(model.getSessionId()))).uniqueResult();
						
						txn2VoidUpdate.setOtpDateTime(new Timestamp(txnDate.getTime()));
						txn2VoidUpdate.setPayoutStatus(701);
						txn2VoidUpdate.setDescription("Transaction Void");
						txn2VoidUpdate.setTxnType("VOID");
						daoSession.update(txn2VoidUpdate);
						
						try { // Removing Transaction from Risk 
							Acq_RiskRules_Entity riskTxn = (Acq_RiskRules_Entity) daoSession
									.createCriteria(Acq_RiskRules_Entity.class)
									.add(Restrictions.eq("txnId", txn2VoidUpdate.getId())).uniqueResult();
							if (riskTxn != null) {
								riskTxn.setStatus("0");
								daoSession.save(riskTxn);
							}
						} catch (Exception e) {
							logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Exception in Removing From Risk");
							e.printStackTrace();
						}
						dbTransaction.commit();
						daoResponse.setStatus(Acq_Status_Definations.OK.getId());
						daoResponse.setMessage(Acq_Status_Definations.OK.getDescription());
						daoResponse.setResult(IsoResultJson);
					}catch (Exception e) {							
						logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Exception in Saving Data");
						e.printStackTrace();
						if(dbTransaction.isActive()){dbTransaction.rollback();}
						daoResponse.setStatus(Acq_Status_Definations.DataPersistanceError.getId());
						daoResponse.setMessage(Acq_Status_Definations.DataPersistanceError.getDescription());
					}
					finally {
						if (daoSession.isOpen() == true || daoSession.isConnected())
							daoSession.close();
					}
				}
			else
			{
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Void Failed");
				daoResponse.setStatus(Acq_Status_Definations.VoidFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.VoidFailed.getDescription());
				daoResponse.setResult(IsoResultJson);
			}
		}
		catch (Exception e) {
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
			daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setResult(null);
		}
		return daoResponse;
	}

	// DAO For Void Txn List Service
	@Override
	public DbDto<Object> getVoidList(Acq_TransTrac_VoidTxnList_Model model) {
		logger.info("/TransTrac/tpos/doVoidList/v1" + "::" + "DAO" + "::" + "Begin");
		DbDto<Object> daoResponse = new DbDto<Object>();
		SimpleDateFormat df = null;
		df = new SimpleDateFormat("yyyy-MM-dd");		
		WeakHashMap<String,WeakHashMap<String,String>> responseMap = new WeakHashMap<String,WeakHashMap<String,String>>();
		Session session = null;
		try{
			session = createNewSession();
			String fromDate = df.format(new Date())+ "00:00:00";
			String toDate =  df.format(new Date())+ "23:59:59";
			String queryStr = "select m.id,m.txnType from acq_transaction_summary m, acq_transaction_card_summary c where m.id=c.transactionId and m.cardPanNo='"+model.getMaskPan()+"' and m.txnType in('VOID','CARD','CASHATPOS','CVOID','CASHBACK','CBVOID') and m.status=505 and m.payoutStatus=700 and m.otpdatetime between '2017-06-07 00:00:00' and '2017-06-07 23:23:59' and m.amount not in (select amount from acq_transaction_summary where amount like '-%') and m.amount not in (select REPLACE(amount,'-','') from acq_transaction_summary where amount like '-%') order by m.id desc";
			//String queryStr = "select m.id,m.txnType from Acq_CardTransaction_Entity m, Acq_Card_Details c where m.id=c.transactionId and m.carPanNo='"+model.getMaskPan()+"' and m.txnType in('VOID','CARD','CASHATPOS','CVOID','CASHBACK','CBVOID') and m.status=505 and m.payoutStatus=700 and m.otpdatetime between '2017-06-07 00:00:00' and '2017-06-07 23:23:59' and m.amount not in (select amount from Acq_CardTransaction_Entity where amount like '-%') and m.amount not in (select REPLACE(amount,'-','') from Acq_CardTransaction_Entity where amount like '-%') order by m.id desc";
			SQLQuery query = (SQLQuery)session.createSQLQuery(queryStr);
			List txnList = query.list();
			if(txnList.isEmpty()){
				daoResponse.setStatus(Acq_Status_Definations.NotFound.getId());
				daoResponse.setMessage(Acq_Status_Definations.NotFound.getDescription());
				daoResponse.setResult(null);
				System.out.println("Void List Not Found");	
			}else{
			JSONObject json = null;
			for(Object ob:txnList){
				Object obj[] = (Object[]) ob;
				json = new JSONObject();
				json.put("transactionId", obj[0].toString());
				System.out.println(obj[0]+":::::"+obj[1]);
			}	
			daoResponse.setMessage("OK");
			daoResponse.setStatus(0);
			daoResponse.setResult(json);
			System.out.println("response for void transaction list dao");
			}
			return daoResponse;
		}catch(Exception e){
			System.err.println("error to connect ogs "+e);
			daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
			return daoResponse; 
		}finally{
			session.close();
		}
	}	
	
// DAO For Txn Reversal Service
	@Override
	public DbDto<Object> doReversal(Acq_TransTrac_Reversal_Model model) {

		logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Begin");
		DbDto<Object> daoResponse = new DbDto<Object>();
		Session daoSession = null;

		try {
			
			Acq_TUser_Entity terminalUser = null;
			Acq_TerminalInfo_Entity terminalInfo = null;
			Acq_Store_Entity storeInfo = null;			
			try {// checking user details and key details 
				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Validating User Details");
				daoSession = createNewSession();
				// Fetching User Details
				terminalUser = (Acq_TUser_Entity) daoSession.get(Acq_TUser_Entity.class,
						Long.valueOf(model.getSessionId()));
				if (terminalUser == null) {
					logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Terminal User Not Found");
					daoResponse.setStatus(Acq_Status_Definations.TerminalUserNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalUserNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Terminal User Found");
				
				// Fetching Terminal Info
				terminalInfo = (Acq_TerminalInfo_Entity) daoSession.createCriteria(Acq_TerminalInfo_Entity.class)
						.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				if (terminalInfo == null || terminalInfo + "" == "") {
					daoResponse.setStatus(Acq_Status_Definations.TerminalInfoNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalInfoNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				model.setTid(terminalInfo.getBankTid());
				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Terminal Info Found");
				
				// Fetching Merchant Details
				storeInfo = (Acq_Store_Entity) daoSession.get(Acq_Store_Entity.class, terminalUser.getOrgId());
				Acq_Merchant_Entity merchantInfo = (Acq_Merchant_Entity) daoSession
						.createCriteria(Acq_Merchant_Entity.class)
						.add(Restrictions.eq("merchantId", "" + storeInfo.getMerchantId())).uniqueResult();
				if (merchantInfo == null) {
					logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Terminal Merchant Not Found");
					daoResponse.setStatus(Acq_Status_Definations.TerminalMerchantNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TerminalMerchantNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				model.setMid(merchantInfo.getMerchantTID());

				// Fetching Dukpt Key Details
				Acq_KeyExchange_Entity dukptDetails = (Acq_KeyExchange_Entity) daoSession
						.createCriteria(Acq_KeyExchange_Entity.class).add(Restrictions.eq("tid", model.getDeviceId()))
						.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();

				if (dukptDetails == null) {
					logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Dukpt Keys Missing");
					daoResponse.setStatus(Acq_Status_Definations.DukptMissing.getId());
					daoResponse.setMessage(Acq_Status_Definations.DukptMissing.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				}
				model.setAcquirerId(dukptDetails.getAcquirerId());
				
			}catch (Exception e) {

				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::"
						+ "Exception in Validating User Details");
				e.printStackTrace();
				daoResponse.setStatus(Acq_Status_Definations.ExceptionInMerchantInfo.getId());
				daoResponse.setMessage(Acq_Status_Definations.ExceptionInMerchantInfo.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} finally {
				if (daoSession.isOpen() == true || daoSession.isConnected())
					daoSession.close();
			}
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "User Details Validated ");
			
			
			try{ //Searching Txn to be Voided
				
				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Trying to Find Txn");
				daoSession = createNewSession();
				
				Acq_Card_Details txn2Reverse = (Acq_Card_Details) daoSession.createCriteria(Acq_Card_Details.class)
						.add(Restrictions.eq("transactionId", Integer.valueOf(model.getTransactionId())))
						.uniqueResult();
				if (txn2Reverse == null) {
					logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "No Transaction For Txn Id "+model.getTransactionId());
					daoResponse.setStatus(Acq_Status_Definations.TransactionsNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TransactionsNotFound.getDescription());
					daoResponse.setResult(null);
					return daoResponse;
				} else {
					logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Transaction Found For Txn Id "+model.getTransactionId());
					model.setDe61(txn2Reverse.getDe61());
					model.setDe38(txn2Reverse.getAuthCode());
					//model.setGmtDateTime(txn2Void.getGmtDateTime());  //TODO Original Txn Date Time Abhishek
					model.setStan(txn2Reverse.getStan());  //Original Txn Stan
					model.setRrNo(txn2Reverse.getRrNo()); //Original Txn Rrno
				}

				
				Acq_CardTransaction_Entity txnEnt = (Acq_CardTransaction_Entity) daoSession 
						.createCriteria(Acq_CardTransaction_Entity.class)
						.add(Restrictions.eq("id", Integer.valueOf(model.getTransactionId())))
						.add(Restrictions.eq("userid", Integer.valueOf(model.getSessionId()))).uniqueResult();  
				txnEnt.setPayoutStatus(701);
				Date txn2VoidDate = new Date(txnEnt.getOtpDateTime().getTime());
				SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");  //Original Txn Date Time
				dateFormatGmt.format(txn2VoidDate).substring(0, 4);
				String HHmmss = dateFormatGmt.format(txn2VoidDate).substring(4, 10); 
				
				//model.setDe13(MMdd);    // TODO Check Abhishek
				model.setDe12(HHmmss);
				model.setDe54(txnEnt.getCashBackAmt());
				model.setTxnType(txnEnt.getTxnType());
			} catch (Exception e) {

				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::"
						+ "Exception in Finding Transaction Details");
				e.printStackTrace();
				daoResponse.setStatus(Acq_Status_Definations.TransactionSearchFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.TransactionSearchFailed.getDescription());
				daoResponse.setResult(null);
				return daoResponse;
			} finally {
				if (daoSession.isOpen() == true || daoSession.isConnected())
					daoSession.close();
			}
			
			JSONObject IsoResultJson = new JSONObject();
			
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "DAO" + "::" + "Preparing ISO Message");
			String reversalRequestIso = reversalISOBuilder(model);
			
			String portResponse = Acq_TransTrac_Port_Connector
					.portConnectorForKeyExchange(model.getIsoData().get("ksn") + reversalRequestIso, model.getUat(),"/TransTrac/tpos/doReversal/v1",0);
			
			
			if (portResponse.isEmpty() || portResponse.equals("null")) { // Blank Response From Switch
				daoResponse.setStatus(Acq_Status_Definations.NoResponseFromSwitch.getId());
				daoResponse.setMessage(Acq_Status_Definations.NoResponseFromSwitch.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else if (portResponse.equals("Connection Timeout")) { // Connection Timed Out
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionTimeOut.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionTimeOut.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			} else if (portResponse.equalsIgnoreCase("Connection Failed")) { // Connection Failed Due To Unexpected Error
				daoResponse.setStatus(Acq_Status_Definations.SwitchConnectionFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.SwitchConnectionFailed.getDescription());
				daoResponse.setResult(IsoResultJson);
				return daoResponse;
			}

			String subStr = portResponse.substring(22);
			IsoResultJson = translateResponseISO(subStr, "doReversal","/TransTrac/tpos/doPurchase/v1");
			
			if (IsoResultJson == null) {
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Invalid Response From Switch");
				daoResponse.setMessage(Acq_Status_Definations.InvalidResponseFromSwitch.getDescription());
				daoResponse.setStatus(Acq_Status_Definations.InvalidResponseFromSwitch.getId());
				daoResponse.setResult(null);
				return daoResponse;
			}
			
			if (IsoResultJson.containsKey("DE39") && IsoResultJson.get("DE39").equals("00")) 
			{  // start from here.
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Void Success, Updating DB");
				Transaction dbTransaction = null;
				try {
						daoSession = createNewSession();
						dbTransaction = daoSession.beginTransaction();
						
						Acq_CardTransaction_Entity txn2ReverseUpdate = (Acq_CardTransaction_Entity) daoSession
								.createCriteria(Acq_CardTransaction_Entity.class)
								.add(Restrictions.eq("id", Integer.valueOf(model.getTransactionId()))).uniqueResult();
						
						txn2ReverseUpdate.setStatus(504);
						txn2ReverseUpdate.setDescription("Reversal Successful");
						txn2ReverseUpdate.setTxnType("REVERSED");
						daoSession.update(txn2ReverseUpdate);
						
						dbTransaction.commit();
						daoResponse.setStatus(Acq_Status_Definations.OK.getId());
						daoResponse.setMessage(Acq_Status_Definations.OK.getDescription());
						logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Reversal Success, DB Updated");
						daoResponse.setResult(IsoResultJson);
					} catch (Exception e) {							
						logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Exception in Saving Data");
						e.printStackTrace();
						if(dbTransaction.isActive()){dbTransaction.rollback();}
						daoResponse.setStatus(Acq_Status_Definations.DataPersistanceError.getId());
						daoResponse.setMessage(Acq_Status_Definations.DataPersistanceError.getDescription());
					}
					finally {
						if (daoSession.isOpen() == true || daoSession.isConnected())
							daoSession.close();
					}
				} 
				else
				{
					logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Void Failed");
					daoResponse.setStatus(Acq_Status_Definations.VoidFailed.getId());
					daoResponse.setMessage(Acq_Status_Definations.VoidFailed.getDescription());
					daoResponse.setResult(IsoResultJson);
				}
		
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "DAO" + "::" + "Unexpected Server Error");
			daoResponse.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			daoResponse.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
			daoResponse.setResult(null);
		}
		return daoResponse;
	}
	
	@Override
	public DbDto<Object> sendPurchaseAck(Acq_PurchaseAck_Model model) {
		logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "DAO" + "::" + "Begin");
		DbDto<Object> daoResponse = new DbDto<Object>();
		
		Session daoSession = null;
		Transaction dbTransaction = null;
		
		try { // Begin Updating the transactions
				
				daoSession = createNewSession();
				Acq_CardTransaction_Entity txnDetails = (Acq_CardTransaction_Entity) daoSession
						.createCriteria(Acq_CardTransaction_Entity.class)
						.add(Restrictions.eq("id", Integer.valueOf(model.getTransactionId()))).uniqueResult();
				
				if (txnDetails != null)
				{
					logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "DAO" + "::" + "Transaction Found");
					dbTransaction = daoSession.beginTransaction();
	
					if (model.getStatusCode().equals("0")) 
					{
						txnDetails.setStatus(505);
						txnDetails.setDescription("Transaction Successful");
					}
					else
					{
						txnDetails.setStatus(504);
					}
	
					if (model.getScriptResponse() != null)
					{
						txnDetails.setScriptResult(model.getScriptResponse());
					}
	
					daoSession.update(txnDetails);
					dbTransaction.commit();
					daoResponse.setStatus(Acq_Status_Definations.OK.getId());
					daoResponse.setMessage(Acq_Status_Definations.OK.getDescription());
					daoResponse.setResult(null);
				}
				else
				{
					logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Transaction Not Found");
					daoResponse.setStatus(Acq_Status_Definations.TransactionsNotFound.getId());
					daoResponse.setMessage(Acq_Status_Definations.TransactionsNotFound.getDescription());
					daoResponse.setResult(null);
				}
				
			} catch (Exception e) {
				logger.info(
						"/TransTrac/tpos/getTMK/v1" + "::" + "DAO" + "::" + "Exception in Finding and Updating Txn Details");
				e.printStackTrace();
				if(dbTransaction.isActive()){dbTransaction.rollback();}
				daoResponse.setStatus(Acq_Status_Definations.TransactionSearchFailed.getId());
				daoResponse.setMessage(Acq_Status_Definations.TransactionSearchFailed.getDescription());
				daoResponse.setResult(null);
			} finally {
				if (daoSession.isOpen() == true || daoSession.isConnected() == true) {
					daoSession.close();
				}
			}
			return daoResponse;
	}
		
	public static String voidISOBuilder(Acq_TransTrac_VoidTxn_Model model , String callFrom) throws ISOException, IOException {

			GenericPackager isoPackager = new GenericPackager("purchaseTxn.xml");
			ISOMsg isoObj = new ISOMsg();
			isoObj.setPackager(isoPackager);
			isoObj.setMTI("0420");
			isoObj.set(2, model.getIsoData().get("DE02").toString());
			
			if (model.getTxnType().equals("CASHBACK")) {      //Coming from Device 
				isoObj.set(3, "090000"); 
			} else if (model.getTxnType().equals("CARD")) {
				isoObj.set(3, "000000"); 
			} else if (model.getTxnType().equals("CASHATPOS")) {
				isoObj.set(3, "010000"); 
			}
			
			String longAmount = String.format("%012d", Integer.valueOf(model.getAmount().replace(".", "")));
			isoObj.set(4, longAmount);   
			
			isoObj.set(7, model.getGmtDateTime());   
			isoObj.set(11, model.getStan());   
			isoObj.set(12, model.getLocalTime());
			isoObj.set(13, model.getDe13()); 
			
			if (model.getEmv().equals("")) {
				isoObj.set(22, "951"); 
			} else {
				isoObj.set(22, "901"); 

			}
			if (model.getIsoData().get("DE23") != null 
					&& model.getIsoData().get("DE23").toString().length() > 0
					&& !model.getIsoData().get("DE23").toString().equals("000")) 
			{
				isoObj.set(23, model.getIsoData().get("DE23").toString());
			}
			
			isoObj.set(25, "00"); 
			isoObj.set(32, model.getAcquirerId()); 
			isoObj.set(37, model.getRrNo()); 
			
			if (model.getIsoData().get("DE38") != null
					&& model.getIsoData().get("DE38").toString().length() > 0
					&& !model.getIsoData().get("DE38").toString().equals("000")) 
			{
				isoObj.set(38, model.getIsoData().get("DE38").toString());
			}
			
			isoObj.set(39, "17");
			isoObj.set(41, model.getTid()); 
			isoObj.set(42, model.getMid());
			isoObj.set(49, "356"); 
			
			if (model.getTxnType().equals("CASHBACK")) 
			{
				String cashBackAmount = String.format("%012d",
						Integer.valueOf(model.getAddtionalAmount().replace(".", "")));
				isoObj.set(54, "9090356D" + cashBackAmount); 
			}

			if (model.getEmv().equals("True")) {
				isoObj.set(55, model.getScriptResponse());
			}
			if (model.getDe61() != null) {
				isoObj.set(61, model.getDe61());
			}
			
			String de90 = "0200" + model.getStan() + model.getGmtDateTime() + model.getAcquirerId() + "00000000000"; // CHECK GMTDATETIME
			isoObj.set(90, de90);
			
			logISOData(isoObj, callFrom+" - voidISOBuilder");
			
			byte[] isoBytes = isoObj.pack();
			String strISOBytes = new String(isoBytes);
			return strISOBytes;
		
	}
	
	public static String reversalISOBuilder(Acq_TransTrac_Reversal_Model model) {
		try {
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			String dateTime = dateFormatGmt.format(new Date());
			String MMdd = dateTime.substring(0, 4);
			dateTime.substring(4, 10);
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			String gmtDateTime = dateFormatGmt.format(new Date());
			GenericPackager packager = new GenericPackager("purchaseTxn.xml");
			ISOMsg isoMsg = new ISOMsg();
			isoMsg.setPackager(packager);
			isoMsg.setMTI("0420");
			int hod = Integer.valueOf(dateTime.substring(4, 6));
			if (hod < 10) {
			} else {
			}
			Calendar cal = Calendar.getInstance();
			String year = "" + cal.get(Calendar.YEAR);
			year.substring(3, 4);
			int doy = cal.get(Calendar.DAY_OF_YEAR);
			if (doy < 10) {
			} else if (doy > 10 && doy < 100) {
			} else {
			}
			isoMsg.set(2, model.getIsoData().get("DE02").toString());
			// System.out.println("------------aaaaaaaaaaaa");
			if (model.getIsoData().containsKey("DE03") == false || model.getIsoData().get("DE03") == null
					|| model.getIsoData().get("DE03").equals("")) {
				isoMsg.set(3, "000000"); // procesing code
				System.out.println("------------bbbbbbbbbbbb");
			} else {
				isoMsg.set(3, model.getIsoData().get("DE03").toString());
				System.out.println("------------cccccccccccccccccc");
			}
			// String rrNo =
			// charYear+doys+hods+model.getStan();//model.getIsoData().get("DE11");
			isoMsg.set(4, model.getIsoData().get("DE04").toString()); // txn
			// amount
			isoMsg.set(7, gmtDateTime); // txn date time
			// isoMsg.set(11,
			// model.getIsoData().get("DE11").toString());//system trace audit
			// number
			System.out.println("stan is:" + model.getStan());
			isoMsg.set(11, model.getStan());
			// isoMsg.set(12, hhmmss); //Time, local transaction hhmmss
			System.out.println("de12::::::::" + model.getDe12());
			isoMsg.set(12, model.getDe12());
			isoMsg.set(13, MMdd); // Date,local transaction // MMdd
			// System.out.println("mcc:" + mcc);
			if (model.getEmv().equals("")) {
				isoMsg.set(22, "951"); // POSentrymode //TODO change according
				// to card type swipe -> 901/ chip ->
				// 951
			} else {
				isoMsg.set(22, "901"); // POSentrymode //TODO change according
				// to card type swipe -> 901/ chip ->
				// 951
			}
			if (model.getIsoData().containsKey("DE23") && model.getIsoData().get("DE23").toString().length() > 1) {
				isoMsg.set(23, model.getIsoData().get("DE23").toString());
			}
			// isoMsg.set(23, model.getIsoData().get("DE23").toString());
			// isoMsg.set(22, "901"); //POSentrymode //static
			isoMsg.set(25, "00"); // POSconditioncode // space is coming for
			// de25, have you any idea? are you
			// there?wait
			// isoMsg.set(32, "720399"); //Acquiringinstitutioncode //apending
			// 25
			// isoMsg.set(32, "00201000003");
			isoMsg.set(32, model.getAcquirerId());
			// isoMsg.set(35, reversalObject.getTrack2Data());
			// System.out.println("bbbbbbbbbbbbbbbbbbbbb");
			isoMsg.set(37, model.getRrNo());
			System.out.println("model.getDe38():::" + model.getDe38());
			if (model.getDe38() == null || model.getDe38() == "" || model.getDe38().equalsIgnoreCase("NA")) {
			} else {
				isoMsg.set(38, model.getDe38());
			}
			isoMsg.set(39, model.getIsoData().get("DE39").toString());
			System.out.println("reversal tid:::" + model.getTid());
			isoMsg.set(41, model.getTid()); // Cardacceptor terminalID
			System.out.println("reversal mid:::" + model.getMid());
			isoMsg.set(42, model.getMid());
			isoMsg.set(49, "356"); // Currencycode, transaction //it will be
			// static till new fields add
			System.out.println("ppppppppppppppp");
			String de90 = "0200" + model.getStan() + model.getIsoData().get("DE07").toString() + model.getAcquirerId()
			+ "00000000000";// "0000072039900000000000";
			System.out.println("de90:" + de90);
			if (model.getTxnType().equals("CASHBACK")) {
				int index = model.getDe54().indexOf('.');
				String cashbackAmt = model.getDe54().substring(0, index);
				// System.out.println("cashbackAmt:"+cashbackAmt);
				String cashBackAmount = String.format("%012d", Integer.valueOf(cashbackAmt));
				isoMsg.set(54, "9090356D" + cashBackAmount); // cash back amount
				// System.out.println("final cashBackAmount:"+cashBackAmount);
			}
			System.out.println("de55:" + model.getIsoData().containsKey("DE55"));
			if (model.getIsoData().containsKey("DE55") && model.getIsoData().get("DE55").toString().length() > 1) {
				isoMsg.set(55, model.getIsoData().get("DE55").toString());
			}
			System.out.println("555 55555555555");
			/*
			 * if(model.getIsoData().get("DE61")!=null&&model.getIsoData().get(
			 * "DE61").toString().length()>0&&!model.getIsoData().get("DE61").
			 * toString().equals("000")){ isoMsg.set(61,
			 * model.getIsoData().get("DE61").toString()); }
			 */
			System.out.println("model.getDe61():::" + model.getDe61());
			if (model.getDe61() == null || model.getDe61() == "") {

			} else {
				isoMsg.set(61, model.getDe61());
			}
			isoMsg.set(90, de90);
			// System.out.println("cccccccccccccccccccccccccccccccc");
			String plainIso = "";
			try {
				logISOData(isoMsg,"revesal");
				byte[] data = isoMsg.pack();
				plainIso = new String(data);
				System.out.println("plainIso:" + plainIso);
			} catch (Exception e) {
				System.err.println("error " + e);
			}
			return plainIso;
		} catch (Exception e) {
			System.err.println("erroreeeer  " + e);
			logger.error("error to parse revesal iso " + e);
			return "";
		}
	}

	private static JSONObject convertIsoToJson(ISOMsg isoData, String txnType) {
		JSONObject isoJson = new JSONObject();
		try {
			
			if(txnType.equals("KeyEx"))
			{
				if (isoData.hasField(7))
				isoJson.put("DE07", isoData.getString(7));
				
				if (isoData.hasField(11))
				isoJson.put("DE11", isoData.getString(11));
				
				if (isoData.hasField(37))
				isoJson.put("DE37", isoData.getString(37));
				
				if (isoData.hasField(39))
				{
					isoJson.put("DE39", isoData.getString(39));
					isoJson.put("responseMessage",Acq_TransTrac_Port_Connector.translateDe39(isoData.getString(39)));
				}
				
				if (isoData.hasField(61))
				isoJson.put("DE61", isoData.getString(61));
				
				if (isoData.hasField(70))
				isoJson.put("DE70", isoData.getString(70));
				
				if (isoData.hasField(98))
				isoJson.put("DE98", isoData.getString(98));			
			}
			else if(txnType.equals("KeyAck"))
			{
				if (isoData.hasField(7))
				isoJson.put("DE07", isoData.getString(7));
				
				if (isoData.hasField(11))
				isoJson.put("DE11", isoData.getString(11));
				
				if (isoData.hasField(37))
				isoJson.put("DE37", isoData.getString(37));
				
				if (isoData.hasField(39))
				{
					isoJson.put("DE39", isoData.getString(39));
					isoJson.put("responseMessage",Acq_TransTrac_Port_Connector.translateDe39(isoData.getString(39)));
				}
				
				if (isoData.hasField(44))
				isoJson.put("DE44", isoData.getString(44));
				
				if (isoData.hasField(61))
				isoJson.put("DE61", isoData.getString(61));
				
				if (isoData.hasField(70))
				isoJson.put("DE70", isoData.getString(70));
			}
			else if(txnType.equals("Dukpt"))
			{
				if (isoData.hasField(7))
				isoJson.put("DE07", isoData.getString(7));
				
				if (isoData.hasField(11))
				isoJson.put("DE11", isoData.getString(11));
				
				if (isoData.hasField(37))
				isoJson.put("DE37", isoData.getString(37));
				
				if (isoData.hasField(39))
				{
					isoJson.put("DE39", isoData.getString(39));
					isoJson.put("responseMessage",Acq_TransTrac_Port_Connector.translateDe39(isoData.getString(39)));
				}
				
				if (isoData.hasField(42))
				isoJson.put("DE42", isoData.getString(42));
				
				if (isoData.hasField(63))
				isoJson.put("DE63", isoData.getString(63));
				
				if (isoData.hasField(70))
				isoJson.put("DE70", isoData.getString(70));
				
				if (isoData.hasField(98))
				isoJson.put("DE98", isoData.getString(98));			
			}
			
			else if(txnType.equals("DukptAck"))
			{
				if (isoData.hasField(7))
				isoJson.put("DE07", isoData.getString(7));
				
				if (isoData.hasField(11))
				isoJson.put("DE11", isoData.getString(11));
				
				if (isoData.hasField(37))
				isoJson.put("DE37", isoData.getString(37));
				
				if (isoData.hasField(39))
				{
					isoJson.put("DE39", isoData.getString(39));
					isoJson.put("responseMessage",Acq_TransTrac_Port_Connector.translateDe39(isoData.getString(39)));
				}
				
				if (isoData.hasField(44))
				isoJson.put("DE44", isoData.getString(44));
				
				if (isoData.hasField(61))
					isoJson.put("DE61", isoData.getString(61));
				
				if (isoData.hasField(70))
				isoJson.put("DE70", isoData.getString(70));
					
			}
			else if(txnType.equals("doPurchase"))
			{
				if (isoData.hasField(2))
				isoJson.put("DE02", isoData.getString(2));
				
				if (isoData.hasField(7))
				isoJson.put("DE07", isoData.getString(7));
				
				if (isoData.hasField(11))
				isoJson.put("DE11", isoData.getString(11));
				
				if (isoData.hasField(12))
				isoJson.put("DE12", isoData.getString(12));
				
				if (isoData.hasField(37))
				isoJson.put("DE37", isoData.getString(37));
				
				if (isoData.hasField(38))
					isoJson.put("DE38", isoData.getString(38));
				
				if (isoData.hasField(39))
				{
					isoJson.put("DE39", isoData.getString(39));
					isoJson.put("responseMessage",Acq_TransTrac_Port_Connector.translateDe39(isoData.getString(39)));
				}
				
				if (isoData.hasField(55))
					isoJson.put("DE55", isoData.getString(55));
				
				if (isoData.hasField(61))
					isoJson.put("DE61", isoData.getString(61));
					
			}
			else if(txnType.equals("doCashAtPos"))
			{
				if (isoData.hasField(2))
				isoJson.put("DE02", isoData.getString(2));
				
				if (isoData.hasField(7))
				isoJson.put("DE07", isoData.getString(7));
				
				if (isoData.hasField(11))
				isoJson.put("DE11", isoData.getString(11));
				
				if (isoData.hasField(12))
				isoJson.put("DE12", isoData.getString(12));
				
				if (isoData.hasField(37))
				isoJson.put("DE37", isoData.getString(37));
				
				if (isoData.hasField(38))
					isoJson.put("DE38", isoData.getString(38));
				
				if (isoData.hasField(39))
				{
					isoJson.put("DE39", isoData.getString(39));
					isoJson.put("responseMessage",Acq_TransTrac_Port_Connector.translateDe39(isoData.getString(39)));
				}
				
				if (isoData.hasField(55))
					isoJson.put("DE55", isoData.getString(55));
				
				if (isoData.hasField(61))
					isoJson.put("DE61", isoData.getString(61));
			}
			else if(txnType.equals("doVoid"))
			{
				if (isoData.hasField(2))
				isoJson.put("DE02", isoData.getString(2));
				
				if (isoData.hasField(37))
				isoJson.put("DE37", isoData.getString(37));
				
				if (isoData.hasField(38))
					isoJson.put("DE38", isoData.getString(38));
				
				if (isoData.hasField(39))
				{
					isoJson.put("DE39", isoData.getString(39));
					isoJson.put("responseMessage",Acq_TransTrac_Port_Connector.translateDe39(isoData.getString(39)));
				}
				
				if (isoData.hasField(55))
					isoJson.put("DE55", isoData.getString(55));
			}
			else if(txnType.equals("doReversal"))
			{
				if (isoData.hasField(2))
				isoJson.put("DE02", isoData.getString(2));
				
				if (isoData.hasField(37))
				isoJson.put("DE37", isoData.getString(37));
				
				if (isoData.hasField(38))
					isoJson.put("DE38", isoData.getString(38));
				
				if (isoData.hasField(39))
				{
					isoJson.put("DE39", isoData.getString(39));
					isoJson.put("responseMessage",Acq_TransTrac_Port_Connector.translateDe39(isoData.getString(39)));
				}
				
				if (isoData.hasField(55))
					isoJson.put("DE55", isoData.getString(55));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isoJson;
	}

	public static JSONObject translateResponseISO(String responseISO, String reqType, String callFrom) {
				
		try {// Unpacking Response ISO
			GenericPackager isoUnpackager;
			String firstBitmap = responseISO.substring(4, 20); // Excluding MTI, Extracting Bitmap
			String bitmap1Binary = convertHexToBinary(firstBitmap); //Converting Bitmap to Binary
			String secondBitmap = "";
			if (bitmap1Binary.startsWith("1")) { // Secondary Bitmap Present
			secondBitmap = responseISO.substring(4, 36); // Extracting Secondary Bitmap
			secondBitmap = convertHexToBinary(secondBitmap); //Converting Bitmap to Binary
			}
			logger.info(callFrom + "::" + "DAO - translateResponseISO" + "::" + "secondBitmap" + "::" + secondBitmap);
			
			ISOMsg isoMessage = new ISOMsg();
			if (reqType.equals("doPurchase")) {
				isoUnpackager = new GenericPackager("purchaseTxn.xml");
				isoMessage.setPackager(isoUnpackager);
				isoMessage.unpack(responseISO.getBytes());
				logISOData(isoMessage, callFrom);
				return convertIsoToJson(isoMessage, "doPurchase");
				
			} else if (reqType.equals("doCashAtPos")) {
				isoUnpackager = new GenericPackager("purchaseTxn.xml");
				isoMessage.setPackager(isoUnpackager);
				isoMessage.unpack(responseISO.getBytes());
				logISOData(isoMessage, callFrom);
				return convertIsoToJson(isoMessage, "doCashAtPos");
			
			} else if (reqType.equals("doVoid")) {
				isoUnpackager = new GenericPackager("purchaseTxn.xml");
				isoMessage.setPackager(isoUnpackager);
				isoMessage.unpack(responseISO.getBytes());
				logISOData(isoMessage, callFrom);
				return convertIsoToJson(isoMessage, "doVoid");
			}else if (reqType.equals("doReversal")) {
				isoUnpackager = new GenericPackager("purchaseTxn.xml");
				isoMessage.setPackager(isoUnpackager);
				isoMessage.unpack(responseISO.getBytes());
				logISOData(isoMessage, callFrom);
				return convertIsoToJson(isoMessage, "doReversal");
			}else if (reqType.equals("doCashBack")) {
				isoUnpackager = new GenericPackager("purchaseTxn.xml");
				isoMessage.setPackager(isoUnpackager);
				isoMessage.unpack(responseISO.getBytes());
				return logISOMssCashBack(isoMessage);
			}
		} catch (Exception e) {
			logger.info(callFrom + "::" + "DAO - translateResponseISO" + "::" + "Error In Unpacking Response ISO");
			e.printStackTrace();
		}
		return null;
	}
	
	private static JSONObject logISOMssCashBack(ISOMsg msg) { //TODO Need to Remove
		JSONObject json = new JSONObject();
		System.out.println("----ISO MESSAGE-----");
		try {
			System.out.println("  MTI : " + msg.getMTI());
			for (int i = 1; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-" + i + " : " + msg.getString(i));
					if (i == 2)
						json.put("DE02", msg.getString(i));
					if (i == 7)
						json.put("DE07", msg.getString(i));
					if (i == 11)
						json.put("DE11", msg.getString(i));
					if (i == 12)
						json.put("DE12", msg.getString(i));
					if (i == 37)
						json.put("DE37", msg.getString(i));
					if (i == 38)
						json.put("DE38", msg.getString(i));
					if (i == 39) {
						json.put("DE39", msg.getString(i));
						json.put("responseMessage", Acq_TransTrac_Port_Connector.translateDe39(msg.getString(i)));
					}
					if (i == 54)
						json.put("DE54", msg.getString(i));
					if (i == 55)
						json.put("DE55", msg.getString(i));
					if (i == 61)
						json.put("DE61", msg.getString(i));
				}
			}
			if (!json.containsKey("DE38"))
				json.put("DE38", "");
			if (!json.containsKey("DE55"))
				json.put("DE55", "");
			System.out.println("json obj:" + json);
		} catch (Exception e) {
			System.err.println("errr in parse cashback response iso " + e);
		} finally {
			System.out.println("--------------------");
		}
		return json;
	}

	public static String purchaseDE22Builder(String isEMV, String cardInterchange, String isFallback) throws ISOException, IOException {
		
		if (isEMV.equalsIgnoreCase("true")) {
			if (cardInterchange.equals(Acq_TransTrac_Port_Connector.VISA)
					|| cardInterchange.equals(Acq_TransTrac_Port_Connector.MASTER)
					|| cardInterchange.equals(Acq_TransTrac_Port_Connector.MAESTRO)) {
				return "051"; 
			} else {
				return "951"; 
			}
		} else {
			if (isFallback.equalsIgnoreCase("true")) {
				if (cardInterchange.equals(Acq_TransTrac_Port_Connector.RUPAY)
						|| cardInterchange.equals(Acq_TransTrac_Port_Connector.DISCOVER)
						||cardInterchange.equals(Acq_TransTrac_Port_Connector.DINERS)
						|| cardInterchange.equals(Acq_TransTrac_Port_Connector.MASTER)
						||cardInterchange.equals(Acq_TransTrac_Port_Connector.MAESTRO)) {
					return "801"; 
				} else {
					return "901"; 
				}
			} else {
				return "901";
			}
		}
	}
	
	public static String purchaseISOBuilder(Acq_TransTrac_Purchase_Model model, String txnType) throws ISOException, IOException {
		
			
		GenericPackager isoPackager = new GenericPackager("purchaseTxn.xml");
		ISOMsg isoObj = new ISOMsg();
		isoObj.setPackager(isoPackager);
		isoObj.setMTI("0200");
		isoObj.set(2, model.getIsoData().get("DE02").toString());
		
		if(txnType.equals("doPurchase")){
			isoObj.set(3, "000000"); // Processing Code For Purchase
		}
		else if(txnType.equals("doCashAtPos")){
			isoObj.set(3, "010000"); // Processing Code For Purchase
		}
		isoObj.set(4, model.getIsoData().get("DE04").toString()); // Transaction Amount
		isoObj.set(7, model.getGmtDateTime()); 
		isoObj.set(11, model.getStan());
		isoObj.set(12, model.getDe12());
		isoObj.set(13, model.getDe13()); // Date,local transaction // MMdd
		isoObj.set(14, model.getIsoData().get("DE14").toString()); // Expiry Date		
		isoObj.set(22, purchaseDE22Builder(model.getEmv(),model.getCardType(),model.getFallBack())); 
		isoObj.set(23, "0" + model.getIsoData().get("DE23").toString());
		isoObj.set(25, "00"); 
		isoObj.set(32, model.getAcquirerId());
		isoObj.set(35, model.getIsoData().get("DE35").toString()); // Track2 Data
		isoObj.set(37, model.getRrNo());
		isoObj.set(40, model.getIsoData().get("DE40").toString());
		isoObj.set(41, model.getTid());
		isoObj.set(42, model.getMid());
		isoObj.set(49, "356"); // Currency Code 
		
		
		if (model.getPinEntered().equalsIgnoreCase("true")) {
			String pinblock = model.getIsoData().get("DE52").toString();
			if (!pinblock.equals("0000000000000000")) 
			{
				isoObj.set(52, pinblock); // Pin Block
				if (model.getCardType().equals(Acq_TransTrac_Port_Connector.VISA)) {
					isoObj.set(53, "2001010100000000"); // For Visa Only
				}
			}
		}
		
		
		if(txnType.equals("doCashAtPos")){
			if (model.getCardType().equals(Acq_TransTrac_Port_Connector.VISA)
					|| model.getCardType().equals(Acq_TransTrac_Port_Connector.MASTER)) {
				if (!model.getMcc().equals("6010") && !model.getMcc().equals("6011")) {
					isoObj.set(54, "9090356D" + model.getIsoData().get("DE04")); // cash back amount
				}
			}
		}
		
		if (!(model.getIsoData().get("DE55") == null || model.getIsoData().get("DE55").toString() == "")) {
			isoObj.set(55, model.getIsoData().get("DE55").toString());
		} 

		isoObj.set(61, model.getDe61());
		isoObj.set(106, model.getIsoData().get("DE106").toString());
		logISOData(isoObj, txnType+" - purchaseISOBuilder");
		byte[] isoBytes = isoObj.pack();
		String strISOBytes = new String(isoBytes);
		return strISOBytes;
	}
	
	static String translateCardType(String card) {  //TODO need to change to CDT and HDT 
		String cardNo = card.replaceAll("X", "0");
		String regVisa = "^4[0-9]{12}(?:[0-9]{3})?$";
		String regMaster = "^5[1-5][0-9]{14}$";
		String regExpress = "^3[47][0-9]{13}$";
		String regDiners = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
		String regDiscover = "^6(?:011|5[0-9]{2})[0-9]{12}$";
		String regJCB = "^(?:2131|1800|35\\d{3})\\d{11}$";
		String regRupay = "^6[0-9]{15}$";
		String regMaestro = "^(?:5[0678]\\d\\d|6304|6390|67\\d\\d)\\d{8,15}$";
		if (cardNo.matches(regVisa))
			return "Visa";
		if (cardNo.matches(regMaster))
			return "MasterCard";
		if (cardNo.matches(regExpress))
			return "American Express";
		if (cardNo.matches(regDiners))
			return "DINERS";
		if (cardNo.matches(regDiscover))
			return "Discover";
		if (cardNo.matches(regJCB))
			return "JCB";
		if (cardNo.matches(regRupay))
			return "Rupay";
		if (cardNo.matches(regMaestro))
			return "Maestro";
		return "";
	}

	private static String convertHexToBinary(String data) {

		BigInteger radix16 = new BigInteger(data, 16); // Converting String Data to BigInteger with Radix 16
		String returnValue = radix16.toString(2); // String representation of this BigInteger with radix 2
		return returnValue;
	}

	private static void logISOData(ISOMsg isoData, String callFrom) {

		logger.info(callFrom + "::" + "logISOMasssge" + "::" + "Printing ISO Message Begin");
		try {
			logger.info(callFrom + "::" + "MTI : " + isoData.getMTI());
			for (int fieldCount = 1; fieldCount <= isoData.getMaxField(); fieldCount++) {
				if (isoData.hasField(fieldCount)) {
					logger.info(callFrom + "::" + "DE-" + fieldCount + " : " + isoData.getString(fieldCount));
				}
			}
		} catch (ISOException e) {
			logger.info(callFrom + "::" + "logISOMasssge" + "::" + "Error in Printing ISO Message");
			e.printStackTrace();
		}
		logger.info(callFrom + "::" + "logISOMasssge" + "::" + "Printing ISO Message End");

	}

}
