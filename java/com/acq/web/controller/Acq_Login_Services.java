package com.acq.web.controller;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acq.Acq_Status_Definations;
import com.acq.web.controller.model.Acq_TposLogin_Model;
import com.acq.web.dto.impl.ControllerResponse;
import com.acq.web.dto.impl.ControllerResponse2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.Acq_Login_Handler_Inf;

@Controller
@RequestMapping(value = "/")
public class Acq_Login_Services {

	@Autowired
	Acq_Login_Handler_Inf loginHanler;

	@Value("#{acqConfig['loggers.location']}")
	private String loginLocation;

	public String getLoginLocation() {
		return loginLocation;
	}

	final static Logger logger = Logger.getLogger(Acq_Login_Services.class);

	// Login Service Using Form Data
	@RequestMapping(value = "tpos/login/v1", method = RequestMethod.POST)
	public @ResponseBody ControllerResponse2<Object> logintTposFormData(HttpServletRequest request,
			Acq_TposLogin_Model model) {

		logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TposLogin_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_TposLogin_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TposLogin_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			}  
			else 
			{
				logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = loginHanler.loginTposV1(model);
				logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Return From Handler");

				if (daoResponse.getStatusCode() == Acq_Status_Definations.Authenticated.getId()) {
					// User Authenticated For Login

					HttpSession session = request.getSession();
					session.setAttribute("uname", model.getLoginId());
					session.setAttribute("userid", daoResponse.getStatusMessage());
					logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Session Created");

					controllerResponse.setStatusCode(daoResponse.getStatusCode());
					controllerResponse.setStatusMessage(daoResponse.getStatusMessage());

					if (daoResponse.getBody() != null) {
						controllerResponse.setBody(daoResponse.getBody());
					} else {
						controllerResponse.setBody(null);
					}
				} else {
					// User Not Authenticated For Login
					controllerResponse.setStatusCode(daoResponse.getStatusCode());
					controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
					controllerResponse.setBody(null);
				}
			}
		} catch (Exception e) {
			logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}

	
	// Login Service Using JSON Data
	@RequestMapping(value = "tpos/login/v2", method = RequestMethod.POST)
	public @ResponseBody ControllerResponse2<Object> logintTposJsonData(HttpServletRequest request,
			@RequestBody final Acq_TposLogin_Model model) {

		logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TposLogin_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_TposLogin_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TposLogin_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			}  
			else 
			{
				logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = loginHanler.loginTposV1(model);
				logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Return From Handler");

				if (daoResponse.getStatusCode() == Acq_Status_Definations.Authenticated.getId()) {
					// User Authenticated For Login

					HttpSession session = request.getSession();
					session.setAttribute("uname", model.getLoginId());
					session.setAttribute("userid", daoResponse.getStatusMessage());
					logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Session Created");

					controllerResponse.setStatusCode(daoResponse.getStatusCode());
					controllerResponse.setStatusMessage(daoResponse.getStatusMessage());

					if (daoResponse.getBody() != null) {
						controllerResponse.setBody(daoResponse.getBody());
					} else {
						controllerResponse.setBody(null);
					}
				} else {
					// User Not Authenticated For Login
					controllerResponse.setStatusCode(daoResponse.getStatusCode());
					controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
					controllerResponse.setBody(null);
				}
			}
		} catch (Exception e) {
			logger.info("tpos/login/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	
	@RequestMapping(value = "tpos/logout/v1", method = RequestMethod.GET)
	public @ResponseBody ControllerResponse<Object> tryLogout(HttpServletRequest request) {
		logger.info("logout/v1" + "::" + "Begin");
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		HttpSession session = request.getSession();
		session.invalidate();
		logger.info("logout/v1" + "::" + "Session Invalidated");
		response.setStatus(Acq_Status_Definations.OK.getId());
		response.setMessage(Acq_Status_Definations.OK.getDescription());
		response.setResult(null);
		logger.info("logout/v1" + "::" + "End");
		return response;
	}


}