public class HashTableB extends HashTableBase {

    public HashTableB() {
        // Inicia a tabela com 28 posições na BASE e 4 na EXTRA.
        // Ambas crescem dinamicamente quando atingem o fator de carga definido na classe base.
        super(28, 4);
    }

    @Override
    protected int funcaoHash(String chave) {
        // Esta função converte cada letra da palavra em um número de 1 a 26
        // (A=1, B=2, ..., Z=26) e soma todos esses valores.
        //
        // Exemplo:
        // "ABB" → A=1, B=2, B=2 → hash = 5
        //

        int valorHash = 0; // Acumula a soma total dos valores das letras

        for (int i = 0; i < chave.length(); i++) {
            // Converte cada caractere para maiúsculo para padronizar
            char letra = Character.toUpperCase(chave.charAt(i));

            // Verifica se é uma letra do alfabeto (A-Z)
            if (letra >= 'A' && letra <= 'Z') {
                // Subtrai 'A' (65 no código ASCII) e soma 1
                // para transformar A=1, B=2, ..., Z=26
                int valorLetra = (letra - 'A') + 1;

                // Soma ao valor total da chave
                valorHash += valorLetra;
            }
        }

        return valorHash;
    }
}
