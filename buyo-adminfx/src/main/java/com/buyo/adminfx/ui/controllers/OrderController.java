package com.buyo.adminfx.ui.controllers;

import com.buyo.adminfx.dao.OrderDAO;
import com.buyo.adminfx.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderController {
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, Integer> colId;
    @FXML private TableColumn<Order, Integer> colCustomerId;
    @FXML private TableColumn<Order, BigDecimal> colTotal;
    @FXML private TableColumn<Order, LocalDateTime> colCreatedAt;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        ObservableList<Order> data = FXCollections.observableArrayList(new OrderDAO().listAll());
        table.setItems(data);
    }
}
