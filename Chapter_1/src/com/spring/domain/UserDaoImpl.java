package com.spring.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.spring.exception.DuplicateUserIdException;

public class UserDaoImpl implements UserDao{

	private JdbcTemplate jdbcTemplate;

	Connection c;
	PreparedStatement ps;
	ResultSet rs;

	public UserDaoImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private RowMapper<User> userMapper = new RowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	};

	public void add(User user)   {

		jdbcTemplate.update("insert into users(id,name,password) values(?,?,?)", user.getId(), user.getName(),
				user.getPassword());

	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[] { id }, userMapper);
	}

	public void deleteAll() {

		jdbcTemplate.update("delete from users");

	}

	public int getCount() {

		return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);

	}

	public List<User> getAll() {

		return jdbcTemplate.query("select * from users order by id", userMapper);
	}
}
