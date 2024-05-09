import java.util.ArrayList;
import java.util.List;

public class KnightMoves {

    public static List<String> getPossibleMoves(boolean currentPlayer, int knightPosition) {
        List<String> possibleMoves = new ArrayList<>();

        // Define the chess board
        String[] columns = {"A", "B", "C", "D", "E", "F", "G", "H"};

        // Convert knight position to coordinates
        int row = (knightPosition - 1) / 8;
        int column = (knightPosition - 1) % 8;

        // Define all possible moves for a knight
        int[] moveX = {2, 1, -1, -2};
        int[] moveY = {1, 2, 2, 1};

        // Adjust move directions based on current player
        int direction = currentPlayer ? 1 : -1;

        // Iterate through all possible moves
        for (int i = 0; i < moveX.length; i++) {
            int newX = column + moveX[i];
            int newY = row + direction * moveY[i];

            // Check if the new position is within the board and moving forward/backward
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                // Convert coordinates to chess notation
                String start = columns[column] + (row + 1);
                String end = columns[newX] + (newY + 1);
                possibleMoves.add(start + " - " + end);
            }
        }

        return possibleMoves;
    }

    public static void main(String[] args) {
        // Example usage
        boolean currentPlayer = true; // Player 2
        int knightPosition = 27; // Example position

        List<String> moves = getPossibleMoves(currentPlayer, knightPosition);
        for (String move : moves) {
            System.out.println(move);
        }
    }
}
