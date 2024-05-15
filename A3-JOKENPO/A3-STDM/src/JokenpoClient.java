import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class JokenpoClient {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            Socket socket = new Socket("localhost", 12345);

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            int pontuacaoUsuario = 0;
            int pontuacaoCPU = 0;

            for (int rodada = 0; rodada < 3; rodada++) {
                System.out.println("Rodada " + (rodada + 1));
                System.out.println("Escolha sua jogada: ");
                System.out.println("1. Papel");
                System.out.println("2. Pedra");
                System.out.println("3. Tesoura");

                int escolhaUsuario = 0;
                try {
                    escolhaUsuario = scanner.nextInt();

                    if (escolhaUsuario < 1 || escolhaUsuario > 3) {
                        System.out.println("Escolha invalida, por favor escolha um numero entre 1 e 3.");
                        rodada--; // Decrementa a rodada para repetir a jogada atual
                        continue;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Escolha invalida, por favor escolha um numero entre 1 e 3.");
                    scanner.next(); // Limpa a entrada inválida
                    rodada--; // Decrementa a rodada para repetir a jogada atual
                    continue;
                }

                output.writeInt(escolhaUsuario);
                output.flush();

                // Receber a jogada da CPU
                int jogadaCPU = input.readInt();
                System.out.print("A CPU escolheu: ");
                switch (jogadaCPU) {
                    case 1:
                        System.out.println("Papel");
                        break;
                    case 2:
                        System.out.println("Pedra");
                        break;
                    case 3:
                        System.out.println("Tesoura");
                        break;
                    default:
                        System.out.println("Opcao invalida da CPU");
                        break;
                }

                if (jogadaCPU < 1 || jogadaCPU > 3) {
                    System.out.println("A CPU fez uma jogada invalida, a rodada sera ignorada.");
                    continue; // Pula para a próxima iteração do loop sem alterar a pontuação
                }

                int resultado = input.readInt();
                if (resultado == 0) {
                    System.out.println("Empate!");
                } else if (resultado == 1) {
                    System.out.println("Voce venceu!");
                    pontuacaoUsuario++;
                } else {
                    System.out.println("Voce perdeu!");
                    pontuacaoCPU++;
                }
            }

            System.out.println("\nPontuacao final:");
            System.out.println("Voce: " + pontuacaoUsuario);
            System.out.println("CPU: " + pontuacaoCPU);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
