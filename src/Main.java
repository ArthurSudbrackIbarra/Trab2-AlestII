import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Board board = new Board("casoenunciado.txt");
            board.printBoard();
            board.printPossibleMovements(19, 39);
        }
        catch (IOException error){
            System.out.println("Erro!");
        }
    }

}
