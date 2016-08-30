package com.spring.util;

import com.spring.domain.UserDao;

public class DaoFactory {

	public UserDao userDao() {

		return new UserDao(connectionMaker());
	}

	public UserDao accountDao() {

		return new UserDao(connectionMaker());
	}
	public UserDao messageDao() {

		return new UserDao(connectionMaker());
	}

	private ConnectionMaker connectionMaker() {
		return new SimpleConnectionMaker();
	}

}
