package com.adminapp.dao;

import com.adminapp.db.Database;
import com.adminapp.model.Customer;
import java.sql.*;
import java.util.*;

public class CustomerDAO {
    public List<Customer> findAll() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT id,nome,email,telefone,endereco FROM cliente ORDER BY nome";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                Customer cu = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                list.add(cu);
            }
        }
        return list;
    }

    public Customer findById(int id) throws SQLException {
        String sql = "SELECT id,nome,email,telefone,endereco FROM cliente WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) return new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            }
        }
        return null;
    }

    public void insert(Customer cst) throws SQLException {
        String sql = "INSERT INTO cliente (nome,email,telefone,endereco) VALUES(?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, cst.getNome()); ps.setString(2, cst.getEmail()); ps.setString(3, cst.getTelefone()); ps.setString(4, cst.getEndereco());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()){ if (keys.next()) cst.setId(keys.getInt(1)); }
        }
    }

    public void update(Customer cst) throws SQLException {
        String sql = "UPDATE cliente SET nome=?,email=?,telefone=?,endereco=? WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, cst.getNome()); ps.setString(2, cst.getEmail()); ps.setString(3, cst.getTelefone()); ps.setString(4, cst.getEndereco()); ps.setInt(5, cst.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id); ps.executeUpdate();
        }
    }
}
