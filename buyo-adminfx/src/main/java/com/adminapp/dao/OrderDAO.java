package com.adminapp.dao;

import com.adminapp.db.Database;
import com.adminapp.model.Order;
import com.adminapp.model.OrderItem;
import java.sql.*;
import java.util.*;

public class OrderDAO {
    public List<Order> findAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT id,cliente_id,data_pedido,total FROM pedido ORDER BY data_pedido DESC";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                Order o = new Order();
                o.setId(rs.getInt(1)); o.setClienteId(rs.getInt(2)); o.setDataPedido(rs.getTimestamp(3)); o.setTotal(rs.getBigDecimal(4));
                list.add(o);
            }
        }
        return list;
    }

    public void insert(Order o) throws SQLException {
        String insertPedido = "INSERT INTO pedido (cliente_id,total) VALUES (?,?)";
        String insertItem = "INSERT INTO item_pedido (pedido_id,produto_id,quantidade,subtotal) VALUES (?,?,?,?)";
        Connection c = null;
        try {
            c = Database.getConnection();
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(insertPedido, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1, o.getClienteId()); ps.setBigDecimal(2, o.getTotal()); ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()){ if (keys.next()) o.setId(keys.getInt(1)); }
            }
            try (PreparedStatement psItem = c.prepareStatement(insertItem)){
                for (OrderItem it : o.getItems()){
                    psItem.setInt(1, o.getId()); psItem.setInt(2, it.getProdutoId()); psItem.setInt(3, it.getQuantidade()); psItem.setBigDecimal(4, it.getSubtotal());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }
            c.commit();
        } catch (SQLException ex){ if (c!=null) try{c.rollback();}catch(SQLException e){} throw ex; }
        finally { if (c!=null) try{c.setAutoCommit(true); c.close(); }catch(SQLException e){} }
    }
}
