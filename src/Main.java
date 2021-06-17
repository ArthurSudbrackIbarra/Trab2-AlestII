import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Board board = new Board("casoenunciado.txt");
            board.printBoard();
            System.out.println();
            board.printPossibleMovements(0, 0);
        }
        catch (IOException error){
            System.out.println("Erro!");
        }
    }

}
