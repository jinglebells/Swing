package com.swing.test;
 
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.swing.dao.UserDAO;
import com.swing.entity.User;

import junit.framework.*;
 
 
/**
 * Unit test for simple User.
 */
public class AppTest extends TestCase {
 
	public void testApp() throws Exception {
		User user = new User("firstuser", "rr", "francisco", "rebelo", "fr@fr.com", "912149893");
		UserDAO userDAO = new UserDAO();
		userDAO.insert(user);
	}
}