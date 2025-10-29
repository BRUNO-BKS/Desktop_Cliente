package com.adminapp.model;

public class Customer {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;

    public Customer() {}

    public Customer(int id, String nome, String email, String telefone, String endereco) {
        this.id = id; this.nome = nome; this.email = email; this.telefone = telefone; this.endereco = endereco;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    @Override public String toString(){ return id + " - " + (nome==null?"":nome); }
}
