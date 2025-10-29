package com.buyo.adminfx.dao;

import com.buyo.adminfx.db.Database;
import com.buyo.adminfx.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<Product> listAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, name, price, stock FROM products ORDER BY id DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock")
                ));
            }
        } catch (Exception e) {
            // Sem DB configurado, retorna lista vazia
        }
        return list;
    }
}
