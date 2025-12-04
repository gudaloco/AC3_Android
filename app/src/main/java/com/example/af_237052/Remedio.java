package com.example.af_237052;

import java.io.Serializable;

public class Remedio implements Serializable {
    private String id;
    private String nome;
    private String descricao;
    private String horario;
    private Boolean check;
    private Boolean finalizado;


    public Remedio() {
    }

    public Remedio(String id, String nome, String descricao, String horario, Boolean check, Boolean finalizado) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.horario = horario;
        this.check = check;
        this.finalizado = finalizado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Boolean getFinalizado() {
        return finalizado != null ? finalizado : false;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }
}
