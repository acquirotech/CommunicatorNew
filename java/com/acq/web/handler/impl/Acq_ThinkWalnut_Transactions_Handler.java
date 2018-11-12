package com.acq.web.handler.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.acq.web.controller.model.Acq_TW_OperatorList_Model;
import com.acq.web.controller.model.Acq_TW_DoRecharge_Model;
import com.acq.web.controller.model.Acq_TW_TxnStatus_Model;
import com.acq.web.dao.Acq_ThinkWalnut_Transactions_Dao_Inf;
import com.acq.web.dto.impl.DbDto2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.Acq_ThinkWalnut_Transactions_Handler_Inf;

	@Service
	public class Acq_ThinkWalnut_Transactions_Handler implements Acq_ThinkWalnut_Transactions_Handler_Inf{
		final static Logger logger = Logger.getLogger(Acq_ThinkWalnut_Transactions_Handler.class);
		
		
		@Autowired
		Acq_ThinkWalnut_Transactions_Dao_Inf Acq_TW_Transactions_Dao;
		
		@Autowired
		MessageSource resource;
		
		
		
		@Override
		public ServiceDto2<Object> getOperators(Acq_TW_OperatorList_Model model) {
			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_TW_Transactions_Dao.getOperators(model);
			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
		
		@Override
		public ServiceDto2<Object> doRecharge(Acq_TW_DoRecharge_Model model) {
			logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_TW_Transactions_Dao.doRecharge(model);
			logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
		
		@Override
		public ServiceDto2<Object> getBalance(Acq_TW_OperatorList_Model model) {
			
			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_TW_Transactions_Dao.getBalance(model);
			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;
		}
		
		
		@Override
		public ServiceDto2<Object> getTxnStatus(Acq_TW_TxnStatus_Model model) {
			logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_TW_Transactions_Dao.getTxnStatus(model);
			logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;	
		}
		
		
		
		@Override
		public ServiceDto2<Object> getTxnList(Acq_TW_OperatorList_Model model) {
			
			logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Handler" + "::" + "Begin");
			ServiceDto2<Object>  handlerResponse = new ServiceDto2<Object>();
			
			logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Handler" + "::" + "Forwarded to DAO");
			DbDto2<Object>  daoResponse = Acq_TW_Transactions_Dao.getTxnList(model);
			logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Handler" + "::" + "Return From DAO");
			handlerResponse.setStatusCode(daoResponse.getStatusCode());
			handlerResponse.setBody(daoResponse.getBody());
			handlerResponse.setStatusMessage(daoResponse.getStatusMessage());
			logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Handler" + "::" + "Returning to Controller");
			return handlerResponse;	
		}
		
}
