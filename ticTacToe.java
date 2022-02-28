package cis3700_a2;

import java.util.Scanner;

class ticTacToe{
    public static void main (String[] arg) {

        Scanner scanner = new Scanner(System.in);

        int gameType = 0;

        while (gameType < 1 || gameType > 4) {

            System.out.print("What type of tic-tac-toe game would you like to play? Please enter a number from 1-4.\n1 - 3x3\n2 - 4x4\n3 - 5x5\n4 - Exit\n>>> ");
            gameType = scanner.nextInt();

            int aiType = 0;
            while(aiType < 1 || aiType > 2) {

                System.out.print("What type of ai would you like to play against?Please enter a number from 1-2.\n1 - minimax\n2 - alpha-beta\n>>>");
                aiType = scanner.nextInt();

                if(aiType < 1 || aiType > 2) {

                    System.out.println("Invalid entry. Try again.");

                }

            }

            if(aiType == 1) {

                if (gameType == 1) {

                    threeByThree three = new threeByThree();
                    three.startGame();
    
                } else if (gameType == 2) {
    
                    fourByFour four = new fourByFour();
                    four.startGame();
    
                } else if (gameType == 3) {
    
                    fiveByFive five = new fiveByFive();
                    five.startGame();
    
                } else if (gameType != 4) {
    
                    System.out.println("Invalid entry. Try again.");
    
                }

            } else {

                if (gameType == 1) {

                    threeByThree three = new threeByThree(false);
                    three.startGame();
    
                } else if (gameType == 2) {
    
                    fourByFour four = new fourByFour(false);
                    four.startGame();
    
                } else if (gameType == 3) {
    
                    fiveByFive five = new fiveByFive(false);
                    five.startGame();
    
                } else if (gameType != 4) {
    
                    System.out.println("Invalid entry. Try again.");
    
                }

            }

        }

        scanner.close();

        return;
    }

}