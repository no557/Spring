package com.spring.app;

import java.sql.SQLException;

import com.spring.domain.User;
import com.spring.domain.UserDao;
import com.spring.util.DaoFactory;
import com.spring.util.SimpleConnectionMaker;

public class Main {
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		UserDao dao = new DaoFactory().userDao();
		
		User user = new User();
		
		user.setId("root");
		user.setName("shyun");
		user.setPassword("root");
		
		dao.add(user);
		
		System.out.println(user.getId() + "add success ");
		
		User user2 = dao.get(user.getId());

		System.out.println(user2.getId() + "get success ");
		
	
	}

}
