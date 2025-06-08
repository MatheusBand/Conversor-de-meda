import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import static java.lang.ref.Cleaner.create;

public class principal {
    public static void main(String[] args) throws IOException, InterruptedException {
//Moedas escolhidas:
// Iene Japonês — JPY
//Dólar Canadense — CAD
//Real Brasileiro — BRL
//Franco Suíço — CHF
//Dólar de Singapura — SGD
//Rand Sul-Africano — ZAR

        Scanner menu = new Scanner(System.in);
        String opcao;
        Double valor;

        while (true) {
            System.out.println("""
                    ¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨
                    CONVERSOR DE MOEDAS
                    
                    Opções:
                    1-Real Brasileiro ---> Dólar
                    2-Dólar ---> Real Brasileiro
                    3-Real Brasileiro ---> Iene Japonês
                    4-Iene Japonês ---> Real Brasileiro
                    5-Real Brasileiro ---> Franco Suíço
                    6-Franco Suíço ---> Real Brasileiro
                    7-Sair
                    
                    ¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨
                    """);
            System.out.println("Escolha uma opção de conversão:");
            opcao= menu.nextLine();
            if(opcao.equals("7")) {
                break;
            }
            String moedaOrigem = "";
            String moedaDestino= "";

            if(opcao.equals("1")){

                moedaOrigem="BRL";
                moedaDestino="USD";
            }
            else if (opcao.equals("2")) {

                moedaOrigem="USD";
                moedaDestino="BRL";
            }
            else if (opcao.equals("3")) {

                moedaOrigem="BRL";
                moedaDestino="JPY";
            }
            else if (opcao.equals("4")) {

                moedaOrigem="JPY";
                moedaDestino="BRL";
            }
            else if (opcao.equals("5")) {

                moedaOrigem="BRL";
                moedaDestino="CHF";
            }
            else if (opcao.equals("6")) {

                moedaOrigem="CHF";
                moedaDestino="BRL";
            }
            else{
                System.out.println("Opção invalida, tente novamente...");
                continue;
            }
            System.out.println("Digite o valor para conversão: ");
                try{
                    valor= Double.parseDouble(menu.nextLine());
                }
                catch (NumberFormatException e){
                    System.out.println("Valor invalido!!");
                    continue;

                }
            

            String url = "https://v6.exchangerate-api.com/v6/ec36612bd4878cb9347a92cb/latest/" +moedaOrigem ;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Erro na API. Código: " + response.statusCode());
                continue;
            }

            Gson gson = new Gson();
            Mapear json = gson.fromJson(response.body(), Mapear.class);

            if (!"success".equals(json.result)) {
                System.out.println("Erro na resposta da API.");
                continue;
            }

            if (!json.conversion_rates.containsKey(moedaDestino)) {
                System.out.println("Moeda de destino não encontrada.");
                continue;
            }

            double taxa = json.conversion_rates.get(moedaDestino);
            double convertido = valor * taxa;

            System.out.printf("%.2f %s = %.2f %s%n%n", valor, moedaOrigem, convertido, moedaDestino);
        }

        menu.close();
    }
}