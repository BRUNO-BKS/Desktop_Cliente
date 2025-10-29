package com.buyo.adminfx.db;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        try {
            Properties props = new Properties();
            // Tenta carregar do classpath primeiro
            try (InputStream in = Database.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (in != null) {
                    props.load(in);
                } else {
                    // Fallback: arquivo na raiz do projeto (Eclipse: diretório de trabalho)
                    try (InputStream fin = new FileInputStream("db.properties")) {
                        props.load(fin);
                    }
                }
            }
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.password");
            connection = DriverManager.getConnection(url, user, pass);
            return connection;
        } catch (Exception e) {
            throw new SQLException("Falha ao conectar ao banco: " + e.getMessage(), e);
        }
    }
}
