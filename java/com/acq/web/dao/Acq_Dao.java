package com.acq.web.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class Acq_Dao {

	@Autowired
	private SessionFactory sessionFactory;

	Session session;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session createNewSession() {
		session = sessionFactory.openSession();
		return session;
	}

	@Transactional
	public Object save(Object entity) {
		Session session = null;
		try{
			session = createNewSession();
			Object updatedEntity = session.merge(entity);
			session.flush();
			return updatedEntity;			
		}catch(Exception e){
			return null;
		}finally{			
			session.close();
		}
	}

	@Transactional
	public Object saveOrUpdate(Object entity) {
		Session session = null;
		try{
			session = createNewSession();
			session.saveOrUpdate(entity);
			session.flush();			
		}catch(Exception e){
			System.err.println();
		}finally{
			session.close();
		}
		return entity;
	}
	
	@Transactional
	public int update(String query) {
		int numberOfUpdate=0;
		try {
			session = createNewSession();
			Query q = session.createQuery(query);
			numberOfUpdate=q.executeUpdate();
			session.flush();
			return numberOfUpdate;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());

		} finally {
			session.close();
		}
		return numberOfUpdate;

	}

	@Transactional
	public void delete(Object entity) {
		Session session = null;
		try{
			session = createNewSession();
			session.delete(entity);
			session.flush();
		}catch(Exception e){
			System.err.println(e);
		}finally{
			session.close();
		}
	}



}
