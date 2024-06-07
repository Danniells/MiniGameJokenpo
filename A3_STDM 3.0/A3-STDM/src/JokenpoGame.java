import java.io.*;
import java.net.*;
import java.util.Random;

public class JokenpoGame extends Thread {
    private Socket socket;
    private PrintWriter escritor;
    private BufferedReader leitor;
    private static final Object lock = new Object();
    private static JokenpoGame Cliente1 = null;
    private static JokenpoGame Cliente2 = null;
    private static int rodada = 0;
    private static final int totalRodadas = 3;
    private static String[] escolhas = new String[2];
    private int[] estatisticas = {0, 0, 0};

    public JokenpoGame(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            leitor = new BufferedReader(new InputStreamReader(input, "UTF-8"));

            OutputStream output = socket.getOutputStream();
            escritor = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);

            while (true) {
                escritor.println("Seja Bem vindo ao game Jokenpo! por favor escolha se deseja jogar contra um robo ou contra outro jogador \n  1 - Robo (CPU)  2 - Outro Jogador");

                String escolhermodo = leitor.readLine();
                System.out.println("Escolha do cliente: " + escolhermodo); 

                if ("1".equals(escolhermodo)) {
                   x1Robo();
                } else if ("2".equals(escolhermodo)) {
                    jogarContraOutroJogador();
                } else {
                    escritor.println("Escolha invalida. Encerrando conexao.");
                }

                if (rodada == totalRodadas) {
                    escritor.println("Deseja jogar novamente? (sim/nao)");
                    String resposta = leitor.readLine();
                if ("nao".equalsIgnoreCase(resposta)) {
                escritor.println("Obrigado por jogar! =) ");
                 break;
}
                }}

        }  catch (IOException | InterruptedException ex) {
            System.out.println("Erro: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Erro ao fechar socket: " + ex.getMessage());
            }
        }
    }

    private void x1Robo() throws IOException {
        Random random = new Random();
        String[] opcoes = {"pedra", "papel", "tesoura"};
        int rodada = 0;
        int totalRodadas = 3;
        int[] estatisticas = {0, 0, 0}; 

        escritor.println("A partida comeca! Escolha entre pedra, papel ou tesoura");

        while (rodada < totalRodadas) {
            escritor.println("Sua escolha: ");
            String escolhaJogador = leitor.readLine();
            String escolhaRobo = opcoes[random.nextInt(opcoes.length)];

            escritor.println("Robo escolheu: " + escolhaRobo);

            if (escolhaJogador.equals(escolhaRobo)) {
                escritor.println("Empate!");
                estatisticas[2]++;
            } else if ((escolhaJogador.equals("pedra") && escolhaRobo.equals("tesoura")) ||
                    (escolhaJogador.equals("papel") && escolhaRobo.equals("pedra")) ||
                    (escolhaJogador.equals("tesoura") && escolhaRobo.equals("papel"))) {
                escritor.println("Voce ganhou a rodada!");
                estatisticas[0]++;
            } else {
                escritor.println("Robo ganhou a rodada!");
                estatisticas[1]++;
            }

            escritor.println("Estatisticas da rodada: Vitorias: " + estatisticas[0] + ", Derrotas: " + estatisticas[1] + ", Empates: " + estatisticas[2]);
            rodada++;
            if (rodada < totalRodadas) {
                escritor.println("Proxima rodada. Escolha entre pedra, papel ou tesoura");
            }
        }

        escritor.println("Resultado da partida:  Vitorias: " + estatisticas[0] + ", Derrotas: " + estatisticas[1] + ", Empates: " + estatisticas[2]);
    }

    private void jogarContraOutroJogador() throws IOException, InterruptedException {
        synchronized (lock) {
            if (Cliente1== null) {
                Cliente1= this;
                escritor.println("Esperando outro jogador se conectar...");
                while (Cliente2== null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                iniciarJogo();
            } else {
                Cliente2= this;
                lock.notifyAll();
            }
        }

        awaitPlayers();

        if (rodada == totalRodadas) {
            finalizarJogo();
        }
    }

    private void iniciarJogo() {
       Cliente1.escritor.println("O jogo comecou. Escolha entre pedra, papel ou tesoura");
        Cliente2.escritor.println("O jogo comecou. Escolha entre pedra, papel ou tesoura");
    }

    private void DeterminaGanhadorRodada() throws InterruptedException, IOException {
        rodada++;
        String resultado;
        if (escolhas[0].equals(escolhas[1])) {
            resultado = "Empate!";
           Cliente1.estatisticas[2]++;
            Cliente2.estatisticas[2]++;
        } else if ((escolhas[0].equals("pedra") && escolhas[1].equals("tesoura")) ||
                (escolhas[0].equals("papel") && escolhas[1].equals("pedra")) ||
                (escolhas[0].equals("tesoura") && escolhas[1].equals("papel"))) {
            resultado = "Primeiro cliente ganhou a rodada!";
           Cliente1.estatisticas[0]++;
            Cliente2.estatisticas[1]++;
        } else {
            resultado = "Segundo cliente ganhou a rodada!";
           Cliente1.estatisticas[1]++;
            Cliente2.estatisticas[0]++;
        }

       Cliente1.escritor.println("Estatisticas da rodada - Vitorias: " +Cliente1.estatisticas[0] + ", Derrotas: " +Cliente1.estatisticas[1] + ", Empates: " +Cliente1.estatisticas[2]);
        Cliente2.escritor.println("Estatisticas da rodada - Vitorias: " + Cliente2.estatisticas[0] + ", Derrotas: " + Cliente2.estatisticas[1] + ", Empates: " + Cliente2.estatisticas[2]);
        if (rodada < totalRodadas) {
           Cliente1.escritor.println("Proxima rodada. Escolha entre pedra, papel ou tesoura");
            Cliente2.escritor.println("Proxima rodada. Escolha entre pedra, papel ou tesoura");
        }
    }

    private void finalizarJogo() {
       Cliente1.escritor.println("Resultado da partida - Vitorias: " +Cliente1.estatisticas[0] + ", Derrotas: " +Cliente1.estatisticas[1] + ", Empates: " +Cliente1.estatisticas[2]);
        Cliente2.escritor.println("Resultado da partida - Vitorias: " + Cliente2.estatisticas[0] + ", Derrotas: " + Cliente2.estatisticas[1] + ", Empates: " + Cliente2.estatisticas[2]);
    }

    private void awaitPlayers() throws InterruptedException, IOException {
        while (rodada < totalRodadas) {
            String escolha = leitor.readLine();
            synchronized (lock) {
                if (this ==Cliente1) {
                    escolhas[0] = escolha;
                    escritor.println("Esperando resposta do outro jogador...");
                } else {
                    escolhas[1] = escolha;
                    escritor.println("Esperando resposta do outro jogador...");
                }

                if (escolhas[0] != null && escolhas[1] != null) {
                    DeterminaGanhadorRodada();
                    escolhas[0] = null;
                    escolhas[1] = null;
                    lock.notifyAll();
                } else {
                    while (escolhas[0] == null && escolhas[1] == null) {
                        lock.wait();
                    }
                }
  }}
}
}