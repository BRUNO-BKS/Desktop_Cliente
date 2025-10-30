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
        String sql = "SELECT p.id, p.nome_produto AS name, p.preco AS price, IFNULL(e.quantidade, 0) AS stock, " +
                     "p.categoria_id AS category_id, c.nome AS category_name, p.imagem_url AS image_url " +
                     "FROM produtos p " +
                     "LEFT JOIN estoque e ON e.produto_id = p.id " +
                     "LEFT JOIN categorias c ON c.id = p.categoria_id " +
                     "WHERE p.ativo = 1 " +
                     "ORDER BY p.id DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getBigDecimal("price"),
                    rs.getInt("stock"),
                    (Integer) rs.getObject("category_id"),
                    rs.getString("category_name"),
                    rs.getString("image_url")
                ));
            }
        } catch (Exception e) {
            // Sem DB configurado, retorna lista vazia
        }
        return list;
    }
}
