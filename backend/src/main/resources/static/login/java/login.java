package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Service
public class UserService {

	public boolean authenticate(String username, String password) {
		boolean isValidUser = false;
		String url = "jdbc:mysql://localhost:3306/yourdb";
		String dbUsername = "root";
		String dbPassword = "password";

		try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
			 Statement statement = connection.createStatement()) {

			// SQL Injection 취약점이 있는 쿼리
			String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				isValidUser = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValidUser;
	}
}
