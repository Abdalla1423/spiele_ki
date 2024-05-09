import java.util.ArrayList;
import java.util.List;

public class KnightMoves {
    
    public static List<String> getPossibleMoves(boolean currentPlayer, int knightPosition) {
        List<String> possibleMoves = new ArrayList<>();
        
        // Define the chess board
        String[] columns = {"A", "B", "C", "D", "E", "F", "G", "H"};
        
        // Convert knight position to coordinates
        int row = 8 - (knightPosition - 1) / 8;
        int column = (knightPosition - 1) % 8;
        
        // Define all possible moves for a knight
        int[] moveX = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] moveY = {1, 2, 2, 1, -1, -2, -2, -1};
        
        // Iterate through all possible moves
        for (int i = 0; i < moveX.length; i++) {
            int newX = column + moveX[i];
            int newY = row + moveY[i];
            
            // Check if the new position is within the board
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                // Convert coordinates to chess notation
                String start = columns[column] + row;
                String end = columns[newX] + (8 - newY);
                possibleMoves.add(start + " - " + end);
            }
        }
        
        return possibleMoves;
    }
    
    public static void main(String[] args) {
        // Example usage
        boolean currentPlayer = true; // Player 1
        int knightPosition = 28; // Example position
        
        List<String> moves = getPossibleMoves(currentPlayer, knightPosition);
        for (String move : moves) {
            System.out.println(move);
        }
    }
}
