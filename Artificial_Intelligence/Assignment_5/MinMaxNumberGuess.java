public class MinMaxNumberGuess {
    
    private static int minMax(int low, int high, boolean isMaximizing) {
        if (low == high) return 0; 
        
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int guess = low; guess <= high; guess++) {
                int score = Math.min(
                    minMax(low, guess - 1, false),
                    minMax(guess + 1, high, false)
                );
                bestScore = Math.max(bestScore, score);
            }
            return bestScore + 1;
        } else {
            return 0;
        }
    }
    
    public static int findBestGuess(int low, int high) {
        int bestGuess = low;
        int bestScore = Integer.MIN_VALUE;
        
        for (int guess = low; guess <= high; guess++) {
            int score = Math.min(
                minMax(low, guess - 1, false),
                minMax(guess + 1, high, false)
            );
            if (score > bestScore) {
                bestScore = score;
                bestGuess = guess;
            }
        }
        return bestGuess;
    }
    
    public static void main(String[] args) {
        int low = 1, high = 100;
        System.out.println("Think of a number between " + low + " and " + high);
        System.out.println("Computer will try to guess it optimally");
        
        int firstGuess = findBestGuess(low, high);
        System.out.println("Computer's optimal first guess: " + firstGuess);
    }
}