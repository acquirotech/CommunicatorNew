package com.acq.web.handler;

import com.acq.web.controller.model.Acq_TW_OperatorList_Model;
import com.acq.web.controller.model.Acq_TW_DoRecharge_Model;
import com.acq.web.controller.model.Acq_TW_TxnStatus_Model;
import com.acq.web.dto.impl.ServiceDto2;

public interface Acq_ThinkWalnut_Transactions_Handler_Inf {

	public ServiceDto2<Object> getOperators(Acq_TW_OperatorList_Model model);
	public ServiceDto2<Object> doRecharge(Acq_TW_DoRecharge_Model model);
	public ServiceDto2<Object> getBalance(Acq_TW_OperatorList_Model model);
	public ServiceDto2<Object> getTxnStatus(Acq_TW_TxnStatus_Model model);
	public ServiceDto2<Object> getTxnList(Acq_TW_OperatorList_Model model);
	
	
	
}
