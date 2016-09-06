package com.spring.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.spring.domain.User;

public class UserServiceTx implements UserService {

	UserService userService;
	PlatformTransactionManager transactionManager;

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
		userService.add(user);

	}

	@Override
	public void upgradeLevels() {
		// TODO Auto-generated method stub
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			userService.upgradeLevels();

			this.transactionManager.commit(status);
		} catch (Exception e) {
			// TODO: handle exception
			this.transactionManager.rollback(status);
			throw e;
		}

	}

}
