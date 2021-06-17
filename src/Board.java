import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Board {

    // Classe interna Block (representa as posicoes do labirinto).
    private class Block {

        private final char symbol;
        private final ArrayList<Block> possibleMovements;

        private final int line;
        private final int column;

        public Block(char symbol, int line, int column){
            this.symbol = symbol;
            this.line = line;
            this.column = column;
            possibleMovements = new ArrayList<>();
        }

        public char getSymbol(){
            return this.symbol;
        }

        public void addPossibleMovement(Block block){
            this.possibleMovements.add(block);
        }

        // Exibe na tela todas os possiveis blocos de serem acessados a partir de um bloco qualquer.
        public void printPossibleMovements(){
            for(Block block : this.possibleMovements){
                System.out.println(block);
            }
        }

        @Override
        public String toString(){
            return "[Block]\nSymbol: " + this.symbol + "\nPosition: [" + this.line + ", " + this.column + "]\n";
        }

    }

    private final Block[][] board;

    private Block endBlock;
    private Block currentBlock;

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
        this.board = new Block[lineCount][columnCount];

        // Filling the board.
        reader = Files.newBufferedReader(path, Charset.defaultCharset());
        int j = 0;
        while((line = reader.readLine()) != null){
            if(line.equals("")) continue;
            for(int i = 0; i < line.length(); i++){
                char symbol = line.charAt(i);
                Block block = new Block(symbol, j, i);
                this.board[j][i] = block;
                if(symbol == 'C'){
                    this.currentBlock = block;
                }
                else if(symbol == 'S'){
                    this.endBlock = block;
                }
            }
            j++;
        }
        reader.close();
        fillPossibleMovements();
    }

    // Preenche os possiveis movimentos a partir de cada bloco.
    public void fillPossibleMovements(){
        int lines = this.board.length;
        int columns = this.board[0].length;
        for(int i = 0; i < lines; i++){
            for(int j = 0; j < columns; j++){
                Block block = this.board[i][j];
                Block blockToAdd = null;

                // Block above.
                int above = i - 1;
                if(above > 0) {
                    blockToAdd = this.board[above][j];
                    if(blockToAdd.getSymbol() != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }

                // Block below.
                int below = i + 1;
                if(below < lines) {
                    blockToAdd = this.board[below][j];
                    if(blockToAdd.getSymbol() != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }

                // Block to the left.
                int left = j - 1;
                if(left < 0) left = columns - 1;
                blockToAdd = this.board[i][left];
                if(blockToAdd.getSymbol() != 'x'){
                    block.addPossibleMovement(blockToAdd);
                }

                // Block to the right.
                int right = j + 1;
                if(right >= columns) right = 0;
                blockToAdd = this.board[i][right];
                if(blockToAdd.getSymbol() != 'x'){
                    block.addPossibleMovement(blockToAdd);
                }

                // Block to the diagonal up left.
                int diagonalUpLeftX = i - 2;

                int diagonalUpLeftY = j - 2;
                if(diagonalUpLeftY == -2){
                    diagonalUpLeftY = (columns - 1) - 1;
                }
                else if(diagonalUpLeftY == -1){
                    diagonalUpLeftY = columns - 1;
                }
                if(diagonalUpLeftX > 0){
                    blockToAdd = this.board[diagonalUpLeftX][diagonalUpLeftY];
                    if(blockToAdd.getSymbol() != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }

                // Block to the diagonal up right.
                int diagonalUpRightX = diagonalUpLeftX;

                int diagonalUpRightY = j + 2;
                if(diagonalUpRightY == (columns - 1) + 2){
                    diagonalUpRightY = 1;
                }
                else if(diagonalUpRightY == (columns - 1) + 1){
                    diagonalUpRightY = 0;
                }
                if(diagonalUpRightX > 0){
                    blockToAdd = this.board[diagonalUpRightX][diagonalUpRightY];
                    if(blockToAdd.getSymbol() != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }

                // Block to the diagonal down left.
                int diagonalDownLeftX = i + 2;
                int diagonalDownLeftY = diagonalUpLeftY;
                if(diagonalDownLeftX < lines){
                    blockToAdd = this.board[diagonalDownLeftX][diagonalDownLeftY];
                    if(blockToAdd.getSymbol() != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }

                // Block to the diagonal down right.
                int diagonalDownRightX = diagonalDownLeftX;
                int diagonalDownRightY = diagonalUpRightY;
                if(diagonalDownRightX < lines){
                    blockToAdd = this.board[diagonalDownRightX][diagonalDownRightY];
                    if(blockToAdd.getSymbol() != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
            }
        }
    }

    // Printa os possiveis movimentos a partir de uma linha e coluna informada.
    public void printPossibleMovements(int line, int column){
        Block block = this.board[line][column];
        block.printPossibleMovements();
    }

    // Exibe a matriz (tabuleiro) na tela.
    public void printBoard(){
        for(int i = 0; i < this.board.length; i++){
            for(int j = 0; j < this.board[0].length; j++){
                System.out.print(this.board[i][j].getSymbol());
            }
            System.out.print("\n");
        }
    }
}
