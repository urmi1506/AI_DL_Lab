import java.util.*;
public class EightPuzzleDFS {
    
    static final String GOAL = "123456780";

    static int[][] moves = {
        {1, 3},        
        {0, 2, 4},     
        {1, 5},       
        {0, 4, 6},     
        {1, 3, 5, 7},  
        {2, 4, 8},     
        {3, 7},        
        {4, 6, 8},    
        {5, 7}         
    };

    static class State {
        String board;
        String path;

        State(String board, String path) {
            this.board = board;
            this.path = path;
        }
    }

    static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }

    public static String solve(String start) {
        Set<String> visited = new HashSet<>();
        State result = dfs(new State(start, start + "\n"), visited, 0, 50);  

        if (result != null) {
            return "Solved in DFS:\n" + result.path;
        }
        return "No solution found (or depth limit exceeded).";
    }

    static State dfs(State current, Set<String> visited, int depth, int maxDepth) {
        if (current.board.equals(GOAL)) {
            return current;
        }

        if (depth > maxDepth) return null;  

        visited.add(current.board);

        int zeroPos = current.board.indexOf('0');
        for (int move : moves[zeroPos]) {
            String nextBoard = swap(current.board, zeroPos, move);
            if (!visited.contains(nextBoard)) {
                State result = dfs(new State(nextBoard, current.path + nextBoard + "\n"), visited, depth + 1, maxDepth);
                if (result != null) return result;  
            }
        }

        return null;
    }

    public static void main(String[] args) {
        String start = "120453786";  
        System.out.println(solve(start));
    }
}

