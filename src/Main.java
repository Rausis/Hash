public class Main {

    // Gera N “nomes” inexistentes usando apenas String e NewArrayList
    private static NewArrayList<String> gerarBuscasInexistentes(int n) {
        NewArrayList<String> xs = new NewArrayList<>();
        xs.createArray(n > 0 ? n : 1);
        for (int i = 0; i < n; i++) {
            xs.add("§§NOME_INEXISTENTE_" + i + "§§");
        }
        return xs;
    }

    public static void main(String[] args) {
        // 1) Ler nomes (ajuste o caminho)
        String caminho = "female_names.txt";
        NewArrayList<String> nomes = LeitorArquivo.lerNomes(caminho);

        if (nomes.getSize() == 0) {
            System.out.println("[AVISO] Nenhum nome encontrado. Usando amostra mínima.");
            nomes.add("Ana"); nomes.add("Maria"); nomes.add("Joana");
            nomes.add("Clara"); nomes.add("Paula"); nomes.add("Beatriz");
        }

        // 2) Instanciar as duas tabelas (heterogêneo dinâmico)
        HashTableA tabelaA = new HashTableA(); // BASE=28, EXTRA=4 no construtor
        HashTableB tabelaB = new HashTableB();

        // 3) Inserção cronometrada
        long t0 = System.nanoTime();
        for (int i = 0; i < nomes.getSize(); i++) {
            tabelaA.inserir(nomes.getElement(i));
        }
        long tempoInsercaoA = System.nanoTime() - t0;

        t0 = System.nanoTime();
        for (int i = 0; i < nomes.getSize(); i++) {
            tabelaB.inserir(nomes.getElement(i));
        }
        long tempoInsercaoB = System.nanoTime() - t0;

        // 4) Preparar amostras de busca (existentes + inexistentes)
        int amostra = nomes.getSize() < 1000 ? nomes.getSize() : 1000;

        NewArrayList<String> buscasExistentes = new NewArrayList<>();
        buscasExistentes.createArray(amostra);
        for (int i = 0; i < amostra; i++) {
            buscasExistentes.add(nomes.getElement(i));
        }

        NewArrayList<String> buscasInexistentes = gerarBuscasInexistentes(amostra);

        // 5) Buscar em A
        int hitsA = 0, missA = 0;
        t0 = System.nanoTime();
        for (int i = 0; i < buscasExistentes.getSize(); i++) {
            if (tabelaA.buscar(buscasExistentes.getElement(i))) hitsA++;
        }
        for (int i = 0; i < buscasInexistentes.getSize(); i++) {
            if (!tabelaA.buscar(buscasInexistentes.getElement(i))) missA++;
        }
        long tempoBuscaA = System.nanoTime() - t0;

        // 6) Buscar em B
        int hitsB = 0, missB = 0;
        t0 = System.nanoTime();
        for (int i = 0; i < buscasExistentes.getSize(); i++) {
            if (tabelaB.buscar(buscasExistentes.getElement(i))) hitsB++;
        }
        for (int i = 0; i < buscasInexistentes.getSize(); i++) {
            if (!tabelaB.buscar(buscasInexistentes.getElement(i))) missB++;
        }
        long tempoBuscaB = System.nanoTime() - t0;

        // 7) Relatório
        System.out.println("\n===== RESULTADOS =====");

        System.out.println("\n-- HashTable A (soma chars) --");
        System.out.println("Elementos: " + tabelaA.getTotalElementos());
        System.out.println("BASE: " + tabelaA.getCapacidadeBase() + " | EXTRA: " + tabelaA.getCapacidadeExtra());
        System.out.println("Colisões totais: " + tabelaA.getTotalColisoes());
        System.out.println("Tempo inserção (ns): " + tempoInsercaoA);
        System.out.println("Tempo busca (ns): " + tempoBuscaA);
        System.out.println("Busca existentes (hits): " + hitsA + " / " + amostra);
        System.out.println("Busca inexistentes (miss esperados): " + missA + " / " + amostra);

        System.out.println("\n-- HashTable B (polinomial x31) --");
        System.out.println("Elementos: " + tabelaB.getTotalElementos());
        System.out.println("BASE: " + tabelaB.getCapacidadeBase() + " | EXTRA: " + tabelaB.getCapacidadeExtra());
        System.out.println("Colisões totais: " + tabelaB.getTotalColisoes());
        System.out.println("Tempo inserção (ns): " + tempoInsercaoB);
        System.out.println("Tempo busca (ns): " + tempoBuscaB);
        System.out.println("Busca existentes (hits): " + hitsB + " / " + amostra);
        System.out.println("Busca inexistentes (miss esperados): " + missB + " / " + amostra);

}
}
