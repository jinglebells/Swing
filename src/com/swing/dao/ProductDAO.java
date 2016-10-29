package com.swing.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.swing.entity.File;
import com.swing.entity.Product;
import com.swing.entity.User;

public class ProductDAO {
	private SessionFactory factory;
	private static final Logger log = LogManager.getLogger(ProductDAO.class);

	public ProductDAO() {
	}

	@SuppressWarnings("deprecation")
	public HashSet<String> getProducts() throws Exception{
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(Product.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();
		
		Session session = factory.openSession();
		ArrayList products ;
		products =(ArrayList) session.createSQLQuery("SELECT product FROM data.products").list();
		HashSet results = new HashSet<String>(products);
		session.flush();
		session.close();
		return results;
	}
}
