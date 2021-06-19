import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Board board = new Board("caso010.txt");
            System.out.print("[Labirinto]\n");
            board.printBoard();
        }
        catch (IOException error){
            System.out.println("Erro!");
        }
    }

}
