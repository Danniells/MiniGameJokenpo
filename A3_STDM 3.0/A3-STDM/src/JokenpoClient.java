import java.io.*;
import java.net.*;
import java.util.Scanner;

public class JokenpoClient {
    public static void main(String[] args) {

        int port = 12345;

        Scanner s = new Scanner(System.in);
        System.out.println("Por favor, digite o IP do servidor para iniciarmos os jogos: ");
        String hostname = s.nextLine();

        boolean jogarNovamente = true;

        while (jogarNovamente) {
            try (Socket socket = new Socket(hostname, port)) {
                BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                PrintWriter escritor = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                BufferedReader entradaUsuario = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

                String mensagemServidor;
                while ((mensagemServidor = leitor.readLine()) != null) {
                    System.out.println("Servidor: " + mensagemServidor);

                    if (mensagemServidor.contains("Escolha entre pedra, papel ou tesoura") || mensagemServidor.contains("Proxima rodada")) {
                        String escolha;
                        do {
                            System.out.print("Sua escolha: ");
                            escolha = entradaUsuario.readLine();
                            if (!escolha.equalsIgnoreCase("pedra") && !escolha.equalsIgnoreCase("papel") && !escolha.equalsIgnoreCase("tesoura")) {
                                System.out.println("Escolha invalida. Por favor, escolha entre pedra, papel ou tesoura.");
                            }
                        } while (!escolha.equalsIgnoreCase("pedra") && !escolha.equalsIgnoreCase("papel") && !escolha.equalsIgnoreCase("tesoura"));
                        escritor.println(escolha);
                    } else if (mensagemServidor.contains("2 - Outro Jogador")) {
                        String escolha = entradaUsuario.readLine();
                        escritor.println(escolha);
                    } else if (mensagemServidor.contains("Resultado da partida")) {
                        System.out.println("Deseja jogar novamente? (sim/nao): ");
                        String resposta = entradaUsuario.readLine();
                       if ("nao".equalsIgnoreCase(resposta)) {
    System.out.println("Obrigado por jogar! =) ");
    jogarNovamente = false;
    break;
}}
                }
            } catch (UnknownHostException ex) {
                System.out.println("Servidor nao localizado: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("Erro de I/O: " + ex.getMessage());
            }
        }
    }
}
