package com.spring.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.domain.UserDao;

@Configuration
public class DaoFactory {

	@Bean
	public UserDao userDao() {

		return new UserDao(connectionMaker());
	}

	public UserDao accountDao() {

		return new UserDao(connectionMaker());
	}
	public UserDao messageDao() {

		return new UserDao(connectionMaker());
	}

	@Bean
	public ConnectionMaker connectionMaker() {
		return new SimpleConnectionMaker();
	}

}
