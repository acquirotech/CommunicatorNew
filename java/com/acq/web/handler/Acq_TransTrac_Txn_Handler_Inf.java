package com.acq.web.handler;

import com.acq.web.controller.model.Acq_TransTrac_GetTMK_Model;
import com.acq.web.controller.model.Acq_TransTrac_Reversal_Model;
import com.acq.web.controller.model.Acq_PurchaseAck_Model;
import com.acq.web.controller.model.Acq_TransTrac_Purchase_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxnList_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxn_Model;
import com.acq.web.dto.impl.ServiceDto;

public interface Acq_TransTrac_Txn_Handler_Inf {
	
	ServiceDto<Object> getTMK(Acq_TransTrac_GetTMK_Model model);

	ServiceDto<Object> sendTMKACK(Acq_TransTrac_GetTMK_Model model);
	
	ServiceDto<Object> getDUKPT(Acq_TransTrac_GetTMK_Model model);
	
	ServiceDto<Object> sendDUKPTACK(Acq_TransTrac_GetTMK_Model model);
	
	ServiceDto<Object> doPurchase(Acq_TransTrac_Purchase_Model model);
	
	ServiceDto<Object> doCashAtPos(Acq_TransTrac_Purchase_Model model);
	
	ServiceDto<Object> doVoid(Acq_TransTrac_VoidTxn_Model model);
	
	ServiceDto<Object> getVoidList(Acq_TransTrac_VoidTxnList_Model model);

	ServiceDto<Object> doReversal(Acq_TransTrac_Reversal_Model model);

	ServiceDto<Object> sendPurchaseAck(Acq_PurchaseAck_Model model);

	

}
