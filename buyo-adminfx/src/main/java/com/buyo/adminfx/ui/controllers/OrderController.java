package com.buyo.adminfx.ui.controllers;

import com.buyo.adminfx.dao.OrderDAO;
import com.buyo.adminfx.model.Order;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderController implements SearchableController {
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, Integer> colId;
    @FXML private TableColumn<Order, Integer> colCustomerId;
    @FXML private TableColumn<Order, BigDecimal> colTotal;
    @FXML private TableColumn<Order, LocalDateTime> colCreatedAt;

    private ObservableList<Order> masterData;
    private FilteredList<Order> filtered;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        masterData = FXCollections.observableArrayList(new OrderDAO().listAll());
        filtered = new FilteredList<>(masterData, o -> true);
        table.setItems(filtered);
    }

    @Override
    public void applySearch(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        if (filtered == null) return;
        if (q.isEmpty()) {
            filtered.setPredicate(o -> true);
            return;
        }
        filtered.setPredicate(o -> {
            if (o == null) return false;
            String id = String.valueOf(o.getId());
            String customerId = String.valueOf(o.getCustomerId());
            String total = o.getTotal() == null ? "" : o.getTotal().toPlainString().toLowerCase();
            String created = o.getCreatedAt() == null ? "" : o.getCreatedAt().toString().toLowerCase();
            return id.contains(q) || customerId.contains(q) || total.contains(q) || created.contains(q);
        });
    }
}
