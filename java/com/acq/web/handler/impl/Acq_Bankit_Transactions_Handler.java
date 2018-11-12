package com.acq.web.handler.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.acq.web.controller.model.Acq_Bankit_PrepaidLoad_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_TxnList_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_AddBene_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_AddSender_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_BnkList_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_TxnStatus_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_Neft_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_sendOTP_Model;
import com.acq.web.dao.Acq_Bankit_Transactions_DAO_Inf;
import com.acq.web.dto.impl.DbDto2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.Acq_Bankit_Transactions_Handler_Inf;

	@Service
	public class Acq_Bankit_Transactions_Handler implements Acq_Bankit_Transactions_Handler_Inf{
		final static Logger logger = Logger.getLogger(Acq_Bankit_Transactions_Handler.class);
		
		@Autowired
		Acq_Bankit_Transactions_DAO_Inf Acq_Bankit_Transactions_DAO;
	
		@Autowired
		MessageSource resource;
		
		
		@Override
		public ServiceDto2<Object> addSender(Acq_Bankit_DMT_AddSender_Model model) {

			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.addSender(model);
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Handler" + "::" + "Return From DAO");
			
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
		
		@Override
		public ServiceDto2<Object> addbene(Acq_Bankit_DMT_AddBene_Model model) {
			logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.addBene(model);
			logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Handler" + "::" + "Return From DAO");
			
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
		
		@Override
		public ServiceDto2<Object> sendOtp(Acq_Bankit_DMT_sendOTP_Model model) {
			logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.sendOtp(model);
			logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Handler" + "::" + "Return From DAO");
			
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
	
		
		@Override
		public ServiceDto2<Object> getBeneList(Acq_Bankit_DMT_sendOTP_Model model) {
			logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.getBeneList(model);
			logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Handler" + "::" + "Return From DAO");
			
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
		
		@Override
		public ServiceDto2<Object> deleteBene(Acq_Bankit_DMT_Neft_Model model) {
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.deleteBene(model);
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
		
		@Override
		public ServiceDto2<Object> doNeft(Acq_Bankit_DMT_Neft_Model model) {
			logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.doNeft(model);
			logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
		
		@Override
		public ServiceDto2<Object> doImps(Acq_Bankit_DMT_Neft_Model model) {
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.doImps(model);
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;			
		}
		
		
		@Override
		public ServiceDto2<Object> getTxnList(Acq_Bankit_DMT_TxnList_Model model) {
			logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.getTxnList(model);
			logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
		
		@Override
		public ServiceDto2<Object> getTxnStatus(Acq_Bankit_DMT_TxnStatus_Model model) {
			
			logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.getTxnStatus(model);
			logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
	
		@Override
		public ServiceDto2<Object> getBankList(Acq_Bankit_DMT_BnkList_Model model) {
			logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.getBankList(model);
			logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
			
		//--------------- Need To Fix Below This --------------------------
				
		@Override
		public ServiceDto2<Object> cardLoad(Acq_Bankit_PrepaidLoad_Model model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.cardLoad(model);
				
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get dmt cardLoad handler");		 
			}catch(Exception e){
				logger.error("Error in get dmt cardLoad handler "+e);
			}
			return response;
		}	
		
		@Override
		public ServiceDto2<Object> cardLoadStatus(Acq_Bankit_PrepaidLoad_Model model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.cardLoadStatus(model);
				
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get dmt cardLoadStatus handler");		 
			}catch(Exception e){
				logger.error("Error in get dmt cardLoadStatus handler "+e);
			}
			return response;
		}
		
		
		@Override
		public ServiceDto2<Object> getCardBal(Acq_Bankit_PrepaidLoad_Model model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = Acq_Bankit_Transactions_DAO.getCardBal(model);
				
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get dmt getCardBal handler");		 
			}catch(Exception e){
				logger.error("Error in get dmt getCardBal handler "+e);
			}
			return response;
		}
	
		
		
		
		
		
		
		
		
		
	
		


		
		
}
