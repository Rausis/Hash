public class Main {

    /**
     * Gera N chaves inexistentes para testar "miss" nas buscas.
     * Usa apenas String + sua NewArrayList.
     */
    private static NewArrayList<String> gerarConsultasInexistentes(int quantidade) {
        NewArrayList<String> consultas = new NewArrayList<>();
        consultas.createArray(quantidade > 0 ? quantidade : 1);

        for (int i = 0; i < quantidade; i++) {
            consultas.add("§§NOME_INEXISTENTE_" + i + "§§");
        }
        return consultas;
    }

    public static void main(String[] args) {
        // ========== 1) Ler nomes do arquivo ==========
        String caminhoArquivo = "female_names.txt";
        NewArrayList<String> nomesLidos = LeitorArquivo.lerArquivoDeNomes(caminhoArquivo);

        // Caso o arquivo esteja vazio/ausente, cria uma amostra mínima
        if (nomesLidos.getSize() == 0) {
            System.out.println("[AVISO] Nenhum nome encontrado. Usando amostra mínima.");
            nomesLidos.add("Ana");   nomesLidos.add("Maria"); nomesLidos.add("Joana");
            nomesLidos.add("Clara"); nomesLidos.add("Paula"); nomesLidos.add("Beatriz");
        }

        // ========== 2) Instanciar as tabelas (heterogêneo dinâmico) ==========
        HashTableA tabelaASCII = new HashTableA(); // A: soma ASCII
        HashTableB tabelaAZ    = new HashTableB(); // B: A=1..Z=26

        // ========== 3) Inserção cronometrada ==========
        long tInicio, tempoInsercaoA, tempoInsercaoB;

        tInicio = System.nanoTime();
        for (int i = 0; i < nomesLidos.getSize(); i++) {
            tabelaASCII.inserir(nomesLidos.getElement(i));
        }
        tempoInsercaoA = System.nanoTime() - tInicio;

        tInicio = System.nanoTime();
        for (int i = 0; i < nomesLidos.getSize(); i++) {
            tabelaAZ.inserir(nomesLidos.getElement(i));
        }
        tempoInsercaoB = System.nanoTime() - tInicio;

        // ========== 4) Preparar amostras de busca (existentes + inexistentes) ==========
        // Limita a amostra a no máx. 1000 para padronizar tempos comparáveis
        int tamanhoAmostra = (nomesLidos.getSize() < 1000) ? nomesLidos.getSize() : 1000;

        // Copia os primeiros N nomes para buscas existentes
        NewArrayList<String> consultasExistentes = new NewArrayList<>();
        consultasExistentes.createArray(tamanhoAmostra);
        for (int i = 0; i < tamanhoAmostra; i++) {
            consultasExistentes.add(nomesLidos.getElement(i));
        }

        // Gera N consultas inexistentes
        NewArrayList<String> consultasInexistentes = gerarConsultasInexistentes(tamanhoAmostra);

        // ========== 5) Buscas na Tabela A ==========
        int acertosA = 0;  // hits (existentes encontrados)
        int falhasA  = 0;  // misses (inexistentes corretamente não encontrados)
        long tempoBuscaA;

        tInicio = System.nanoTime();
        // Busca existentes → esperamos true
        for (int i = 0; i < consultasExistentes.getSize(); i++) {
            if (tabelaASCII.buscar(consultasExistentes.getElement(i))) acertosA++;
        }
        // Busca inexistentes → esperamos false
        for (int i = 0; i < consultasInexistentes.getSize(); i++) {
            if (!tabelaASCII.buscar(consultasInexistentes.getElement(i))) falhasA++;
        }
        tempoBuscaA = System.nanoTime() - tInicio;

        // ========== 6) Buscas na Tabela B ==========
        int acertosB = 0;
        int falhasB  = 0;
        long tempoBuscaB;

        tInicio = System.nanoTime();
        for (int i = 0; i < consultasExistentes.getSize(); i++) {
            if (tabelaAZ.buscar(consultasExistentes.getElement(i))) acertosB++;
        }
        for (int i = 0; i < consultasInexistentes.getSize(); i++) {
            if (!tabelaAZ.buscar(consultasInexistentes.getElement(i))) falhasB++;
        }
        tempoBuscaB = System.nanoTime() - tInicio;

        // ========== 7) Relatório consolidado ==========
        System.out.println("\n===== RESULTADOS =====");

        System.out.println("\n-- HashTable A (soma ASCII) --");
        System.out.println("Elementos inseridos: " + tabelaASCII.getTotalElementos());
        System.out.println("BASE: " + tabelaASCII.getCapacidadeBase() + " | EXTRA: " + tabelaASCII.getCapacidadeExtra());
        System.out.println("Colisões totais: " + tabelaASCII.getTotalColisoes());
        System.out.println("Tempo de inserção (ns): " + tempoInsercaoA);
        System.out.println("Tempo de busca (ns): " + tempoBuscaA);
        System.out.println("Buscas existentes (hits): " + acertosA + " / " + tamanhoAmostra);
        System.out.println("Buscas inexistentes (miss esperados): " + falhasA + " / " + tamanhoAmostra);

        System.out.println("\n-- HashTable B (A=1..Z=26) --");
        System.out.println("Elementos inseridos: " + tabelaAZ.getTotalElementos());
        System.out.println("BASE: " + tabelaAZ.getCapacidadeBase() + " | EXTRA: " + tabelaAZ.getCapacidadeExtra());
        System.out.println("Colisões totais: " + tabelaAZ.getTotalColisoes());
        System.out.println("Tempo de inserção (ns): " + tempoInsercaoB);
        System.out.println("Tempo de busca (ns): " + tempoBuscaB);
        System.out.println("Buscas existentes (hits): " + acertosB + " / " + tamanhoAmostra);
        System.out.println("Buscas inexistentes (miss esperados): " + falhasB + " / " + tamanhoAmostra);
    }
}
