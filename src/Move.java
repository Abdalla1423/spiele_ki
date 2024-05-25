import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Move {
    static Set<Integer> edgesRight = Set.of(7, 16, 24, 32, 40, 48, 56, 63);
    static Set<Integer> edgesLeft = Set.of(2, 9, 17, 25, 33, 41, 49, 58);
    static Set<Integer> blueDoubleMovesRight = Set.of(7, 15, 16, 23, 24, 31, 32, 39, 40, 47, 48, 54, 55, 56);
    static Set<Integer> blueDoubleMovesRightUp = Set.of(16, 24, 32, 40, 47, 48, 56);
    static Set<Integer> blueDoubleMovesLeft = Set.of(2, 9, 10, 17, 18, 25, 26, 33, 34, 41, 42, 49, 50, 51);
    static Set<Integer> blueDoubleMovesLeftUp = Set.of(9, 17, 25, 33, 41, 49, 42);
    static Set<Integer> redDoubleMovesRight = Set.of(14, 15, 16, 23, 24, 31, 32, 39, 40, 47, 48, 55, 56, 63);
    static Set<Integer> redDoubleMovesRightInner = Set.of(9, 10, 11, 12, 13, 14, 15, 16, 23, 24, 32, 40, 48, 56);
    static Set<Integer> redDoubleMovesLeft = Set.of(9, 10, 11, 17, 18, 25, 26, 33, 34, 41, 42, 49, 50, 58);
    static Set<Integer> redDoubleMovesLeftInner = Set.of(9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 25, 33, 41, 49);


    public static String pickMove(ArrayList<String> allMoves) {
        int newMove = ThreadLocalRandom.current().nextInt(0, allMoves.size());
        return allMoves.get(newMove);
    }

    public static String pickRandomMove(ArrayList<String> allMoves) {
        int newMove = ThreadLocalRandom.current().nextInt(0, allMoves.size());
        return allMoves.get(newMove);
    }
    public static ArrayList<int[]> possibleMoves(Board board) {
        ArrayList<int[]> res = new ArrayList<>();
        for (int field = 1; field < 64; field++) {
            Player getPlayerAtPos = board.getplayeratpos(field);

            if (board.blauIstDran) {
                //blau einzel figur
                if (getPlayerAtPos == Player.B) {
                    //zug nach vorne
                    if (field + 8 != 57 && field + 8 != 64 && field < 57 && (board.getplayeratpos(field + 8) == Player.EMPTY || board.getplayeratpos(field + 8) == Player.B )) {
                        res.add(new int[] {field, field + 8});
                    }
                    //nach rechts
                    if (!edgesRight.contains(field) && (board.getplayeratpos(field + 1) == Player.EMPTY || board.getplayeratpos(field + 1) == Player.B )) {
                        res.add(new int[] {field, field + 1});
                    }
                    //nach links
                    if (!edgesLeft.contains(field) && (board.getplayeratpos(field - 1) == Player.EMPTY || board.getplayeratpos(field - 1) == Player.B )) {
                        res.add(new int[] {field, field - 1});
                    }

                    //Diagonal nach links
                    if (field < 57 && field % 8 != 1 && (board.getplayeratpos(field +7) == Player.RR || board.getplayeratpos(field +7) == Player.R || board.getplayeratpos(field +7) == Player.BR)) {
                        res.add(new int[] {field, field + 7});
                    }

                    //Diagonal nach rechts
                    if (field < 56 && field % 8 != 0 && (board.getplayeratpos(field +9) == Player.RR || board.getplayeratpos(field +9) == Player.R || board.getplayeratpos(field +9) == Player.BR)) {
                        res.add(new int[] {field, field + 9});
                    }
                }

                //blau doppelt
                else if (getPlayerAtPos == Player.BB || getPlayerAtPos == Player.RB) {
                    // 2 nach rechts 1 nach oben
                    if (!blueDoubleMovesRight.contains(field) && board.getplayeratpos(field+10)!=Player.BB) {
                        res.add(new int[] {field, field + 10});
                    }
                    // 1 nach rechts 2 nach oben
                    if (!blueDoubleMovesRightUp.contains(field) && field<49 && board.getplayeratpos(field+17)!=Player.BB) {
                        res.add(new int[] {field, field + 17});
                    }
                    // 2 nach links 1 nach oben
                    if (!blueDoubleMovesLeft.contains(field) && board.getplayeratpos(field+6)!=Player.BB) {
                        res.add(new int[] {field, field + 6});
                    }
                    // 1 nach links 2 nach oben
                    if (!blueDoubleMovesLeftUp.contains(field) && field<49 && board.getplayeratpos(field+15)!=Player.BB) {
                        res.add(new int[] {field, field + 15});
                    }
                }
            }

            else {
                //rot einzel figur
                if (getPlayerAtPos == Player.R) {
                    //zug nach vorne
                    if (field > 8 && (board.getplayeratpos(field - 8) == Player.EMPTY || board.getplayeratpos(field - 8) == Player.R )) {
                        res.add(new int[] {field, field - 8});
                    }
                    //nach rechts (von blau aus gesehen)
                    if (!edgesRight.contains(field) && (board.getplayeratpos(field + 1) == Player.EMPTY || board.getplayeratpos(field + 1) == Player.R )) {
                        res.add(new int[] {field, field + 1});
                    }
                    //nach links (von blau aus gesehen)
                    if (!edgesLeft.contains(field) && (board.getplayeratpos(field - 1) == Player.EMPTY || board.getplayeratpos(field - 1) == Player.R )) {
                        res.add(new int[] {field, field - 1});
                    }

                    //Diagonal nach links (von blau aus gesehen)
                    if (field > 9 && field % 8 != 1 && (board.getplayeratpos(field - 9)==Player.BB || board.getplayeratpos(field - 9)==Player.B || board.getplayeratpos(field - 9)==Player.RB )) {
                        res.add(new int[] {field, field - 9});
                    }

                    //Diagonal nach rechts (von blau aus gesehen)
                    if (field > 8 && field % 8 != 0 && (board.getplayeratpos(field - 7)==Player.BB || board.getplayeratpos(field - 7)==Player.B || board.getplayeratpos(field - 7)==Player.RB )) {
                        res.add(new int[] {field, field - 7});
                    }
                }

                // rot doppelt
                else if (getPlayerAtPos == Player.RR || getPlayerAtPos == Player.BR) {
                    //Links außen (von Blau gesehen)
                    if (!redDoubleMovesLeft.contains(field) && (board.getplayeratpos(field - 10)!=Player.RR)) {
                        res.add(new int[] {field, field - 10});
                    }

                    //Links innen (von Blau gesehen)
                    if (!redDoubleMovesLeftInner.contains(field) && (board.getplayeratpos(field - 17)!=Player.RR)) {
                        res.add(new int[] {field, field - 17});
                    }

                    //Rechts außen (von Blau gesehen)
                    if (!redDoubleMovesRight.contains(field) && (board.getplayeratpos(field - 6)!=Player.RR)) {
                        res.add(new int[] {field, field - 6});
                    }

                    //Rechts innen (von Blau gesehen)
                    if (!redDoubleMovesRightInner.contains(field) && (board.getplayeratpos(field - 15)!=Player.RR)) {
                        res.add(new int[] {field, field - 15});
                    }
                }
            }
        }

        // Duplikate entfernen
        Set<int[]> set = new HashSet<>(res);
        res.clear();
        res.addAll(set);

        return res;
    }

    public static ArrayList<String> convertMoves(ArrayList<int[]> moves) {
        ArrayList<String> stringList = new ArrayList<>();

        for (int[] move : moves) {
            stringList.add(Board.fieldToString(move[0]) + "-" + Board.fieldToString(move[1]));
        }

        Collections.sort(stringList);
        return stringList;
    }
}

