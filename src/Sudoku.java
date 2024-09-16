import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.LinkedList;

public class Sudoku {

    private static int boardSize = 0;
    private static int partitionSize = 0;
    private static String fileNum = "";
    private static LinkedList<int[]> emptyLocations = new LinkedList<int[]>();

    public static void main(String[] args){
        String filename = args[0];
        File inputFile = new File(filename);

        String fileNum = "";
        for(int i = filename.lastIndexOf('\\')+1; i < filename.length(); i++){
            char ch = filename.charAt(i);
            if(Character.isDigit(ch)){
                fileNum += ch;
            }
        }

        Scanner input = null;
        int[][] vals = null;

        int temp = 0;
        int count = 0;

        try {
            input = new Scanner(inputFile);
            temp = input.nextInt();
            boardSize = temp;
            partitionSize = (int) Math.sqrt(boardSize);
            System.out.println("Boardsize: " + temp + "x" + temp);
            vals = new int[boardSize][boardSize];

            System.out.println("Input:");
            int i = 0;
            int j = 0;
            while (input.hasNext()){
                temp = input.nextInt();
                count++;
                System.out.printf("%3d", temp);
                vals[i][j] = temp;
                if (temp == 0) {
                    int[] emptyCor = {i,j};
                    emptyLocations.push(emptyCor);
                }
                j++;
                if (j == boardSize) {
                    j = 0;
                    i++;
                    System.out.println();
                }
                if (j == boardSize) {
                    break;
                }
            }
            input.close();
        } catch (FileNotFoundException exception) {
            System.out.println("Input file not found: " + filename);
            System.out.println(exception);
        }
        if (count != boardSize*boardSize) throw new RuntimeException("Incorrect number of inputs.");


        boolean solved = solve(vals);

        try{
            // Output
            File of = new File(filename.substring(0, filename.lastIndexOf('.')) + "Solution.txt");
            of.createNewFile();
            FileWriter output = new FileWriter(of);
            System.out.println("\nOutput\n");
            output.write(fileNum + "\n");

            if (!solved) {
                System.out.println("No solution found.");
                output.write(-1+"");
                output.close();
                return;
            }

            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    System.out.printf("%3d", vals[i][j]);
                    output.write(vals[i][j]+"");
                }
                System.out.println();
                output.write("\n");
            }
            output.close();
        }catch(IOException e){
            System.out.println("Error writing to file");
            System.out.println("Error: " + e.getMessage());
        }


    }

    public static boolean solve(int[][] board){
        //Check if finished
        if(emptyLocations.isEmpty()){
            return true;
        }

        int[] emptyCell = emptyLocations.pop();

        for(int i = 1; i <= boardSize; i++){
            //Evaluate if this number produces a viable step
            if(isViable(board, emptyCell, i)){
                //Increment depth by 1 decision
                board[emptyCell[0]][emptyCell[1]] = i;
                //Recurse
                if (solve(board)) {
                    return true;
                }
                board[emptyCell[0]][emptyCell[1]] = 0;
            }
        }

        //Getting down here means there is no solution (darn)
        emptyLocations.push(emptyCell);
        return false;
    }

    public static boolean isViable(int[][] board, int[] emptyCell, int valToTest){
        int row = emptyCell[0];
        int col = emptyCell[1];

        for(int i = 0; i < boardSize; i++) {
            //Check the row
            if (board[row][i] == valToTest) {
                return false;
            }
            //Check the column
            if (board[i][col] == valToTest) {
                return false;
            }
        }

        //Check the box
        int startRow = row - (row % partitionSize);
        int startCol = col - (col % partitionSize);
        for(int j = 0; j < partitionSize; j++){
            for(int k = 0; k < partitionSize; k++){
                if(board[startRow+j][startCol+k] == valToTest && !(startRow == row && startCol == col)){
                    return false;
                }
            }
        }

        return true;
    }

}