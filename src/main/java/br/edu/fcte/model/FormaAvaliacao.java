package br.edu.fcte.model;

/**
 * Enumeração que representa as formas de avaliação possíveis no sistema acadêmico.
 * 
 * MEDIA_SIMPLES:
 * - Todas as notas têm o mesmo peso (20% cada)
 * - Fórmula: (P1 + P2 + P3 + Listas + Seminário) / 5
 * - Ideal para disciplinas teóricas básicas
 * 
 * MEDIA_PONDERADA:
 * - P1: peso 1 (12.5%)
 * - P2: peso 2 (25%)
 * - P3: peso 3 (37.5%)
 * - Listas: peso 1 (12.5%)
 * - Seminário: peso 1 (12.5%)
 * - Fórmula: (P1 + 2*P2 + 3*P3 + Listas + Seminário) / 8
 * - Ideal para disciplinas avançadas onde as últimas provas são mais importantes
 */
public enum FormaAvaliacao {
    MEDIA_SIMPLES {
        @Override
        public double calcularMedia(double p1, double p2, double p3, double listas, double seminario) {
            // Média aritmética simples - todas as notas têm peso igual (20%)
            return (p1 + p2 + p3 + listas + seminario) / 5.0;
        }
    },
    MEDIA_PONDERADA {
        @Override
        public double calcularMedia(double p1, double p2, double p3, double listas, double seminario) {
            // P1: 12.5%, P2: 25%, P3: 37.5%, Listas: 12.5%, Seminário: 12.5%
            return (p1 + p2 * 2 + p3 * 3 + listas + seminario) / 8.0;
        }
    };

    /**
     * Calcula a média final do aluno de acordo com a forma de avaliação escolhida.
     * 
     * Regras:
     * - Todas as notas devem estar entre 0 e 10
     * - A média é calculada com precisão de uma casa decimal
     * - Média final >= 5.0 para aprovação (junto com frequência >= 75%)
     * 
     * @throws IllegalArgumentException se alguma nota estiver fora do intervalo [0,10]
     */
    public abstract double calcularMedia(double p1, double p2, double p3, double listas, double seminario);
} 