import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Board board = new Board("caso040.txt");
            System.out.print("[Labirinto]\n");
            board.printBoard();
            // board.possibleMovementsFrom(0, 0);
            board.findShortestPath();
        }
        catch (IOException error){
            System.out.println("Erro!");
        }
    }

}
