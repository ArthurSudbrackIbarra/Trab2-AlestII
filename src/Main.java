import customexceptions.NoCharacterOrDestinationFoundException;
import customexceptions.NotEnoughLinesOrColumnsException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Labyrinth labyrinth = new Labyrinth("caso200.txt");
            int numberOfSteps = labyrinth.findShortestPath();
            if(numberOfSteps == 0){
                System.out.print("Nao ha caminho para o labirinto em questao.");
            }
            else{
                System.out.print("\n[Menor caminho]\n");
                labyrinth.printLabyrinth();
                System.out.print("\nNumero de blocos: " + numberOfSteps);
                System.out.print("\n\nAs bolinhas 'O' indicam por onde o carneirinho passou.");
            }
        }
        catch (IOException error){
            System.out.print("Erro durante a leitura do arquivo! Certifique-se de que " +
                    "\nos arquivos dos casos de teste estao presentes na raiz do projeto!");
        }
        catch (NoCharacterOrDestinationFoundException | NotEnoughLinesOrColumnsException error){
            System.out.print("Erro: " + error.getMessage());
        }
    }
}
