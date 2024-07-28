import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Move {
    static Set<Integer> edgesRight = Set.of(7, 16, 24, 32, 40, 48, 56, 63); // Set of positions at the right edge of the board
    static Set<Integer> edgesLeft = Set.of(2, 9, 17, 25, 33, 41, 49, 58); // Set of positions at the left edge of the board
    static Set<Integer> blueDoubleMovesRight = Set.of(7, 15, 16, 23, 24, 31, 32, 39, 40, 47, 48, 54, 55, 56); // Set of positions for blue's double moves to the right
    static Set<Integer> blueDoubleMovesRightUp = Set.of(16, 24, 32, 40, 47, 48, 56); // Set of positions for blue's double moves to the right-up
    static Set<Integer> blueDoubleMovesLeft = Set.of(2, 9, 10, 17, 18, 25, 26, 33, 34, 41, 42, 49, 50, 51); // Set of positions for blue's double moves to the left
    static Set<Integer> blueDoubleMovesLeftUp = Set.of(9, 17, 25, 33, 41, 49, 42); // Set of positions for blue's double moves to the left-up
    static Set<Integer> redDoubleMovesRight = Set.of(14, 15, 16, 23, 24, 31, 32, 39, 40, 47, 48, 55, 56, 63); // Set of positions for red's double moves to the right
    static Set<Integer> redDoubleMovesRightInner = Set.of(9, 10, 11, 12, 13, 14, 15, 16, 23, 24, 32, 40, 48, 56); // Set of positions for red's double moves to the right-inner
    static Set<Integer> redDoubleMovesLeft = Set.of(9, 10, 11, 17, 18, 25, 26, 33, 34, 41, 42, 49, 50, 58); // Set of positions for red's double moves to the left
    static Set<Integer> redDoubleMovesLeftInner = Set.of(9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 25, 33, 41, 49); // Set of positions for red's double moves to the left-inner

    /**
     * Pick a random move from a list of all possible moves.
     *
     * @param allMoves List of all possible moves represented as arrays of two integers.
     * @return A randomly selected move from the list.
     */
    public static int[] pickRandomMove(ArrayList<int[]> allMoves) {
        int newMove = ThreadLocalRandom.current().nextInt(0, allMoves.size());
        return allMoves.get(newMove);
    }

    /**
     * Generate a list of all possible moves for the current player.
     *
     * @param board The current state of the board.
     * @return A list of all possible moves represented as arrays of two integers.
     */
    public static ArrayList<int[]> possibleMoves(Board board) {
        ArrayList<int[]> res = new ArrayList<>();
        for (int field = 1; field < 64; field++) {
            Player getPlayerAtPos = board.getplayeratpos(field);

            if (board.blauIstDran) {
                // Blue single piece
                if (getPlayerAtPos == Player.B) {
                    // Move forward
                    if (field + 8 != 57 && field + 8 != 64 && field < 57 && (board.getplayeratpos(field + 8) == Player.EMPTY || board.getplayeratpos(field + 8) == Player.B)) {
                        res.add(new int[]{field, field + 8});
                    }
                    // Move right
                    if (!edgesRight.contains(field) && (board.getplayeratpos(field + 1) == Player.EMPTY || board.getplayeratpos(field + 1) == Player.B)) {
                        res.add(new int[]{field, field + 1});
                    }
                    // Move left
                    if (!edgesLeft.contains(field) && (board.getplayeratpos(field - 1) == Player.EMPTY || board.getplayeratpos(field - 1) == Player.B)) {
                        res.add(new int[]{field, field - 1});
                    }
                    // Diagonal move left
                    if (field < 57 && field % 8 != 1 && (board.getplayeratpos(field + 7) == Player.RR || board.getplayeratpos(field + 7) == Player.R || board.getplayeratpos(field + 7) == Player.BR)) {
                        res.add(new int[]{field, field + 7});
                    }
                    // Diagonal move right
                    if (field < 56 && field % 8 != 0 && (board.getplayeratpos(field + 9) == Player.RR || board.getplayeratpos(field + 9) == Player.R || board.getplayeratpos(field + 9) == Player.BR)) {
                        res.add(new int[]{field, field + 9});
                    }
                }
                // Blue double piece
                else if (getPlayerAtPos == Player.BB || getPlayerAtPos == Player.RB) {
                    // 2 steps right, 1 step up
                    if (!blueDoubleMovesRight.contains(field) && board.getplayeratpos(field + 10) != Player.BB) {
                        res.add(new int[]{field, field + 10});
                    }
                    // 1 step right, 2 steps up
                    if (!blueDoubleMovesRightUp.contains(field) && field < 49 && board.getplayeratpos(field + 17) != Player.BB) {
                        res.add(new int[]{field, field + 17});
                    }
                    // 2 steps left, 1 step up
                    if (!blueDoubleMovesLeft.contains(field) && board.getplayeratpos(field + 6) != Player.BB) {
                        res.add(new int[]{field, field + 6});
                    }
                    // 1 step left, 2 steps up
                    if (!blueDoubleMovesLeftUp.contains(field) && field < 49 && board.getplayeratpos(field + 15) != Player.BB) {
                        res.add(new int[]{field, field + 15});
                    }
                }
            } else {
                // Red single piece
                if (getPlayerAtPos == Player.R) {
                    // Move forward
                    if (field - 8 != 1 && field - 8 != 8 && field > 8 && (board.getplayeratpos(field - 8) == Player.EMPTY || board.getplayeratpos(field - 8) == Player.R)) {
                        res.add(new int[]{field, field - 8});
                    }
                    // Move right (from blue's perspective)
                    if (!edgesRight.contains(field) && (board.getplayeratpos(field + 1) == Player.EMPTY || board.getplayeratpos(field + 1) == Player.R)) {
                        res.add(new int[]{field, field + 1});
                    }
                    // Move left (from blue's perspective)
                    if (!edgesLeft.contains(field) && (board.getplayeratpos(field - 1) == Player.EMPTY || board.getplayeratpos(field - 1) == Player.R)) {
                        res.add(new int[]{field, field - 1});
                    }
                    // Diagonal move left (from blue's perspective)
                    if (field > 9 && field % 8 != 1 && (board.getplayeratpos(field - 9) == Player.BB || board.getplayeratpos(field - 9) == Player.B || board.getplayeratpos(field - 9) == Player.RB)) {
                        res.add(new int[]{field, field - 9});
                    }
                    // Diagonal move right (from blue's perspective)
                    if (field > 8 && field % 8 != 0 && (board.getplayeratpos(field - 7) == Player.BB || board.getplayeratpos(field - 7) == Player.B || board.getplayeratpos(field - 7) == Player.RB)) {
                        res.add(new int[]{field, field - 7});
                    }
                }
                // Red double piece
                else if (getPlayerAtPos == Player.RR || getPlayerAtPos == Player.BR) {
                    // 2 steps left (from blue's perspective)
                    if (!redDoubleMovesLeft.contains(field) && (board.getplayeratpos(field - 10) != Player.RR)) {
                        res.add(new int[]{field, field - 10});
                    }
                    // 2 steps left-inner (from blue's perspective)
                    if (!redDoubleMovesLeftInner.contains(field) && (board.getplayeratpos(field - 17) != Player.RR)) {
                        res.add(new int[]{field, field - 17});
                    }
                    // 2 steps right (from blue's perspective)
                    if (!redDoubleMovesRight.contains(field) && (board.getplayeratpos(field - 6) != Player.RR)) {
                        res.add(new int[]{field, field - 6});
                    }
                    // 2 steps right-inner (from blue's perspective)
                    if (!redDoubleMovesRightInner.contains(field) && (board.getplayeratpos(field - 15) != Player.RR)) {
                        res.add(new int[]{field, field - 15});
                    }
                }
            }
        }
        return res;
    }

    /**
     * Convert a move to its string representation.
     *
     * @param move An array representing the move with two integers.
     * @return The string representation of the move.
     */
    public static String moveToString(int[] move) {
        return Board.fieldToString(move[0]) + "-" + Board.fieldToString(move[1]);
    }

    /**
     * Convert a list of moves to their string representations.
     *
     * @param moves A list of moves, each represented as an array of two integers.
     * @return A list of string representations of the moves.
     */
    public static ArrayList<String> convertMoves(ArrayList<int[]> moves) {
        ArrayList<String> stringList = new ArrayList<>();

        for (int[] move : moves) {
            stringList.add(moveToString(move));
        }

        Collections.sort(stringList);
        return stringList;
    }
}
