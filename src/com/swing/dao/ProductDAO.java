package com.swing.dao;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.swing.entity.Product;

public class ProductDAO {
	private SessionFactory factory;
	private static final Logger log = LogManager.getLogger(ProductDAO.class);

	public ProductDAO() {
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public HashSet<String> getProducts() throws Exception{
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(Product.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();

		Session session = factory.openSession();
		@SuppressWarnings("rawtypes")
		ArrayList products ;
		products =(ArrayList) session.createSQLQuery("SELECT product FROM data.products").list();
		@SuppressWarnings("unchecked")
		HashSet results = new HashSet<String>(products);
		session.flush();
		session.close();
		return results;
	}

	@SuppressWarnings("deprecation")
	public String getSizeMalePants(String gender, String product, String waist,  String insideleg) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(Product.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		Query query=session.createQuery("Select size from Product where gender='" + gender
				+"' and product='" + product + "' and waist='" + waist + "' and insideleg='" + insideleg+ "'");

		if (!query.list().isEmpty()) {
			log.debug("Size Selected");
			return query.list().get(0).toString();
		}
		else {
			log.debug("We don't have information to get your size.");
			return null;

		}
	}

	@SuppressWarnings("deprecation")
	public String getSizeMaleKnits(String gender, String product, String chest) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(Product.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		Query query=session.createQuery("Select size from Product where gender='" + gender
				+"' and product='" + product + "' and chest='" + chest +"'");

		if (!query.list().isEmpty()) {
			log.debug("Size Selected");
			return query.list().get(0).toString();
		}
		else {
			log.debug("We don't have information to get your size.");
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public String getSizeFemalePants(String gender, String product, String waist, String hip) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(Product.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		Query query=session.createQuery("Select size from Product where gender='" + gender
				+"' and product='" + product + "' and waist='" + waist +"' and hip='" + hip + "'");

		if (!query.list().isEmpty()) {
			log.debug("Size Selected");
			return query.list().get(0).toString();
		}
		else {
			log.debug("We don't have information to get your size.");
			return null;

		}
	}

	@SuppressWarnings("deprecation")
	public String getSizeFemaleKnits(String gender, String product, String bust) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(Product.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		Query query=session.createQuery("Select size from Product where gender='" + gender
				+"' and product='" + product + "' and bust='" + bust +"'");

		if (!query.list().isEmpty()) {
			log.debug("Size Selected");
			return query.list().get(0).toString();
		}
		else {
			log.debug("We don't have information to get your size.");
			return null;

		}
	}

	@SuppressWarnings("deprecation")
	public String getSizeFemaleDress(String gender, String product, String height, String waist, String hip,
			String bust) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addClass(Product.class);
		log.debug("configuration created");
		factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		Query query=session.createQuery("Select size from Product where gender='" + gender
				+"' and product='" + product + "' and waist='" + waist +" and bust='" + bust + "' and hip='" + hip +"'");

		if (!query.list().isEmpty()) {
			log.debug("Size Selected");
			return query.list().get(0).toString();
		}
		else {
			log.debug("We don't have information to get your size.");
			return null;

		}
	}
}
