package com.spring.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;

import com.spring.domain.User;
import com.spring.domain.UserDao;
import com.spring.util.Level;

public class UserServiceImpl implements UserService {

	private UserDao userDao;
	private MailSender mailSender;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	// 트렌젝션이 필요한 부분
	public void upgradeLevels() {

		List<User> users = userDao.getAll();

		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		} // end for

	}

	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}

	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}

	private boolean canUpgradeLevel(User user) {

		Level currentLevel = user.getLevel();

		switch (currentLevel) {
		case BASIC:
			return (user.getLogin() >= 50);
		case SILVER:
			return (user.getRecommend() >= 30);
		case GOLD:
			return false;
		default:
			throw new AssertionError("unknown level: " + currentLevel);
		}

	}

	private void sendUpgradeEMail(User user) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setFrom("n87929@gmail.com");
		message.setSubject("Upgrade 안내");
		message.setText("등급 업그레이드 : " + user.getLevel().name());

		this.mailSender.send(message);
	}

}
