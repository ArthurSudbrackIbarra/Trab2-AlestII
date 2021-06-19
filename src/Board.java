import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Board {

    // Inner class Block (represents the positions inside the board).
    private static class Block {

        public char symbol;
        public int line;
        public int column;
        public final ArrayList<Block> possibleMovements;

        public Block(char symbol, int line, int column){
            this.symbol = symbol;
            this.line = line;
            this.column = column;
            possibleMovements = new ArrayList<>();
        }
        
        public void addPossibleMovement(Block block){
            this.possibleMovements.add(block);
        }

        // equals method for our vertex HashMap.
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Block block = (Block) o;
            return symbol == block.symbol &&
                    line == block.line &&
                    column == block.column;
        }

        // hashCode method for our vertex HashMap.
        @Override
        public int hashCode() {
            return (Integer.toString(this.line) + this.column).hashCode();
        }

        @Override
        public String toString(){
            return "[Block]\nSymbol: " + this.symbol + "\nPosition: [" + this.line + ", " + this.column + "]\n";
        }

    }

    private final Block[][] board;
    private Block endBlock;
    private Block startBlock;

    private final BFSGraph graph;
    private final HashMap<Block, Integer> vertexMap;

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
                    this.startBlock = block;
                }
                else if(symbol == 'S'){
                    this.endBlock = block;
                }
            }
            j++;
        }
        reader.close();

        this.graph = new BFSGraph(lineCount * columnCount);
        this.vertexMap = new HashMap<>();

        fillPossibleMovements();
        addEdgesToGraph();
    }

    // Fills each possible movement from each block.
    public void fillPossibleMovements(){
        int lines = this.board.length;
        int columns = this.board[0].length;
        int vertex = 0;
        for(int i = 0; i < lines; i++){
            for(int j = 0; j < columns; j++){
                // Setting up vertex map.
                Block block = this.board[i][j];
                this.vertexMap.put(block, vertex);
                vertex++;
                
                Block blockToAdd = null;
                
                // Block above.
                int above = i - 1;
                if(above > 0) {
                    blockToAdd = this.board[above][j];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
                // Block below.
                int below = i + 1;
                if(below < lines) {
                    blockToAdd = this.board[below][j];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
                // Block to the left.
                int left = j - 1;
                if(left < 0) left = columns - 1;
                blockToAdd = this.board[i][left];
                if(blockToAdd.symbol != 'x'){
                    block.addPossibleMovement(blockToAdd);
                }
                // Block to the right.
                int right = j + 1;
                if(right >= columns) right = 0;
                blockToAdd = this.board[i][right];
                if(blockToAdd.symbol != 'x'){
                    block.addPossibleMovement(blockToAdd);
                }
                // Block to the diagonal up left.
                int diagonalUpLeftY = i - 2;

                int diagonalUpLeftX = j - 2;
                if(diagonalUpLeftX == -2){
                    diagonalUpLeftX = (columns - 1) - 1;
                }
                else if(diagonalUpLeftX == -1){
                    diagonalUpLeftX = columns - 1;
                }
                if(diagonalUpLeftY > 0){
                    blockToAdd = this.board[diagonalUpLeftY][diagonalUpLeftX];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
                // Block to the diagonal up right.
                int diagonalUpRightX = j + 2;
                if(diagonalUpRightX == (columns - 1) + 2){
                    diagonalUpRightX = 1;
                }
                else if(diagonalUpRightX == (columns - 1) + 1){
                    diagonalUpRightX = 0;
                }
                if(diagonalUpLeftY > 0){
                    blockToAdd = this.board[diagonalUpLeftY][diagonalUpRightX];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
                // Block to the diagonal down left.
                int diagonalDownLeftY = i + 2;
                int diagonalDownLeftX = diagonalUpLeftX;
                if(diagonalDownLeftY < lines){
                    blockToAdd = this.board[diagonalDownLeftY][diagonalDownLeftX];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
                // Block to the diagonal down right.
                int diagonalDownRightX = diagonalUpRightX;
                if(diagonalDownLeftY < lines){
                    blockToAdd = this.board[diagonalDownLeftY][diagonalDownRightX];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
            }
        }
    }

    public void possibleMovementsFrom(int i, int j){
        Block block = this.board[i][j];
        if(block == null) return;
        for(Block adjacent : block.possibleMovements){
            System.out.println(adjacent);
        }
    }

    // Adds edges to our graph depending on each block possible movement.
    private void addEdgesToGraph() {
        for(int i = 0; i < this.board.length; i++){
            for(int j = 0; j < this.board[0].length; j++){
                Block currentBlock = this.board[i][j];
                int currentBlockVertex = this.vertexMap.get(currentBlock);
                for(Block adjacent : currentBlock.possibleMovements){
                    int adjacentBlockVertex = this.vertexMap.get(adjacent);
                    graph.addEdge(currentBlockVertex, adjacentBlockVertex);
                }
            }
        }
    }

    public void findShortestPath(){
        int startBlockVertex = this.vertexMap.get(this.startBlock);
        int endBlockVertex = this.vertexMap.get(this.endBlock);
        int minimumEdges = this.graph.minEdgeBFS(startBlockVertex, endBlockVertex);
        System.out.println("\nCaminho com menor numero de passos: " + minimumEdges);
    }
    
    // Prints labyrinth on screen.
    public void printBoard(){
        for(int i = 0; i < this.board.length; i++){
            for(int j = 0; j < this.board[0].length; j++){
                System.out.print(this.board[i][j].symbol);
            }
            System.out.print("\n");
        }
    }
}
