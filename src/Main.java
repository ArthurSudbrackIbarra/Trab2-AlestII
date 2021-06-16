import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Board board = new Board("casoenunciado.txt");
            board.printBoard();
        }
        catch (IOException error){
            System.out.println("Erro!");
        }
    }

}
