package com.swing.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.swing.entity.File;

public class FileDAO {
	private SessionFactory factory;
	private static final Logger log = LogManager.getLogger(FileDAO.class);

	public FileDAO() {
	}

	@SuppressWarnings("deprecation")
	public boolean insert(File file) throws Exception{
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(File.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();
		
		Session session = factory.openSession();
		session.beginTransaction();
		session.save(file);
		session.getTransaction().commit();
		session.flush();
		
		if (session.getTransaction().wasCommitted()) {
			log.debug("File inserted.");
			session.close();
			return true;
		}
		else {
			log.debug("Failed file insertion.");
			session.close();
			return false;
		}
	}
}
