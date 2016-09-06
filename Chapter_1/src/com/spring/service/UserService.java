package com.spring.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.spring.domain.User;
import com.spring.domain.UserDao;
import com.spring.util.Level;

public class UserService {

	private UserDao userDao;
	private DataSource dataSource;
	

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	//트렌젝션이 필요한 부분 
	public void upgradeLevels() throws SQLException {
		TransactionSynchronizationManager.initSynchronization();
		
		Connection c = DataSourceUtils.getConnection(dataSource);
		c.setAutoCommit(false);
		try {
			
		List<User> users = userDao.getAll();

		for (User user : users) {
			if(canUpgradeLevel(user)){
				upgradeLevel(user);
			}
		}//end for 
		
		c.commit();

		} catch (Exception e) {
			// TODO: handle exception
			
			c.rollback();
			throw e ;

		}finally {
			
			DataSourceUtils.releaseConnection(c, dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
			
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
