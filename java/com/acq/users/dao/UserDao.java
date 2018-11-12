package com.acq.users.dao;

import com.acq.users.model.Acq_User;

public interface UserDao {

	Acq_User SearchByUserName(String username);
	
}