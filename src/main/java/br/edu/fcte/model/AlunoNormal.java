package br.edu.fcte.model;

/**
 * Classe que representa um aluno normal no sistema acadêmico
 */
public class AlunoNormal extends Aluno {
    
    public AlunoNormal(String nome, String matricula, String cursoGraduacao) {
        super(nome, matricula, cursoGraduacao);
    }

    @Override
    public boolean podeMatricular(Disciplina disciplina) {
        // Verifica se o aluno possui todos os pré-requisitos
        return disciplina.getPreRequisitos().stream()
                .allMatch(preReq -> getDisciplinasMatriculadas().contains(preReq));
    }

    @Override
    public String toString() {
        return String.format("Aluno Normal: %s (%s) - %s", 
            getNome(), getMatricula(), getCursoGraduacao());
    }
} 