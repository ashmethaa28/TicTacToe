package cis3700_a2;

import java.util.Scanner;

public class board {

    private int boardSize;
    private char[][] board = new char[5][5];
    private boolean minimax;
    private long startMove;
    private long finishMove;
    private int nodeExplored;
    private int deepestDepth;

    //Constructors
    public board() {

        setSize(3);
        setSearchType(true);
        setBoard();

    }

    public board(int size, boolean value) {

        setSize(size);
        setSearchType(value);
        setBoard();

    }

    /**
     * Sets the start time of the move
     */
    public void setStartMoveTime() {

        startMove = System.currentTimeMillis();

    }

    /**
     *  Gets the starts time of the move
     * @return (long) - start time
     */
    public long getStartMoveTime() {

        return startMove;

    }

    /**
     * Sets time that move finished
     */
    public void setFinishMoveTime() {

        finishMove = System.currentTimeMillis();

    }

    /**
     * Gets time that the move finished
     * @return (long) - finish time
     */
    public long getFinishMoveTime() {

        return finishMove;

    }

    /**
     * Sets the current number of nodes explored
     * @param node - number of nodes explored
     */
    public void setNodeCount(int node) {

        nodeExplored = node;

    }

    /**
     * Adds one to the current number of nodes explored
     */
    public void addToNodeCount() {

        setNodeCount(getNodeCount() + 1);

    }

    /**
     * Gets the total number of nodes explored
     * @return - number of nodes explored
     */
    public int getNodeCount() {

        return nodeExplored;

    }

    /**
     * Sets deepest depth explored
     * @param depth - new deepest depth
     */
    public void setDeepestDepth(int depth) {

        deepestDepth = depth;

    }

    /**
     * Checks to see if new depth is deeper than current deepest depth to resets deepest depth if so
     * @param newDepth - new depth
     */
    public void validNewDepth(int newDepth) {

        if(newDepth > getDepth() && newDepth != getSize() * getSize()) {

            setDeepestDepth(newDepth);

        }

    }

    /**
     * Gets the deepest depth explored
     * @return (int) - deepest depth explored
     */
    public int getDepth() {

        return deepestDepth;

    }

    /**
     * Sets true or false to minimax search
     * @param value - true or false to use minimax search
     */
    public void setSearchType(boolean value) {

        minimax = value;

    }

    /**
     * Get search type that is being used
     * @return (boolean) - true or false whether minimax search is being used
     */
    public boolean getSearchType() {

        return minimax;
        
    }

    /**
     * Set size of the board
     * @param size - size of board
     */
    public void setSize(int size) {

        boardSize = size;

    }

    /**
     * Get size of the board
     * @return (int) - size of board
     */
    public int getSize() {

        return boardSize;

    }

    /**
     * Set empty board
     */
    public void setBoard() {

        for (int x = 0 ; x < getSize() ; x++) {

            for (int y = 0 ; y < getSize() ; y++) {

                board[x][y] = '*';

            }

        }

    }

    /**
     * Display board
     */
    public void displayBoard() {

        char[][] currentBoard = getBoard();

        for(int x = 0 ; x < getSize() ; x++) {

            for(int y = 0 ; y < getSize() ; y++) {

                if (y < getSize() - 1) {

                    System.out.print(currentBoard[x][y] + " | ");

                } else {

                    System.out.println(currentBoard[x][y]);

                }

            }

            if (x < getSize() - 1) {

                if(getSize() == 4) {

                    System.out.println("--------------");

                } else if (getSize() == 5) {

                    System.out.println("------------------");

                } else {

                    System.out.println("----------");

                }
                
            }
        }

        return;
    }

    /**
     * Get board
     * @return (char[][]) - tic tac toe board
     */
    public char[][] getBoard() {

        return board;

    }

