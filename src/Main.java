import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Labyrinth labyrinth = new Labyrinth("caso040.txt");
            System.out.print("[Labirinto]\n");
            labyrinth.printLabyrinth();
            // labyrinth.possibleMovementsFrom(0, 0);
            labyrinth.findShortestPath();
        }
        catch (IOException error){
            System.out.println("Erro!");
        }
    }

}
