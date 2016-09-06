package com.spring.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.spring.domain.User;
import com.spring.domain.UserDao;
import com.spring.util.Level;

public class UserService {

	private UserDao userDao;
	private DataSource dataSource;
	private PlatformTransactionManager txManager;
	

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	//트렌젝션이 필요한 부분 
	public void upgradeLevels() throws SQLException {
		
		TransactionStatus status = this.txManager.getTransaction(new DefaultTransactionDefinition());
		try {
			
		List<User> users = userDao.getAll();

		for (User user : users) {
			if(canUpgradeLevel(user)){
				upgradeLevel(user);
			}
		}//end for 
		
		this.txManager.commit(status);

		} catch (Exception e) {
			// TODO: handle exception
			
			this.txManager.rollback(status);
			throw e ;

		}
	}
	
	public void add(User user){
		if(user.getLevel()==null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	protected void upgradeLevel(User user){
		user.upgradeLevel();
		userDao.update(user);
	}
	
	private boolean canUpgradeLevel(User user) {
		
		Level currentLevel = user.getLevel();

		switch (currentLevel) {
		case BASIC: return (user.getLogin()>=50);
		case SILVER:return (user.getRecommend()>=30);
		case GOLD: return false;
		default: throw new AssertionError("unknown level: "+ currentLevel);
		}
		
	}
	

}
