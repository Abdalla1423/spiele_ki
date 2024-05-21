
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Move {

    public static String pickMove(ArrayList<String> allMoves) {
        int newMove = ThreadLocalRandom.current().nextInt(0, allMoves.size());
        return allMoves.get(newMove);
    }

    public static ArrayList<String> possiblemoves(Board board) {
        ArrayList<String> res = new ArrayList<>();
        for (int field = 1; field < 64; field++) {
            //blau einzel figur
            if (board.blauIstDran && board.getplayeratpos(field)==Player.B) {
                //zug nach vorne
                if ((field + 8) != 57 && (field + 8) != 64 && field<57 && (board.getplayeratpos(field + 8) == Player.EMPTY || board.getplayeratpos(field + 8) == Player.B )) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field+8);
                    res.add(cur + "-" + to);
                }
                //nach rechts
                if (!Arrays.asList(7, 16, 24, 32, 40, 48, 56, 63).contains(field) && (board.getplayeratpos(field + 1) == Player.EMPTY || board.getplayeratpos(field + 1) == Player.B )) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field+1);
                    res.add(cur + "-" + to);
                }
                //nach links
                if (!Arrays.asList(2, 9, 17, 25, 33, 41, 49, 58).contains(field) && (board.getplayeratpos(field - 1) == Player.EMPTY || board.getplayeratpos(field - 1) == Player.B )) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field-1);
                    res.add(cur + "-" + to);
                }

                //Diagonal nach links
                if (!Arrays.asList(9, 10, 17, 25, 33, 41, 49, 50).contains(field) && (board.getplayeratpos(field +7) == Player.RR || board.getplayeratpos(field +7) == Player.R || board.getplayeratpos(field +7) == Player.BR)) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field+7);
                    res.add(cur + "-" + to);
                }

                //Diagonal nach rechts
                if (!Arrays.asList(16, 24, 32, 40, 48, 56, 55).contains(field) && (board.getplayeratpos(field +9) == Player.RR || board.getplayeratpos(field +9) == Player.R || board.getplayeratpos(field +9) == Player.BR)) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field+9);
                    res.add(cur + "-" + to);
                }
            }

            //blau doppelt
            if (board.blauIstDran && (board.getplayeratpos(field)==Player.BB || board.getplayeratpos(field)==Player.RB)) {
                // 2 nach rechts 1 nach oben
                if (!Arrays.asList(7, 15, 16, 23, 24, 31, 32, 39, 40, 47, 48, 54, 55, 56).contains(field) && board.getplayeratpos(field+10)!=Player.BB) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field+10);
                    res.add(cur + "-" + to);
                }
                // 1 nach rechts 2 nach oben
                if (!Arrays.asList(16, 24, 32, 40, 47, 48, 56).contains(field) && field<49 && board.getplayeratpos(field+17)!=Player.BB) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field+17);
                    res.add(cur + "-" + to);
                }
                // 2 nach links 1 nach oben
                if (!Arrays.asList(2, 9, 10, 17, 18, 25, 26, 33, 34, 41, 42, 49, 50, 51).contains(field) && board.getplayeratpos(field+6)!=Player.BB) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field+6);
                    res.add(cur + "-" + to);
                }
                // 1 nach links 2 nach oben
                if (!Arrays.asList(9, 17, 25, 33, 41, 49, 42).contains(field) && field<49 && board.getplayeratpos(field+15)!=Player.BB) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field+15);
                    res.add(cur + "-" + to);
                }
            }

            //rot einzel figur
            if (!board.blauIstDran && board.getplayeratpos(field)==Player.R) {
                //zug nach vorne
                if ((field-8)>1 && (field-8)!=8 && (board.getplayeratpos(field - 8) == Player.EMPTY || board.getplayeratpos(field - 8) == Player.R )) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field-8);
                    res.add(cur + "-" + to);
                }
                //nach rechts (von blau aus gesehen)
                if (!Arrays.asList(7, 16, 24, 32, 40, 48, 56, 63).contains(field) && (board.getplayeratpos(field + 1) == Player.EMPTY || board.getplayeratpos(field + 1) == Player.R )) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field+1);
                    res.add(cur + "-" + to);
                }
                //nach links (von blau aus gesehen)
                if (!Arrays.asList(2, 9, 17, 25, 33, 41, 49, 58).contains(field) && (board.getplayeratpos(field - 1) == Player.EMPTY || board.getplayeratpos(field - 1) == Player.R )) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field-1);
                    res.add(cur + "-" + to);
                }

                //Diagonal nach links (von blau aus gesehen)
                if (!Arrays.asList(9, 17, 25, 33, 41, 49, 10).contains(field) && (board.getplayeratpos(field - 9)==Player.BB || board.getplayeratpos(field - 9)==Player.B || board.getplayeratpos(field - 9)==Player.RB )) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field-9);
                    res.add(cur + "-" + to);
                }

                //Diagonal nach rechts (von blau aus gesehen)
                if (!Arrays.asList(16, 24, 32, 40, 48, 56, 15).contains(field) && (board.getplayeratpos(field - 7)==Player.BB || board.getplayeratpos(field - 7)==Player.B || board.getplayeratpos(field - 7)==Player.RB )) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field-7);
                    res.add(cur + "-" + to);
                }
            }

            // rot doppelt
            if (!board.blauIstDran && board.getplayeratpos(field)==Player.RR || board.getplayeratpos(field)==Player.BR) {
                //Links außen (von Blau gesehen)
                if (!Arrays.asList(9, 10, 11, 17, 18, 25, 26, 33, 34, 41, 42, 49, 50, 58).contains(field) && (board.getplayeratpos(field - 10)!=Player.RR)) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field-10);
                    res.add(cur + "-" + to);
                }

                //Links innen (von Blau gesehen)
                if (!Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 25, 33, 41, 49).contains(field) && (board.getplayeratpos(field - 17)!=Player.RR)) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field-17);
                    res.add(cur + "-" + to);
                }

                //Rechts außen (von Blau gesehen)
                if (!Arrays.asList(14, 15, 16, 23, 24, 31, 32, 39, 40, 47, 48, 55, 56, 63).contains(field) && (board.getplayeratpos(field - 6)!=Player.RR)) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field-6);
                    res.add(cur + "-" + to);
                }

                //Rechts innen (von Blau gesehen)
                if (!Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 23, 24, 32, 40, 48, 56).contains(field) && (board.getplayeratpos(field - 15)!=Player.RR)) {
                    String cur = Board.fieldToString(field);
                    String to = Board.fieldToString(field-15);
                    res.add(cur + "-" + to);
                }
            }
        }

        // Duplikate entfernen
        Set<String> set = new HashSet<>(res);
        res.clear();
        res.addAll(set);

        return res;
    }

     /*
    ArrayList<String> getPossibleKnightMoves(boolean currentPlayer, int knightPosition) {
        ArrayList<String> possibleMoves = new ArrayList<>();

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
            Player playerAtPos = getplayeratpos(newX * newY);

            // Check if the new position is within the board and moving forward/backward
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && playerAtPos == Player.R || playerAtPos == Player.B || playerAtPos == Player.EMPTY) {
                // Convert coordinates to chess notation
                String start = columns[column] + (row + 1);
                String end = columns[newX] + (newY + 1);
                possibleMoves.add(start + " - " + end);
            }
        }

        return possibleMoves;
    } */
}
