import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Random;

import javax.swing.JPanel;

public class MazeGridPanel extends JPanel{
    private int rows;
    private int cols;
    private Cell[][] maze;



    // extra credit
    public void genDFSMaze() {
        boolean[][] visited = new boolean[rows][cols];
        Random rand = new Random();
        Stack<Cell> stack = new Stack<>();
        Cell start = maze[0][0];
        start.setBackground(Color.orange);
        stack.push(start);

        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            int row = current.row;
            int col = current.col;
            visited[row][col] = true;

            int[] directions = {0, 1, 2, 3};
            shuffleArray(directions);

            for (int direction: directions) {
                int nextRow = row;
                int nextCol = col;
                if (direction == 0){// north
                    nextRow--;
                }else if (direction == 1){// east
                    nextCol++;
                } else if (direction == 2){ // south
                    nextRow++;
                }else if (direction == 3){// west
                    nextCol--;
                }

                if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols && !visited[nextRow][nextCol]) {
                    if (nextRow < row) {
                        maze[row][col].northWall = false;
                        maze[nextRow][nextCol].southWall = false;
                    } else if (nextRow > row) {
                        maze[nextRow][nextCol].northWall = false;
                        maze[row][col].southWall = false;
                    } else if (nextCol < col) {
                        maze[row][col].westWall = false;
                        maze[nextRow][nextCol].eastWall = false;
                    } else if (nextCol > col) {
                        maze[nextRow][nextCol].westWall = false;
                        maze[row][col].eastWall = false;
                    }

                    stack.push(maze[nextRow][nextCol]);
                    visited[nextRow][nextCol] = true;

                }
            }
        }
        maze[rows - 1][cols - 1].setBackground(Color.red);
    }

    private void shuffleArray(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }



    //homework
    public void solveMaze() {
        Stack<Cell> stack  = new Stack<Cell>();
        Cell start = maze[0][0];
        start.setBackground(Color.GREEN);
        Cell finish = maze[rows-1][cols-1];
        finish.setBackground(Color.RED);
        stack.push(start);

        boolean chickenDinner = false;

        while (!chickenDinner && !stack.isEmpty()) {
            Cell current = stack.peek();
            if (current.getBackground().equals(Color.RED)){
                chickenDinner = true;
            } else if (!current.northWall && !visited(current.row - 1, current.col)) {
                current.setBackground(Color.orange);
                stack.push(maze[current.row - 1][current.col]);
            } else if (!current.southWall && !visited(current.row + 1, current.col)) {
                current.setBackground(Color.orange);
                stack.push(maze[current.row + 1][current.col]);
            } else if (!current.eastWall && !visited(current.row, current.col + 1)) {
                current.setBackground(Color.orange);
                stack.push(maze[current.row][current.col + 1]);
            } else if (!current.westWall && !visited(current.row, current.col - 1)) {
                current.setBackground(Color.orange);
                stack.push(maze[current.row][current.col - 1]);
            } else {//deadend
                stack.pop();
                current.setBackground(Color.lightGray);
            }


        }


    }

    public boolean visited(int row, int col) {
        Cell c = maze[row][col];
        Color status = c.getBackground();
        if(status.equals(Color.WHITE)  || status.equals(Color.RED)  ) {
            return false;
        }
        return true;

    }


    public void genNWMaze() {

        for(int row = 0; row  < rows; row++) {
            for(int col = 0; col < cols; col++) {

                if(row == 0 && col ==0) {
                    continue;
                }

                else if(row == 0) {
                    maze[row][col].westWall = false;
                    maze[row][col-1].eastWall = false;
                } else if(col == 0) {
                    maze[row][col].northWall = false;
                    maze[row-1][col].southWall = false;
                }else {
                    boolean north = Math.random()  < 0.5;
                    if(north ) {
                        maze[row][col].northWall = false;
                        maze[row-1][col].southWall = false;
                    } else {  // remove west
                        maze[row][col].westWall = false;
                        maze[row][col-1].eastWall = false;
                    }
                    maze[row][col].repaint();
                }
            }
        }
        this.repaint();

    }

    public MazeGridPanel(int rows, int cols) {
        this.setPreferredSize(new Dimension(800,800));
        this.rows = rows;
        this.cols = cols;
        this.setLayout(new GridLayout(rows,cols));
        this.maze =  new Cell[rows][cols];
        for(int row = 0 ; row  < rows ; row++) {
            for(int col = 0; col < cols; col++) {

                maze[row][col] = new Cell(row,col);

                this.add(maze[row][col]);
            }

        }


        this.genDFSMaze();
        this.solveMaze();

    }




}
