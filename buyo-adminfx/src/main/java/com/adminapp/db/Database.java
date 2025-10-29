package com.adminapp.db;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class Database {
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream in = Database.class.getResourceAsStream("/db.properties")) {
            Properties p = new Properties();
            if (in != null) p.load(in);
            url = p.getProperty("db.url", "jdbc:mysql://localhost:3306/admin_app_db?useSSL=false&serverTimezone=UTC");
            user = p.getProperty("db.user", "root");
            password = p.getProperty("db.password", "");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
