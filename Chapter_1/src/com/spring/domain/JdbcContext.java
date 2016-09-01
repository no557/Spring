package com.spring.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.spring.factory.StatementStrategy;

public class JdbcContext {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		try {

			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);

			ps.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			if (ps != null) {
				try {
					ps.close();

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (c != null) {
				try {
					c.close();

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
	}

}
