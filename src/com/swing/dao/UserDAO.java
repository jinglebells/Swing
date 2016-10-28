package com.swing.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.swing.entity.User;

public class UserDAO {
	private SessionFactory factory;
	private static final Logger log = LogManager.getLogger(UserDAO.class);

	public UserDAO() {
	}

	@SuppressWarnings("deprecation")
	public boolean insert(User user) throws Exception{
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(User.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();
		
		Session session = factory.openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.flush();
		
		if (session.getTransaction().wasCommitted()) {
			log.debug("User inserted.");
			session.close();
			return true;
		}
		else {
			log.debug("Failed user insertion.");
			session.close();
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean checkUser(String username) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(User.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		Query query=session.createQuery("from User where username like:a");
		query.setString("a",username);
		if (!query.list().isEmpty()) {
			session.close();
			log.debug("Username already exists.");
			return true;
		}
		else {
			session.close();
			log.debug("Username does not exist.");
			return false;
		}
	}
}
