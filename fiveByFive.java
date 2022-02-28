package cis3700_a2;

public class fiveByFive extends board {

    public fiveByFive() {

        super(5, true);

    }

    public fiveByFive(boolean value) {

        super(5, value);

    }

    public void startGame() {

        for(int i = 0 ; i < 25 ; i++) {

            displayBoard();
            
            if(i % 2 == 0) {

                setStartMoveTime();
                userMove();
                setFinishMoveTime();

                System.out.println("\n_________________________________________________________________________________");
                System.out.println("USER MOVE\tTime taken for move: " + (getFinishMoveTime() - getStartMoveTime()) +"ms");

                if(gameWon(getBoard(), true)) {

                    break;

                }

            } else {

                setDeepestDepth(0);
                setNodeCount(0);
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

    public boolean gameWon(char[][] board, boolean print) {

        if(hasRow(board) == 'X' || hasCol(board) == 'X' || hasDiag(board) == 'X' || hasPlus(board) == 'X' || hasL(board) == 'X') {

            if(print) {

                displayBoard();
                System.out.println("\n\nUSER WON!!!");

            }

            return true;

        } else if(hasRow(board) == 'O' || hasCol(board) == 'O' || hasDiag(board) == 'O' || hasPlus(board) == 'O' || hasL(board) == 'O') {

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

    public void minimaxSearch() {

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

                    int status[] = minValue(board, 1, startTime);
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

    public int[] minValue(char[][] board, int depth, long startTime) {

        int[] status = new int[2];

        if (isTie(board)) {

            status[0] = 0;
            status[1] = depth;
            return status;

        } else if(gameWon(board, false)) {

            status[1] = depth;
            status[0] = 1;
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

                    status = maxValue(board, depth + 1, startTime);
                    addToNodeCount();

                    board[x][y] = '*';

                    if(status[0] < bestScore) {

                        bestScore = status[0];
                        bestDepth = status[1];
                        
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

    public int[] maxValue(char[][] board, int depth, long startTime) {

        int[] status = new int[2];

        if (isTie(board)) {

            status[0] = 0;
            status[1] = depth;
            return status;

        } else if(gameWon(board, false)) {

            status[1] = depth;
            status[0] = -1;
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

                    status = minValue(board, depth + 1, startTime);
                    addToNodeCount();

                    board[x][y] = '*';

                    if(status[0] > bestScore) {

                        bestScore = status[0];
                        bestDepth = status[1];
                        
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

    public char hasPlus(char[][] board) {

        for(int x = 1 ; x < getSize() - 2 ; x++) {

            for(int y = 0 ; y < getSize() - 2 ; y++) {

                if(board[x][y] != '*') {

                    if(board[x][y] == board[x - 1][y + 1] && board[x][y] == board[x + 1][y + 1] && board[x][y] == board[x][y + 2] && board[x][y] == board[x][y + 1]) {

                        return board[x][y];

                    }
                    
                }

            }

        }

        return 'n';
    }

    public char hasL(char[][] board) {

        for(int x = 0 ; x < getSize() - 2 ; x++) {

            for(int y = 0 ; y < getSize() - 2 ; y++) {

                if(board[x][y] != '*') {

                    if(board[x][y] == board[x][y + 1] && board[x][y] == board[x][y + 2] && board[x][y] == board[x + 1][y] && board[x][y] == board[x + 2][y]) {

                        return board[x][y];

                    }
                    
                    if(board[x][y] == board[x + 2][y + 1] && board[x][y] == board[x + 2][y + 2] && board[x][y] == board[x + 1][y] && board[x][y] == board[x + 2][y]) {

                        return board[x][y];

                    }

                    if(board[x][y] == board[x][y + 1] && board[x][y] == board[x][y + 2] && board[x][y] == board[x + 2][y + 2] && board[x][y] == board[x + 1][y + 2]) {

                        return board[x][y];

                    }

                } 
                
                if(board[x + 2][y + 2] != '*') {

                    if(board[x + 2][y + 2] == board[x + 2][y] && board[x + 2][y + 2]== board[x + 2][y + 1] && board[x + 2][y + 2] == board[x + 1][y + 2] && board[x + 2][y + 2] == board[x][y + 2]) {

                        return board[x + 2][y + 2];
                        
                    }

                }

            }
        }

        return 'n';
    }

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

    public int[] alphaBetaMax(char[][] board, int depth, long startTime, double alpha, double beta) {

        int[] status = new int[2];

        if (isTie(board)) {

            status[0] = 0;
            status[1] = depth;
            return status;

        } else if(gameWon(board, false)) {

            status[1] = depth;
            status[0] = -1;
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

    public int[] alphaBetaMin(char[][] board, int depth, long startTime, double alpha, double beta) {
        
        int[] status = new int[2];

        if (isTie(board)) {

            status[0] = 0;
            status[1] = depth;
            return status;

        } else if(gameWon(board, false)) {

            status[1] = depth;
            status[0] = 1;
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
