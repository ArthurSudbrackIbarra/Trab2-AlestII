import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Labyrinth labyrinth = new Labyrinth("caso200.txt");

            labyrinth.possibleMovementsFrom(38, 81);

            System.out.print("[Labirinto]\n");
            labyrinth.printLabyrinth();

            int numberOfSteps = labyrinth.findShortestPath();
            if(numberOfSteps == 0){
                System.out.print("\nNao ha caminho para o labirinto em questao.");
            }

            System.out.print("\n[Menor caminho]\n");
            labyrinth.printLabyrinth();
            System.out.print("\nNumero de passos: " + numberOfSteps);
            System.out.print("\n\nAs bolinhas 'O' indicam por onde o carneirinho passou.");
        }
        catch (IOException error){
            System.out.println("Erro!");
        }
    }

}
