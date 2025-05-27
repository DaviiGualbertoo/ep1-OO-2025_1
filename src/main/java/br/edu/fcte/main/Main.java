package br.edu.fcte.main;

import br.edu.fcte.controller.*;
import br.edu.fcte.model.*;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

/**
 * Classe principal do Sistema Acadêmico FCTE
 * 
 * Esta classe implementa a interface do usuário via terminal e coordena todas as operações
 * do sistema acadêmico, incluindo:
 * - Gerenciamento de alunos (normais e especiais)
 * - Gerenciamento de disciplinas e turmas
 * - Controle de notas e frequências
 * - Geração de relatórios
 *
 * O sistema utiliza um padrão MVC (Model-View-Controller) simplificado, onde:
 * - Model: Classes no pacote br.edu.fcte.model
 * - View: Interface via terminal nesta classe
 * - Controller: Classes no pacote br.edu.fcte.controller
 * 
 * @author [Davi Gualberto Rocha - 241012196]
 * @version 1.0
 */
public class Main {
    // Constantes para nomes dos arquivos de persistência
    private static final String ARQUIVO_ALUNOS = "alunos.txt";
    private static final String ARQUIVO_DISCIPLINAS = "disciplinas.txt";
    private static final String ARQUIVO_AVALIACOES = "avaliacoes.txt";

    // Controllers para gerenciamento das entidades
    private final AlunoController alunoController;
    private final DisciplinaController disciplinaController;
    private final AvaliacaoController avaliacaoController;
    private final Scanner scanner;

