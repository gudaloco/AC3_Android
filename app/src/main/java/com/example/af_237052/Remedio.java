package com.example.af_237052;

public class Remedio {
    private String id;
    private String nome;
    private String descricao;
    private Integer horario;
    private Boolean check;


    public Remedio() {
    }

    public Remedio(String id, String nome, String descricao, Integer horario, Boolean check) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.horario = horario;
        this.check = check;
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

    public Integer getHorario() {
        return horario;
    }

    public void setHorario(Integer horario) {
        this.horario = horario;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
