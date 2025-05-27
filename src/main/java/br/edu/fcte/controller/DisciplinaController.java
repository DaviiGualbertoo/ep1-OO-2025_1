package br.edu.fcte.controller;

import br.edu.fcte.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador responsável por gerenciar as disciplinas e turmas do sistema
 */
public class DisciplinaController implements Persistivel {
    private Map<String, Disciplina> disciplinas;
    private Map<String, Turma> turmas;
    private Map<String, Professor> professores;
    private String ultimoErro;

    public DisciplinaController() {
        this.disciplinas = new HashMap<>();
        this.turmas = new HashMap<>();
        this.professores = new HashMap<>();
    }

    public String getUltimoErro() {
        return ultimoErro;
    }

    /**
     * Cadastra uma nova disciplina no sistema
     * @param nome Nome da disciplina
     * @param codigo Código da disciplina
     * @param cargaHoraria Carga horária da disciplina
     * @return true se a disciplina foi cadastrada com sucesso, false se já existe uma disciplina com o mesmo código
     */
    public boolean cadastrarDisciplina(String nome, String codigo, int cargaHoraria) {
        if (disciplinas.containsKey(codigo)) {
            return false;
        }

        Disciplina disciplina = new Disciplina(nome, codigo, cargaHoraria);
        disciplinas.put(codigo, disciplina);
        return true;
    }

    /**
     * Adiciona um pré-requisito a uma disciplina
     * @param codigoDisciplina Código da disciplina
     * @param codigoPreRequisito Código do pré-requisito
     * @return true se o pré-requisito foi adicionado com sucesso, false caso contrário
     */
    public boolean adicionarPreRequisito(String codigoDisciplina, String codigoPreRequisito) {
        Disciplina disciplina = disciplinas.get(codigoDisciplina);
        Disciplina preRequisito = disciplinas.get(codigoPreRequisito);

        if (disciplina == null || preRequisito == null) {
            return false;
        }

        disciplina.adicionarPreRequisito(preRequisito);
        return true;
    }

    /**
     * Cria uma nova turma para uma disciplina
     * @param codigoTurma Código da turma
     * @param codigoDisciplina Código da disciplina
     * @param professor Professor responsável
     * @param semestre Semestre da turma
     * @param formaAvaliacao Forma de avaliação da turma
     * @param presencial Se a turma é presencial
     * @param horario Horário da turma
     * @param capacidadeMaxima Capacidade máxima de alunos
     * @return true se a turma foi criada com sucesso, false caso contrário
     */
    public boolean criarTurma(String codigoTurma, String codigoDisciplina, Professor professor,
                            String semestre, FormaAvaliacao formaAvaliacao, boolean presencial,
                            String horario, int capacidadeMaxima) {
        System.out.println("\nTentando criar turma:");
        System.out.println("- Código da turma: " + codigoTurma);
        System.out.println("- Código da disciplina: " + codigoDisciplina);
        System.out.println("- Professor: " + professor.getNome() + " (" + professor.getMatricula() + ")");
        System.out.println("- Horário: " + horario);

        if (turmas.containsKey(codigoTurma)) {
            ultimoErro = "Já existe uma turma com o código " + codigoTurma;
            System.out.println("ERRO: " + ultimoErro);
            return false;
        }

        Disciplina disciplina = disciplinas.get(codigoDisciplina);
        if (disciplina == null) {
            ultimoErro = "Disciplina não encontrada: " + codigoDisciplina;
            System.out.println("ERRO: " + ultimoErro);
            return false;
        }

        // Verifica se o professor já tem uma turma no mesmo horário
        for (Turma turmaExistente : professor.getTurmasMinistradas()) {
            if (turmaExistente.getHorario().equals(horario)) {
                ultimoErro = "Professor já tem uma turma no horário " + horario;
                System.out.println("ERRO: " + ultimoErro);
                return false;
            }
        }

        Turma turma = new Turma(codigoTurma, disciplina, professor, semestre,
                formaAvaliacao, presencial, horario, capacidadeMaxima);
        
        turmas.put(codigoTurma, turma);
        disciplina.adicionarTurma(turma);
        professor.adicionarTurma(turma);
        
        System.out.println("Turma criada com sucesso!");
        return true;
    }

    /**
     * Define a sala de uma turma presencial
     * @param codigoTurma Código da turma
     * @param sala Sala da turma
     * @return true se a sala foi definida com sucesso, false caso contrário
     */
    public boolean definirSala(String codigoTurma, String sala) {
        Turma turma = turmas.get(codigoTurma);
        if (turma == null || !turma.isPresencial()) {
            return false;
        }

        turma.setSala(sala);
        return true;
    }

    /**
     * Lista todas as turmas disponíveis
     * @return Lista com todas as turmas
     */
    public List<Turma> listarTurmas() {
        return new ArrayList<>(turmas.values());
    }

    /**
     * Lista todas as disciplinas cadastradas
     * @return Lista com todas as disciplinas
     */
    public List<Disciplina> listarDisciplinas() {
        return new ArrayList<>(disciplinas.values());
    }

    /**
     * Busca uma disciplina pelo código
     * @param codigo Código da disciplina
     * @return A disciplina encontrada ou null se não existir
     */
    public Disciplina buscarDisciplina(String codigo) {
        return disciplinas.get(codigo);
    }

    /**
     * Busca uma turma pelo código
     * @param codigo Código da turma
     * @return A turma encontrada ou null se não existir
     */
    public Turma buscarTurma(String codigo) {
        return turmas.get(codigo);
    }

