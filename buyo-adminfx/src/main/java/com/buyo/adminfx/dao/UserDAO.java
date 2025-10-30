package com.buyo.adminfx.dao;

import com.buyo.adminfx.db.Database;
import com.buyo.adminfx.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    public User authenticate(String email, String password) {
        String sql = "SELECT id, nome, email, is_admin, senha_hash FROM usuarios WHERE email = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
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
}
