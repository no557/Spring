package com.spring.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.spring.domain.User;
import com.spring.domain.UserDao;
import com.spring.util.Level;

public class UserService {

	private UserDao userDao;
	private DataSource dataSource;
	private PlatformTransactionManager txManager;
	private MailSender mailSender;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	// 트렌젝션이 필요한 부분
	public void upgradeLevels() throws SQLException {

		TransactionStatus status = this.txManager.getTransaction(new DefaultTransactionDefinition());
		try {
			
			upgradeLevelsInternal();
			this.txManager.commit(status);

		} catch (Exception e) {
			// TODO: handle exception

			this.txManager.rollback(status);
			throw e;

		}
	}

	private void upgradeLevelsInternal() {

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
