package com.buyo.adminfx.ui.controllers;

import com.buyo.adminfx.auth.Session;
import com.buyo.adminfx.dao.UserDAO;
import com.buyo.adminfx.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void onLogin(ActionEvent e) {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pass = passwordField.getText() == null ? "" : passwordField.getText();
        if (email.isBlank() || pass.isBlank()) {
            setError("Informe email e senha.");
            return;
        }
        User user = userDAO.authenticate(email, pass);
        if (user == null) {
            setError("Credenciais inválidas.");
            return;
        }
        Session.setCurrentUser(user);
        navigateToMain(e);
    }

    private void setError(String msg) {
        if (errorLabel != null) {
            errorLabel.setText(msg);
        }
    }

    private void navigateToMain(ActionEvent e) {
        try {
            URL fxml = getClass().getResource("/com/buyo/adminfx/ui/MainView.fxml");
            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 650);
            URL css = getClass().getResource("/com/buyo/adminfx/ui/styles.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Buyo AdminFX");
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            setError("Falha ao abrir a aplicação.");
        }
    }
}
