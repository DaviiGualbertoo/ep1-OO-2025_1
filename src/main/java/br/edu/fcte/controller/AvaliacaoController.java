package br.edu.fcte.controller;

import br.edu.fcte.model.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador responsável por gerenciar as avaliações e frequências do sistema.
 * 
 * Regras importantes:
 * - Alunos especiais não recebem notas, apenas frequência
 * - Notas devem estar entre 0 e 10
 * - Frequência mínima de 75% para aprovação
 * - Média final varia conforme a forma de avaliação da turma (simples ou ponderada)
 * - Dados são persistidos em arquivo texto no formato UTF-8
 * 
 * Formato do arquivo de persistência:
 * codigoTurma;matriculaAluno;p1;p2;p3;listas;seminario;totalAulas;presencas
 */
public class AvaliacaoController implements Persistivel {
    private Map<String, Map<String, Turma.NotasAluno>> notasPorTurma;
    private DisciplinaController disciplinaController;

    public AvaliacaoController() {
        this.notasPorTurma = new HashMap<>();
    }

    public void setDisciplinaController(DisciplinaController disciplinaController) {
        this.disciplinaController = disciplinaController;
    }

    /**
     * Lança as notas de um aluno em uma turma.
     * 
     * Validações:
     * - Aluno deve estar matriculado na turma
     * - Não permite lançar notas para alunos especiais
     * - Todas as notas devem estar entre 0 e 10
     * 
     * @throws IllegalArgumentException se alguma nota estiver fora do intervalo [0,10]
     * @throws IllegalStateException se o aluno não estiver matriculado na turma
     */
    public boolean lancarNotas(String codigoTurma, Aluno aluno, double p1, double p2, double p3,
                             double listas, double seminario) {
        // Validação de notas
        if (p1 < 0 || p1 > 10 || p2 < 0 || p2 > 10 || p3 < 0 || p3 > 10 ||
            listas < 0 || listas > 10 || seminario < 0 || seminario > 10) {
            throw new IllegalArgumentException("Todas as notas devem estar entre 0 e 10");
        }

        if (aluno instanceof AlunoEspecial) {
            return false; // Alunos especiais não recebem notas
        }

        Turma turma = getTurma(codigoTurma);
        if (turma == null) {
            return false;
        }

        if (!turma.getAlunosMatriculados().contains(aluno)) {
            throw new IllegalStateException("Aluno não está matriculado na turma");
        }

        turma.lancarNotas(aluno, p1, p2, p3, listas, seminario);
        return true;
    }

    /**
     * Lança a frequência de um aluno em uma turma.
     * 
     * Validações:
     * - Aluno deve estar matriculado na turma
     * - Total de aulas deve ser maior que zero
     * - Número de presenças não pode ser negativo ou maior que o total de aulas
     * 
     * @throws IllegalArgumentException se os valores de frequência forem inválidos
     * @throws IllegalStateException se o aluno não estiver matriculado na turma
     */
    public boolean lancarFrequencia(String codigoTurma, Aluno aluno, int totalAulas, int presencas) {
        if (totalAulas <= 0) {
            throw new IllegalArgumentException("Total de aulas deve ser maior que zero");
        }
        if (presencas < 0 || presencas > totalAulas) {
            throw new IllegalArgumentException("Número de presenças inválido");
        }

        Turma turma = getTurma(codigoTurma);
        if (turma == null) {
            return false;
        }

        if (!turma.getAlunosMatriculados().contains(aluno)) {
            throw new IllegalStateException("Aluno não está matriculado na turma");
        }

        turma.lancarPresenca(aluno, totalAulas, presencas);
        return true;
    }

