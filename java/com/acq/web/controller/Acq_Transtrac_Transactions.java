package com.acq.web.controller;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acq.Acq_Status_Definations;
import com.acq.web.controller.model.Acq_TransTrac_GetTMK_Model;
import com.acq.web.controller.model.Acq_TransTrac_Reversal_Model;
import com.acq.web.controller.model.Acq_PurchaseAck_Model;
import com.acq.web.controller.model.Acq_TransTrac_Purchase_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxnList_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxn_Model;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse;
import com.acq.web.dto.impl.ControllerResponse2;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.handler.Acq_TransTrac_Txn_Handler_Inf;

@Controller
@RequestMapping(value = "/TransTrac")
public class Acq_Transtrac_Transactions {

	final static Logger logger = Logger.getLogger(Acq_Transtrac_Transactions.class);

	@Autowired
	Acq_TransTrac_Txn_Handler_Inf Acq_TransTrac_Txn_Handler;

	// Service to Get encrypted TMK
	@RequestMapping(value = "/tpos/getTMK/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getTMK(@RequestBody Acq_TransTrac_GetTMK_Model model,
			HttpServletRequest request) {

		logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TransTrac_GetTMK_Model>> validationErrors = modelValidator.validate(model);

			if (validationErrors.size() > 0) {
				String ValidationErrors = "";
				try {
					Iterator<ConstraintViolation<Acq_TransTrac_GetTMK_Model>> errorIterator = validationErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TransTrac_GetTMK_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + " " + violation.getMessage()+ " "+ violation.getInvalidValue() ;
						logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info(
							"/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Exception in Error Printing");
					e.printStackTrace();
				}

				logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			} else {
				logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.getTMK(model);
				logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}

	// Service to Send TMK Acknowledgement
	@RequestMapping(value = "/tpos/sendTMKACK/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> sendTMKACK(@RequestBody Acq_TransTrac_GetTMK_Model model,
			HttpServletRequest request) {
		logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TransTrac_GetTMK_Model>> validationErrors = modelValidator.validate(model);

			if (validationErrors.size() > 0) {
				String ValidationErrors = "";
				try {
					Iterator<ConstraintViolation<Acq_TransTrac_GetTMK_Model>> errorIterator = validationErrors
							.iterator();
					while (errorIterator.hasNext()) {
						String vError = errorIterator.next().getMessage();
						logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vError);
						ValidationErrors = ValidationErrors + vError + "--";
					}
				} catch (Exception e) {

					logger.info(
							"/TransTrac/tpos/sendTMKACK/v1" + "::" + "Controller" + "::" + "Exception in Error Printing");
					e.printStackTrace();
				}

				logger.info("/TransTrac/tpos/sendTMKACK/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			} else {
				logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.sendTMKACK(model);
				logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/getTMK/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	
	}

	// Service to Get DUKPT
	@RequestMapping(value = "/tpos/getDUKPT/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getDUKPT(@RequestBody Acq_TransTrac_GetTMK_Model model,
			HttpServletRequest request) {
		
		logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TransTrac_GetTMK_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				try {
					Iterator<ConstraintViolation<Acq_TransTrac_GetTMK_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						String vError = errorIterator.next().getMessage();
						logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vError);
						ValidationErrors = ValidationErrors + vError + "--";
					}
				} catch (Exception e) {

					logger.info(
							"/TransTrac/tpos/getDUKPT/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			}  else {
				logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.getDUKPT(model);
				logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/getDUKPT/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}

	// Service to Send DUKPT Acknowledgement
	@RequestMapping(value = "/tpos/sendDUKPTACK/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> sendDUKPTACK(@RequestBody Acq_TransTrac_GetTMK_Model model,
			HttpServletRequest request) {
		
		logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TransTrac_GetTMK_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_TransTrac_GetTMK_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						String vError = errorIterator.next().getMessage();
						logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vError);
						ValidationErrors = ValidationErrors + vError + "--";
					}
				} catch (Exception e) {

					logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
				
			} else {
				
				logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.sendDUKPTACK(model);
				logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Controller" + "::" + "Response From Controlller");
				
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/sendDUKPTACK/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	// Service For Purchase Transaction
	@RequestMapping(value = "/tpos/doPurchase/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> doPurchase(@RequestBody Acq_TransTrac_Purchase_Model model,
			HttpServletRequest request) {

		logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TransTrac_Purchase_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_TransTrac_Purchase_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						String vError = errorIterator.next().getMessage();
						logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vError);
						ValidationErrors = ValidationErrors + vError + "--";
					}
				} catch (Exception e) {

					logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
				
			} else {
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.doPurchase(model);
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/doPurchase/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	// Service For CashAtPos Transaction
	@RequestMapping(value = "/tpos/doCashAtPos/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> doCashAtPos(@RequestBody Acq_TransTrac_Purchase_Model model,
			HttpServletRequest request) {
		logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TransTrac_Purchase_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_TransTrac_Purchase_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TransTrac_Purchase_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			} else {
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.doCashAtPos(model);
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/doCashAtPos/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	// Service For Void Transaction
	@RequestMapping(value = "/tpos/doVoid/version1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> doVoid(@RequestBody Acq_TransTrac_VoidTxn_Model model,
			HttpServletRequest request) {
		logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TransTrac_VoidTxn_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_TransTrac_VoidTxn_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TransTrac_VoidTxn_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			} else {
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.doVoid(model);
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Controller" + "::" + "Response From Controlller");
				return controllerResponse;
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/doVoid/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	// Service For Void List Transaction
	@RequestMapping(value = "/tpos/voidList/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getVoidList(@RequestBody Acq_TransTrac_VoidTxnList_Model model,
			HttpServletRequest request) {

		logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TransTrac_VoidTxnList_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TransTrac_VoidTxnList_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TransTrac_VoidTxnList_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage() + "-"
								+ violation.getInvalidValue();
						logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Controller" + "::" + "ValidationError"
								+ "::" + vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Controller" + "::"
							+ "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			} else {

				logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.getVoidList(model);
				logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/getVoidList/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	// Service For Reversal Transaction
	@RequestMapping(value = "/tpos/doReversal/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> doReversal(@RequestBody Acq_TransTrac_Reversal_Model model,
			HttpServletRequest request) {
		
		logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TransTrac_Reversal_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_TransTrac_Reversal_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TransTrac_Reversal_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			} else {

				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.doReversal(model);
				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/doReversal/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	

	// Service For Purchase Complete Transaction
	@RequestMapping(value = "/tpos/sendPurchaseAck/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> sendPurchaseAck(@RequestBody Acq_PurchaseAck_Model model,
			HttpServletRequest request) {
		logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_PurchaseAck_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_PurchaseAck_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_PurchaseAck_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}				
				logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			} else {
				logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto<Object> daoResponse = Acq_TransTrac_Txn_Handler.sendPurchaseAck(model);
				logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatus());
				controllerResponse.setStatusMessage(daoResponse.getMessage());
				controllerResponse.setBody(daoResponse.getResult());
				logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/TransTrac/tpos/sendPurchaseAck/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}

























}