package com.adminapp.dao;

import com.adminapp.db.Database;
import com.adminapp.model.Product;
import java.sql.*;
import java.util.*;

public class ProductDAO {
    public List<Product> findAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id,nome,descricao,preco,quantidade FROM produto ORDER BY nome";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                Product p = new Product();
                p.setId(rs.getInt(1)); p.setNome(rs.getString(2)); p.setDescricao(rs.getString(3)); p.setPreco(rs.getBigDecimal(4)); p.setQuantidade(rs.getInt(5));
                list.add(p);
            }
        }
        return list;
    }

    public void insert(Product p) throws SQLException {
        String sql = "INSERT INTO produto (nome,descricao,preco,quantidade) VALUES(?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, p.getNome()); ps.setString(2, p.getDescricao()); ps.setBigDecimal(3, p.getPreco()); ps.setInt(4, p.getQuantidade());
            ps.executeUpdate(); try (ResultSet keys = ps.getGeneratedKeys()){ if (keys.next()) p.setId(keys.getInt(1)); }
        }
    }

    public void update(Product p) throws SQLException {
        String sql = "UPDATE produto SET nome=?,descricao=?,preco=?,quantidade=? WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, p.getNome()); ps.setString(2, p.getDescricao()); ps.setBigDecimal(3, p.getPreco()); ps.setInt(4, p.getQuantidade()); ps.setInt(5, p.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id); ps.executeUpdate();
        }
    }
}
