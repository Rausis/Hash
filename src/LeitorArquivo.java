import java.io.FileReader;
import java.io.BufferedReader;

public class LeitorArquivo {

    // Lê um arquivo texto UTF-8 (uma chave por linha) para NewArrayList<String>
    public static NewArrayList<String> lerNomes(String caminho) {
        NewArrayList<String> nomes = new NewArrayList<>();
        nomes.createArray(32); // capacidade inicial qualquer; cresce sozinha

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(caminho));
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (!linha.equals("")) {
                    nomes.add(linha);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (Exception ignore) {}
        }
        return nomes;
    }

    // Utilitário opcional: converter NewArrayList<String> em String[]
    public static String[] toArray(NewArrayList<String> lista) {
        int n = lista.getSize();
        String[] arr = new String[n];
        for (int i = 0; i < n; i++) arr[i] = lista.getElement(i);
        return arr;
    }
}
