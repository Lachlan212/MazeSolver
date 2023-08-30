/*
 * Authors - Joshua Burwood and Lachlan Higgins - c3324819 & c3374994
 * MazeGenerator.java
 * Implements the main maze generator class by using a DFS random walkthrough.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class MazeGenerator{

    public static Random rnd = new Random();
    static int maxRow;
    static int maxColumn;
    public static void main(String[] args){

        // Check input is valid..
        try {
            maxRow = Integer.parseInt(args[0]);
            maxColumn = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Must input an integer.");
        }
        


        if ( maxRow < 0 || maxColumn < 0 || maxColumn * maxRow < 2){
            System.out.println("Invalid input. Input must be greater than 0 and multiply to greater than 2.");
            System.exit(1);
        }


        // Or automatically set variables
        // maxRow = 10;
        // maxColumn = 10;
        
        Node[][] maze = new Node[maxRow][maxColumn];
        for(int i=0; i< maxRow ; i++){
            for(int j=0; j<maxColumn; j++){
                maze[i][j] = new Node();
            }
        }

        int[] nextNode = {rnd.nextInt(maxRow), rnd.nextInt(maxColumn)};
        maze[nextNode[0]][nextNode[1]].visit();


        // try catch used to catch memory allocation errors.
        try {
            // recursive maze generation
            walkthrough(maze, nextNode);
            // generate start and finish tiles
            int[] start = {rnd.nextInt(maxRow), rnd.nextInt(maxColumn)};
            int[] finish = {rnd.nextInt(maxRow), rnd.nextInt(maxColumn)};

            while( start[0] == finish[0] && finish[1] == start[1]){ //ensure maze doesn't finish where it starts
                finish[0]  = rnd.nextInt(maxRow);
                finish[1] = rnd.nextInt(maxColumn);
            }

            
            if(maxColumn < 11 && maxColumn < 11){ // print conditions
                printMaze(maze, start, finish);
            }

            writeToFile(new mazeState(maze, start, finish, maxRow, maxColumn), args[2]);
            System.out.println(readFromFile(args[2]));
            System.out.print("File saved to " + args[2]);

        } catch (StackOverflowError t){
            System.out.println("Unable to allocate enough memory... Quitting.");
        }
        

        
        
    }

    public static void walkthrough(Node[][] maze, int[] startingNode){

        // visit current node
        maze[startingNode[0]][startingNode[1]].visit(); 

        int[] nextNode = pickNeighbour(maze, startingNode);

        while ( nextNode != null){

            // INDEX 0 = ROW | INDEX 1 = COLUMN
            if(nextNode[0] > startingNode[0]){ // next node is BELOW the current node
                maze[startingNode[0]][startingNode[1]].openDown();
            }
            else if(nextNode[0] < startingNode[0]){ //next node ABOVE
                maze[nextNode[0]][nextNode[1]].openDown();
            }
            else if(nextNode[1] > startingNode[1]){ //next node on the RIGHT
                maze[startingNode[0]][startingNode[1]].openRight();
            }
            else{ //next node on the LEFT
                maze[nextNode[0]][nextNode[1]].openRight();
            }

            walkthrough(maze, nextNode);

            nextNode = pickNeighbour(maze, startingNode);

        }

        //if you reach here, maze complete

    }

    // simple file saver
    public static void writeToFile(mazeState m, String filename){
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(m);

            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // simple public file reader
    public static String readFromFile(String filename){
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            mazeState m = (mazeState)ois.readObject();
            //System.out.println(m);
            ois.close();

            return m.getMazeState();

        } catch (IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return "";
    }

    // simple maze printer
    public static void printMaze(Node[][] maze, int[] start, int[] finish){
        
        // print top bar
        System.out.print("-");
        for(int i = 0; i < maxColumn; i++){
            System.out.print("---");
        }
        System.out.println();

        // iterative step
        for(int i=0; i<maxRow; i++){
            // for(int j=0; j<maxColumn; j++){
            //     System.out.print(maze[i][j]);
            // }

            // print left bar
            System.out.print("|");

            

            for(int j=0; j<maxColumn; j++){ // check right loop and fill current space

                // check start/finish flags and fill space
                if (i == start[0] && j == start[1]){
                    System.out.print("S ");
                } else if (i == finish[0] && j == finish[1]){
                    System.out.print("F ");
                } else {
                    System.out.print("  ");
                }

                if ( maze[i][j].getState() == 0 || maze[i][j].getState() == 2){
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
            }

            System.out.println();

            if ( i == maxRow - 1){ // last down's p
                break;
            }

            for(int j=0; j<maxColumn; j++){ // check down
                System.out.print("|");
                if ( maze[i][j].getState() == 0 || maze[i][j].getState() == 1){ // down is closed
                    System.out.print("--");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.print("|");

            System.out.println("");
        }

        // print bottom bar
        System.out.print("-");
        for(int i = 0; i < maxColumn; i++){
            System.out.print("---");
        }
        System.out.println();


        //System.out.println("\n\n\n");
    }

    // returns an array of available neighbours
    public static int[] pickNeighbour(Node[][] maze, int[] startingNode){

        ArrayList<int[]> unvisited = new ArrayList<>();

        if((startingNode[0]+1)<maxRow){ //check down
            if(!maze[startingNode[0]+1][startingNode[1]].isVisited()){
                int[] x = {startingNode[0]+1, startingNode[1]};
                unvisited.add(x);
            }
        }
        if((startingNode[1]+1)<maxColumn){ //check right
            if(!maze[startingNode[0]][startingNode[1]+1].isVisited()){
                int[] x = {startingNode[0], startingNode[1]+1};
                unvisited.add(x);
            }
        }
        if((startingNode[0]-1)>=0){ //check up
            if(!maze[startingNode[0]-1][startingNode[1]].isVisited()){
                int[] x = {startingNode[0]-1, startingNode[1]};
                unvisited.add(x);
            }
        }
        if((startingNode[1]-1)>=0){ //check left
            if(!maze[startingNode[0]][startingNode[1]-1].isVisited()){
                int[] x = {startingNode[0], startingNode[1]-1};
                unvisited.add(x);
            }
        }
        if(unvisited.isEmpty()){
            return null;
        }
        int next = rnd.nextInt(unvisited.size());

        return unvisited.get(next);
    }

}