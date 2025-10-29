package com.adminapp.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import com.adminapp.dao.CustomerDAO;
import com.adminapp.model.Customer;
import java.sql.SQLException;
import java.util.List;

public class CustomerController {

    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String> colNome;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colTelefone;
    @FXML private TableColumn<Customer, String> colEndereco;

    private final CustomerDAO dao = new CustomerDAO();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        colNome.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNome()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        colTelefone.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefone()));
        colEndereco.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEndereco()));
        onRefresh();
    }

    @FXML private void onNew() { showDialog(null); }
    @FXML private void onEdit() { var sel = tableCustomers.getSelectionModel().getSelectedItem(); if (sel!=null) showDialog(sel); }
    @FXML private void onDelete() {
        var sel = tableCustomers.getSelectionModel().getSelectedItem(); if (sel==null) return;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Excluir cliente?", ButtonType.YES, ButtonType.NO);
        a.showAndWait().ifPresent(btn -> { if (btn==ButtonType.YES) { try { dao.delete(sel.getId()); onRefresh(); } catch (SQLException e) { showError(e); } } });
    }
    @FXML private void onRefresh() { try { List<Customer> list = dao.findAll(); tableCustomers.getItems().setAll(list);} catch(SQLException e){ showError(e);} }

    private void showDialog(Customer c){
        Dialog<Customer> dialog = new Dialog<>(); dialog.setTitle(c==null?"Novo Cliente":"Editar Cliente");
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE); dialog.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        TextField nome = new TextField(); TextField email = new TextField(); TextField telefone = new TextField(); TextField endereco = new TextField();
        if (c!=null) { nome.setText(c.getNome()); email.setText(c.getEmail()); telefone.setText(c.getTelefone()); endereco.setText(c.getEndereco()); }
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Nome:"),0,0); grid.add(nome,1,0);
        grid.add(new Label("Email:"),0,1); grid.add(email,1,1);
        grid.add(new Label("Telefone:"),0,2); grid.add(telefone,1,2);
        grid.add(new Label("Endereço:"),0,3); grid.add(endereco,1,3);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(db -> {
            if (db==ok){
                try {
                    Customer target = (c==null) ? new Customer() : c;
                    target.setNome(nome.getText());
                    target.setEmail(email.getText());
                    target.setTelefone(telefone.getText());
                    target.setEndereco(endereco.getText());
                    if (target.getId()==0) dao.insert(target); else dao.update(target);
                    return target;
                } catch(Exception e){ showError(e); }
            }
            return null;
        });
        dialog.showAndWait(); onRefresh();
    }

    private void showError(Exception e){ e.printStackTrace(); new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); }
}
