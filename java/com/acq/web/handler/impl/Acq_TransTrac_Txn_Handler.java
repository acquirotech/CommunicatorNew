package com.acq.web.handler.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acq.Acq_Status_Definations;
import com.acq.Acq_TransTrac_IsoValidator;
import com.acq.web.controller.model.Acq_TransTrac_GetTMK_Model;
import com.acq.web.controller.model.Acq_TransTrac_Reversal_Model;
import com.acq.web.controller.model.Acq_PurchaseAck_Model;
import com.acq.web.controller.model.Acq_TransTrac_Purchase_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxnList_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxn_Model;
import com.acq.web.dao.Acq_TransTrac_Txn_DAO_Inf;
import com.acq.web.dto.impl.DbDto;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.handler.Acq_TransTrac_Txn_Handler_Inf;

@Service
public class Acq_TransTrac_Txn_Handler implements Acq_TransTrac_Txn_Handler_Inf {

	private static Logger logger = Logger.getLogger(Acq_TransTrac_Txn_Handler.class);

	@Autowired
	Acq_TransTrac_Txn_DAO_Inf Acq_TransTrac_Txn_DAO;

	@Override
	public ServiceDto<Object> getTMK(Acq_TransTrac_GetTMK_Model model) {
		logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Handler" + "::" + "Begin");
		ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
		logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
		DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.getTMK(model);
		logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Handler" + "::" + "Return From DAO");
		handlerResponse.setMessage(daoResponse.getMessage());
		handlerResponse.setResult(daoResponse.getResult());
		handlerResponse.setStatus(daoResponse.getStatus());
		logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Handler" + "::" + "Returning to Controller");
		return handlerResponse;
	}

	
	@Override
	public ServiceDto<Object> sendTMKACK(Acq_TransTrac_GetTMK_Model model) {
		logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "Handler" + "::" + "Begin");
		ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
		logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
		DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.sendTMKACK(model);
		logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "Handler" + "::" + "Return From DAO");
		handlerResponse.setMessage(daoResponse.getMessage());
		handlerResponse.setResult(daoResponse.getResult());
		handlerResponse.setStatus(daoResponse.getStatus());
		logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "Handler" + "::" + "Returning to Controller");
		return handlerResponse;
	}
	
	
	@Override
	public ServiceDto<Object> getDUKPT(Acq_TransTrac_GetTMK_Model model) {
		logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Handler" + "::" + "Begin");
		ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
		logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
		DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.getDUKPT(model);
		logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Handler" + "::" + "Return From DAO");
		handlerResponse.setMessage(daoResponse.getMessage());
		handlerResponse.setResult(daoResponse.getResult());
		handlerResponse.setStatus(daoResponse.getStatus());
		logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Handler" + "::" + "Returning to Controller");
		return handlerResponse;
	}
	
	
	@Override
	public ServiceDto<Object> sendDUKPTACK(Acq_TransTrac_GetTMK_Model model) {
		logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Handler" + "::" + "Begin");
		ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
		logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
		DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.sendDUKPTACK(model);
		logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Handler" + "::" + "Return From DAO");
		handlerResponse.setMessage(daoResponse.getMessage());
		handlerResponse.setResult(daoResponse.getResult());
		handlerResponse.setStatus(daoResponse.getStatus());
		logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Handler" + "::" + "Returning to Controller");
		return handlerResponse;
	}
	
	
	@Override
	public ServiceDto<Object> doPurchase(Acq_TransTrac_Purchase_Model model) {
		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Begin");
		ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
		
		if (!Acq_TransTrac_IsoValidator.isDE02(model.getIsoData().get("DE02").toString())) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE02");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE02");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isKsn(model.getIsoData().get("ksn").toString())) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid KSN");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid KSN");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE04(model.getIsoData().get("DE04").toString())) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE04");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE04");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE11(model.getIsoData().get("DE11").toString())) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE11");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE11");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE14(model.getIsoData().get("DE14").toString())) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE14");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE14");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE23(model.getIsoData().get("DE23").toString()) ) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE23");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE23");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE35(model.getIsoData().get("DE35").toString()) ) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE35");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE35");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE40(model.getIsoData().get("DE40").toString()) ) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE40");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE40");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE52(model.getIsoData().get("DE52").toString())) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE52");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE52");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE55(model.getIsoData().get("DE55").toString())) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE55");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE55");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE106(model.getIsoData().get("DE106").toString())) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Invalid DE106");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE106");
			handlerResponse.setResult(null);
			return handlerResponse;
		}

		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
		DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.doPurchase(model);
		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Return From DAO");
		handlerResponse.setMessage(daoResponse.getMessage());
		handlerResponse.setResult(daoResponse.getResult());
		handlerResponse.setStatus(daoResponse.getStatus());
		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Returning to Controller");
		return handlerResponse;
	}
	
	
	@Override
	public ServiceDto<Object> doCashAtPos(Acq_TransTrac_Purchase_Model model) {

			logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
			
			if (!Acq_TransTrac_IsoValidator.isDE02(model.getIsoData().get("DE02").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE02");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE02");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isKsn(model.getIsoData().get("ksn").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid KSN");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid KSN");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isDE04(model.getIsoData().get("DE04").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE04");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE04");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isDE11(model.getIsoData().get("DE11").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE11");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE11");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isDE14(model.getIsoData().get("DE14").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE14");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE14");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isDE23(model.getIsoData().get("DE23").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE23");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE23");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isDE35(model.getIsoData().get("DE35").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE35");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE35");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isDE40(model.getIsoData().get("DE40").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE40");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE40");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isDE52(model.getIsoData().get("DE52").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE52");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE52");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isDE55(model.getIsoData().get("DE55").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE55");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE55");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			else if (!Acq_TransTrac_IsoValidator.isDE106(model.getIsoData().get("DE106").toString())) {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Invalid DE106");
				handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
				handlerResponse.setMessage("Invalid DE106");
				handlerResponse.setResult(null);
				return handlerResponse;
			}
			logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.doCashAtPos(model);
			logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setMessage(daoResponse.getMessage());
			handlerResponse.setResult(daoResponse.getResult());
			handlerResponse.setStatus(daoResponse.getStatus());
			logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
	}

	
	@Override
	public ServiceDto<Object> doVoid(Acq_TransTrac_VoidTxn_Model model) {
		
		logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Handler" + "::" + "Begin");
		ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
		
		if (!Acq_TransTrac_IsoValidator.isDE02(model.getIsoData().get("DE02").toString())) {
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Handler" + "::" + "Invalid DE02");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE02");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isKsn(model.getIsoData().get("ksn").toString())) {
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Handler" + "::" + "Invalid KSN");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid KSN");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE04(model.getIsoData().get("DE04").toString())) {
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Handler" + "::" + "Invalid DE04");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE04");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE11(model.getIsoData().get("DE11").toString())) {
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Handler" + "::" + "Invalid DE11");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE11");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (! Acq_TransTrac_IsoValidator.isDE23(model.getIsoData().get("DE23").toString())) {
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Handler" + "::" + "Invalid DE23");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE23");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		
		logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
		DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.doVoid(model);
		logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Handler" + "::" + "Return From DAO");
		handlerResponse.setMessage(daoResponse.getMessage());
		handlerResponse.setResult(daoResponse.getResult());
		handlerResponse.setStatus(daoResponse.getStatus());
		logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Handler" + "::" + "Returning to Controller");
		return handlerResponse;
	}
	

	@Override
	public ServiceDto<Object> getVoidList(Acq_TransTrac_VoidTxnList_Model model) {
		
		logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Handler" + "::" + "Begin");
		ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
		logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
		DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.getVoidList(model);
		logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Handler" + "::" + "Return From DAO");
		handlerResponse.setMessage(daoResponse.getMessage());
		handlerResponse.setResult(daoResponse.getResult());
		handlerResponse.setStatus(daoResponse.getStatus());
		logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Handler" + "::" + "Returning to Controller");
		return handlerResponse;
	}
	
	
	@Override
	public ServiceDto<Object> doReversal(Acq_TransTrac_Reversal_Model model) {
		logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Begin");
		ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
		
		if (!Acq_TransTrac_IsoValidator.isDE02(model.getIsoData().get("DE02").toString())) {
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Invalid DE02");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE02");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isKsn(model.getIsoData().get("ksn").toString())) {
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Invalid KSN");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid KSN");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE04(model.getIsoData().get("DE04").toString())) {
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Invalid DE04");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE04");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
//		if (!Acq_TransTrac_IsoValidator.isDE07(model.getIsoData().get("DE04").toString()) {
//			//TODO Put Date Validation for DE07
//		}
		else if (!Acq_TransTrac_IsoValidator.isDE11(model.getIsoData().get("DE11").toString())) {
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Invalid DE11");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE11");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE23(model.getIsoData().get("DE23").toString())) {
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Invalid DE23");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE23");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE39(model.getIsoData().get("DE39").toString())) {
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Invalid DE39");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE39");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		else if (!Acq_TransTrac_IsoValidator.isDE55(model.getIsoData().get("DE55").toString())) {
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Invalid DE55");
			handlerResponse.setStatus(Acq_Status_Definations.InvalidCrediential.getId());
			handlerResponse.setMessage("Invalid DE55");
			handlerResponse.setResult(null);
			return handlerResponse;
		}
		
		logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
		DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.doReversal(model);
		logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Return From DAO");
		handlerResponse.setMessage(daoResponse.getMessage());
		handlerResponse.setResult(daoResponse.getResult());
		handlerResponse.setStatus(daoResponse.getStatus());
		logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Handler" + "::" + "Returning to Controller");
		return handlerResponse;
	}
	

	@Override
	public ServiceDto<Object> sendPurchaseAck(Acq_PurchaseAck_Model model) {
		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Begin");
		ServiceDto<Object> handlerResponse = new ServiceDto<Object>();
		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
		DbDto<Object> daoResponse = Acq_TransTrac_Txn_DAO.sendPurchaseAck(model);
		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Return From DAO");
		handlerResponse.setMessage(daoResponse.getMessage());
		handlerResponse.setResult(daoResponse.getResult());
		handlerResponse.setStatus(daoResponse.getStatus());
		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Handler" + "::" + "Returning to Controller");
		return handlerResponse;
	}


	


	

	

}
