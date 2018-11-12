package com.acq.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import com.acq.users.model.Acq_User;

public class UserDaoImpl implements UserDao{

	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	
	public Acq_User SearchByUserName(String username) {
		List<Acq_User> users = new ArrayList<Acq_User>();
		try{
		users = getSessionFactory().getCurrentSession().createQuery("from Acq_User where username=?")
				.setParameter(0, username).list();
		
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
		}catch(Exception e){
			System.out.println("exception "+e);
			return null;
		}
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
}