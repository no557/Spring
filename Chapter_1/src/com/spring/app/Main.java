package com.spring.app;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.spring.domain.User;
import com.spring.domain.UserDao;
import com.spring.factory.CountingDaoFactory;
import com.spring.factory.DaoFactory;
import com.spring.util.CountingConnectionMaker;

public class Main {
	
	
	private static ApplicationContext context;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		
		context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user = new User();
		
		user.setId("root4");
		user.setName("shyun");
		user.setPassword("root");
		
		dao.add(user);
		
		System.out.println(user.getId() + "add success ");
		
		User user2 = dao.get(user.getId());

		System.out.println(user2.getId() + "get success ");
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker",CountingConnectionMaker.class);
		
		System.out.println("connection Counter : " + ccm.getCounter());
		
	
	}

}
