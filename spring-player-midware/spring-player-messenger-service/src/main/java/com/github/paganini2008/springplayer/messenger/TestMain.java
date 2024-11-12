package com.github.paganini2008.springplayer.messenger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.collection.Tuple;
import com.github.paganini2008.devtools.jdbc.Cursor;
import com.github.paganini2008.devtools.jdbc.JdbcUtils;

public class TestMain {

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}
	}

	private static Connection getMySqlConnection() throws SQLException {
		String jdbcUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";
		return DriverManager.getConnection(jdbcUrl, "fengy", "123456");
	}

	private static Connection getPgSqlConnection() throws SQLException {
		String jdbcUrl = "jdbc:postgresql://54.252.7.227:5432/maxibet_user?characterEncoding=utf8&allowMultiQueries=true&useSSL=false&stringtype=unspecified";
		return DriverManager.getConnection(jdbcUrl, "maxibet", "globalTLLC09");
	}

	private static int saveCountries(int countryId, String name, String chName, String code) throws Exception {
		String insertCountries = "insert into area(name,cn_name,code,type,pid) values (?,?,?,?,?)";
		Connection connection = getPgSqlConnection();
		try {
			return JdbcUtils.insert(connection, insertCountries, new Object[] { name, chName, code, 1, 0 });
		} finally {
			JdbcUtils.close(connection);
			System.out.println("Save country: " + name);
		}
	}

	private static int saveCities(int countryId, String name, String chName, String code) throws Exception {
		String insertCountries = "insert into area(name,cn_name,code,type,pid) values (?,?,?,?,?)";
		Connection connection = getPgSqlConnection();
		try {
			return JdbcUtils.insert(connection, insertCountries, new Object[] { name, chName, code, 2, countryId });
		} finally {
			JdbcUtils.close(connection);
			System.out.println("Save city: " + name);
		}
	}

	private static void findStates(int countryId, int myCountryId) throws Exception {
		Connection connection = getMySqlConnection();
		try {
			Cursor<Tuple> cursor = JdbcUtils.cursor(connection, "select * from states where country_id=?", new Object[] { countryId });
			while (cursor.hasNext()) {
				Tuple tuple = cursor.next();
				int stateId = (Integer) tuple.get("id");
				findCities(stateId, myCountryId);
			}
		} finally {
			JdbcUtils.close(connection);
		}
	}

	private static void findCities(int stateId, int myCountryId) throws Exception {
		Connection connection = getMySqlConnection();
		try {
			Cursor<Tuple> cursor = JdbcUtils.cursor(connection, "select * from cities where state_id=?", new Object[] { stateId });
			while (cursor.hasNext()) {
				Tuple tuple = cursor.next();
				String code = (String) tuple.get("code");
				String name = (String) tuple.get("name");
				String chName = (String) tuple.get("cname");
				if (StringUtils.isBlank(name)) {
					continue;
				}
				saveCities(myCountryId, name, chName, code);
			}
		} finally {
			JdbcUtils.close(connection);
		}
	}

	public static void main(String[] args) throws Exception {

		Connection connection = getMySqlConnection();
		try {
			Cursor<Tuple> cursor = JdbcUtils.cursor(connection, "select * from countries");
			while (cursor.hasNext()) {
				Tuple tuple = cursor.next();
				int id = (Integer) tuple.get("id");
				String code = (String) tuple.get("code");
				String name = (String) tuple.get("name");
				String chName = (String) tuple.get("cname");
				if (StringUtils.isBlank(name)) {
					continue;
				}
				int countryId = saveCountries(id, name, chName, code);
				if (countryId > 0) {
					findStates(id, countryId);
				}
			}
		} finally {
			JdbcUtils.close(connection);
		}
		System.out.println("Over");
	}

}
