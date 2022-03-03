package cis3700_a2;

public class fourByFour extends board {

    //Constructors
    public fourByFour() {

        super(4, true);

    }

    public fourByFour(boolean value) {
        
        super(4, value);

    }

    /**
     * Starts the a four by four tic tac toe game
     */
    public void startGame() {

        for(int i = 0 ; i < 16 ; i++) {

            displayBoard();
            
            if(i % 2 == 0) {

                userMove();

                System.out.println("\n_________________________________________________________________________________");
                System.out.println("USER MOVE");

                if(gameWon(getBoard(), true)) {

                    break;

                }

            } else {

                setNodeCount(0);
                setDeepestDepth(0);
                setStartMoveTime();

                if(getSearchType()) {

                    minimaxSearch();

                } else {

                    alphaBetaSearch();

                }

                setFinishMoveTime();

                System.out.println("\n_________________________________________________________________________________");
                System.out.println("AI MOVE\t\tTime taken for move: " + (getFinishMoveTime() - getStartMoveTime()) + "ms\tDepth of search: " + getDepth() + "\tNumber of nodes explored: " + getNodeCount());

                if(gameWon(getBoard(), true)) {

                    break;

                }

            }

        }

        return;

    }

    /**
     * Checks to see if there is a tie or someone won
     * @param board
     * @param print
     * @return (boolean) true or false value if there was a tie or someone won the game
     */
    public boolean gameWon(char[][] board, boolean print) {

        if(hasRow(board) == 'X' || hasCol(board) == 'X' || hasDiag(board) == 'X' || hasSquare(board) == 'X' || hasDiamond(board) == 'X') {

            if(print) {

                displayBoard();
                System.out.println("\n\nUSER WON!!!");

            }

            return true;

        } else if(hasRow(board) == 'O' || hasCol(board) == 'O' || hasDiag(board) == 'O' || hasSquare(board) == 'O' || hasDiamond(board) == 'O') {

            if(print) {

                displayBoard();
                System.out.println("\n\nAI WON!!!");

            }

            return true;

        } else if (isTie(board)) {

            if(print) {

                displayBoard();
                System.out.println("\n\nIT'S A TIE!!!");

            }

            return true;

        }

        return false;

    }

    /**
     * Searches for the best move based of the number of moves it takes and whether the ai wins using minimax
     */
    public void minimaxSearch() {

        int[] move = new int[2];
        move[0] = -1;
        move[1] = -1;
        char board[][] = getBoard();
        double bestScore = Double.NEGATIVE_INFINITY;
        double bestDepth = Double.POSITIVE_INFINITY;
        long startTime = System.currentTimeMillis();

        // checks every possible out come
        for(int x = 0 ; x < getSize() ; x++) {

            for(int y = 0 ; y < getSize() ; y++) {

                if(board[x][y] == '*') {

                    board[x][y] = 'O';

                    int status[] = minValue(board, 1, startTime);
                    addToNodeCount();

                    board[x][y] = '*';

                    // saves the move that has a better result
                    if(status[0] > bestScore) {

                        bestScore = status[0];
                        bestDepth = status[1];
                        move[0] = x;
                        move[1] = y;
                    
                    // saves the move that takes less moves for the same result
                    } else if(status[0] == bestScore && bestDepth > status[1]) {

                        bestDepth = status[1];
                        move[0] = x;
                        move[1] = y;

                    }

                    validNewDepth(status[1]);

                }

            }

        }

        // sets the ai move for this turn
        inputMove(move[0] + 1, move[1] + 1, 'O');

        return;

    }

