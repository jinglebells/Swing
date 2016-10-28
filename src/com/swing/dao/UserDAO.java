package com.swing.dao;

import org.hibernate.Query;
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

	public boolean insert(User user) throws Exception{
		Session session = factory.openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.flush();
		session.close();
		if (session.getTransaction().wasCommitted()) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean checkUser(String username) {
		Session session = factory.openSession();
		Query query=session.createQuery("from User where username like:a");
		query.setString("a",username);
		if (!query.list().isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
}
