public class HashTableA extends HashTableBase {

    public HashTableA() {
        // Inicia a tabela com:
        // BASE = 28 posições
        // EXTRA = 4 posições
        // Ambas crescem dinamicamente conforme o fator de carga.
        super(28, 4);
    }

    @Override
    protected int funcaoHash(String chave) {
        // Função Hash A:
        // Calcula o hash somando diretamente o valor ASCII de cada caractere da palavra.
        //
        // Exemplo:
        // 'A' = 65, 'B' = 66, 'C' = 67
        // "ABC" → 65 + 66 + 67 = 198

        int valorHash = 0; // Acumulador da soma dos códigos ASCII

        for (int i = 0; i < chave.length(); i++) {
            // Soma o valor numérico (ASCII) do caractere atual
            valorHash += chave.charAt(i);
        }

        // Retorna a soma total — a classe base calculará o índice com base nesse valor
        return valorHash;
    }
}
