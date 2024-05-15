
import java.util.Random;

public class JokenpoGame {
    private static final int PAPEL = 1;
    private static final int PEDRA = 2;
    private static final int TESOURA = 3;

    public static int[] jogar(int escolhaUsuario) {
        Random gerador = new Random();
        int escolhaComputador = gerador.nextInt(3) + 1;
        int[] resultado = new int[2]; // Array para armazenar escolha da CPU e resultado

        switch (escolhaComputador) {
            case PAPEL:
                System.out.println("Computador escolheu Papel!");
                break;
            case PEDRA:
                System.out.println("Computador escolheu Pedra!");
                break;
            case TESOURA:
                System.out.println("Computador escolheu Tesoura!");
                break;
        }

        resultado[0] = escolhaComputador; // Armazenar escolha da CPU
        if (escolhaUsuario == escolhaComputador) {
            resultado[1] = 0; // Empate
        } else if ((escolhaUsuario - escolhaComputador) == -1 || (escolhaUsuario - escolhaComputador) == 2) {
            resultado[1] = 1; // Usuário vence
        } else {
            resultado[1] = -1; // Computador vence
        }
        return resultado;
    }
}
