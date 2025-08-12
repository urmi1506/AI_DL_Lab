package Assignment_2;
    import java.util.*;

public class MapColor {

    static String[] colors = {"Red", "Green", "Blue"}; 
    static Map<String, List<String>> neighbors = new HashMap<>(); 
    // Assign Color
    static Map<String, String> colorMap = new HashMap<>(); 

    // Assign colors using backtracking
    static boolean colorRegions(List<String> regions, int idx) {
        if (idx == regions.size()) return true; 

        String region = regions.get(idx);

        for (String col : colors) {
            if (isValid(region, col)) {
                colorMap.put(region, col); 
                if (colorRegions(regions, idx + 1)) return true;
                colorMap.remove(region); 
            }
        }
        return false; 
    }

    // Check if this color is valid for this region
    static boolean isValid(String region, String col) {
        for (String nb : neighbors.get(region)) {
            if (col.equals(colorMap.get(nb))) return false;
        }
        return true;
    }
    public static void main(String[] args) {
        // Define neighbours 
        neighbors.put("A", Arrays.asList("B", "C"));
        neighbors.put("B", Arrays.asList("A", "C"));
        neighbors.put("C", Arrays.asList("A", "B"));

        if (colorRegions(new ArrayList<>(neighbors.keySet()), 0)) {
            System.out.println("Solution: " + colorMap);
        } else {
            System.out.println("No solution found.");
        }
    }
}