    /**
     * Construtor da classe Main
     * Inicializa os controllers e cria os arquivos de persistência se não existirem
     * Também carrega os dados salvos anteriormente
     */
    public Main() {
        // Configuração do encoding para UTF-8
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("console.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        this.alunoController = new AlunoController();
        this.disciplinaController = new DisciplinaController();
        this.avaliacaoController = new AvaliacaoController();
        this.scanner = new Scanner(System.in, "UTF-8");

        // Configura a referência do DisciplinaController no AvaliacaoCo0ntroller
        this.avaliacaoController.setDisciplinaController(disciplinaController);

        // Inicialização do sistema de arquivos
        criarArquivoSeNaoExiste(ARQUIVO_ALUNOS);
        criarArquivoSeNaoExiste(ARQUIVO_DISCIPLINAS);
        criarArquivoSeNaoExiste(ARQUIVO_AVALIACOES);

        // Carregamento dos dados
        alunoController.carregar(ARQUIVO_ALUNOS);
        disciplinaController.carregar(ARQUIVO_DISCIPLINAS);
        avaliacaoController.carregar(ARQUIVO_AVALIACOES);
    }

    /**
     * Cria um arquivo se ele não existir
     * @param nomeArquivo Nome do arquivo a ser criado
     */
    private void criarArquivoSeNaoExiste(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
                // Garante que o arquivo seja criado com encoding UTF-8
                try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(arquivo), "UTF-8"))) {
                    writer.println(""); // Escreve uma linha vazia para garantir o encoding
                }
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo " + nomeArquivo + ": " + e.getMessage());
            }
        }
    }

    /**
     * Método principal que inicia o sistema
     * Exibe o menu principal e gerencia o fluxo de execução
     */
    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            exibirMenuPrincipal();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> modoAluno();        // Gerenciamento de alunos
                case 2 -> modoDisciplina();    // Gerenciamento de disciplinas
                case 3 -> modoAvaliacao();     // Gerenciamento de avaliações
                case 0 -> continuar = false;   // Sair do sistema
                default -> System.out.println("Opção inválida!");
            }
        }

        // Salva todos os dados antes de encerrar
        alunoController.salvar(ARQUIVO_ALUNOS);
        disciplinaController.salvar(ARQUIVO_DISCIPLINAS);
        avaliacaoController.salvar(ARQUIVO_AVALIACOES);

        scanner.close();
    }

    /**
     * Exibe o menu principal do sistema
     */
    private void exibirMenuPrincipal() {
        System.out.println("\n=== Sistema Acadêmico FCTE ===");
        System.out.println("1. Modo Aluno");
        System.out.println("2. Modo Disciplina/Turma");
        System.out.println("3. Modo Avaliação/Frequência");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Modo Aluno: Gerencia todas as operações relacionadas a alunos
     * - Cadastro e edição de alunos
     * - Matrícula em disciplinas
     * - Trancamento de disciplinas e semestre
     */
    private void modoAluno() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== Modo Aluno ===");
            System.out.println("1. Cadastrar Aluno");
            System.out.println("2. Editar Aluno");
            System.out.println("3. Listar Alunos");
            System.out.println("4. Matricular em Disciplina");
            System.out.println("5. Trancar Disciplina");
            System.out.println("6. Trancar Semestre");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao();
            switch (opcao) {
                case 1 -> cadastrarAluno();
                case 2 -> editarAluno();
                case 3 -> listarAlunos();
                case 4 -> matricularEmDisciplina();
                case 5 -> trancarDisciplina();
                case 6 -> trancarSemestre();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    /**
     * Modo Disciplina: Gerencia todas as operações relacionadas a disciplinas e turmas
     * - Cadastro de disciplinas
     * - Definição de pré-requisitos
     * - Criação e gerenciamento de turmas
     * - Cadastro de professores
     */
    private void modoDisciplina() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== Modo Disciplina/Turma ===");
            System.out.println("1. Cadastrar Disciplina");
            System.out.println("2. Adicionar Pré-requisito");
            System.out.println("3. Criar Turma");
            System.out.println("4. Definir Sala");
            System.out.println("5. Listar Disciplinas");
            System.out.println("6. Listar Turmas");
            System.out.println("7. Cadastrar Professor");
            System.out.println("8. Listar Professores");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao();
            switch (opcao) {
                case 1 -> cadastrarDisciplina();
                case 2 -> adicionarPreRequisito();
                case 3 -> criarTurma();
                case 4 -> definirSala();
                case 5 -> listarDisciplinas();
                case 6 -> listarTurmas();
                case 7 -> cadastrarProfessor();
                case 8 -> listarProfessores();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    /**
     * Modo Avaliação: Gerencia todas as operações relacionadas a notas e frequências
     * - Lançamento de notas
     * - Lançamento de frequência
     * - Geração de relatórios
     * - Emissão de boletins
     */
    private void modoAvaliacao() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== Modo Avaliação/Frequência ===");
            System.out.println("1. Lançar Notas");
            System.out.println("2. Lançar Frequência");
            System.out.println("3. Relatório por Turma");
            System.out.println("4. Relatório por Disciplina");
            System.out.println("5. Relatório por Professor");
            System.out.println("6. Boletim do Aluno");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao();
            switch (opcao) {
                case 1 -> lancarNotas();
                case 2 -> lancarFrequencia();
                case 3 -> relatorioTurma();
                case 4 -> relatorioDisciplina();
                case 5 -> relatorioProfessor();
                case 6 -> boletimAluno();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    /**
     * Lê uma opção numérica do usuário
     * @return número inteiro representando a opção escolhida, ou -1 em caso de erro
     */
    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Cadastra um novo aluno no sistema
     * Permite cadastrar alunos normais ou especiais
     */
    private void cadastrarAluno() {
        System.out.println("\n=== Cadastrar Aluno ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Curso: ");
        String curso = scanner.nextLine();
        System.out.print("Aluno Especial (S/N)? ");
        boolean especial = scanner.nextLine().equalsIgnoreCase("S");

        if (alunoController.cadastrarAluno(nome, matricula, curso, especial)) {
            System.out.println("Aluno cadastrado com sucesso!");
            // Salva os dados após o cadastro
            alunoController.salvar(ARQUIVO_ALUNOS);
        } else {
            System.out.println("Erro: Matrícula já existe!");
        }
    }

    /**
     * Edita os dados de um aluno existente
     */
    private void editarAluno() {
        System.out.println("\n=== Editar Aluno ===");
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine();
        System.out.print("Novo curso: ");
        String novoCurso = scanner.nextLine();

        if (alunoController.editarAluno(matricula, novoNome, novoCurso)) {
            System.out.println("Aluno editado com sucesso!");
            // Salva os dados após a edição
            alunoController.salvar(ARQUIVO_ALUNOS);
        } else {
            System.out.println("Erro: Aluno não encontrado!");
        }
    }

    /**
     * Lista todos os alunos cadastrados no sistema
     */
    private void listarAlunos() {
        System.out.println("\n=== Lista de Alunos ===");
        System.out.println("Nome                  Matrícula     Curso                  Tipo");
        System.out.println("------------------------------------------------------------------");
        for (Aluno aluno : alunoController.listarAlunos()) {
            String tipoAluno = (aluno instanceof AlunoEspecial) ? "Especial" : "Normal";
            System.out.printf("%-20s %-12s %-20s %s%n", 
                    aluno.getNome(), 
                    aluno.getMatricula(), 
                    aluno.getCursoGraduacao(),
                    tipoAluno);
        }
    }

    /**
     * Realiza a matrícula de um aluno em uma disciplina
     * Verifica pré-requisitos e restrições específicas para alunos especiais
     */
    private void matricularEmDisciplina() {
        System.out.println("\n=== Matricular em Disciplina ===");
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();

        Aluno aluno = alunoController.buscarAluno(matricula);
        if (aluno == null) {
            System.out.println("Erro: Aluno não encontrado!");
            return;
        }

        Disciplina disciplina = disciplinaController.buscarDisciplina(codigoDisciplina);
        if (disciplina == null) {
            System.out.println("Erro: Disciplina não encontrada!");
            return;
        }

        Turma turma = disciplinaController.buscarTurma(codigoTurma);
        if (turma == null) {
            System.out.println("Erro: Turma não encontrada!");
            return;
        }

        if (turma.getDisciplina().getCodigo().equals(codigoDisciplina)) {
            if (alunoController.matricularEmDisciplina(matricula, disciplina) && turma.matricularAluno(aluno)) {
                System.out.println("Matrícula realizada com sucesso!");
            } else {
                System.out.println("Erro: Não foi possível realizar a matrícula!");
            }
        } else {
            System.out.println("Erro: A turma não pertence à disciplina selecionada!");
        }
    }

    /**
     * Realiza o trancamento de uma disciplina para um aluno
     */
    private void trancarDisciplina() {
        System.out.println("\n=== Trancar Disciplina ===");
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();

        Aluno aluno = alunoController.buscarAluno(matricula);
        if (aluno == null) {
            System.out.println("Erro: Aluno não encontrado!");
            return;
        }

        Disciplina disciplina = disciplinaController.buscarDisciplina(codigoDisciplina);
        if (disciplina == null) {
            System.out.println("Erro: Disciplina não encontrada!");
            return;
        }

        // Encontra a turma em que o aluno está matriculado
        Turma turmaAluno = null;
        for (Turma turma : disciplinaController.listarTurmas()) {
            if (turma.getDisciplina().equals(disciplina) && 
                turma.getAlunosMatriculados().contains(aluno)) {
                turmaAluno = turma;
                break;
            }
        }

        if (turmaAluno != null) {
            // Remove o aluno da turma
            turmaAluno.desmatricularAluno(aluno);
        }

        // Remove a disciplina do aluno
        if (alunoController.trancarDisciplina(matricula, disciplina)) {
            System.out.println("Disciplina trancada com sucesso!");
            // Salva os dados após o trancamento
            alunoController.salvar(ARQUIVO_ALUNOS);
            disciplinaController.salvar(ARQUIVO_DISCIPLINAS);
        } else {
            System.out.println("Erro: Não foi possível trancar a disciplina!");
        }
    }

    /**
     * Realiza o trancamento do semestre para um aluno
     */
    private void trancarSemestre() {
        System.out.println("\n=== Trancar Semestre ===");
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();

        Aluno aluno = alunoController.buscarAluno(matricula);
        if (aluno == null) {
            System.out.println("Erro: Aluno não encontrado!");
            return;
        }

        // Remove o aluno de todas as turmas
        for (Turma turma : disciplinaController.listarTurmas()) {
            if (turma.getAlunosMatriculados().contains(aluno)) {
                turma.desmatricularAluno(aluno);
            }
        }

        if (alunoController.trancarSemestre(matricula)) {
            System.out.println("Semestre trancado com sucesso!");
            // Salva os dados após o trancamento
            alunoController.salvar(ARQUIVO_ALUNOS);
            disciplinaController.salvar(ARQUIVO_DISCIPLINAS);
        } else {
            System.out.println("Erro: Não foi possível trancar o semestre!");
        }
    }

    /**
     * Cadastra uma nova disciplina no sistema
     */
    private void cadastrarDisciplina() {
        System.out.println("\n=== Cadastrar Disciplina ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Carga horária: ");
        int cargaHoraria = Integer.parseInt(scanner.nextLine());

        if (disciplinaController.cadastrarDisciplina(nome, codigo, cargaHoraria)) {
            System.out.println("Disciplina cadastrada com sucesso!");
            // Salva os dados após o cadastro
            disciplinaController.salvar(ARQUIVO_DISCIPLINAS);
        } else {
            System.out.println("Erro: Código já existe!");
        }
    }

    /**
     * Adiciona um pré-requisito a uma disciplina
     */
    private void adicionarPreRequisito() {
        System.out.println("\n=== Adicionar Pré-requisito ===");
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();
        System.out.print("Código do pré-requisito: ");
        String codigoPreRequisito = scanner.nextLine();

        if (disciplinaController.adicionarPreRequisito(codigoDisciplina, codigoPreRequisito)) {
            System.out.println("Pré-requisito adicionado com sucesso!");
            // Salva os dados após adicionar o pré-requisito
            disciplinaController.salvar(ARQUIVO_DISCIPLINAS);
        } else {
            System.out.println("Erro: Disciplina ou pré-requisito não encontrado!");
        }
    }

    /**
     * Cria uma nova turma para uma disciplina
     */
    private void criarTurma() {
        System.out.println("\n=== Criar Turma ===");
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();
        System.out.print("Matrícula do professor: ");
        String matriculaProfessor = scanner.nextLine();
        System.out.print("Semestre (ex: 2024.1): ");
        String semestre = scanner.nextLine();
        System.out.print("Forma de avaliação (1-Média Simples, 2-Média Ponderada): ");
        FormaAvaliacao formaAvaliacao = lerOpcao() == 1 ? FormaAvaliacao.MEDIA_SIMPLES : FormaAvaliacao.MEDIA_PONDERADA;
        System.out.print("Presencial (S/N)? ");
        boolean presencial = scanner.nextLine().equalsIgnoreCase("S");
        System.out.print("Horário (ex: SEG 14:00-15:40): ");
        String horario = scanner.nextLine();
        System.out.print("Capacidade máxima: ");
        int capacidadeMaxima = Integer.parseInt(scanner.nextLine());

        Professor professor = disciplinaController.buscarProfessor(matriculaProfessor);
        if (professor == null) {
            System.out.println("Erro: Professor não encontrado!");
            return;
        }

        if (disciplinaController.criarTurma(codigoTurma, codigoDisciplina, professor, 
                semestre, formaAvaliacao, presencial, horario, capacidadeMaxima)) {
            System.out.println("Turma criada com sucesso!");
            // Salva os dados após criar a turma
            disciplinaController.salvar(ARQUIVO_DISCIPLINAS);
        } else {
            System.out.println("Erro: Não foi possível criar a turma!");
        }
    }

    /**
     * Define a sala para uma turma presencial
     */
    private void definirSala() {
        System.out.println("\n=== Definir Sala ===");
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        System.out.print("Sala: ");
        String sala = scanner.nextLine();

        if (disciplinaController.definirSala(codigoTurma, sala)) {
            System.out.println("Sala definida com sucesso!");
            // Salva os dados após definir a sala
            disciplinaController.salvar(ARQUIVO_DISCIPLINAS);
        } else {
            System.out.println("Erro: Turma não encontrada ou não é presencial!");
        }
    }

    /**
     * Lista todas as disciplinas cadastradas
     */
    private void listarDisciplinas() {
        System.out.println("\n=== Lista de Disciplinas ===");
        System.out.println("Código    Nome                           Carga Horária");
        System.out.println("-----------------------------------------------------");
        for (Disciplina disciplina : disciplinaController.listarDisciplinas()) {
            System.out.printf("%-9s %-30s %3d horas%n",
                    disciplina.getCodigo(),
                    disciplina.getNome(),
                    disciplina.getCargaHoraria());
        }
    }

    /**
     * Lista todas as turmas cadastradas
     */
    private void listarTurmas() {
        System.out.println("\n=== Lista de Turmas ===");
        System.out.println("Código    Disciplina                    Professor           Alunos");
        System.out.println("------------------------------------------------------------------");
        for (Turma turma : disciplinaController.listarTurmas()) {
            System.out.printf("%-9s %-28s %-18s %d%n",
                    turma.getCodigo(),
                    turma.getDisciplina().getNome(),
                    turma.getProfessor().getNome(),
                    turma.getAlunosMatriculados().size());
        }
    }

    /**
     * Cadastra um novo professor no sistema
     */
    private void cadastrarProfessor() {
        System.out.println("\n=== Cadastrar Professor ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Departamento: ");
        String departamento = scanner.nextLine();

        if (disciplinaController.cadastrarProfessor(nome, matricula, departamento)) {
            System.out.println("Professor cadastrado com sucesso!");
            // Salva os dados após o cadastro
            disciplinaController.salvar(ARQUIVO_DISCIPLINAS);
        } else {
            System.out.println("Erro: Matrícula já existe!");
        }
    }

    /**
     * Lista todos os professores cadastrados
     */
    private void listarProfessores() {
        System.out.println("\n=== Lista de Professores ===");
        System.out.println("Nome                  Matrícula     Departamento");
        System.out.println("---------------------------------------------------");
        for (Professor professor : disciplinaController.listarProfessores()) {
            System.out.printf("%-20s %-12s %-20s%n",
                    professor.getNome(),
                    professor.getMatricula(),
                    professor.getDepartamento());
        }
    }

    /**
     * Lança notas para um aluno em uma turma
     * Não permite lançamento de notas para alunos especiais
     */
    private void lancarNotas() {
        System.out.println("\n=== Lançar Notas ===");
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();

        Turma turma = disciplinaController.buscarTurma(codigoTurma);
        if (turma == null) {
            System.out.println("Erro: Turma não encontrada!");
            return;
        }

        System.out.println("\nDisciplina: " + turma.getDisciplina().getNome());
        System.out.println("Professor: " + turma.getProfessor().getNome());
        System.out.println("Forma de Avaliação: " + turma.getFormaAvaliacao());
        System.out.println("\nAlunos matriculados:");
        for (Aluno a : turma.getAlunosMatriculados()) {
            System.out.printf("- %s (%s)%n", a.getNome(), a.getMatricula());
        }

        System.out.print("\nMatrícula do aluno: ");
        String matriculaAluno = scanner.nextLine();

        Aluno aluno = alunoController.buscarAluno(matriculaAluno);
        if (aluno == null) {
            System.out.println("Erro: Aluno não encontrado!");
            return;
        }

        if (!turma.getAlunosMatriculados().contains(aluno)) {
            System.out.println("Erro: Aluno não está matriculado nesta turma!");
            return;
        }

        if (aluno instanceof AlunoEspecial) {
            System.out.println("Erro: Alunos especiais não recebem notas!");
            return;
        }

        try {
            System.out.print("Nota P1 (0-10): ");
            double p1 = Double.parseDouble(scanner.nextLine());
            if (p1 < 0 || p1 > 10) throw new IllegalArgumentException("Nota deve estar entre 0 e 10");

            System.out.print("Nota P2 (0-10): ");
            double p2 = Double.parseDouble(scanner.nextLine());
            if (p2 < 0 || p2 > 10) throw new IllegalArgumentException("Nota deve estar entre 0 e 10");

            System.out.print("Nota P3 (0-10): ");
            double p3 = Double.parseDouble(scanner.nextLine());
            if (p3 < 0 || p3 > 10) throw new IllegalArgumentException("Nota deve estar entre 0 e 10");

            System.out.print("Nota Listas (0-10): ");
            double listas = Double.parseDouble(scanner.nextLine());
            if (listas < 0 || listas > 10) throw new IllegalArgumentException("Nota deve estar entre 0 e 10");

            System.out.print("Nota Seminário (0-10): ");
            double seminario = Double.parseDouble(scanner.nextLine());
            if (seminario < 0 || seminario > 10) throw new IllegalArgumentException("Nota deve estar entre 0 e 10");

            if (avaliacaoController.lancarNotas(codigoTurma, aluno, p1, p2, p3, listas, seminario)) {
                System.out.println("\nNotas lançadas com sucesso!");
                System.out.println("Forma de Avaliação: " + turma.getFormaAvaliacao());
                System.out.printf("Notas: P1=%.1f, P2=%.1f, P3=%.1f, Listas=%.1f, Seminário=%.1f%n",
                    p1, p2, p3, listas, seminario);
            } else {
                System.out.println("Erro: Não foi possível lançar as notas!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite apenas números!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /**
     * Lança frequência para um aluno em uma turma
     */
    private void lancarFrequencia() {
        System.out.println("\n=== Lançar Frequência ===");
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();

        Turma turma = disciplinaController.buscarTurma(codigoTurma);
        if (turma == null) {
            System.out.println("Erro: Turma não encontrada!");
            return;
        }

        System.out.println("\nDisciplina: " + turma.getDisciplina().getNome());
        System.out.println("Professor: " + turma.getProfessor().getNome());
        System.out.println("\nAlunos matriculados:");
        for (Aluno a : turma.getAlunosMatriculados()) {
            System.out.printf("- %s (%s)%n", a.getNome(), a.getMatricula());
        }

        System.out.print("\nMatrícula do aluno: ");
        String matriculaAluno = scanner.nextLine();

        Aluno aluno = alunoController.buscarAluno(matriculaAluno);
        if (aluno == null) {
            System.out.println("Erro: Aluno não encontrado!");
            return;
        }

        if (!turma.getAlunosMatriculados().contains(aluno)) {
            System.out.println("Erro: Aluno não está matriculado nesta turma!");
            return;
        }

        try {
            System.out.print("Total de aulas ministradas: ");
            int totalAulas = Integer.parseInt(scanner.nextLine());
            if (totalAulas <= 0) throw new IllegalArgumentException("Total de aulas deve ser maior que zero");

            System.out.print("Número de presenças: ");
            int presencas = Integer.parseInt(scanner.nextLine());
            if (presencas < 0) throw new IllegalArgumentException("Número de presenças não pode ser negativo");
            if (presencas > totalAulas) throw new IllegalArgumentException("Número de presenças não pode ser maior que o total de aulas");

            if (avaliacaoController.lancarFrequencia(codigoTurma, aluno, totalAulas, presencas)) {
                System.out.println("\nFrequência lançada com sucesso!");
                double percentualFrequencia = (presencas * 100.0) / totalAulas;
                System.out.printf("Frequência: %d de %d aulas (%.1f%%)%n", 
                    presencas, totalAulas, percentualFrequencia);
                
                if (percentualFrequencia < 75) {
                    System.out.println("ATENÇÃO: Frequência abaixo de 75% - Risco de reprovação por falta!");
                }
            } else {
                System.out.println("Erro: Não foi possível lançar a frequência!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite apenas números inteiros!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /**
     * Gera relatório de desempenho por turma
     */
    private void relatorioTurma() {
        System.out.println("\n=== Relatório por Turma ===");
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();

        String relatorio = avaliacaoController.gerarRelatorioTurma(codigoTurma);
        System.out.println(relatorio);
    }

    /**
     * Gera relatório de desempenho por disciplina
     */
    private void relatorioDisciplina() {
        System.out.println("\n=== Relatório por Disciplina ===");
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();

        String relatorio = avaliacaoController.gerarRelatorioDisciplina(codigoDisciplina, disciplinaController);
        System.out.println(relatorio);
    }

    /**
     * Gera relatório de desempenho por professor
     */
    private void relatorioProfessor() {
        System.out.println("\n=== Relatório por Professor ===");
        System.out.print("Matrícula do professor: ");
        String matriculaProfessor = scanner.nextLine();

        String relatorio = avaliacaoController.gerarRelatorioProfessor(
                matriculaProfessor, disciplinaController.listarTurmas());
        System.out.println(relatorio);
    }

    /**
     * Gera boletim individual do aluno
     */
    private void boletimAluno() {
        System.out.println("\n=== Boletim do Aluno ===");
        System.out.print("Matrícula do aluno: ");
        String matriculaAluno = scanner.nextLine();
        System.out.print("Semestre (ex: 2024.1): ");
        String semestre = scanner.nextLine();
        System.out.print("Incluir dados da turma (S/N)? ");
        boolean incluirDadosTurma = scanner.nextLine().equalsIgnoreCase("S");

        String boletim = avaliacaoController.gerarBoletimAluno(
                matriculaAluno, semestre, incluirDadosTurma, disciplinaController.listarTurmas());
        System.out.println(boletim);
    }

    /**
     * Método main que inicia o sistema
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Configuração do encoding para UTF-8
        System.setProperty("console.encoding", "UTF-8");
        System.setProperty("file.encoding", "UTF-8");
        
        new Main().iniciar();
    }
} 