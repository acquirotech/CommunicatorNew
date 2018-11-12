package com.acq.web.handler;

import com.acq.web.controller.model.Acq_TposLogin_Model;
import com.acq.web.dto.impl.ServiceDto2;

public interface Acq_Login_Handler_Inf {

	public ServiceDto2<Object> loginTposV1(Acq_TposLogin_Model model);

}
