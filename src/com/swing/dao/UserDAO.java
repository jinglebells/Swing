package com.swing.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.swing.entity.User;

public class UserDAO {
	private SessionFactory factory;

	@SuppressWarnings("deprecation")
	public UserDAO() throws Exception{
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(User.class);
 		factory = cfg.buildSessionFactory();
	}
	
	public void insert(User user) throws Exception{
		Session session = factory.openSession();
		session.beginTransaction();
     	session.save(user);
     	session.getTransaction().commit();
     	session.flush();
     	session.close();
	}
}
