package com.adk.persistence;

import org.hibernate.*;
import org.hibernate.cfg.*;


public class HibernateUtil {

	private static SessionFactory sessionFactory;
	
	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionFactory() {
		if(sessionFactory.isClosed()){
			rebuildSessionFactory();
		}
		
		return sessionFactory;
	}
	
	public static void shutdown() {
		getSessionFactory().close();
	}
	
	public static void rebuildSessionFactory(){
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
    
}