    /**
     * check the users move is to an empty space
     * @param row - user row input
     * @param col - user column input
     * @return (boolean) - true or false based off the fact if the space is empty
     */
    public boolean checkInvalidInput(int row, int col) {

        char[][] board = getBoard();

        if(board[row - 1][col - 1] != '*') {

            System.out.println("There is already a '" + board[row - 1][col - 1] + "' character in that position. Please choose something else");
            return true;

        }

        return false;
    }

    /**
     * Get user move and check if move is valid
     */
    public void userMove() {

        Scanner scanner = new Scanner(System.in);
        boolean invalidInput = true;

        while (invalidInput) {

            int userInputRow = 0;
            int userInputCol = 0;

            while (userInputRow < 1 || userInputRow > getSize()) {

                System.out.print("\nWhat row would you like to enter a 'X'? Enter a value between 1-" + getSize() + "\n>>> ");
                userInputRow = scanner.nextInt();

                if(userInputRow < 1 || userInputRow > getSize()) {

                    System.out.println("Invalid entry. Try again.");

                }
            }

            while (userInputCol < 1 || userInputCol > getSize()) {

                System.out.print("What column would you like to enter a 'X'? Enter a value between 1-" + getSize() + "\n>>> ");
                userInputCol = scanner.nextInt();

                if(userInputCol < 1 || userInputCol > getSize()) {

                    System.out.println("Invalid entry. Try again.");

                }
            }

            invalidInput = checkInvalidInput(userInputRow, userInputCol);

            if (invalidInput) {

                userInputCol = 0;
                userInputRow = 0;

            } else {

                inputMove(userInputRow, userInputCol, 'X');

            }

        }

        return;

    }

    /**
     * Insert move onto the board
     * @param row - row of the move
     * @param col - column of the move
     * @param userMove - character that is being placed on the board
     */
    public void inputMove(int row, int col, char userMove) {

        board[row - 1][col - 1] = userMove;

        return;
    }

    /**
     * Checks for winning condition of having a full row
     * @param board - current board
     * @return (char) - character that has the winning condition
     */
    public char hasRow(char[][] board) {

        for(int i = 0 ; i < getSize() ; i++) {

            boolean completeRow = true;

            for(int x = 1 ; x < getSize() ; x++) {

                if (board[i][0] != board[i][x] || board[i][0] == '*') {

                    completeRow = false;
                    break;

                }

            }

            if(completeRow) {

                return board[i][0];

            }
        }

        return 'n';

    }

    /**
     * Checks for winning condition of having a full column
     * @param board - current board
     * @return (char) - character with the winning condition
     */
    public char hasCol(char[][] board) {

        for(int i = 0 ; i < getSize() ; i++) {

            boolean completeCol = true;

            for(int x = 1 ; x < getSize() ; x++) {

                if (board[0][i] != board[x][i] || board[0][i] == '*') {
 
                    completeCol = false;
                    break;

                }

            }

            if(completeCol) {

                return board[0][i];

            }
        }

        return 'n';
        
    }

    /**
     * Checks for winning condition of a full diagonal
     * @param board - current board
     * @return (char) - character with the winning condition
     */
    public char hasDiag(char[][] board) {

        boolean diagOne = true;
        boolean diagTwo = true;

        for(int i = 1 ; i < getSize() ; i++) {

            if((diagOne && board[0][0] != board[i][i]) || board[0][0] == '*') {

                diagOne = false;

            } 
            
            if((diagTwo && board[0][getSize() - 1] != board[i][getSize() - 1 - i]) || board[0][getSize() - 1] == '*') {

                diagTwo = false;

            }
            
        }

        if(diagOne) {

            return board[0][0];

        } else if (diagTwo) {

            return board[0][getSize() - 1];
        }

        return 'n';
        
    }
    
    public boolean isTie(char[][] board) {

        for(int x = 0 ; x < getSize() ; x++) {

            for(int y = 0 ; y < getSize() ; y++) {

                if(board[x][y] == '*') {

                    return false;

                }

            }

        }

        return true;
    }

}