    /**
     * finds the best move for the user based off the board provided
     * @param board - current tic tac toe board
     * @param depth - depth in the tree
     * @param startTime - when the ai started to make a move
     * @return (int[]) - best score and best depth
     */
    public int[] minValue(char[][] board, int depth, long startTime) {

        int[] status = new int[2];

        //checks to see if there is already a winning condition on the board
        if(gameWon(board, false)) {

            status[1] = depth;
            status[0] = 1;
            return status;

        //checks to see if the board provided is in a tie
        } else if (isTie(board)) {

            status[0] = 0;
            status[1] = depth;
            return status;

        // checks if the ai is taking longer than a minute to complete a move
        } else  if(System.currentTimeMillis() - startTime >= 60000) {

            status[0] = 0;
            status[1] = getSize() * getSize();
            validNewDepth(depth);
            return status;

        }

        double bestScore = Double.POSITIVE_INFINITY;
        double bestDepth = Double.POSITIVE_INFINITY;

        // checks every possible out come
        for(int x = 0 ; x < getSize() ; x++) {

            for(int y = 0 ; y < getSize() ; y++) {

                if(board[x][y] == '*') {

                    board[x][y] = 'X';

                    status = maxValue(board, depth + 1, startTime);
                    addToNodeCount();

                    board[x][y] = '*';

                    // saves the move that has a better result for the user
                    if(status[0] < bestScore) {

                        bestScore = status[0];
                        bestDepth = status[1];

                    // saves the move that has the same result but smaller amount of moves to get there
                    } else if(status[0] == bestScore && bestDepth > status[1]) {

                        bestDepth = status[1];

                    }

                }

            }

        }

        status[0] = (int)bestScore;
        status[1] = (int)bestDepth;

        return status;

    }

    /**
     * finds the best move for the ai based off the board provided
     * @param board - current tic tac toe board
     * @param depth - depth in the tree
     * @param startTime - when the ai started to make a move
     * @return (int[]) - best score and best depth
     */
    public int[] maxValue(char[][] board, int depth, long startTime) {

        int[] status = new int[2];

        //checks to see if there is already a winning condition on the board
        if(gameWon(board, false)) {

            status[1] = depth;
            status[0] = -1;
            return status;
        
        //checks to see if the board provided is in a tie
        } else if (isTie(board)) {

            status[0] = 0;
            status[1] = depth;
            return status;

        // checks if the ai is taking longer than a minute to complete a move
        } else if(System.currentTimeMillis() - startTime >= 60000) {

            status[0] = 0;
            status[1] = getSize() * getSize();
            validNewDepth(depth);
            return status;

        }

        double bestScore = Double.NEGATIVE_INFINITY;
        double bestDepth = Double.POSITIVE_INFINITY;

        // checks every possible out come
        for(int x = 0 ; x < getSize() ; x++) {

            for(int y = 0 ; y < getSize() ; y++) {

                if(board[x][y] == '*') {

                    board[x][y] = 'O';

                    status = minValue(board, depth + 1, startTime);
                    addToNodeCount();

                    board[x][y] = '*';

                    // saves the move that has a better result for the ai
                    if(status[0] > bestScore) {

                        bestScore = status[0];
                        bestDepth = status[1];
                    
                    // saves the move that has the same result but smaller amount of moves to get there
                    } else if(status[0] == bestScore && bestDepth > status[1]) {

                        bestDepth = status[1];

                    }

                }

            }

        }

        status[0] = (int)bestScore;
        status[1] = (int)bestDepth;

        return status;
        
    }

    /**
     * Checks to see if the winning condition for a square shape is on the board
     * @param board - current board
     * @return (char) - character that has the winning condition
     */
    public char hasSquare(char[][] board) {

        for(int x = 0 ; x < getSize() - 1 ; x++) {

            for(int y = 0 ; y < getSize() - 1 ; y++) {

                if(board[x][y] != '*') {

                    if(board[x][y] == board[x + 1][y] && board[x][y] == board[x + 1][y + 1] && board[x][y] == board[x][y + 1]) {

                        return board[x][y];

                    }

                }

            }

        }

        return 'n';
    }

    /**
     * Checks to see if the winning condition for a square shape is on the board
     * @param board - current board
     * @return (char) - character that has the winning condition
     */
    public char hasDiamond(char[][] board) {

        for(int x = 1 ; x < getSize() - 1 ; x++) {

            for(int y = 0 ; y < getSize() - 2 ; y++) {

                if(board[x][y] != '*') {

                    if(board[x][y] == board[x - 1][y + 1] && board[x][y] == board[x + 1][y + 1] && board[x][y] == board[x][y + 2]) {

                        return board[x][y];

                    }
                    
                }

            }

        }

        return 'n';
    }

