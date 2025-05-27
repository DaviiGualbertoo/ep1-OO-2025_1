package br.edu.fcte.controller;

/**
 * Interface que define os métodos de persistência de dados
 */
public interface Persistivel {
    /**
     * Salva os dados em arquivo
     * @param arquivo Nome do arquivo onde os dados serão salvos
     * @return true se os dados foram salvos com sucesso, false caso contrário
     */
    boolean salvar(String arquivo);

    /**
     * Carrega os dados de um arquivo
     * @param arquivo Nome do arquivo de onde os dados serão carregados
     * @return true se os dados foram carregados com sucesso, false caso contrário
     */
    boolean carregar(String arquivo);
} 