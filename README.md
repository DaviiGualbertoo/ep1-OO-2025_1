# Sistema Acadêmico - FCTE

## Descrição do Projeto

Desenvolvimento de um sistema acadêmico para gerenciar alunos, disciplinas, professores, turmas, avaliações e frequência, utilizando os conceitos de orientação a objetos (herança, polimorfismo e encapsulamento) e persistência de dados em arquivos.

O enunciado do trabalho pode ser encontrado aqui:
- [Trabalho 1 - Sistema Acadêmico](https://github.com/lboaventura25/OO-T06_2025.1_UnB_FCTE/blob/main/trabalhos/ep1/README.md)

## Dados do Aluno

- **Nome completo: Davi Gualberto Rocha
- **Matrícula: 241012196
- **Curso: Engenharias
- **Turma: 06

---

## Instruções para Compilação e Execução

1. **Compilação:**  
   javac Main.java

2. **Execução:**  
   Main.java

3. **Estrutura de Pastas:**  
  src/
├── main/
│   └── java/
│       └── br/
│           └── edu/
│               └── fcte/
│                   ├── main/
│                       ├── main.java
│                   ├── model/
│                   │   ├── Pessoa.java
│                   │   ├── Aluno.java
│                   │   ├── AlunoNormal.java
│                   │   ├── AlunoEspecial.java
│                   │   ├── Professor.java
│                   │   ├── Disciplina.java
│                   │   ├── Turma.java
│                   │   └── FormaAvaliacao.java
│                   ├── controller/
│                   │   ├── Persistivel.java
│                   │   ├── AlunoController.java
│                   │   ├── DisciplinaController.java
│                   │   └── AvaliacaoController.java
│                   └── SistemaAcademico.java
└── test/
    └── java/


3. **Versão do JAVA utilizada:**  
   Java 17      

---

## Vídeo de Demonstração

- [Inserir o link para o vídeo no YouTube/Drive aqui]

---

## Prints da Execução

1. Menu Principal:  
   ![Inserir Print 1](caminho/do/print1.png)

2. Cadastro de Aluno:  
   ![Inserir Print 2](caminho/do/print2.png)

3. Relatório de Frequência/Notas:  
   ![Inserir Print 3](caminho/do/print3.png)

---

## Principais Funcionalidades Implementadas

- [ ] Cadastro, listagem, matrícula e trancamento de alunos (Normais e Especiais)
- [ ] Cadastro de disciplinas e criação de turmas (presenciais e remotas)
- [ ] Matrícula de alunos em turmas, respeitando vagas e pré-requisitos
- [ ] Lançamento de notas e controle de presença
- [ ] Cálculo de média final e verificação de aprovação/reprovação
- [ ] Relatórios de desempenho acadêmico por aluno, turma e disciplina
- [ ] Persistência de dados em arquivos (.txt ou .csv)
- [ ] Tratamento de duplicidade de matrículas
- [ ] Uso de herança, polimorfismo e encapsulamento

---

## Observações (Extras ou Dificuldades)

Minha maior dificuldade nesse trabalho foi a parte de ligar os alunos e professores a uma turma, e depois gerar notas e frequências que ficassem gravadas em um arquivo.

---

## Contato

- davigualbertorocha1@gmail.com