    /**
     * Cadastra um novo professor no sistema
     * @param nome Nome do professor
     * @param matricula Matrícula do professor
     * @param departamento Departamento do professor
     * @return true se o professor foi cadastrado com sucesso, false se já existe um professor com a mesma matrícula
     */
    public boolean cadastrarProfessor(String nome, String matricula, String departamento) {
        if (professores.containsKey(matricula)) {
            return false;
        }

        Professor professor = new Professor(nome, matricula, departamento);
        professores.put(matricula, professor);
        return true;
    }

    /**
     * Busca um professor pela matrícula
     * @param matricula Matrícula do professor
     * @return O professor encontrado ou null se não existir
     */
    public Professor buscarProfessor(String matricula) {
        return professores.get(matricula);
    }

    /**
     * Lista todos os professores cadastrados
     * @return Lista com todos os professores
     */
    public List<Professor> listarProfessores() {
        return new ArrayList<>(professores.values());
    }

    @Override
    public boolean salvar(String arquivo) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(arquivo), "UTF-8"))) {
            // Salva os professores
            writer.println("### PROFESSORES ###");
            for (Professor professor : professores.values()) {
                // Formato: nome;matricula;departamento
                String nome = professor.getNome();
                String matricula = professor.getMatricula();
                String departamento = professor.getDepartamento();
                
                // Garante que nenhum campo esteja vazio
                if (departamento == null || departamento.trim().isEmpty()) {
                    departamento = "Não informado";
                }
                
                writer.printf("PROF;%s;%s;%s%n",
                    nome, matricula, departamento);
            }

            // Salva as disciplinas
            writer.println("### DISCIPLINAS ###");
            for (Disciplina disciplina : disciplinas.values()) {
                // Formato: nome;codigo;cargaHoraria;preRequisitos
                String nome = disciplina.getNome();
                String codigo = disciplina.getCodigo();
                int cargaHoraria = disciplina.getCargaHoraria();
                
                StringBuilder preReqs = new StringBuilder();
                for (Disciplina preReq : disciplina.getPreRequisitos()) {
                    if (preReqs.length() > 0) preReqs.append(",");
                    preReqs.append(preReq.getCodigo());
                }
                
                writer.printf("DISC;%s;%s;%d;%s%n",
                    nome, codigo, cargaHoraria, preReqs.toString());
            }

            // Salva as turmas
            writer.println("### TURMAS ###");
            for (Turma turma : turmas.values()) {
                // Formato: codigo;codigoDisciplina;matriculaProfessor;semestre;formaAvaliacao;presencial;horario;capacidadeMaxima;sala
                String codigo = turma.getCodigo();
                String codigoDisciplina = turma.getDisciplina().getCodigo();
                String matriculaProfessor = turma.getProfessor().getMatricula();
                String semestre = turma.getSemestre();
                String formaAvaliacao = turma.getFormaAvaliacao().toString();
                boolean presencial = turma.isPresencial();
                String horario = turma.getHorario();
                int capacidadeMaxima = turma.getCapacidadeMaxima();
                String sala = turma.getSala() != null ? turma.getSala() : "";
                
                writer.printf("TURM;%s;%s;%s;%s;%s;%b;%s;%d;%s%n",
                    codigo, codigoDisciplina, matriculaProfessor,
                    semestre, formaAvaliacao, presencial,
                    horario, capacidadeMaxima, sala);
            }

            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ultimoErro = "Erro ao salvar: " + e.getMessage();
            return false;
        }
    }

    @Override
    public boolean carregar(String arquivo) {
        disciplinas.clear();
        turmas.clear();
        professores.clear();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"))) {
            String linha;
            
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("###")) {
                    continue;
                }

                String[] partes = linha.split(";");
                if (partes.length < 2) continue;

                switch (partes[0]) {
                    case "PROF" -> {
                        if (partes.length == 4) {
                            String nome = partes[1];
                            String matricula = partes[2];
                            String departamento = partes[3];
                            cadastrarProfessor(nome, matricula, departamento);
                        }
                    }
                    case "DISC" -> {
                        if (partes.length == 5) {
                            String nome = partes[1];
                            String codigo = partes[2];
                            int cargaHoraria = Integer.parseInt(partes[3]);
                            cadastrarDisciplina(nome, codigo, cargaHoraria);

                            // Adiciona pré-requisitos
                            if (!partes[4].isEmpty()) {
                                String[] preReqs = partes[4].split(",");
                                for (String preReq : preReqs) {
                                    adicionarPreRequisito(codigo, preReq);
                                }
                            }
                        }
                    }
                    case "TURM" -> {
                        if (partes.length == 10) {
                            String codigo = partes[1];
                            String codigoDisciplina = partes[2];
                            String matriculaProfessor = partes[3];
                            String semestre = partes[4];
                            FormaAvaliacao formaAvaliacao = FormaAvaliacao.valueOf(partes[5]);
                            boolean presencial = Boolean.parseBoolean(partes[6]);
                            String horario = partes[7];
                            int capacidadeMaxima = Integer.parseInt(partes[8]);
                            String sala = partes[9];

                            Professor professor = buscarProfessor(matriculaProfessor);
                            if (professor != null) {
                                if (criarTurma(codigo, codigoDisciplina, professor, semestre,
                                    formaAvaliacao, presencial, horario, capacidadeMaxima)) {
                                    
                                    if (presencial && !sala.isEmpty()) {
                                        definirSala(codigo, sala);
                                    }
                                } else {
                                    System.err.println("Erro ao criar turma: " + ultimoErro);
                                }
                            } else {
                                System.err.println("Professor não encontrado: " + matriculaProfessor);
                            }
                        }
                    }
                }
            }
            return true;
        } catch (IOException e) {
            // Se o arquivo não existir na primeira execução, não é erro
            if (!(e instanceof FileNotFoundException)) {
                e.printStackTrace();
                ultimoErro = "Erro ao carregar: " + e.getMessage();
            }
            return false;
        }
    }
} 