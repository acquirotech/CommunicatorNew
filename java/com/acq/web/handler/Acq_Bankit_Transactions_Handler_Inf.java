package com.acq.web.handler;

import com.acq.web.controller.model.Acq_Bankit_PrepaidLoad_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_TxnList_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_AddBene_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_AddSender_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_BnkList_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_TxnStatus_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_Neft_Model;
import com.acq.web.controller.model.Acq_Bankit_DMT_sendOTP_Model;
import com.acq.web.dto.impl.ServiceDto2;

public interface Acq_Bankit_Transactions_Handler_Inf {


	public ServiceDto2<Object> addSender(Acq_Bankit_DMT_AddSender_Model model);
	public ServiceDto2<Object> addbene(Acq_Bankit_DMT_AddBene_Model model);
	public ServiceDto2<Object> sendOtp(Acq_Bankit_DMT_sendOTP_Model model);
	public ServiceDto2<Object> getBeneList(Acq_Bankit_DMT_sendOTP_Model model);
	public ServiceDto2<Object> deleteBene(Acq_Bankit_DMT_Neft_Model model);
	public ServiceDto2<Object> doNeft(Acq_Bankit_DMT_Neft_Model model);
	public ServiceDto2<Object> doImps(Acq_Bankit_DMT_Neft_Model model);
	public ServiceDto2<Object> getTxnList(Acq_Bankit_DMT_TxnList_Model model);
	public ServiceDto2<Object> getTxnStatus(Acq_Bankit_DMT_TxnStatus_Model model);
	public ServiceDto2<Object> getBankList(Acq_Bankit_DMT_BnkList_Model model);
	public ServiceDto2<Object> cardLoad(Acq_Bankit_PrepaidLoad_Model model);
	public ServiceDto2<Object> cardLoadStatus(Acq_Bankit_PrepaidLoad_Model model);
	public ServiceDto2<Object> getCardBal(Acq_Bankit_PrepaidLoad_Model model);
	
	
	
}
