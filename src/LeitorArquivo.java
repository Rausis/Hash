import java.io.FileReader;
import java.io.BufferedReader;

public class LeitorArquivo {

    /**
     * Lê um arquivo texto (uma palavra por linha) e retorna uma NewArrayList<String>
     * Cada linha não vazia é considerada um nome.
     */
    public static NewArrayList<String> lerArquivoDeNomes(String caminhoArquivo) {
        // Lista dinâmica para armazenar todos os nomes lidos do arquivo
        NewArrayList<String> listaDeNomes = new NewArrayList<>();
        listaDeNomes.createArray(32); // capacidade inicial (aumenta automaticamente conforme necessário)

        BufferedReader leitor = null;
        try {
            // Abre o arquivo indicado
            leitor = new BufferedReader(new FileReader(caminhoArquivo));
            String linhaAtual;

            // Lê linha por linha até o final do arquivo
            while ((linhaAtual = leitor.readLine()) != null) {
                // Remove espaços no início e fim da linha
                linhaAtual = linhaAtual.trim();

                // Ignora linhas completamente vazias
                if (!linhaAtual.equals("")) {
                    // Adiciona o nome lido na lista
                    listaDeNomes.add(linhaAtual);
                }
            }
        } catch (Exception erro) {
            System.out.println("⚠️ Erro ao ler arquivo: " + erro.getMessage());
        } finally {
            // Fecha o arquivo de forma segura (mesmo em caso de erro)
            try {
                if (leitor != null) leitor.close();
            } catch (Exception ignore) {}
        }

        return listaDeNomes; // retorna a lista preenchida
    }

    /**
     * Converte uma NewArrayList<String> em um vetor (String[])
     * útil para testes ou compatibilidade com métodos que exigem arrays.
     */
    public static String[] converterListaParaArray(NewArrayList<String> listaDeNomes) {
        int tamanho = listaDeNomes.getSize();
        String[] vetorDeNomes = new String[tamanho];

        // Copia manualmente os elementos da lista para o vetor
        for (int i = 0; i < tamanho; i++) {
            vetorDeNomes[i] = listaDeNomes.getElement(i);
        }

        return vetorDeNomes;
    }
}
