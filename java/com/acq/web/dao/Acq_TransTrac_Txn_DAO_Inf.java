package com.acq.web.dao;

import com.acq.web.controller.model.Acq_TransTrac_GetTMK_Model;
import com.acq.web.controller.model.Acq_TransTrac_Reversal_Model;
import com.acq.web.controller.model.Acq_PurchaseAck_Model;
import com.acq.web.controller.model.Acq_TransTrac_Purchase_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxnList_Model;
import com.acq.web.controller.model.Acq_TransTrac_VoidTxn_Model;
import com.acq.web.dto.impl.DbDto;

public interface Acq_TransTrac_Txn_DAO_Inf {
	
	
	DbDto<Object> getTMK(Acq_TransTrac_GetTMK_Model model);

	DbDto<Object> sendTMKACK(Acq_TransTrac_GetTMK_Model model);

	DbDto<Object> getDUKPT(Acq_TransTrac_GetTMK_Model model);

	DbDto<Object> sendDUKPTACK(Acq_TransTrac_GetTMK_Model model);
	
	DbDto<Object> doPurchase(Acq_TransTrac_Purchase_Model model);
	
	DbDto<Object> doCashAtPos(Acq_TransTrac_Purchase_Model model);
	
	DbDto<Object> doVoid(Acq_TransTrac_VoidTxn_Model model);
	
	DbDto<Object> getVoidList(Acq_TransTrac_VoidTxnList_Model model);
	
	DbDto<Object> doReversal(Acq_TransTrac_Reversal_Model model);

	DbDto<Object> sendPurchaseAck(Acq_PurchaseAck_Model model);

}
