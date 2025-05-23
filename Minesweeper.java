/**
 *  Daniel Liao - 2025
 */

import java.util.Random;

public class Minesweeper {

    private int[][] board;
    private boolean boardCreated = false;
    private int boardWidth = 16, boardHeight = 16;
    private int numMines = 40;
    
    public Minesweeper(){
        Window window = new Window(this);
    }

    // safeY(row) and safeX(col) are the coords of the first click
    public void generateBoard(int safeY, int safeX){
        board = new int[boardWidth][boardHeight];

        Random random = new Random();

        int count = 0;
        
        // Populate the board randomly with numMines number of mines
        while (count < numMines){
            int x = random.nextInt(boardWidth);
            int y = random.nextInt(boardHeight);

            // Bombs can't be within 3x3 surrounding area of first click
            if (Math.abs(x - safeX) > 1 || Math.abs(y - safeY) > 1){
                if (board[y][x] != -1){
                    board[y][x] = -1;  // -1 used to represent bombs
                    count++;
                }
            }
        }

        // Modify board cells to match number of neighboring bombs
        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[row].length; col++){
                if (board[row][col] != -1){
                    board[row][col] = numNeighborMines(row, col);
                }
            }
        }

        boardCreated = true;
    }

    public int numNeighborMines(int y, int x){
        int count = 0;

        if (y > 0){
            // Check N cell
            if (board[y-1][x] == -1){
                count++;
            }
            if (x > 0){
                // Check NW cell
                if (board[y-1][x-1] == -1){
                    count++;
                }
            }
            if (x < board[0].length - 1){
                // Check NE cell
                if (board[y-1][x+1] == -1){
                    count++;
                }
            }
        }

        if (y < board.length - 1){
            // Check S cell
            if (board[y+1][x] == -1){
                count++;
            }
            if (x > 0){
                // Check SW cell
                if (board[y+1][x-1] == -1){
                    count++;
                }
            }
            if (x < board[0].length - 1){
                // Check SE cell
                if (board[y+1][x+1] == -1){
                    count++;
                }
            }
        }

        if (x > 0){
            // Check W cell
            if (board[y][x-1] == -1){
                count++;
            }
        }
        if (x < board[0].length - 1){
            // Check E cell
            if (board[y][x+1] == -1){
                count++;
            }
        }
        
        return count;
    }

    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper();
    }

    public boolean boardCreated(){
        return boardCreated;
    }

    public void setBoardCreated(boolean boardCreated){
        this.boardCreated = boardCreated;
    }

    public int getCellValue(int row, int col){
        return board[row][col];
    }

    public int getNumMines(){
        return numMines;
    }

    public int getBoardWidth(){
        return boardWidth;
    }

    public int getBoardHeight(){
        return boardHeight;
    }
}
