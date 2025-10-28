public abstract class HashTableBase {
    // ----- Estados possíveis de cada posição -----
    private static final byte VAZIO = 0;     // nunca usado
    private static final byte OCUPADO = 1;   // contém um valor ativo
    private static final byte REMOVIDO = 2;  // já teve valor, mas foi removido

    // ----- Capacidades -----
    private int capacidadeBase;    // área principal (hash % capacidadeBase)
    private int capacidadeExtra;   // área de colisões (heterogênea)

    // ----- Armazenamento -----
    private String[] chaves;       // vetor de todas as chaves (base + extra)
    private byte[] estados;        // estado de cada posição do vetor

    // ----- Métricas -----
    private int totalElementos = 0;         // total de elementos armazenados
    private int ocupacaoBase = 0;           // quantos slots da BASE já foram usados
    private long totalColisoes = 0;         // número total de colisões
    private int[] colisoesPorPosicaoBase;   // colisões por índice da BASE (clusterização)

    // ----- Limiar para crescer a BASE -----
    private static final double LIMIAR_OCUPACAO_BASE = 0.85; // 85%

    public HashTableBase(int baseInicial, int extraInicial) {
        capacidadeBase = Math.max(1, baseInicial);
        capacidadeExtra = Math.max(1, extraInicial);
        chaves = new String[capacidadeBase + capacidadeExtra];
        estados = new byte[capacidadeBase + capacidadeExtra];
        colisoesPorPosicaoBase = new int[capacidadeBase];
    }

    // ================= API PRINCIPAL =================

    // Inserção (ignora duplicatas)
    public void inserir(String chave) {
        if (chave == null) return;

        int indiceBase = calcularIndiceBase(chave);
        byte estadoPosicao = estados[indiceBase];

        // 1) Tenta colocar na BASE
        if (estadoPosicao == VAZIO || estadoPosicao == REMOVIDO) {
            if (estadoPosicao == VAZIO) ocupacaoBase++; // nova ocupação da base
            chaves[indiceBase] = chave;
            estados[indiceBase] = OCUPADO;
            totalElementos++;

            // Verifica se precisa expandir a BASE
            if ((double) ocupacaoBase / capacidadeBase > LIMIAR_OCUPACAO_BASE) {
                expandirBase(); // rehash total
            }
            return;
        }

        // 2) Já existe algo na BASE
        if (chave.equals(chaves[indiceBase])) return; // duplicata

        totalColisoes++;
        colisoesPorPosicaoBase[indiceBase]++;

        // 3) Procura espaço livre na EXTRA (linear)
        while (true) {
            int indiceLivre = encontrarSlotExtra(chave);
            if (indiceLivre >= 0) {
                if (estados[indiceLivre] == OCUPADO && chave.equals(chaves[indiceLivre])) return; // duplicata na extra
                chaves[indiceLivre] = chave;
                estados[indiceLivre] = OCUPADO;
                totalElementos++;
                return;
            }
            expandirExtra(); // se não tiver espaço na extra, dobra
        }
    }

    // Busca uma chave (true se existe)
    public boolean buscar(String chave) {
        if (chave == null) return false;

        int indiceBase = calcularIndiceBase(chave);

        if (estados[indiceBase] == OCUPADO && chave.equals(chaves[indiceBase])) return true;

        // Busca linear na área extra
        for (int i = capacidadeBase; i < capacidadeBase + capacidadeExtra; i++) {
            byte estado = estados[i];
            if (estado == VAZIO) return false; // posição nunca usada => pode parar
            if (estado == OCUPADO && chave.equals(chaves[i])) return true;
        }
        return false;
    }

    // Remove uma chave (marca como REMOVIDO)
    public boolean remover(String chave) {
        if (chave == null) return false;

        int indiceBase = calcularIndiceBase(chave);

        // tenta na base
        if (estados[indiceBase] == OCUPADO && chave.equals(chaves[indiceBase])) {
            estados[indiceBase] = REMOVIDO;
            chaves[indiceBase] = null;
            totalElementos--;
            return true;
        }

        // tenta na área de colisões
        for (int i = capacidadeBase; i < capacidadeBase + capacidadeExtra; i++) {
            byte estado = estados[i];
            if (estado == VAZIO) return false; // nunca teve nada -> para
            if (estado == OCUPADO && chave.equals(chaves[i])) {
                estados[i] = REMOVIDO;
                chaves[i] = null;
                totalElementos--;
                return true;
            }
        }
        return false;
    }

    // ================= CRESCIMENTO =================

    // Dobra a área EXTRA (sem rehash)
    private void expandirExtra() {
        int novaExtra = Math.max(4, capacidadeExtra * 2);
        String[] novasChaves = new String[capacidadeBase + novaExtra];
        byte[] novosEstados = new byte[capacidadeBase + novaExtra];

        // copia base e extra antiga
        System.arraycopy(chaves, 0, novasChaves, 0, capacidadeBase + capacidadeExtra);
        System.arraycopy(estados, 0, novosEstados, 0, capacidadeBase + capacidadeExtra);

        chaves = novasChaves;
        estados = novosEstados;
        capacidadeExtra = novaExtra;
    }

    // Dobra a BASE (faz rehash total)
    private void expandirBase() {
        int novaBase = Math.max(1, capacidadeBase * 2);
        int novaExtra = Math.max(4, capacidadeExtra);

        String[] chavesAntigas = chaves;
        byte[] estadosAntigos = estados;

        int totalAntigo = capacidadeBase + capacidadeExtra;

        capacidadeBase = novaBase;
        capacidadeExtra = novaExtra;

        chaves = new String[capacidadeBase + capacidadeExtra];
        estados = new byte[capacidadeBase + capacidadeExtra];
        colisoesPorPosicaoBase = new int[capacidadeBase];

        totalElementos = 0;
        ocupacaoBase = 0;
        totalColisoes = 0;

        for (int i = 0; i < totalAntigo; i++) {
            if (estadosAntigos[i] == OCUPADO) inserir(chavesAntigas[i]);
        }
    }

    // ================= INTERNOS =================

    private int calcularIndiceBase(String chave) {
        int hash = funcaoHash(chave);
        int indice = hash % capacidadeBase;
        return (indice < 0) ? indice + capacidadeBase : indice;
    }

    // Procura primeiro slot livre (EMPTY/REM) na área EXTRA
    private int encontrarSlotExtra(String chave) {
        int primeiroLivre = -1;
        for (int i = capacidadeBase; i < capacidadeBase + capacidadeExtra; i++) {
            byte estado = estados[i];
            if (estado == OCUPADO) {
                if (chave.equals(chaves[i])) return i; // duplicata
            } else {
                if (primeiroLivre == -1) primeiroLivre = i; // guarda o primeiro vazio
            }
        }
        return primeiroLivre;
    }

    // ================= HASH (definida nas subclasses) =================
    protected abstract int funcaoHash(String chave);

    // ================= GETTERS =================
    public int getCapacidadeBase() { return capacidadeBase; }
    public int getCapacidadeExtra() { return capacidadeExtra; }
    public int getTotalElementos() { return totalElementos; }
    public long getTotalColisoes() { return totalColisoes; }
    public int[] getColisoesPorPosicaoBase() { return colisoesPorPosicaoBase.clone(); }

    public int[] getDistribuicaoPorIndice() {
        int[] dist = new int[capacidadeBase + capacidadeExtra];
        for (int i = 0; i < dist.length; i++) {
            dist[i] = (estados[i] == OCUPADO) ? 1 : 0;
        }
        return dist;
    }

    public double getTaxaOcupacaoBase() {
        return (double) ocupacaoBase / capacidadeBase;
    }
}
