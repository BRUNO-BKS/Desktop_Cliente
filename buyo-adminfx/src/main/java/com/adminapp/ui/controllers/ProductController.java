package com.adminapp.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import com.adminapp.dao.ProductDAO;
import com.adminapp.model.Product;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;

public class ProductController {
    @FXML private TableView<Product> tableProducts;
    @FXML private TableColumn<Product, Integer> pColId;
    @FXML private TableColumn<Product, String> pColNome;
    @FXML private TableColumn<Product, Object> pColPreco;
    @FXML private TableColumn<Product, Integer> pColQtd;

    private final ProductDAO dao = new ProductDAO();

    @FXML
    private void initialize(){
        pColId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        pColNome.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNome()));
        pColPreco.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPreco()));
        pColQtd.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantidade()).asObject());
        onRefresh();
    }

    @FXML private void onNew(){ showDialog(null); }
    @FXML private void onEdit(){ var sel = tableProducts.getSelectionModel().getSelectedItem(); if (sel!=null) showDialog(sel); }
    @FXML private void onDelete(){ var sel = tableProducts.getSelectionModel().getSelectedItem(); if (sel==null) return; try{ dao.delete(sel.getId()); onRefresh(); }catch(SQLException e){ showError(e);} }
    @FXML private void onRefresh(){ try{ List<Product> list = dao.findAll(); tableProducts.getItems().setAll(list);}catch(SQLException e){ showError(e);} }

    private void showDialog(Product p){ Dialog<Product> dialog = new Dialog<>(); dialog.setTitle(p==null?"Novo Produto":"Editar Produto"); ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE); dialog.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        TextField nome = new TextField(); TextField preco = new TextField(); TextField qtd = new TextField(); if (p!=null){ nome.setText(p.getNome()); preco.setText(p.getPreco()!=null?p.getPreco().toString():""); qtd.setText(String.valueOf(p.getQuantidade())); }
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.add(new Label("Nome:"),0,0); grid.add(nome,1,0); grid.add(new Label("Preço:"),0,1); grid.add(preco,1,1); grid.add(new Label("Qtd:"),0,2); grid.add(qtd,1,2);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(db -> { if (db==ok){ try{ Product target = (p==null)? new Product() : p; target.setNome(nome.getText()); target.setPreco(new BigDecimal(preco.getText())); target.setQuantidade(Integer.parseInt(qtd.getText())); if (target.getId()==0) dao.insert(target); else dao.update(target); return target; } catch(Exception e){ showError(e);} } return null; });
        dialog.showAndWait(); onRefresh(); }

    private void showError(Exception e){ e.printStackTrace(); new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); }
}
