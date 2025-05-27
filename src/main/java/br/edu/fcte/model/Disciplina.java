package br.edu.fcte.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma disciplina no sistema acadêmico
 */
public class Disciplina implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String codigo;
    private int cargaHoraria;
    private List<Disciplina> preRequisitos;
    private List<Turma> turmas;

    public Disciplina(String nome, String codigo, int cargaHoraria) {
        this.nome = nome;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
        this.preRequisitos = new ArrayList<>();
        this.turmas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public List<Disciplina> getPreRequisitos() {
        return new ArrayList<>(preRequisitos);
    }

    public void adicionarPreRequisito(Disciplina disciplina) {
        if (!preRequisitos.contains(disciplina)) {
            preRequisitos.add(disciplina);
        }
    }

    public void removerPreRequisito(Disciplina disciplina) {
        preRequisitos.remove(disciplina);
    }

    public List<Turma> getTurmas() {
        return new ArrayList<>(turmas);
    }

    public void adicionarTurma(Turma turma) {
        if (!turmas.contains(turma)) {
            turmas.add(turma);
        }
    }

    public void removerTurma(Turma turma) {
        turmas.remove(turma);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Disciplina that)) return false;
        return codigo.equals(that.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %dh", nome, codigo, cargaHoraria);
    }
} 