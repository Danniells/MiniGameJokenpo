public class JokenpoGame {
    private static final int PAPEL = 1;
    private static final int PEDRA = 2;
    private static final int TESOURA = 3;

    public static int[] jogar(int escolhaPlayer1, int escolhaPlayer2) {
        int[] resultado = new int[2]; // Array para armazenar resultado para cada jogador

        if (escolhaPlayer1 == escolhaPlayer2) {
            resultado[0] = 0; // Empate para jogador 1
            resultado[1] = 0; // Empate para jogador 2
        } else if ((escolhaPlayer1 == PAPEL && escolhaPlayer2 == PEDRA) ||
                   (escolhaPlayer1 == PEDRA && escolhaPlayer2 == TESOURA) ||
                   (escolhaPlayer1 == TESOURA && escolhaPlayer2 == PAPEL)) {
            resultado[0] = 1; // Jogador 1 vence
            resultado[1] = -1; // Jogador 2 perde
        } else {
            resultado[0] = -1; // Jogador 1 perde
            resultado[1] = 1; // Jogador 2 vence
        }
        return resultado;
    }
}
