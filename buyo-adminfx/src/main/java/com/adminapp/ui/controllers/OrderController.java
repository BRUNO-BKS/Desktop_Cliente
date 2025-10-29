package com.adminapp.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.adminapp.dao.OrderDAO;
import com.adminapp.dao.CustomerDAO;
import com.adminapp.dao.ProductDAO;
import com.adminapp.model.Order;
import com.adminapp.model.OrderItem;
import com.adminapp.model.Customer;
import com.adminapp.model.Product;
import java.sql.SQLException;
import java.util.*;
import java.math.BigDecimal;

public class OrderController {
    @FXML private TableView<Order> tableOrders;
    @FXML private TableColumn<Order,Integer> oColId;
    @FXML private TableColumn<Order,Integer> oColCliente;
    @FXML private TableColumn<Order,Object> oColData;
    @FXML private TableColumn<Order,Object> oColTotal;

    private final OrderDAO dao = new OrderDAO();

    @FXML
    private void initialize(){
        oColId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        oColCliente.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getClienteId()).asObject());
        oColData.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDataPedido()));
        oColTotal.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTotal()));
        onRefresh();
    }

    @FXML private void onNewOrder(){ createOrder(); }
    @FXML private void onRefresh(){ refresh(); }

    private void createOrder(){
        try{
            CustomerDAO cdao = new CustomerDAO(); ProductDAO pdao = new ProductDAO();
            List<Customer> customers = cdao.findAll(); List<Product> products = pdao.findAll();
            if (customers.isEmpty() || products.isEmpty()) { new Alert(Alert.AlertType.INFORMATION, "Cadastre clientes e produtos antes de criar pedidos.").showAndWait(); return; }

            ChoiceDialog<Customer> cd = new ChoiceDialog<>(customers.get(0), customers);
            cd.setTitle("Selecionar Cliente"); cd.setHeaderText("Escolha o cliente");
            Optional<Customer> chosen = cd.showAndWait(); if (!chosen.isPresent()) return;
            int clienteId = chosen.get().getId();

            List<OrderItem> items = new ArrayList<>();
            boolean adding = true;
            while (adding){
                ChoiceDialog<Product> pd = new ChoiceDialog<>(products.get(0), products);
                pd.setTitle("Selecionar Produto"); pd.setHeaderText("Escolha o produto");
                Optional<Product> pch = pd.showAndWait(); if (!pch.isPresent()) break;
                TextInputDialog qtdDia = new TextInputDialog("1"); qtdDia.setHeaderText("Quantidade"); Optional<String> qres = qtdDia.showAndWait(); if (!qres.isPresent()) break;
                int qtd = Integer.parseInt(qres.get()); Product prod = pch.get(); BigDecimal subtotal = prod.getPreco().multiply(new BigDecimal(qtd));
                OrderItem it = new OrderItem(); it.setProdutoId(prod.getId()); it.setQuantidade(qtd); it.setSubtotal(subtotal); items.add(it);
                Alert more = new Alert(Alert.AlertType.CONFIRMATION, "Adicionar mais itens?", ButtonType.YES, ButtonType.NO); Optional<ButtonType> resp = more.showAndWait(); if (!resp.isPresent() || resp.get()!=ButtonType.YES) adding=false;
            }
            if (items.isEmpty()) return;
            BigDecimal total = BigDecimal.ZERO; for (OrderItem it : items) total = total.add(it.getSubtotal());
            Order o = new Order(); o.setClienteId(clienteId); o.setItems(items); o.setTotal(total);
            dao.insert(o);
            new Alert(Alert.AlertType.INFORMATION, "Pedido criado com ID: " + o.getId()).showAndWait();
            refresh();
        } catch(Exception e){ e.printStackTrace(); new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); }
    }

    private void refresh(){ try{ List<Order> list = dao.findAll(); tableOrders.getItems().setAll(list); } catch(SQLException e){ e.printStackTrace(); new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); } }
}
