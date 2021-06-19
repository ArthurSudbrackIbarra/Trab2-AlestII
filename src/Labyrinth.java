import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Labyrinth {

    // Inner class Block (represents the positions inside the labyrinth).
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

    private final Block[][] labyrinth;
    private Block endBlock;
    private Block startBlock;

    private final BFSGraph graph;
    private final HashMap<Block, Integer> vertexMap;
    private final HashMap<Integer, Block> invertedVertexMap;

    public Labyrinth(String fileDirectory) throws IOException {
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

        // Creating the labyrinth.
        this.labyrinth = new Block[lineCount][columnCount];

        // Filling the labyrinth.
        reader = Files.newBufferedReader(path, Charset.defaultCharset());
        int j = 0;
        while((line = reader.readLine()) != null){
            if(line.equals("")) continue;
            for(int i = 0; i < line.length(); i++){
                char symbol = line.charAt(i);
                Block block = new Block(symbol, j, i);
                this.labyrinth[j][i] = block;
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
        this.invertedVertexMap = new HashMap<>();

        fillPossibleMovements();
        addEdgesToGraph();
    }

    // Fills each possible movement from each block.
    public void fillPossibleMovements(){
        int lines = this.labyrinth.length;
        int columns = this.labyrinth[0].length;
        int vertexCount = 0;
        for(int i = 0; i < lines; i++){
            for(int j = 0; j < columns; j++){
                // Setting up vertex map.
                Block block = this.labyrinth[i][j];
                if(block.symbol == 'x') continue;
                this.vertexMap.put(block, vertexCount);
                this.invertedVertexMap.put(vertexCount, block);
                vertexCount++;
                
                Block blockToAdd = null;
                
                // Block above.
                int above = i - 1;
                if(above > 0) {
                    blockToAdd = this.labyrinth[above][j];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
                // Block below.
                int below = i + 1;
                if(below < lines) {
                    blockToAdd = this.labyrinth[below][j];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
                // Block to the left.
                int left = j - 1;
                if(left < 0) left = columns - 1;
                blockToAdd = this.labyrinth[i][left];
                if(blockToAdd.symbol != 'x'){
                    block.addPossibleMovement(blockToAdd);
                }
                // Block to the right.
                int right = j + 1;
                if(right >= columns) right = 0;
                blockToAdd = this.labyrinth[i][right];
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
                    blockToAdd = this.labyrinth[diagonalUpLeftY][diagonalUpLeftX];
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
                    blockToAdd = this.labyrinth[diagonalUpLeftY][diagonalUpRightX];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
                // Block to the diagonal down left.
                int diagonalDownLeftY = i + 2;
                int diagonalDownLeftX = diagonalUpLeftX;
                if(diagonalDownLeftY < lines){
                    blockToAdd = this.labyrinth[diagonalDownLeftY][diagonalDownLeftX];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
                // Block to the diagonal down right.
                int diagonalDownRightX = diagonalUpRightX;
                if(diagonalDownLeftY < lines){
                    blockToAdd = this.labyrinth[diagonalDownLeftY][diagonalDownRightX];
                    if(blockToAdd.symbol != 'x'){
                        block.addPossibleMovement(blockToAdd);
                    }
                }
            }
        }
    }

    // Prints the possible movements from a block in the line and column specified.
    public void possibleMovementsFrom(int i, int j){
        boolean case1 = i < 0;
        boolean case2 = i >= this.labyrinth.length;
        boolean case3 = j < 0;
        boolean case4 = j >= this.labyrinth[0].length;
        if(case1 || case2 || case3 || case4){
            throw new IndexOutOfBoundsException("Linha e/ou coluna informada nao existe(m) no labirinto.");
        }
        Block block = this.labyrinth[i][j];
        if(block == null) {
            throw new NullPointerException("O bloco na posicao informada ainda nao foi iniciado.");
        }
        for(Block adjacent : block.possibleMovements){
            System.out.println(adjacent);
        }
    }

    // Adds edges to the graph depending on each block possible movements.
    private void addEdgesToGraph() {
        for(int i = 0; i < this.labyrinth.length; i++){
            for(int j = 0; j < this.labyrinth[0].length; j++){
                Block currentBlock = this.labyrinth[i][j];
                if(currentBlock.symbol == 'x') continue;
                int currentBlockVertex = this.vertexMap.get(currentBlock);
                for(Block adjacent : currentBlock.possibleMovements){
                    int adjacentBlockVertex = this.vertexMap.get(adjacent);
                    graph.addEdge(currentBlockVertex, adjacentBlockVertex);
                }
            }
        }
    }

    // Finds the path with the least amount of edges (steps) in the graph.
    public int findShortestPath(){
        int startBlockVertex = this.vertexMap.get(this.startBlock);
        int endBlockVertex = this.vertexMap.get(this.endBlock);
        LinkedList<Integer> path = this.graph.minEdgeBFS(startBlockVertex, endBlockVertex);
        for(Integer vertex : path){
            Block block = this.invertedVertexMap.get(vertex);
            if(block != null && block.symbol != 'C' && block.symbol != 'S') {
                block.symbol = 'O';
            }
        }
        return path.size();
    }
    
    // Prints the labyrinth on screen.
    public void printLabyrinth(){
        for(int i = 0; i < this.labyrinth.length; i++){
            for(int j = 0; j < this.labyrinth[0].length; j++){
                System.out.print(this.labyrinth[i][j].symbol);
            }
            System.out.print("\n");
        }
    }
}
