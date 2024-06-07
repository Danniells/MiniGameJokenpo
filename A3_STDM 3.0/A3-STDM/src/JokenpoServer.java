
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JokenpoServer {
    private final int porta;

    public JokenpoServer(int porta) {
        this.porta = porta;
    }

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor rodando na porta " + porta);

            while (true) {
                Socket socket = serverSocket.accept(); 
                System.out.println("Novo cliente conectado");

                JokenpoGame clienteThread = new JokenpoGame(socket);
                clienteThread.start(); 
            }
        } catch (IOException ex) {
            System.out.println("Erro no servidor: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        int porta = 12345; 
        JokenpoServer servidor = new JokenpoServer(porta);
        servidor.iniciar();
    }
}