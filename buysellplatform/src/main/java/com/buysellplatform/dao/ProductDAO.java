package com.buysellplatform.dao;

import com.buysellplatform.model.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class ProductDAO {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/buyselldb";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "root";

    public boolean listProduct(Product product) {
        String INSERT_PRODUCT_SQL = "INSERT INTO products (title, image_url, description, min_bid_price, current_bid_price, auction_end_date, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL)) {
            	
            	System.out.println("Inserting product: ");
                System.out.println("Title: " + product.getTitle());
                System.out.println("Image URL: " + product.getImage());
                System.out.println("Description: " + product.getDescription());
                System.out.println("Min Bid Price: " + product.getMinBidPrice());
                System.out.println("Auction End Date: " + product.getAuctionEndDate());
                System.out.println("Seller ID: " + product.getSellerId());


                preparedStatement.setString(1, product.getTitle());
                preparedStatement.setString(2, product.getImage());
                preparedStatement.setString(3, product.getDescription());
                preparedStatement.setDouble(4, product.getMinBidPrice());
                preparedStatement.setDouble(5, 0.0); // Initial current bid price

                // Convert LocalDateTime to SQL Timestamp
                preparedStatement.setTimestamp(6, Timestamp.valueOf(product.getAuctionEndDate()));
                preparedStatement.setInt(7, product.getSellerId());

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("ProductDAO: Rows affected - " + rowsAffected);
                return rowsAffected > 0;
            }
        } catch (ClassNotFoundException | SQLException e) {
        	System.out.println("ProductDAO: SQL exception - " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public Product getProductDetails(int productId) {
        String SELECT_PRODUCT_SQL = "SELECT * FROM products WHERE id = ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_SQL)) {

                preparedStatement.setInt(1, productId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setTitle(resultSet.getString("title"));
                    product.setImage(resultSet.getString("image_url"));
                    product.setDescription(resultSet.getString("description"));
                    product.setMinBidPrice(resultSet.getDouble("min_bid_price"));
                    product.setCurrentBidPrice(resultSet.getDouble("current_bid_price"));
                    product.setAuctionEndDate(resultSet.getString("auction_end_date"));
                    product.setSellerId(resultSet.getInt("seller_id"));
                    return product;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateCurrentBidPrice(Product product) {
        String UPDATE_BID_PRICE_SQL = "UPDATE products SET current_bid_price = ? WHERE id = ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BID_PRICE_SQL)) {

                preparedStatement.setDouble(1, product.getCurrentBidPrice());
                preparedStatement.setInt(2, product.getId());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Product> getAllProducts() {
        String SELECT_ALL_PRODUCTS_SQL = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS_SQL);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setTitle(resultSet.getString("title"));
                    product.setImage(resultSet.getString("image_url"));
                    product.setDescription(resultSet.getString("description"));
                    product.setMinBidPrice(resultSet.getDouble("min_bid_price"));
                    product.setCurrentBidPrice(resultSet.getDouble("current_bid_price"));
                    product.setAuctionEndDate(resultSet.getString("auction_end_date"));
                    product.setSellerId(resultSet.getInt("seller_id"));
                    products.add(product);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
}
