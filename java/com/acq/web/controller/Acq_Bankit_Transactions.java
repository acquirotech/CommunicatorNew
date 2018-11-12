package com.acq.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.acq.Acq_Status_Definations;
import com.acq.web.controller.model.Acq_Bankit_PrepaidLoad_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_TxnList_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_AddBene_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_AddSender_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_BnkList_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_TxnStatus_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_Neft_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_sendOTP_Model;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.Acq_Bankit_Transactions_Handler_Inf;

@Controller
@RequestMapping(value = "/bankit")
public class Acq_Bankit_Transactions {

	@Autowired
	Acq_Bankit_Transactions_Handler_Inf Acq_Bankit_Transactions_Handler;
	
	@Autowired
	MessageSource resource;

	final static Logger logger = Logger.getLogger(Acq_Bankit_Transactions.class);
	
	//Service for Adding Sender under Bankit
	@RequestMapping(value="/tpos/dmt/addSender/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> addSender(@RequestBody Acq_Bankit_DMT_AddSender_Model model,HttpServletRequest request){
		
		logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_AddSender_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_AddSender_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_AddSender_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else 
			{
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(dateFormat.format(date));
			
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
			ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.addSender(model);
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Controller" + "::" + "Return From Handler");
		
			controllerResponse.setStatusCode(daoResponse.getStatusCode());
			controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
			controllerResponse.setBody(daoResponse.getBody());
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/bankit/tpos/dmt/addSender/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	
	//Service for Adding Beneficiary Under a Sender
	@RequestMapping(value="/tpos/dmt/addBene/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> addbene(@RequestBody Acq_Bankit_DMT_AddBene_Model model,HttpServletRequest request){
		
		logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_AddBene_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_AddBene_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_AddBene_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			}else {
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
				model.setDateTime(dateFormat.format(date));
				
				logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.addbene(model);
				logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Controller" + "::" + "Return From Handler");
				
				
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(daoResponse.getBody());
				logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/bankit/tpos/dmt/addBene/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}	


	//Service for Generating OTP
	@RequestMapping(value="/tpos/dmt/sendOtp/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> sendOtp(@RequestBody Acq_Bankit_DMT_sendOTP_Model model,HttpServletRequest request){
		
		logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_sendOTP_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_sendOTP_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_sendOTP_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			} 
			else 
			{
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
				model.setDateTime(dateFormat.format(date));
				
				logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.sendOtp(model);
				logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Controller" + "::" + "Return From Handler");
				
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());			
				controllerResponse.setBody(daoResponse.getBody());
				logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/bankit/tpos/dmt/sendOtp/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}	
	
	
	//Service for Getting Beneficiary List Under a Sender
	@RequestMapping(value="/tpos/dmt/getBeneList/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getBeneList(@RequestBody Acq_Bankit_DMT_sendOTP_Model model,HttpServletRequest request){
		
		logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_sendOTP_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_sendOTP_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_sendOTP_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);
			}
			else
			{
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
				model.setDateTime(dateFormat.format(date));
				
				logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.getBeneList(model);
				logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Controller" + "::" + "Return From Handler");
				
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());		
				controllerResponse.setBody(daoResponse.getBody());
				logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/bankit/tpos/dmt/getBeneList/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	
	//Service for Deleting Beneficiary Under a Sender
	@RequestMapping(value="/tpos/dmt/deleteBene/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> deleteBene(@RequestBody Acq_Bankit_DMT_Neft_Model model,HttpServletRequest request){
		
		logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();

		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_Neft_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_Neft_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_Neft_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			} 
			else 
			{
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
				model.setDateTime(dateFormat.format(date));
				logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.deleteBene(model);
				logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "Controller" + "::" + "Return From Handler");
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());			
				controllerResponse.setBody(daoResponse.getBody());
				logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "Controller" + "::" + "Response From Controlller");
				}
			} catch (Exception e) {
				logger.info("/bankit/tpos/dmt/deleteBene/v1" + "::" + "Controller" + "::" + "Unexpected Error");
				e.printStackTrace();
				controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
				controllerResponse.setBody(null);
			}
		return controllerResponse;
	}
	
	
	//Service for NEFT Transfer To Bene
	@RequestMapping(value="/tpos/dmt/doNeft/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> doNeft(@RequestBody Acq_Bankit_DMT_Neft_Model model,HttpServletRequest request){
		logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_Neft_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_Neft_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_Neft_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			}
			else 
			{
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(dateFormat.format(date));
			
			logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
			ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.doNeft(model);
			logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Controller" + "::" + "Return From Handler");
			controllerResponse.setStatusCode(daoResponse.getStatusCode());
			controllerResponse.setStatusMessage(daoResponse.getStatusMessage());			
			controllerResponse.setBody(daoResponse.getBody());
			logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/bankit/tpos/dmt/doNeft/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	
	//Service for IMPS Transfer To Bene
	@RequestMapping(value="/tpos/dmt/doImps/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> doImps(@RequestBody Acq_Bankit_DMT_Neft_Model model,HttpServletRequest request){
		logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_Neft_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_Neft_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_Neft_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			}
			else 
			{
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(dateFormat.format(date));
			
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
			ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.doImps(model);
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Controller" + "::" + "Return From Handler");
			controllerResponse.setStatusCode(daoResponse.getStatusCode());
			controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
			controllerResponse.setBody(daoResponse.getBody());
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/bankit/tpos/dmt/doImps/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}	
	
	
	//Service for Getting the Transaction List
	@RequestMapping(value="/tpos/dmt/getTxnList/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getTxnList(@RequestBody Acq_Bankit_DMT_TxnList_Model model,HttpServletRequest request) throws ParseException{
		logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_TxnList_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_TxnList_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_TxnList_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			}
			else 
			{
				logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.getTxnList(model);
				logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Controller" + "::" + "Return From Handler");
				
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(daoResponse.getBody());
				logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/bankit/tpos/dmt/getTxnList/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}	
	
	
	//Service for Getting the Transaction Status
	@RequestMapping(value="/tpos/dmt/getTxnStatus/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getTxnStatus(@RequestBody Acq_Bankit_DMT_TxnStatus_Model model,HttpServletRequest request){
		
		logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_TxnStatus_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) {
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_TxnStatus_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_TxnStatus_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			}
			else 
			{
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(dateFormat.format(date));
			
			logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
			ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.getTxnStatus(model);
			logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Controller" + "::" + "Return From Handler");
			controllerResponse.setStatusCode(daoResponse.getStatusCode());
			controllerResponse.setStatusMessage(daoResponse.getStatusMessage());			
			controllerResponse.setBody(daoResponse.getBody());
			logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/bankit/tpos/dmt/getTxnStatus/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
		
	
	//Service for Getting the Bank List From Bankit
	@RequestMapping(value="/tpos/dmt/getBankList/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getBankList(@RequestBody Acq_Bankit_DMT_BnkList_Model model,HttpServletRequest request){

		logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Controller" + "::" + "Begin");
		ControllerResponse2<Object> controllerResponse = new ControllerResponse2<Object>();
		
		try {
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator modelValidator = vFactory.getValidator();
			Set<ConstraintViolation<Acq_Bankit_DMT_BnkList_Model>> modelErrors = modelValidator.validate(model);
			
			if (modelErrors.size() > 0) 
			{
				String ValidationErrors = "";
				
				try {
					Iterator<ConstraintViolation<Acq_Bankit_DMT_BnkList_Model>> errorIterator = modelErrors
							.iterator();
					while (errorIterator.hasNext()) {
						ConstraintViolation<Acq_Bankit_DMT_BnkList_Model> violation = errorIterator.next();
						String vErrors = violation.getPropertyPath() + "-" + violation.getMessage()+ "-"+ violation.getInvalidValue();
						logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Controller" + "::" + "ValidationError" + "::"
								+ vErrors);
						ValidationErrors = ValidationErrors + vErrors + "--";
					}
				} catch (Exception e) {

					logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Controller" + "::" + "Exception in Validation Error Printing");
					e.printStackTrace();
				}
				
				logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Controller" + "::" + "Parameter Validation Failed");

				controllerResponse.setStatusCode(Acq_Status_Definations.InvalidParameters.getId());
				controllerResponse.setStatusMessage(Acq_Status_Definations.InvalidParameters.getDescription());
				controllerResponse.setBody(ValidationErrors);

			}
			else 
			{			
				logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Controller" + "::" + "Forwarded To Handler");
				ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.getBankList(model);
				logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Controller" + "::" + "Return From Handler");
				
				controllerResponse.setStatusCode(daoResponse.getStatusCode());
				controllerResponse.setStatusMessage(daoResponse.getStatusMessage());
				controllerResponse.setBody(daoResponse.getBody());
				logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Controller" + "::" + "Response From Controlller");
			}
		} catch (Exception e) {
			logger.info("/bankit/tpos/dmt/getBankList/v1" + "::" + "Controller" + "::" + "Unexpected Error");
			e.printStackTrace();
			controllerResponse.setStatusCode(Acq_Status_Definations.UnexpectedServerError.getId());
			controllerResponse.setStatusMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			controllerResponse.setBody(null);
		}
		return controllerResponse;
	}
	
	
	
	//--------------- Need To Fix Below This --------------------------
	
	
	
	//Service for Loading the Prepaid Card
	@RequestMapping(value="/tpos/dmt/doCardLoad/v1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> cardLoad(@RequestBody Acq_Bankit_PrepaidLoad_Model model,HttpServletRequest request){
		logger.info("request landing for cardLoad");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<Acq_Bankit_PrepaidLoad_Model>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(Acq_Status_Definations.InvalidCrediential.getId());
			Iterator<ConstraintViolation<Acq_Bankit_PrepaidLoad_Model>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<Acq_Bankit_PrepaidLoad_Model> validate = (ConstraintViolation<Acq_Bankit_PrepaidLoad_Model>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.cardLoad(model);
				System.out.println("response from get cardLoad version1 controller");
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()){
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from cardLoad controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt cardLoad controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	
	@RequestMapping(value="/dmt/cardLoadStatus/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> cardLoadStatus(@RequestBody Acq_Bankit_PrepaidLoad_Model model,HttpServletRequest request){
		logger.info("request landing for cardLoad");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<Acq_Bankit_PrepaidLoad_Model>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(Acq_Status_Definations.InvalidCrediential.getId());
			Iterator<ConstraintViolation<Acq_Bankit_PrepaidLoad_Model>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<Acq_Bankit_PrepaidLoad_Model> validate = (ConstraintViolation<Acq_Bankit_PrepaidLoad_Model>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.cardLoadStatus(model);
				System.out.println("response from get cardLoadStatus version1 controller");
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()){
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from cardLoadStatus controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt cardLoad controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/getCardBal/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getCardBal(@RequestBody Acq_Bankit_PrepaidLoad_Model model,HttpServletRequest request){
		logger.info("request landing for getCardBal");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<Acq_Bankit_PrepaidLoad_Model>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(Acq_Status_Definations.InvalidCrediential.getId());
			Iterator<ConstraintViolation<Acq_Bankit_PrepaidLoad_Model>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<Acq_Bankit_PrepaidLoad_Model> validate = (ConstraintViolation<Acq_Bankit_PrepaidLoad_Model>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = Acq_Bankit_Transactions_Handler.getCardBal(model);
				System.out.println("response from getCardBal version1 controller");
				if (daoResponse.getStatusCode() == Acq_Status_Definations.OK.getId()){
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from getCardBal controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt getCardBal controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
}
