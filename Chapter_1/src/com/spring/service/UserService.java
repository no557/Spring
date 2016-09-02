package com.spring.service;

import java.util.List;

import com.spring.domain.User;
import com.spring.domain.UserDao;
import com.spring.util.Level;

public class UserService {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void upgradeLevels() {
		List<User> users = userDao.getAll();

		for (User user : users) {
			if(canUpgradeLevel(user)){
				upgradeLevel(user);
			}
		}//end for 
	}
	
	public void add(User user){
		if(user.getLevel()==null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	private void upgradeLevel(User user){
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
