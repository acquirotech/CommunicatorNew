package com.acq.web.controller;

import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acq.Acq_Data_Validator;
import com.acq.Acq_Status_Definations;
import com.acq.web.controller.model.Acq_TW_OperatorList_Model;
import com.acq.web.controller.model.Acq_TW_DoRecharge_Model;
import com.acq.web.controller.model.Acq_TW_TxnStatus_Model;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.Acq_ThinkWalnut_Transactions_Handler_Inf;

@Controller
@RequestMapping(value = "/tw")
public class Acq_ThinkWalnut_Transactions {


	@Autowired
	Acq_ThinkWalnut_Transactions_Handler_Inf Acq_TW_Transactions_Handler;

	@Autowired
	MessageSource resource;

	final static Logger logger = Logger.getLogger(Acq_ThinkWalnut_Transactions.class);

	//Service To Get Operator List From Think Walnut Using Form Data
	@RequestMapping(value="/tpos/billpay/getOperators/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getOperators(@ModelAttribute Acq_TW_OperatorList_Model model,HttpServletRequest request) throws ParseException{

		logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_OperatorList_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_OperatorList_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_OperatorList_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{

				logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.getOperators(model);
				logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject operatorList = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					operatorList = (JSONObject)new JSONParser().parse(daoResponse.getBody().toString());	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(operatorList);
				logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/getOperators/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}


	//Service To Get Operator List From Think Walnut Using Json
	@RequestMapping(value="/tpos/billpay/getOperators/v2", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getOperators2(@RequestBody final Acq_TW_OperatorList_Model model,HttpServletRequest request) throws ParseException{

		logger.info("/tw/tpos/billpay/getOperators/v2" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_OperatorList_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_OperatorList_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_OperatorList_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/getOperators/v2" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/getOperators/v2" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/getOperators/v2" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{

				logger.info("/tw/tpos/billpay/getOperators/v2" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.getOperators(model);
				logger.info("/tw/tpos/billpay/getOperators/v2" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject operatorList = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					operatorList = (JSONObject)new JSONParser().parse(daoResponse.getBody().toString());	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(operatorList);
				logger.info("/tw/tpos/billpay/getOperators/v2" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/getOperators/v2" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}


	//Service To Do Mobile/DTH Recharges Using Form Data
	@RequestMapping(value="/tpos/billpay/doRecharge/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> doRecharge(@ModelAttribute Acq_TW_DoRecharge_Model model,HttpServletRequest request){

		logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_DoRecharge_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_DoRecharge_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_DoRecharge_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{

				if(model.getServiceType().equalsIgnoreCase("Mobile")){
					if(!Acq_Data_Validator.isPhoneNo(model.getSubscriberId())){
						logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Controller" + "::" + "Mobile Number Invalid");
						controllerResponse. setStatusCode(Acq_Status_Definations.UserValidationError.getId());
						controllerResponse.setStatusMessage("Mobile Number Invalid");
						controllerResponse.setBody(null);
						return controllerResponse;
					}
				}

				logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.doRecharge(model);
				logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject responseJson = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					responseJson = (JSONObject) daoResponse.getBody();	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(responseJson);
				logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/doRecharge/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}


	//Service To Do Mobile/DTH Recharges Using Json
	@RequestMapping(value="/tpos/billpay/doRecharge/v2", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> doRecharge2(@RequestBody final Acq_TW_DoRecharge_Model model,HttpServletRequest request){

		logger.info("/tw/tpos/billpay/doRecharge/v2" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_DoRecharge_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_DoRecharge_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_DoRecharge_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/doRecharge/v2" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/doRecharge/v2" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/doRecharge/v2" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{

				if(model.getServiceType().equalsIgnoreCase("Mobile")){
					if(!Acq_Data_Validator.isPhoneNo(model.getSubscriberId())){
						logger.info("/tw/tpos/billpay/doRecharge/v2" + "::" + "Controller" + "::" + "Mobile Number Invalid");
						controllerResponse. setStatusCode(Acq_Status_Definations.UserValidationError.getId());
						controllerResponse.setStatusMessage("Mobile Number Invalid");
						controllerResponse.setBody(null);
						return controllerResponse;
					}
				}

				logger.info("/tw/tpos/billpay/doRecharge/v2" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.doRecharge(model);
				logger.info("/tw/tpos/billpay/doRecharge/v2" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject responseJson = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					responseJson = (JSONObject) daoResponse.getBody();	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(responseJson);
				logger.info("/tw/tpos/billpay/doRecharge/v2" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/doRecharge/v2" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}


	//Service To Get Merchant Balance Using Form Data
	@RequestMapping(value = "/tpos/billpay/getBalance/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getBalance(@ModelAttribute Acq_TW_OperatorList_Model model,HttpServletRequest request) {

		logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_OperatorList_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_OperatorList_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_OperatorList_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{

				logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.getBalance(model);
				logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject responseJson = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					responseJson = (JSONObject)new JSONParser().parse(daoResponse.getBody().toString());	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(responseJson);
				logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/getBalance/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}


	//Service To Get Merchant Balance Using Json
	@RequestMapping(value = "/tpos/billpay/getBalance/v2", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getBalance2(@RequestBody final Acq_TW_OperatorList_Model model,
			HttpServletRequest request) {

		logger.info("/tw/tpos/billpay/getBalance/v2" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_OperatorList_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_OperatorList_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_OperatorList_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/getBalance/v2" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/getBalance/v2" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/getBalance/v2" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{
				logger.info("/tw/tpos/billpay/getBalance/v2" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.getBalance(model);
				logger.info("/tw/tpos/billpay/getBalance/v2" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject responseJson = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					responseJson = (JSONObject)new JSONParser().parse(daoResponse.getBody().toString());	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(responseJson);
				logger.info("/tw/tpos/billpay/getBalance/v2" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/getBalance/v2" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}


	//Service To Get Txn Status Using Form Data
	@RequestMapping(value = "/tpos/billpay/getTxnStatus/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getTxnStatus(@ModelAttribute Acq_TW_TxnStatus_Model model,
			HttpServletRequest request) {

		logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_TxnStatus_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_TxnStatus_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_TxnStatus_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{

				logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.getTxnStatus(model);
				logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject responseJson = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					responseJson = (JSONObject)new JSONParser().parse(daoResponse.getBody().toString());	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(responseJson);
				logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/getTxnStatus/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}


	//Service To Get Txn Status Using Json
	@RequestMapping(value = "/tpos/billpay/getTxnStatus/v2", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getTxnStatus2(@RequestBody final Acq_TW_TxnStatus_Model model,
			HttpServletRequest request) {

		logger.info("/tw/tpos/billpay/getTxnStatus/v2" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_TxnStatus_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_TxnStatus_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_TxnStatus_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/getTxnStatus/v2" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/getTxnStatus/v2" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/getTxnStatus/v2" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{

				logger.info("/tw/tpos/billpay/getTxnStatus/v2" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.getTxnStatus(model);
				logger.info("/tw/tpos/billpay/getTxnStatus/v2" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject responseJson = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					responseJson = (JSONObject)new JSONParser().parse(daoResponse.getBody().toString());	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(responseJson);
				logger.info("/tw/tpos/billpay/getTxnStatus/v2" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/getTxnStatus/v2" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}


	//Service To Get Txn List Using Form Data
	@RequestMapping(value = "/tpos/billpay/getTxnList/v1", method = RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getTxnList(@ModelAttribute Acq_TW_OperatorList_Model model,
			HttpServletRequest request) 
	{
		logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_OperatorList_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_OperatorList_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_OperatorList_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{

				logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.getTxnList(model);
				logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject operatorList = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					operatorList = (JSONObject)new JSONParser().parse(daoResponse.getBody().toString());	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(operatorList);
				logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/getTxnList/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}


	@RequestMapping(value = "/tpos/billpay/getTxnList/v2", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getTxnList2(@RequestBody final Acq_TW_OperatorList_Model model,
			HttpServletRequest request) {

		logger.info("/tw/tpos/billpay/getTxnList/v2" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_TW_OperatorList_Model>> modelErrors = modelValidator.validate(model);

			if (modelErrors.size() > 0) {
				String ValidationErrors = "";

				try {
					Iterator<ConstraintViolation<Acq_TW_OperatorList_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_TW_OperatorList_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/tw/tpos/billpay/getTxnList/v2" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/tw/tpos/billpay/getTxnList/v2" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}

				logger.info("/tw/tpos/billpay/getTxnList/v2" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{

				logger.info("/tw/tpos/billpay/getTxnList/v2" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_TW_Transactions_Handler.getTxnList(model);
				logger.info("/tw/tpos/billpay/getTxnList/v2" + "::" + "Controller" + "::" + "Return From Handler");

				JSONObject operatorList = new JSONObject();
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()) {
					operatorList = (JSONObject)new JSONParser().parse(daoResponse.getBody().toString());	
				}
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(operatorList);
				logger.info("/tw/tpos/billpay/getTxnList/v2" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/tw/tpos/billpay/getTxnList/v2" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}

}
