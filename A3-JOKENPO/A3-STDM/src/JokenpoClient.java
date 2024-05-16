import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class JokenpoClient {
    private static final int PORT = 12345;
    private static final int NUM_ROUNDS = 3;
    private static final int MIN_CHOICE = 1;
    private static final int MAX_CHOICE = 3;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", PORT);
             Scanner scanner = new Scanner(System.in);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Conectado ao servidor.");
            System.out.println("Escolha o modo de jogo:");
            System.out.println("1 - Jogar contra CPU");
            System.out.println("2 - Jogar contra outro jogador");

            int modoDeJogo = getUserMode(scanner);
            output.writeInt(modoDeJogo);
            output.flush();

            if (modoDeJogo == 1) {
                jogarContraCPU(scanner, output, input);
            } else {
                jogarContraOutroJogador(scanner, output, input);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getUserMode(Scanner scanner) {
        while (true) {
            try {
                int modo = scanner.nextInt();
                if (modo == 1 || modo == 2) {
                    return modo;
                } else {
                    System.out.println("Escolha inválida, por favor escolha 1 ou 2");
                }
            } catch (InputMismatchException e) {
                System.out.println("Escolha inválida, por favor escolha 1 ou 2");
                scanner.next(); // Clear invalid input
            }
        }
    }

    private static void jogarContraCPU(Scanner scanner, ObjectOutputStream output, ObjectInputStream input) throws IOException {
        int pontuacaoUsuario = 0;
        int pontuacaoCPU = 0;

        for (int rodada = 0; rodada < NUM_ROUNDS; rodada++) {
            System.out.println("Rodada " + (rodada + 1));
            System.out.println("Escolha sua jogada:");
            System.out.println("1 - Papel");
            System.out.println("2 - Pedra");
            System.out.println("3 - Tesoura");

            int escolhaUsuario = getUserChoice(scanner);

            output.writeInt(escolhaUsuario);
            output.flush();

            int jogadaAdversario = input.readInt();
            System.out.print("O adversário escolheu: ");
            printAdversarioChoice(jogadaAdversario);

            int resultado = input.readInt();

            if (resultado == 1) {
                pontuacaoUsuario++;
            } else if (resultado == -1) {
                pontuacaoCPU++;
            }

            System.out.println(getRoundMessage(resultado));
            System.out.println("Pontuação atual: Você " + pontuacaoUsuario + " x " + pontuacaoCPU + " CPU\n");
        }

        System.out.println("\nPontuação final:");
        System.out.println("Você: " + pontuacaoUsuario);
        System.out.println("CPU: " + pontuacaoCPU);
    }

    private static void jogarContraOutroJogador(Scanner scanner, ObjectOutputStream output, ObjectInputStream input) throws IOException {
        int pontuacao = 0;

        for (int rodada = 0; rodada < NUM_ROUNDS; rodada++) {
            System.out.println("Rodada " + (rodada + 1));
            int escolhaUsuario = getUserChoice(scanner);

            output.writeInt(escolhaUsuario);
            output.flush();

            int jogadaAdversario = input.readInt();
            System.out.print("O adversário escolheu: ");
            printAdversarioChoice(jogadaAdversario);

            int resultado = input.readInt();
            pontuacao += getRoundResult(resultado);

            System.out.println(getRoundMessage(resultado));
        }

        System.out.println("\nPontuação final:");
        System.out.println("Você: " + pontuacao);
    }

    private static int getUserChoice(Scanner scanner) {
        while (true) {
            try {
                int escolhaUsuario = scanner.nextInt();
                if (escolhaUsuario < MIN_CHOICE || escolhaUsuario > MAX_CHOICE) {
                    System.out.println("Escolha inválida, por favor escolha um número entre " + MIN_CHOICE + " e " + MAX_CHOICE);
                } else {
                    return escolhaUsuario;
                }
            } catch (InputMismatchException e) {
                System.out.println("Escolha inválida, por favor escolha um número entre " + MIN_CHOICE + " e " + MAX_CHOICE);
                scanner.next(); // Clear invalid input
            }
        }
    }

    private static void printAdversarioChoice(int jogadaAdversario) {
        switch (jogadaAdversario) {
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
                System.out.println("Opção inválida do adversário");
        }
    }

    private static int getRoundResult(int resultado) {
        if (resultado == 0) {
            return 0; // Empate
        } else if (resultado == 1) {
            return 1; // Você venceu
        } else {
            return 0; // Você perdeu
        }
    }

    private static String getRoundMessage(int resultado) {
        if (resultado == 0) {
            return "Empate!";
        } else if (resultado == 1) {
            return "Você venceu!";
        } else {
            return "Você perdeu!";
        }
    }
}
