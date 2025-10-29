package com.buyo.adminfx.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class MainController {
    @FXML
    private BorderPane rootPane;

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

    private void setCenterView(String fxmlPath) {
        try {
            Node content = FXMLLoader.load(getClass().getResource(fxmlPath));
            rootPane.setCenter(content);
        } catch (Exception ex) {
            // simples: não quebra a UI, apenas ignora caso erro
        }
    }
}
