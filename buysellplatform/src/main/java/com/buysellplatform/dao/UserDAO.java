package com.buysellplatform.dao;

import com.buysellplatform.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/justbidlocal";
    private static final String JDBC_USER = "sohinimallick";
    private static final String JDBC_PASSWORD = "root";

    public boolean registerUser(User user) {
        String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "email VARCHAR(100) UNIQUE, " +
                "college VARCHAR(150), " +
                "whatsapp_number VARCHAR(15), " +
                "password VARCHAR(255));";

        String INSERT_USER_SQL = "INSERT INTO users (name, email, college, whatsapp_number, password) VALUES (?, ?, ?, ?, ?)";
        
        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            PreparedStatement createUserTableStmt = conn.prepareStatement(createUserTableSQL);
            createUserTableStmt.executeUpdate();
            
            // Establish the connection
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
                 
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getCollege());
                preparedStatement.setString(4, user.getWhatsappNumber());
                preparedStatement.setString(5, user.getPassword());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        
        return false;
    }

    public User loginUser(String email, String password) {
        String LOGIN_SQL = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Establish the connection
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_SQL)) {
                 
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setCollege(resultSet.getString("college"));
                    user.setWhatsappNumber(resultSet.getString("whatsapp_number"));
                    user.setPassword(resultSet.getString("password"));
                    return user;
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        
        return null;
    }
}
