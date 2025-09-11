public class EightQueens {
    
    static int[] queens = new int[8]; 
    
    static boolean isSafe(int row, int col) {
        for (int i = 0; i < row; i++) {
            if (queens[i] == col || Math.abs(queens[i] - col) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }
    
    static boolean solve(int row) {
        if (row == 8) return true; 
        
        for (int col = 0; col < 8; col++) {
            if (isSafe(row, col)) {
                queens[row] = col; 
                if (solve(row + 1)) {
                    return true;
                }
                
                queens[row] = 0;
            }
        }
        return false; 
    }
    
    static void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(queens[i] == j ? "Q " : ". ");
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        if (solve(0)) {
            System.out.println("8-Queens Solution Found:");
            printBoard();
        } else {
            System.out.println("No solution exists");
        }
    }
}