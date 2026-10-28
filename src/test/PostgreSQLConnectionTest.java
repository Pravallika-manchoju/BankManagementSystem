package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnectionTest {

	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/bank_management_db";
		String username = "postgres";
		String password = "p185589a$$$";
		try(Connection connection = DriverManager.getConnection(url, username, password)){
			System.out.println("Connected to postgreSQL successfully");
			System.out.println("Database: "+ connection.getCatalog());
		}
		catch(SQLException e) {
			System.out.println("Database connection failed");
			e.printStackTrace();
		}
	}
}
