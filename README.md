# Sistema Acadêmico FCTE

Sistema de gerenciamento acadêmico desenvolvido para a FCTE, permitindo o controle de alunos, disciplinas, turmas, notas e frequências.

## Autor

- **Davi Gualberto Rocha**
  - Estudante de Engenharia de Software
  - UnB - Faculdade de Ciências e Tecnologias em Engenharia 

## Tecnologias Utilizadas

- **Linguagem Principal:** Java 17
- **IDE Recomendada:** VS Code, Eclipse ou IntelliJ IDEA
- **Controle de Versão:** Git
- **Persistência de Dados:** Arquivos texto (.txt)
- **Paradigma:** Orientação a Objetos

## Funcionalidades

### 1. Modo Aluno
- Cadastro e edição de alunos (normais e especiais)
- Matrícula em disciplinas
- Trancamento de disciplinas e semestre
- Restrições para alunos especiais:
  - Máximo de 2 disciplinas
  - Não recebem notas, apenas frequência
  - Não precisam cumprir pré-requisitos

### 2. Modo Disciplina/Turma
- Cadastro de disciplinas e definição de pré-requisitos
- Criação de turmas com:
  - Professor responsável
  - Modalidade (presencial/remota)
  - Horário
  - Forma de avaliação (média simples ou ponderada)
  - Capacidade máxima
- Definição de sala para turmas presenciais
- Cadastro e gerenciamento de professores

### 3. Modo Avaliação/Frequência
- Lançamento de notas (P1, P2, P3, listas e seminário)
- Lançamento de frequência
- Relatórios:
  - Por turma
  - Por disciplina
  - Por professor
- Boletim do aluno por semestre

## Como Executar

1. Certifique-se de ter o Java 17 ou superior instalado em sua máquina

2. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/SistemaAcademicoFCTE.git
cd SistemaAcademicoFCTE
```

3. Compile o projeto:
```bash
javac -d target/classes -encoding UTF-8 src/main/java/br/edu/fcte/*.java src/main/java/br/edu/fcte/*/*.java
```

4. Execute o sistema:
```bash
java -cp target/classes br.edu.fcte.SistemaAcademico
```

## Estrutura do Projeto

```
src/
├── main/
│   └── java/
│       └── br/
│           └── edu/
│               └── fcte/
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
```

## Persistência de Dados

Os dados são salvos em arquivos de texto (.txt):
- `alunos.txt`: dados dos alunos
- `disciplinas.txt`: dados das disciplinas, turmas e professores
- `avaliacoes.txt`: notas e frequências

## Critérios de Aprovação

- Média final >= 5.0
- Frequência >= 75%

## Detalhes Técnicos

### Herança
- `Pessoa` (abstrata)
  - `Aluno` (abstrata)
    - `AlunoNormal`
    - `AlunoEspecial`
  - `Professor`

### Polimorfismo
1. Método `podeMatricular()` nas classes `AlunoNormal` e `AlunoEspecial`
2. Método `calcularMedia()` na enumeração `FormaAvaliacao`
3. Tratamento diferenciado de notas para `AlunoEspecial`

### Encapsulamento
- Atributos privados com getters/setters
- Listas retornadas como cópias defensivas
- Validações de regras de negócio nos controladores

## Observações

- Uma disciplina pode ter múltiplas turmas, desde que em horários diferentes
- Alunos especiais têm restrições específicas
- Todas as operações são salvas automaticamente

## Prints do Sistema

[Prints serão adicionados durante o desenvolvimento]

## Vídeo Demonstrativo

[Link do vídeo será adicionado após a conclusão do desenvolvimento]
