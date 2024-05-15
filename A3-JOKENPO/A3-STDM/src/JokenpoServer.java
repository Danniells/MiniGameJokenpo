import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class JokenpoServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor aguardando conexao dos usuarios");

            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            int pontuacaoUsuario = 0;
            int pontuacaoCPU = 0;

            for (int rodada = 0; rodada < 3; rodada++) {
                int escolhaUsuario = input.readInt();
                int[] resultado = JokenpoGame.jogar(escolhaUsuario);

                // Enviar escolha da CPU
                output.writeInt(resultado[0]);
                output.flush();

                // Enviar resultado
                output.writeInt(resultado[1]);
                output.flush();

                if (resultado[1] == 1) {
                    pontuacaoUsuario++;
                } else if (resultado[1] == -1) {
                    pontuacaoCPU++;
                }
            }

            System.out.println("\nPontuacao final:");
            System.out.println("Voce: " + pontuacaoUsuario);
            System.out.println("CPU: " + pontuacaoCPU);

            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
