package br.edu.fcte.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que representa um aluno no sistema acadêmico
 */
public abstract class Aluno extends Pessoa {
    private String cursoGraduacao;
    private List<Disciplina> disciplinasMatriculadas;
    private boolean semestreTrancado;

    public Aluno(String nome, String matricula, String cursoGraduacao) {
        super(nome, matricula);
        this.cursoGraduacao = cursoGraduacao;
        this.disciplinasMatriculadas = new ArrayList<>();
        this.semestreTrancado = false;
    }

    public String getCursoGraduacao() {
        return cursoGraduacao;
    }

    public void setCursoGraduacao(String cursoGraduacao) {
        this.cursoGraduacao = cursoGraduacao;
    }

    public List<Disciplina> getDisciplinasMatriculadas() {
        return new ArrayList<>(disciplinasMatriculadas);
    }

    public boolean isSemestreTrancado() {
        return semestreTrancado;
    }

    public void trancarSemestre() {
        this.semestreTrancado = true;
        this.disciplinasMatriculadas.clear();
    }

    public void destrancarSemestre() {
        this.semestreTrancado = false;
    }

    /**
     * Método abstrato que verifica se o aluno pode se matricular em uma disciplina
     * @param disciplina A disciplina que o aluno deseja se matricular
     * @return true se o aluno pode se matricular, false caso contrário
     */
    public abstract boolean podeMatricular(Disciplina disciplina);

    /**
     * Método que realiza a matrícula do aluno em uma disciplina
     * @param disciplina A disciplina em que o aluno será matriculado
     * @return true se a matrícula foi realizada com sucesso, false caso contrário
     */
    public boolean matricular(Disciplina disciplina) {
        if (semestreTrancado) {
            return false;
        }
        
        if (!podeMatricular(disciplina)) {
            return false;
        }

        return disciplinasMatriculadas.add(disciplina);
    }

    /**
     * Método que realiza o trancamento de uma disciplina
     * @param disciplina A disciplina que será trancada
     * @return true se o trancamento foi realizado com sucesso, false caso contrário
     */
    public boolean trancarDisciplina(Disciplina disciplina) {
        return disciplinasMatriculadas.remove(disciplina);
    }
} 