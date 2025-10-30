package com.buyo.adminfx.ui.controllers;

import com.buyo.adminfx.dao.ProductDAO;
import com.buyo.adminfx.model.Product;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;

public class ProductController implements SearchableController {
    @FXML private TableView<Product> table;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, BigDecimal> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, String> colImage;
    @FXML private ComboBox<String> categoryFilter;

    private ObservableList<Product> masterData;
    private FilteredList<Product> filtered;
    private String currentQuery = "";
    private String currentCategory = null; // null or "Todas"

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        if (colCategory != null) {
            colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        }
        if (colImage != null) {
            colImage.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
            colImage.setCellFactory(col -> new TableCell<>() {
                private final ImageView imageView = new ImageView();
                {
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(60);
                    imageView.setPreserveRatio(true);
                }
                @Override
                protected void updateItem(String url, boolean empty) {
                    super.updateItem(url, empty);
                    if (empty || url == null || url.isBlank()) {
                        setGraphic(null);
                    } else {
                        try {
                            imageView.setImage(new Image(url, true));
                            setGraphic(imageView);
                        } catch (Exception ex) {
                            setGraphic(null);
                        }
                    }
                }
            });
        }

        masterData = FXCollections.observableArrayList(new ProductDAO().listAll());
        filtered = new FilteredList<>(masterData, p -> true);
        table.setItems(filtered);

        // Populate category filter
        if (categoryFilter != null) {
            ObservableList<String> categories = FXCollections.observableArrayList();
            categories.add("Todas");
            masterData.stream()
                .map(Product::getCategoryName)
                .filter(s -> s != null && !s.isBlank())
                .distinct()
                .sorted()
                .forEach(categories::add);
            categoryFilter.setItems(categories);
            categoryFilter.getSelectionModel().selectFirst();
            categoryFilter.valueProperty().addListener((obs, old, val) -> {
                currentCategory = (val == null || val.equals("Todas")) ? null : val;
                refilter();
            });
        }
    }

    @Override
    public void applySearch(String query) {
        currentQuery = query == null ? "" : query.trim().toLowerCase();
        refilter();
    }

    private void refilter() {
        if (filtered == null) return;
        final String q = currentQuery;
        final String cat = currentCategory;
        filtered.setPredicate(p -> {
            if (p == null) return false;
            // Category filter
            if (cat != null) {
                String pn = p.getCategoryName();
                if (pn == null || !pn.equalsIgnoreCase(cat)) return false;
            }
            // Text query filter
            if (q == null || q.isBlank()) return true;
            String id = String.valueOf(p.getId());
            String name = p.getName() == null ? "" : p.getName().toLowerCase();
            String price = p.getPrice() == null ? "" : p.getPrice().toPlainString().toLowerCase();
            String stock = String.valueOf(p.getStock());
            String category = p.getCategoryName() == null ? "" : p.getCategoryName().toLowerCase();
            return id.contains(q) || name.contains(q) || price.contains(q) || stock.contains(q) || category.contains(q);
        });
    }
}
