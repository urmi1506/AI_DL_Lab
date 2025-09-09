package Assignment_4;

import java.util.*;

/**
 * A* Pathfinding Algorithm Implementation
 * Time Complexity: O(b^d) where b is branching factor, d is depth
 * Space Complexity: O(b^d)
 */
public class AStarAlgo {
    
    static class Node implements Comparable<Node> {
        final int x, y;
        int gCost, hCost, fCost;
        Node parent;
        
        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        void calculateHeuristic(Node target) {
            this.hCost = Math.abs(x - target.x) + Math.abs(y - target.y);
            this.fCost = gCost + hCost;
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fCost, other.fCost);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
        
        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }
    
    public static List<Node> findOptimalPath(int[][] grid, Node start, Node target) {
        validateInput(grid, start, target);
        
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();
        
        start.gCost = 0;
        start.calculateHeuristic(target);
        openSet.add(start);
        
        int nodesExplored = 0;
        
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            nodesExplored++;
            
            if (current.equals(target)) {
                System.out.printf("Path found. Nodes explored: %d%n", nodesExplored);
                return reconstructPath(current);
            }
            
            closedSet.add(current);
            
            for (Node neighbor : getNeighbors(grid, current)) {
                if (closedSet.contains(neighbor)) continue;
                
                int tentativeGCost = current.gCost + 1;
                
                if (!openSet.contains(neighbor) || tentativeGCost < neighbor.gCost) {
                    neighbor.parent = current;
                    neighbor.gCost = tentativeGCost;
                    neighbor.calculateHeuristic(target);
                    
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        
        System.out.println("No valid path exists");
        return Collections.emptyList();
    }
    
    private static void validateInput(int[][] grid, Node start, Node target) {
        if (grid == null || grid.length == 0) {
            throw new IllegalArgumentException("Grid cannot be null or empty");
        }
        if (!isValidNode(grid, start) || grid[start.x][start.y] == 1) {
            throw new IllegalArgumentException("Invalid start node");
        }
        if (!isValidNode(grid, target) || grid[target.x][target.y] == 1) {
            throw new IllegalArgumentException("Invalid target node");
        }
    }
    
    private static boolean isValidNode(int[][] grid, Node node) {
        return node != null && 
               node.x >= 0 && node.x < grid.length && 
               node.y >= 0 && node.y < grid[0].length;
    }
    
    private static List<Node> getNeighbors(int[][] grid, Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] dir : directions) {
            int newX = node.x + dir[0];
            int newY = node.y + dir[1];
            
            if (newX >= 0 && newX < grid.length && 
                newY >= 0 && newY < grid[0].length && 
                grid[newX][newY] == 0) {
                neighbors.add(new Node(newX, newY));
            }
        }
        return neighbors;
    }
    
    private static List<Node> reconstructPath(Node target) {
        LinkedList<Node> path = new LinkedList<>();
        Node current = target;
        
        while (current != null) {
            path.addFirst(current);
            current = current.parent;
        }
        return path;
    }
    
    public static void displayGridWithPath(int[][] grid, List<Node> path) {
        System.out.println("\nGRID VISUALIZATION:");
        System.out.println(". = Free space");
        System.out.println("# = Obstacle");
        System.out.println("S = Start");
        System.out.println("E = End");
        System.out.println("* = Path");
        System.out.println();
        
        // Create display matrix
        char[][] display = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                display[i][j] = grid[i][j] == 1 ? '#' : '.';
            }
        }
        
        // Mark path (excluding start and end)
        for (int i = 1; i < path.size() - 1; i++) {
            Node node = path.get(i);
            display[node.x][node.y] = '*';
        }
        
        // Mark start and end
        if (!path.isEmpty()) {
            Node start = path.get(0);
            Node end = path.get(path.size() - 1);
            display[start.x][start.y] = 'S';
            display[end.x][end.y] = 'E';
        }
        
        // Print grid with coordinates
        System.out.print("    ");
        for (int j = 0; j < grid[0].length; j++) {
            System.out.printf("%2d ", j);
        }
        System.out.println();
        
        System.out.print("    ");
        for (int j = 0; j < grid[0].length; j++) {
            System.out.print("---");
        }
        System.out.println();
        
        for (int i = 0; i < grid.length; i++) {
            System.out.printf("%2d | ", i);
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(display[i][j] + "  ");
            }
            System.out.println();
        }
    }
    
    public static void printPathAnalysis(List<Node> path) {
        if (path.isEmpty()) return;
        
        System.out.println("\nPATH ANALYSIS:");
        System.out.println("==============");
        
        Node start = path.get(0);
        Node end = path.get(path.size() - 1);
        int manhattanDistance = Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
        
        System.out.printf("Path length: %d steps%n", path.size() - 1);
        System.out.printf("Optimal distance: %d%n", manhattanDistance);
        System.out.printf("Efficiency ratio: %.1f%%%n", 
                         (path.size() - 1) * 100.0 / manhattanDistance);
        System.out.printf("Start: %s%n", start);
        System.out.printf("End: %s%n", end);
        
        System.out.println("\nPATH COORDINATES:");
        for (int i = 0; i < path.size(); i++) {
            if (i == 0) System.out.printf("START -> %s%n", path.get(i));
            else if (i == path.size() - 1) System.out.printf("END   -> %s%n", path.get(i));
            else System.out.printf("Step %2d -> %s%n", i, path.get(i));
        }
    }
    
    public static void main(String[] args) {
        // Test grid
        int[][] grid = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 1, 1, 0},
            {0, 1, 0, 0, 0, 1, 0},
            {0, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0}
        };
        
        Node start = new Node(0, 0);
        Node target = new Node(6, 6);
        
        System.out.println("A* PATHFINDING ALGORITHM");
        System.out.println("========================");
        
        long startTime = System.currentTimeMillis();
        List<Node> path = findOptimalPath(grid, start, target);
        long duration = System.currentTimeMillis() - startTime;
        
        if (!path.isEmpty()) {
            displayGridWithPath(grid, path);
            printPathAnalysis(path);
            System.out.printf("%nExecution time: %d ms%n", duration);
        }
    }
}
