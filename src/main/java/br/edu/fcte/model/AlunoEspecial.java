package br.edu.fcte.model;

/**
 * Classe que representa um aluno especial no sistema acadêmico.
 * 
 * Características e restrições:
 * 1. Matrícula:
 *    - Limite máximo de 2 disciplinas simultâneas
 *    - Não precisa cumprir pré-requisitos das disciplinas
 *    - Pode se matricular em qualquer disciplina com vaga
 * 
 * 2. Avaliação:
 *    - Não recebe notas nas disciplinas
 *    - Apenas controle de frequência é registrado
 *    - Precisa cumprir 75% de frequência
 * 
 * 3. Limitações:
 *    - Não pode trancar disciplinas individualmente
 *    - Não recebe histórico com notas
 *    - Não conta para o cálculo de médias da turma
 * 
 * 4. Documentação:
 *    - Recebe apenas declaração de participação
 *    - Frequência é registrada no histórico
 */
public class AlunoEspecial extends Aluno {
    // Número máximo de disciplinas que um aluno especial pode cursar simultaneamente
    private static final int MAXIMO_DISCIPLINAS = 2;
    
    public AlunoEspecial(String nome, String matricula, String cursoGraduacao) {
        super(nome, matricula, cursoGraduacao);
    }

    @Override
    public boolean podeMatricular(Disciplina disciplina) {
        // Verifica apenas o limite de disciplinas, ignorando pré-requisitos
        // Alunos especiais podem se matricular em qualquer disciplina
        // desde que não ultrapassem o limite de 2 disciplinas
        if (getDisciplinasMatriculadas().size() >= MAXIMO_DISCIPLINAS) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Aluno Especial: %s (%s) - %s", 
            getNome(), getMatricula(), getCursoGraduacao());
    }
} 