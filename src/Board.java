import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Board {

    private class Block {

        public char symbol;
        public ArrayList<Block> possibleMovements;

        public Block(char symbol){
            this.symbol = symbol;
            possibleMovements = new ArrayList<>();
        }

    }

    private Block[][] board;

    private Block startBlock;
    private Block endBlock;

    public Board (String fileDirectory) throws IOException {
        Path path = Paths.get(fileDirectory);
        BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());

        // Obtaining line count and column count.
        int lineCount = 1;
        int columnCount = reader.readLine().length();

        String line;
        while((line = reader.readLine()) != null){
            if(line.equals("")) continue;
            lineCount++;
        }
        reader.close();

        // Creating the board.
        this.board = new Block[columnCount][lineCount];

        // Filling the board.
        reader = Files.newBufferedReader(path, Charset.defaultCharset());
        int j = 0;
        while((line = reader.readLine()) != null){
            if(line.equals("")) continue;
            for(int i = 0; i < line.length(); i++){
                this.board[i][j] = new Block(line.charAt(i));
            }
            j++;
        }
    }

    public void printBoard(){
        for(int i = 0; i < this.board.length; i++){
            for(int j = 0; j < this.board[0].length; j++){
                System.out.print(this.board[i][j].symbol);
            }
            System.out.print("\n");
        }
    }
}
