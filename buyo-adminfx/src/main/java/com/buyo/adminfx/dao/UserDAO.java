package com.buyo.adminfx.dao;

import com.buyo.adminfx.db.Database;
import com.buyo.adminfx.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    public User authenticate(String emailOrName, String password) {
        String input = emailOrName == null ? "" : emailOrName.trim();
        if (input.isEmpty()) return null;
        boolean looksEmail = input.contains("@");
        String sql = looksEmail
                ? "SELECT id, nome, email, is_admin, senha_hash FROM usuarios WHERE email = ? LIMIT 1"
                : "SELECT id, nome, email, is_admin, senha_hash FROM usuarios WHERE nome = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, input);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String mail = rs.getString("email");
                    boolean admin = rs.getInt("is_admin") == 1;
                    String hash = rs.getString("senha_hash");
                    if (hash == null) return null;

                    String pass = (password == null) ? "" : password;
                    boolean ok = false;

                    boolean looksBcrypt = (hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"))
                            && hash.length() >= 56 && hash.length() <= 80;
                    if (looksBcrypt) {
                        try {
                            ok = BCrypt.checkpw(pass, hash);
                        } catch (Exception ignore) {
                            ok = false; // malformed hash -> treat as invalid
                        }
                    } else {
                        // Fallback: plain-text compare if your DB stores plaintext
                        ok = hash.equals(pass);
                    }

                    if (!ok) return null;
                    return new User(id, nome, mail, admin);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public boolean createAdmin(String name, String email, String rawPassword) {
        String nm = name == null ? "" : name.trim();
        String em = email == null ? "" : email.trim();
        String pw = rawPassword == null ? "" : rawPassword;
        if (nm.isEmpty() || em.isEmpty() || pw.isEmpty()) return false;
        String hash = BCrypt.hashpw(pw, BCrypt.gensalt(10));
        String sql = "INSERT INTO usuarios (nome, email, senha_hash, is_admin) VALUES (?,?,?,1) " +
                     "ON DUPLICATE KEY UPDATE nome=VALUES(nome), senha_hash=VALUES(senha_hash), is_admin=1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nm);
            ps.setString(2, em);
            ps.setString(3, hash);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean hasAnyAdmin() {
        String sql = "SELECT 1 FROM usuarios WHERE is_admin = 1 LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean promoteOrResetAdmin(String name, String email, String rawPassword) {
        String nm = name == null ? "" : name.trim();
        String em = email == null ? "" : email.trim();
        String pw = rawPassword == null ? "" : rawPassword;
        if (em.isEmpty() || pw.isEmpty()) return false;
        String hash = BCrypt.hashpw(pw, BCrypt.gensalt(10));
        String sql = "UPDATE usuarios SET nome = COALESCE(?, nome), senha_hash = ?, is_admin = 1 WHERE email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nm.isEmpty() ? null : nm);
            ps.setString(2, hash);
            ps.setString(3, em);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean existsByEmail(String email) {
        String em = email == null ? "" : email.trim();
        if (em.isEmpty()) return false;
        String sql = "SELECT 1 FROM usuarios WHERE email = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, em);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