    /**
     * Searches for the best move based of the number of moves it takes and whether the ai wins using alpha beta
     */
    public void alphaBetaSearch() {

        int[] move = new int[2];
        move[0] = -1;
        move[1] = -1;
        char board[][] = getBoard();
        double bestScore = Double.NEGATIVE_INFINITY;
        double bestDepth = Double.POSITIVE_INFINITY;
        long startTime = System.currentTimeMillis();

        for(int x = 0 ; x < getSize() ; x++) {

            for(int y = 0 ; y < getSize() ; y++) {

                if(board[x][y] == '*') {

                    board[x][y] = 'O';

                    int status[] = alphaBetaMin(board, 1, startTime, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                    addToNodeCount();

                    board[x][y] = '*';

                    if(status[0] > bestScore) {

                        bestScore = status[0];
                        bestDepth = status[1];
                        move[0] = x;
                        move[1] = y;
                        
                    } else if(status[0] == bestScore && bestDepth > status[1]) {

                        bestDepth = status[1];
                        move[0] = x;
                        move[1] = y;

                    }

                    validNewDepth(status[1]);

                }

            }

        }

        inputMove(move[0] + 1, move[1] + 1, 'O');

        return;

        

    }

    /**
     * Finds the best move for the ai
     * @param board - current board
     * @param depth - current depth in the tree
     * @param startTime - start time for when the ai started to calculate it's move
     * @param alpha - highest value maximizer found
     * @param beta - lowest value minimizer found
     * @return (int[]) - best result and best depth
     */
    public int[] alphaBetaMax(char[][] board, int depth, long startTime, double alpha, double beta) {

        int[] status = new int[2];

        if(gameWon(board, false)) {

            status[1] = depth;
            status[0] = -1;
            return status;

        } else if (isTie(board)) {

            status[0] = 0;
            status[1] = depth;
            return status;

        } else if(System.currentTimeMillis() - startTime >= 60000) {

            status[0] = 0;
            status[1] = getSize() * getSize();
            validNewDepth(depth);
            return status;

        }

        double bestScore = Double.NEGATIVE_INFINITY;
        double bestDepth = Double.POSITIVE_INFINITY;

        for(int x = 0 ; x < getSize() ; x++) {

            for(int y = 0 ; y < getSize() ; y++) {

                if(board[x][y] == '*') {

                    board[x][y] = 'O';

                    status = alphaBetaMin(board, depth + 1, startTime, alpha, beta);
                    addToNodeCount();

                    board[x][y] = '*';

                    if(status[0] > bestScore) {

                        bestScore = status[0];
                        bestDepth = status[1];

                        if(alpha < bestScore) {

                            alpha = bestScore;

                        }
                        
                    } else if(status[0] == bestScore && bestDepth > status[1]) {

                        bestDepth = status[1];

                    }

                    if(bestScore >= beta) {
                        
                        status[0] = (int)bestScore;
                        status[1] = (int)bestDepth;

                        return status;

                    }

                }

            }

        }

        status[0] = (int)bestScore;
        status[1] = (int)bestDepth;

        return status;

    }

    /**
     * Finds the best move for the user
     * @param board - current board
     * @param depth - current depth in the tree
     * @param startTime - start time for when the ai started to calculate it's move
     * @param alpha - highest value maximizer found
     * @param beta - lowest value minimizer found
     * @return (int[]) - best result and best depth
     */
    public int[] alphaBetaMin(char[][] board, int depth, long startTime, double alpha, double beta) {
        
        int[] status = new int[2];

        if(gameWon(board, false)) {

            status[1] = depth;
            status[0] = 1;
            return status;

        } else if (isTie(board)) {

            status[0] = 0;
            status[1] = depth;
            return status;

        } else if(System.currentTimeMillis() - startTime >= 60000) {

            status[0] = 0;
            status[1] = getSize() * getSize();
            validNewDepth(depth);
            return status;

        }

        double bestScore = Double.POSITIVE_INFINITY;
        double bestDepth = Double.POSITIVE_INFINITY;

        for(int x = 0 ; x < getSize() ; x++) {

            for(int y = 0 ; y < getSize() ; y++) {

                if(board[x][y] == '*') {

                    board[x][y] = 'X';

                    status = alphaBetaMax(board, depth + 1, startTime, alpha, beta);
                    addToNodeCount();

                    board[x][y] = '*';

                    if(status[0] < bestScore) {

                        bestScore = status[0];
                        bestDepth = status[1];

                        if(beta > bestScore) {

                            beta = bestScore;

                        }
                        
                    } else if(status[0] == bestScore && bestDepth > status[1]) {

                        bestDepth = status[1];

                    }

                    if(bestScore <= alpha) {

                        status[0] = (int)bestScore;
                        status[1] = (int)bestDepth;

                        return status;


                    }

                }

            }

        }

        status[0] = (int)bestScore;
        status[1] = (int)bestDepth;

        return status;

    }
    
}
