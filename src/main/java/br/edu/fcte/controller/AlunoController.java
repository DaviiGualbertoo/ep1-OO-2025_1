package br.edu.fcte.controller;

import br.edu.fcte.model.Aluno;
import br.edu.fcte.model.AlunoEspecial;
import br.edu.fcte.model.AlunoNormal;
import br.edu.fcte.model.Disciplina;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador responsável por gerenciar os alunos do sistema
 */
public class AlunoController implements Persistivel {
    private Map<String, Aluno> alunos;

    public AlunoController() {
        this.alunos = new HashMap<>();
    }

    /**
     * Cadastra um novo aluno no sistema
     * @param nome Nome do aluno
     * @param matricula Matrícula do aluno
     * @param cursoGraduacao Curso de graduação do aluno
     * @param especial Se true, cria um aluno especial, se false, cria um aluno normal
     * @return true se o aluno foi cadastrado com sucesso, false se já existe um aluno com a mesma matrícula
     */
    public boolean cadastrarAluno(String nome, String matricula, String cursoGraduacao, boolean especial) {
        if (alunos.containsKey(matricula)) {
            return false;
        }

        Aluno aluno = especial ? 
            new AlunoEspecial(nome, matricula, cursoGraduacao) :
            new AlunoNormal(nome, matricula, cursoGraduacao);
        
        alunos.put(matricula, aluno);
        return true;
    }

    /**
     * Edita os dados de um aluno existente
     * @param matricula Matrícula do aluno
     * @param novoNome Novo nome do aluno
     * @param novoCurso Novo curso de graduação do aluno
     * @return true se o aluno foi editado com sucesso, false se o aluno não existe
     */
    public boolean editarAluno(String matricula, String novoNome, String novoCurso) {
        Aluno aluno = alunos.get(matricula);
        if (aluno == null) {
            return false;
        }

        aluno.setNome(novoNome);
        aluno.setCursoGraduacao(novoCurso);
        return true;
    }

    /**
     * Busca um aluno pela matrícula
     * @param matricula Matrícula do aluno
     * @return O aluno encontrado ou null se não existir
     */
    public Aluno buscarAluno(String matricula) {
        return alunos.get(matricula);
    }

    /**
     * Lista todos os alunos cadastrados
     * @return Lista com todos os alunos
     */
    public List<Aluno> listarAlunos() {
        return new ArrayList<>(alunos.values());
    }

    /**
     * Matricula um aluno em uma disciplina
     * @param matricula Matrícula do aluno
     * @param disciplina Disciplina em que o aluno será matriculado
     * @return true se a matrícula foi realizada com sucesso, false caso contrário
     */
    public boolean matricularEmDisciplina(String matricula, Disciplina disciplina) {
        Aluno aluno = alunos.get(matricula);
        if (aluno == null) {
            return false;
        }

        return aluno.matricular(disciplina);
    }

    /**
     * Tranca a matrícula do aluno em uma disciplina
     * @param matricula Matrícula do aluno
     * @param disciplina Disciplina que será trancada
     * @return true se o trancamento foi realizado com sucesso, false caso contrário
     */
    public boolean trancarDisciplina(String matricula, Disciplina disciplina) {
        Aluno aluno = alunos.get(matricula);
        if (aluno == null) {
            return false;
        }

        return aluno.trancarDisciplina(disciplina);
    }

    /**
     * Tranca o semestre do aluno
     * @param matricula Matrícula do aluno
     * @return true se o trancamento foi realizado com sucesso, false caso contrário
     */
    public boolean trancarSemestre(String matricula) {
        Aluno aluno = alunos.get(matricula);
        if (aluno == null) {
            return false;
        }

        aluno.trancarSemestre();
        return true;
    }

    @Override
    public boolean salvar(String arquivo) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(arquivo), "UTF-8"))) {
            for (Aluno aluno : alunos.values()) {
                // Formato: tipo;nome;matricula;curso
                String tipo = aluno instanceof AlunoEspecial ? "ESPECIAL" : "NORMAL";
                String curso = aluno.getCursoGraduacao();
                // Se o curso estiver vazio, usa um valor padrão para evitar problemas
                if (curso == null || curso.trim().isEmpty()) {
                    curso = "Não informado";
                }
                writer.printf("%s;%s;%s;%s%n", 
                    tipo, aluno.getNome(), aluno.getMatricula(), curso);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean carregar(String arquivo) {
        alunos.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 4) {
                    String tipo = partes[0];
                    String nome = partes[1];
                    String matricula = partes[2];
                    String curso = partes[3];
                    
                    boolean especial = tipo.equals("ESPECIAL");
                    cadastrarAluno(nome, matricula, curso, especial);
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