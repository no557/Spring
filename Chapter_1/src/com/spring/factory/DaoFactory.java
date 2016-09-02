package com.spring.factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.domain.UserDaoImpl;
import com.spring.util.ConnectionMaker;
import com.spring.util.SimpleConnectionMaker;

@Configuration
public class DaoFactory {

//	@Bean
//	public UserDao userDao() {
//
//		return new UserDao(connectionMaker());
//	}
//
//	public UserDao accountDao() {
//
//		return new UserDao(connectionMaker());
//	}
//	public UserDao messageDao() {
//
//		return new UserDao(connectionMaker());
//	}

	@Bean
	public ConnectionMaker connectionMaker() {
		return new SimpleConnectionMaker();
	}

}
