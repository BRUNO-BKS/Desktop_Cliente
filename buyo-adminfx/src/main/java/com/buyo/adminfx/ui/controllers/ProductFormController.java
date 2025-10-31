package com.buyo.adminfx.ui.controllers;

import com.buyo.adminfx.dao.ProductDAO;
import com.buyo.adminfx.dao.CategoryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProductFormController {
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextField stockField;
    @FXML private TextField imageUrlField;
    @FXML private javafx.scene.control.CheckBox activeCheck;
    @FXML private Label statusLabel;

    private Integer editingId = null;
    private final Map<String, Integer> categoryIndex = new LinkedHashMap<>();
    private boolean saved = false;

    public boolean isSaved() { return saved; }

    @FXML
    public void initialize() {
        // Carrega categorias no combobox
        var catDao = new CategoryDAO();
        ObservableList<String> items = FXCollections.observableArrayList();
        catDao.listAll().forEach(c -> {
            items.add(c.getName());
            categoryIndex.put(c.getName(), c.getId());
        });
        categoryCombo.setItems(items);
    }

    public void setProduct(com.buyo.adminfx.model.Product p) {
        if (p == null) return;
        editingId = p.getId();
        nameField.setText(p.getName());
        priceField.setText(p.getPrice() == null ? "" : p.getPrice().toPlainString());
        stockField.setText(String.valueOf(p.getStock()));
        imageUrlField.setText(p.getImageUrl());
        if (p.getCategoryName() != null) {
            categoryCombo.getSelectionModel().select(p.getCategoryName());
        }
        activeCheck.setSelected(true);
    }

    @FXML
    public void onPickImage(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Escolher imagem do produto");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File f = fc.showOpenDialog(nameField.getScene().getWindow());
        if (f != null) {
            imageUrlField.setText(f.toURI().toString());
            setStatus("Imagem selecionada.");
        }
    }

    @FXML
    public void onSave(ActionEvent e) {
        String name = trim(nameField.getText());
        String priceStr = trim(priceField.getText());
        String stockStr = trim(stockField.getText());
        String imageUrl = trim(imageUrlField.getText());
        boolean active = activeCheck.isSelected();

        if (name.isBlank() || priceStr.isBlank()) {
            setStatus("Preencha nome e preço.");
            return;
        }
        BigDecimal price;
        try {
            price = new BigDecimal(priceStr.replace(",", "."));
        } catch (Exception ex) {
            setStatus("Preço inválido.");
            return;
        }
        int stock = 0;
        try { stock = Integer.parseInt(stockStr.isBlank() ? "0" : stockStr); } catch (Exception ignore) {}
        Integer categoryId = null;
        String sel = categoryCombo.getSelectionModel().getSelectedItem();
        if (sel != null) categoryId = categoryIndex.get(sel);

        var dao = new ProductDAO();
        boolean ok;
        if (editingId == null) {
            Integer newId = dao.createProduct(name, price, categoryId, imageUrl, stock, active);
            ok = newId != null;
        } else {
            ok = dao.updateProduct(editingId, name, price, categoryId, imageUrl, stock, active);
        }
        if (!ok) {
            setStatus("Falha ao salvar produto.");
            return;
        }
        saved = true;
        close();
    }

    @FXML
    public void onCancel(ActionEvent e) { close(); }

    private void close() {
        Stage st = (Stage) nameField.getScene().getWindow();
        st.close();
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
    private void setStatus(String s) { if (statusLabel != null) statusLabel.setText(s); }
}
