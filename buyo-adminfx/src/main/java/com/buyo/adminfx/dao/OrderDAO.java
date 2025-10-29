package com.buyo.adminfx.dao;

import com.buyo.adminfx.db.Database;
import com.buyo.adminfx.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public List<Order> listAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT id, customer_id, total, created_at FROM orders ORDER BY id DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getBigDecimal("total"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (Exception e) {
            // Sem DB configurado, retorna lista vazia
        }
        return list;
    }
}
