package com.adminapp.model;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidade;

    public Product() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    @Override public String toString(){ return id + " - " + (nome==null?"":nome); }
}
