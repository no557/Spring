package com.spring.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

import org.springframework.dao.EmptyResultDataAccessException;

import com.spring.factory.StatementStrategy;
import com.spring.util.ConnectionMaker;
import com.spring.util.SimpleConnectionMaker;

public class UserDao {

	private DataSource  dataSource;
	private JdbcContext jdbcContext;

	Connection c;
	PreparedStatement ps;
	ResultSet rs;

	public UserDao(DataSource connectionMaker,JdbcContext jdbcContext) {

		this.dataSource = connectionMaker;
		this.jdbcContext = jdbcContext;

	}

	public void add(User user) throws ClassNotFoundException, SQLException {

		StatementStrategy stmt = new StatementStrategy() {

			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				// TODO Auto-generated method stub
				ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());

				return ps;
			}
		};

		jdbcContext.workWithStatementStrategy(stmt);

	}

	public User get(String id) throws ClassNotFoundException, SQLException {

		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("select * from users where id=?");
			ps.setString(1, id);
			rs = ps.executeQuery();

			User user = null;
			if (rs.next()) {

				user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));

			}
			return user;

		} catch (Exception e) {
			// TODO: handle exception
			throw new EmptyResultDataAccessException(1);
		} finally {
			ps.close();
			c.close();
		}

	}

	public void deleteAll() throws SQLException {

		StatementStrategy stmt = new StatementStrategy() {

			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				// TODO Auto-generated method stub

				c = dataSource.getConnection();
				ps = c.prepareStatement("delete from users");
			
				return ps;
			}
		};

		jdbcContext.workWithStatementStrategy(stmt);
	}

	public int getCount() throws SQLException {

		try {

			c = dataSource.getConnection();
			ps = c.prepareStatement("select count(*) from users");
			rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);

			return count;

		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

		}
	}
}
