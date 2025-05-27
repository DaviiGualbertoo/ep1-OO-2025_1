package br.edu.fcte.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa uma turma no sistema acadêmico
 */
public class Turma implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigo;
    private Disciplina disciplina;
    private Professor professor;
    private String semestre;
    private FormaAvaliacao formaAvaliacao;
    private boolean presencial;
    private String sala;
    private String horario;
    private int capacidadeMaxima;
    private List<Aluno> alunosMatriculados;
    private Map<String, NotasAluno> notasAlunos;

    public Turma(String codigo, Disciplina disciplina, Professor professor, String semestre,
                 FormaAvaliacao formaAvaliacao, boolean presencial, String horario, int capacidadeMaxima) {
        this.codigo = codigo;
        this.disciplina = disciplina;
        this.professor = professor;
        this.semestre = semestre;
        this.formaAvaliacao = formaAvaliacao;
        this.presencial = presencial;
        this.horario = horario;
        this.capacidadeMaxima = capacidadeMaxima;
        this.alunosMatriculados = new ArrayList<>();
        this.notasAlunos = new HashMap<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public String getSemestre() {
        return semestre;
    }

    public FormaAvaliacao getFormaAvaliacao() {
        return formaAvaliacao;
    }

    public boolean isPresencial() {
        return presencial;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        // Só permite definir sala se a turma for presencial
        if (presencial) {
            this.sala = sala;
        }
    }

    public String getHorario() {
        return horario;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public List<Aluno> getAlunosMatriculados() {
        return new ArrayList<>(alunosMatriculados);
    }

    public boolean matricularAluno(Aluno aluno) {
        if (alunosMatriculados.size() >= capacidadeMaxima) {
            return false;
        }

        if (!alunosMatriculados.contains(aluno)) {
            alunosMatriculados.add(aluno);
            NotasAluno notas = new NotasAluno();
            notas.setFormaAvaliacao(formaAvaliacao);
            notasAlunos.put(aluno.getMatricula(), notas);
            return true;
        }
        return false;
    }

    public void desmatricularAluno(Aluno aluno) {
        alunosMatriculados.remove(aluno);
        notasAlunos.remove(aluno.getMatricula());
    }

    public void lancarNotas(Aluno aluno, double p1, double p2, double p3, double listas, double seminario) {
        if (aluno instanceof AlunoEspecial) {
            return; // Alunos especiais não recebem notas
        }

        NotasAluno notas = notasAlunos.get(aluno.getMatricula());
        if (notas != null) {
            notas.setNotas(p1, p2, p3, listas, seminario);
        }
    }

    public void lancarPresenca(Aluno aluno, int totalAulas, int presencas) {
        NotasAluno notas = notasAlunos.get(aluno.getMatricula());
        if (notas != null) {
            notas.setFrequencia(totalAulas, presencas);
        }
    }

    public NotasAluno getNotasAluno(Aluno aluno) {
        return notasAlunos.get(aluno.getMatricula());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turma turma)) return false;
        return codigo.equals(turma.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    /**
     * Classe interna que representa as notas e frequência de um aluno
     */
    public static class NotasAluno implements Serializable {
        private static final long serialVersionUID = 1L;
        private double p1;
        private double p2;
        private double p3;
        private double listas;
        private double seminario;
        private int totalAulas;
        private int presencas;
        private FormaAvaliacao formaAvaliacao;

        public NotasAluno() {
            this.p1 = 0;
            this.p2 = 0;
            this.p3 = 0;
            this.listas = 0;
            this.seminario = 0;
            this.totalAulas = 0;
            this.presencas = 0;
        }

        public void setNotas(double p1, double p2, double p3, double listas, double seminario) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.listas = listas;
            this.seminario = seminario;
        }

        public void setFrequencia(int totalAulas, int presencas) {
            this.totalAulas = totalAulas;
            this.presencas = presencas;
        }

        public void setFormaAvaliacao(FormaAvaliacao formaAvaliacao) {
            this.formaAvaliacao = formaAvaliacao;
        }

        public double getMediaFinal() {
            if (formaAvaliacao == null) {
                return 0;
            }
            return formaAvaliacao.calcularMedia(p1, p2, p3, listas, seminario);
        }

        public double getFrequencia() {
            return totalAulas > 0 ? (double) presencas / totalAulas * 100 : 0;
        }

        public boolean isAprovado() {
            return getMediaFinal() >= 5.0 && getFrequencia() >= 75.0;
        }

        public String getSituacao() {
            if (isAprovado()) {
                return "Aprovado";
            } else if (getFrequencia() < 75.0) {
                return "Reprovado por Falta";
            } else {
                return "Reprovado por Nota";
            }
        }

        @Override
        public String toString() {
            return String.format("P1: %.1f, P2: %.1f, P3: %.1f, Listas: %.1f, Seminário: %.1f\n" +
                            "Média Final: %.1f, Frequência: %.1f%%, Situação: %s",
                    p1, p2, p3, listas, seminario, getMediaFinal(), getFrequencia(), getSituacao());
        }

        public double getP1() { return p1; }
        public double getP2() { return p2; }
        public double getP3() { return p3; }
        public double getListas() { return listas; }
        public double getSeminario() { return seminario; }
        public int getTotalAulas() { return totalAulas; }
        public int getPresencas() { return presencas; }
    }
} 