import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class JokenpoServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor aguardando conexao dos usuarios");

            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            // Receber tipo de jogo
            int modoDeJogo = input.readInt();

            if (modoDeJogo == 1) {
                jogarContraCPU(input, output);
            } else {
                jogarContraOutroJogador(serverSocket, input, output);
            }

            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void jogarContraCPU(ObjectInputStream input, ObjectOutputStream output) throws IOException {
        Random random = new Random();
        int pontuacaoUsuario = 0;
        int pontuacaoCPU = 0;

        for (int rodada = 0; rodada < 3; rodada++) {
            int escolhaUsuario = input.readInt();
            int escolhaCPU = random.nextInt(3) + 1;
            int[] resultado = JokenpoGame.jogar(escolhaUsuario, escolhaCPU);

            // Enviar escolha da CPU
            output.writeInt(escolhaCPU);
            output.flush();

            // Enviar resultado
            output.writeInt(resultado[1]);
            output.flush();

            if (resultado[0] == 1) {
                pontuacaoUsuario++;
            } else if (resultado[0] == -1) {
                pontuacaoCPU++;
            }
        }

        System.out.println("\nPontuacao final:");
        System.out.println("Voce: " + pontuacaoUsuario);
        System.out.println("CPU: " + pontuacaoCPU);
    }

    private static void jogarContraOutroJogador(ServerSocket serverSocket, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        System.out.println("Aguardando segundo jogador...");
        Socket socket2 = serverSocket.accept();
        System.out.println("Segundo jogador conectado.");

        ObjectOutputStream output2 = new ObjectOutputStream(socket2.getOutputStream());
        ObjectInputStream input2 = new ObjectInputStream(socket2.getInputStream());

        int pontuacaoJogador1 = 0;
        int pontuacaoJogador2 = 0;

        for (int rodada = 0; rodada < 3; rodada++) {
            int escolhaJogador1 = input.readInt();
            int escolhaJogador2 = input2.readInt();
            int[] resultado = JokenpoGame.jogar(escolhaJogador1, escolhaJogador2);

            // Enviar escolha do adversário
            output.writeInt(escolhaJogador2);
            output.flush();
            output2.writeInt(escolhaJogador1);
            output2.flush();

            // Enviar resultado
            output.writeInt(resultado[0]);
            output.flush();
            output2.writeInt(resultado[1]);
            output2.flush();

            if (resultado[0] == 1) {
                pontuacaoJogador1++;
            } else if (resultado[1] == 1) {
                pontuacaoJogador2++;
            }
        }

        System.out.println("\nPontuacao final:");
        System.out.println("Jogador 1: " + pontuacaoJogador1);
        System.out.println("Jogador 2: " + pontuacaoJogador2);

        socket2.close();
    }
}
