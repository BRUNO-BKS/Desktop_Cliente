package com.buyo.adminfx.ui.controllers;

import com.buyo.adminfx.dao.CategoryDAO;
import com.buyo.adminfx.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CategoryController implements SearchableController {
    @FXML private TableView<Category> table;
    @FXML private TableColumn<Category, Integer> colId;
    @FXML private TableColumn<Category, String> colName;
    @FXML private TableColumn<Category, String> colDescription;

    private ObservableList<Category> masterData;
    private FilteredList<Category> filtered;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        masterData = FXCollections.observableArrayList(new CategoryDAO().listAll());
        filtered = new FilteredList<>(masterData, c -> true);
        table.setItems(filtered);
    }

    @Override
    public void applySearch(String query) {
        String q = (query == null) ? "" : query.trim().toLowerCase();
        if (filtered == null) return;
        if (q.isEmpty()) { filtered.setPredicate(c -> true); return; }
        filtered.setPredicate(c -> {
            if (c == null) return false;
            String id = String.valueOf(c.getId());
            String name = c.getName() == null ? "" : c.getName().toLowerCase();
            String desc = c.getDescription() == null ? "" : c.getDescription().toLowerCase();
            return id.contains(q) || name.contains(q) || desc.contains(q);
        });
    }
}
