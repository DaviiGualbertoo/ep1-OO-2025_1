package br.edu.fcte.model;

import java.io.Serializable;

/**
 * Classe abstrata que representa uma pessoa no sistema acadêmico
 */
public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String matricula;

    public Pessoa(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pessoa pessoa)) return false;
        return matricula.equals(pessoa.matricula);
    }

    @Override
    public int hashCode() {
        return matricula.hashCode();
    }
} 