package com.buyo.adminfx.ui.controllers;

import com.buyo.adminfx.auth.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class MainController {
    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField searchField;

    private SearchableController currentSearchable;

    @FXML
    private Label userNameLabel;

    @FXML
    public void initialize() {
        if (userNameLabel != null) {
            if (Session.getCurrentUser() != null) {
                userNameLabel.setText(Session.getCurrentUser().getName());
            } else if (userNameLabel.getText() == null || userNameLabel.getText().isBlank()) {
                userNameLabel.setText("Admin");
            }
        }
    }

    @FXML
    public void showCustomers(ActionEvent e) {
        setCenterView("/com/buyo/adminfx/ui/CustomerView.fxml");
    }

    @FXML
    public void showProducts(ActionEvent e) {
        setCenterView("/com/buyo/adminfx/ui/ProductView.fxml");
    }

    @FXML
    public void showOrders(ActionEvent e) {
        setCenterView("/com/buyo/adminfx/ui/OrderView.fxml");
    }

    @FXML
    public void showCategories(ActionEvent e) {
        setCenterView("/com/buyo/adminfx/ui/CategoryView.fxml");
    }

    @FXML
    public void onSearch(ActionEvent e) {
        String query = searchField != null ? searchField.getText() : "";
        if (currentSearchable != null) {
            currentSearchable.applySearch(query == null ? "" : query);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Busca");
            alert.setContentText("Abra uma tela (Clientes/Produtos/Pedidos) para usar a busca.");
            alert.show();
        }
    }

    @FXML
    public void onProfile(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Perfil");
        alert.setContentText("Abrir página de perfil do usuário.");
        alert.show();
    }

    @FXML
    public void onLogout(ActionEvent e) {
        Platform.exit();
    }

    private void setCenterView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node content = loader.load();
            Object controller = loader.getController();
            if (controller instanceof SearchableController) {
                currentSearchable = (SearchableController) controller;
            } else {
                currentSearchable = null;
            }
            rootPane.setCenter(content);
        } catch (Exception ex) {
            // simples: não quebra a UI, apenas ignora caso erro
        }
    }
}
