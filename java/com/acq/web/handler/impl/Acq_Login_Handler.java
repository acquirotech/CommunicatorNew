package com.acq.web.handler.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.acq.Acq_Status_Definations;
import com.acq.web.controller.model.Acq_TposLogin_Model;
import com.acq.web.dao.LoginDaoInf;
import com.acq.web.dto.impl.DbDto2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.Acq_Login_Handler_Inf;

@Service
public class Acq_Login_Handler implements Acq_Login_Handler_Inf {

	private static Logger logger = Logger.getLogger(Acq_Login_Handler.class);

	@Autowired
	LoginDaoInf loginDao;

	@Autowired
	MessageSource resource;

	@Value("#{acqConfig['apphost.location']}")
	private String appHost;

	public String getAppHost() {
		return appHost;
	}

	@SuppressWarnings("unchecked")
	public ServiceDto2<Object> loginTposV1(Acq_TposLogin_Model model) {
		ServiceDto2<Object> response = new ServiceDto2<Object>();
		logger.info("tpos/login/v1" + "::" + "Hanlder" + "::" + "Begin");
		try {
			logger.info("tpos/login/v1" + "::" + "Hanlder" + "::" + "Forwarded To DAO");
			DbDto2<Object> daoResponse = loginDao.loginTposV1(model);
			logger.info("tpos/login/v1" + "::" + "Hanlder" + "::" + "Return From DAO");

			Map<String, String> responseBody = (Map<String, String>) daoResponse.getBody();
			if (responseBody == null) {
				logger.info("tpos/login/v1" + "::" + "Hanlder" + "::" + "daoResponse Body Empty");
				response.setStatusMessage(daoResponse.getStatusMessage());
				response.setStatusCode(daoResponse.getStatusCode());
				response.setBody(null);
			} else {
				logger.info("tpos/login/v1" + "::" + "Hanlder" + "::" + "daoResponse Body Not Empty");
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("sessionId", responseBody.get("sessionId"));
				jsonResponse.put("TID", responseBody.get("banktid"));
				jsonResponse.put("MID", responseBody.get("merchantBankMID"));
				jsonResponse.put("keyExchange", responseBody.get("keyExchange"));
				response.setStatusMessage(daoResponse.getStatusMessage());
				response.setStatusCode(daoResponse.getStatusCode());
				response.setBody(jsonResponse);
			}
		} catch (Exception e) {
			logger.info("tpos/login/v1" + "::" + "Hanlder" + "::" + "unexpected error");
			e.printStackTrace();
			response.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			response.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			response.setBody(null);
		}
		return response;
	}

}
