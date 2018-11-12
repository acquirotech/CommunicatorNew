package com.acq.web.dao;

import com.acq.web.controller.model.Acq_TposLogin_Model;
import com.acq.web.dto.impl.DbDto2;

public interface LoginDaoInf {

	DbDto2<Object> loginTposV1(Acq_TposLogin_Model model);	
}