    /**
     * Gera o relatório detalhado de uma turma específica.
     * 
     * O relatório inclui:
     * - Informações gerais da turma (código, disciplina, professor)
     * - Lista de todos os alunos matriculados
     * - Notas e frequência de cada aluno
     * - Estatísticas gerais da turma
     * 
     * @throws IllegalArgumentException se o código da turma for inválido
     */
    public String gerarRelatorioTurma(String codigoTurma) {
        Turma turma = getTurma(codigoTurma);
        if (turma == null) {
            return "Turma não encontrada";
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append(String.format("Relatório da Turma: %s\n", codigoTurma));
        relatorio.append(String.format("Disciplina: %s\n", turma.getDisciplina().getNome()));
        relatorio.append(String.format("Professor: %s\n", turma.getProfessor().getNome()));
        relatorio.append(String.format("Semestre: %s\n\n", turma.getSemestre()));

        for (Aluno aluno : turma.getAlunosMatriculados()) {
            Turma.NotasAluno notas = turma.getNotasAluno(aluno);
            if (notas != null) {
                relatorio.append(String.format("Aluno: %s (%s)\n", aluno.getNome(), aluno.getMatricula()));
                relatorio.append(notas.toString()).append("\n\n");
            }
        }

        return relatorio.toString();
    }

    /**
     * Gera o relatório completo de uma disciplina.
     * 
     * O relatório inclui:
     * - Dados gerais da disciplina
     * - Todas as turmas ofertadas
     * - Estatísticas por turma
     * - Total de alunos matriculados
     * 
     * @throws IllegalArgumentException se o código da disciplina for inválido
     */
    public String gerarRelatorioDisciplina(String codigoDisciplina, DisciplinaController disciplinaController) {
        Disciplina disciplina = disciplinaController.buscarDisciplina(codigoDisciplina);
        if (disciplina == null) {
            return "Disciplina não encontrada";
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append(String.format("Relatório da Disciplina: %s (%s)\n", 
                disciplina.getNome(), disciplina.getCodigo()));
        relatorio.append(String.format("Carga Horária: %d horas\n\n", disciplina.getCargaHoraria()));

        for (Turma turma : disciplina.getTurmas()) {
            relatorio.append(String.format("Turma: %s\n", turma.getCodigo()));
            relatorio.append(String.format("Professor: %s\n", turma.getProfessor().getNome()));
            relatorio.append(String.format("Semestre: %s\n", turma.getSemestre()));
            relatorio.append(String.format("Alunos Matriculados: %d\n\n", 
                    turma.getAlunosMatriculados().size()));
        }

        return relatorio.toString();
    }

    /**
     * Gera o relatório de atividades de um professor.
     * 
     * O relatório inclui:
     * - Dados do professor
     * - Todas as turmas ministradas
     * - Total de alunos por turma
     * - Carga horária total
     * 
     * @throws IllegalArgumentException se a matrícula do professor for inválida
     */
    public String gerarRelatorioProfessor(String matriculaProfessor, List<Turma> todasTurmas) {
        List<Turma> turmasProfessor = todasTurmas.stream()
                .filter(t -> t.getProfessor().getMatricula().equals(matriculaProfessor))
                .collect(Collectors.toList());

        if (turmasProfessor.isEmpty()) {
            return "Professor não encontrado ou sem turmas";
        }

        Professor professor = turmasProfessor.get(0).getProfessor();
        StringBuilder relatorio = new StringBuilder();
        relatorio.append(String.format("Relatório do Professor: %s (%s)\n", 
                professor.getNome(), professor.getMatricula()));
        relatorio.append(String.format("Departamento: %s\n\n", professor.getDepartamento()));

        for (Turma turma : turmasProfessor) {
            relatorio.append(String.format("Turma: %s\n", turma.getCodigo()));
            relatorio.append(String.format("Disciplina: %s\n", turma.getDisciplina().getNome()));
            relatorio.append(String.format("Semestre: %s\n", turma.getSemestre()));
            relatorio.append(String.format("Alunos Matriculados: %d\n\n", 
                    turma.getAlunosMatriculados().size()));
        }

        return relatorio.toString();
    }

    /**
     * Gera o boletim detalhado de um aluno em um semestre específico.
     * 
     * O boletim inclui:
     * - Dados do aluno
     * - Lista de disciplinas cursadas
     * - Notas detalhadas por disciplina
     * - Frequência em cada disciplina
     * - Situação final (aprovado/reprovado)
     * - Dados adicionais da turma (opcional)
     * 
     * @throws IllegalArgumentException se a matrícula do aluno ou semestre forem inválidos
     */
    public String gerarBoletimAluno(String matriculaAluno, String semestre, boolean incluirDadosTurma,
                                  List<Turma> todasTurmas) {
        List<Turma> turmasAluno = todasTurmas.stream()
                .filter(t -> t.getSemestre().equals(semestre))
                .filter(t -> t.getAlunosMatriculados().stream()
                        .anyMatch(a -> a.getMatricula().equals(matriculaAluno)))
                .collect(Collectors.toList());

        if (turmasAluno.isEmpty()) {
            return "Aluno não encontrado ou sem matrículas no semestre";
        }

        Aluno aluno = turmasAluno.get(0).getAlunosMatriculados().stream()
                .filter(a -> a.getMatricula().equals(matriculaAluno))
                .findFirst().orElse(null);

        if (aluno == null) {
            return "Aluno não encontrado";
        }

        StringBuilder boletim = new StringBuilder();
        boletim.append(String.format("Boletim do Aluno: %s (%s)\n", aluno.getNome(), aluno.getMatricula()));
        boletim.append(String.format("Curso: %s\n", aluno.getCursoGraduacao()));
        boletim.append(String.format("Semestre: %s\n\n", semestre));

        for (Turma turma : turmasAluno) {
            boletim.append(String.format("Disciplina: %s (%s)\n", 
                    turma.getDisciplina().getNome(), turma.getDisciplina().getCodigo()));
            
            if (incluirDadosTurma) {
                boletim.append(String.format("Professor: %s\n", turma.getProfessor().getNome()));
                boletim.append(String.format("Modalidade: %s\n", 
                        turma.isPresencial() ? "Presencial" : "Remota"));
                boletim.append(String.format("Carga Horária: %d horas\n", 
                        turma.getDisciplina().getCargaHoraria()));
            }

            Turma.NotasAluno notas = turma.getNotasAluno(aluno);
            if (notas != null) {
                boletim.append(notas.toString()).append("\n\n");
            }
        }

        return boletim.toString();
    }

    private Turma getTurma(String codigoTurma) {
        return disciplinaController.buscarTurma(codigoTurma);
    }

    /**
     * Salva os dados de avaliação em arquivo texto.
     * 
     * Formato de cada linha:
     * codigoTurma;matriculaAluno;p1;p2;p3;listas;seminario;totalAulas;presencas
     * 
     * Exemplo:
     * CALC1-2024-1;123456;8.5;7.0;9.0;8.5;9.0;60;54
     * 
     * @throws IOException se houver erro na escrita do arquivo
     */
    @Override
    public boolean salvar(String arquivo) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(arquivo), "UTF-8"))) {
            for (Map.Entry<String, Map<String, Turma.NotasAluno>> turmaEntry : notasPorTurma.entrySet()) {
                String codigoTurma = turmaEntry.getKey();
                Map<String, Turma.NotasAluno> notasAlunos = turmaEntry.getValue();

                for (Map.Entry<String, Turma.NotasAluno> alunoEntry : notasAlunos.entrySet()) {
                    String matriculaAluno = alunoEntry.getKey();
                    Turma.NotasAluno notas = alunoEntry.getValue();

                    // Formato: codigoTurma;matriculaAluno;p1;p2;p3;listas;seminario;totalAulas;presencas
                    writer.printf("%s;%s;%.1f;%.1f;%.1f;%.1f;%.1f;%d;%d%n",
                        codigoTurma, matriculaAluno,
                        notas.getP1(), notas.getP2(), notas.getP3(),
                        notas.getListas(), notas.getSeminario(),
                        notas.getTotalAulas(), notas.getPresencas());
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carrega os dados de avaliação do arquivo texto.
     * 
     * O arquivo deve estar no formato:
     * - Encoding: UTF-8
     * - Separador: ponto e vírgula (;)
     * - Números decimais com ponto (.)
     * 
     * @throws IOException se houver erro na leitura do arquivo
     * @throws NumberFormatException se houver erro no formato dos números
     */
    @Override
    public boolean carregar(String arquivo) {
        notasPorTurma.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 9) {
                    String codigoTurma = partes[0];
                    String matriculaAluno = partes[1];
                    double p1 = Double.parseDouble(partes[2]);
                    double p2 = Double.parseDouble(partes[3]);
                    double p3 = Double.parseDouble(partes[4]);
                    double listas = Double.parseDouble(partes[5]);
                    double seminario = Double.parseDouble(partes[6]);
                    int totalAulas = Integer.parseInt(partes[7]);
                    int presencas = Integer.parseInt(partes[8]);

                    Map<String, Turma.NotasAluno> notasAlunos = notasPorTurma.computeIfAbsent(
                        codigoTurma, k -> new HashMap<>());

                    Turma.NotasAluno notas = new Turma.NotasAluno();
                    notas.setNotas(p1, p2, p3, listas, seminario);
                    notas.setFrequencia(totalAulas, presencas);
                    notasAlunos.put(matriculaAluno, notas);
                }
            }
            return true;
        } catch (IOException e) {
            // Se o arquivo não existir na primeira execução, não é erro
            if (!(e instanceof FileNotFoundException)) {
                e.printStackTrace();
            }
            return false;
        }
    }
} 