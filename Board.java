import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Board {
    public String[][] board = new String[8][8];

    void readfen(String fen) {
        int col = 2;
        int row = 1;
        for (int i = 0; i < fen.length(); i++) {
            if (Character.isDigit(fen.charAt(i))) {
                int skip = Integer.parseInt(String.valueOf(fen.charAt(i)));
                col = col + skip;
            } else if (fen.charAt(i) == '/') {
                if (row == 7) {
                    col = 2;
                } else {
                    col = 1;
                }
                row = row + 1;
            } else {
                String acc = fen.substring(i, i + 2);

                board[row - 1][col - 1] = acc;
                i = i + 1;
                col = col + 1;
            }
        }
    }

    ArrayList<String> possiblemoves(String[][] board) {
        ArrayList res = new ArrayList();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != null) {
                    if (board[row][col].charAt(1) == '0') {
                        //blau einzel figur
                        if (board[row][col].charAt(0) == 'b') {
                            if (!((col == 7 || col == 0) && row == 1) && !Objects.equals(board[row - 1][col], "r0") && !Objects.equals(board[row - 1][col], "rr") && !Objects.equals(board[row - 1][col], "bb") && !Objects.equals(board[row - 1][col], "rb") && !Objects.equals(board[row - 1][col], "br")) {
                                //zug nach vorne
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row - 1, col);
                                res.add(cur + "-" + to);
                            }
                            if (col != 7 && !Objects.equals(board[row][col + 1], "r0") && !Objects.equals(board[row][col + 1], "rr") && !Objects.equals(board[row][col + 1], "bb") && !Objects.equals(board[row][col + 1], "rb") && !Objects.equals(board[row][col + 1], "br")) {
                                //nach rechts
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row, col + 1);
                                res.add(cur + "-" + to);
                            }
                            if (col != 0 && !Objects.equals(board[row][col - 1], "r0") && !Objects.equals(board[row][col - 1], "rr") && !Objects.equals(board[row][col - 1], "bb") && !Objects.equals(board[row][col - 1], "rb") && !Objects.equals(board[row][col - 1], "br")) {
                                //nach links
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row, col - 1);
                                res.add(cur + "-" + to);
                            }

                            //Diagonal nach links
                            if (col != 0 && (Objects.equals(board[row - 1][col - 1], "r0") || Objects.equals(board[row - 1][col - 1], "rr") || Objects.equals(board[row - 1][col - 1], "br"))) {
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row - 1, col - 1);
                                res.add(cur + "-" + to);
                            }

                            //Diagonal nach rechts
                            if (col != 7 && (Objects.equals(board[row - 1][col + 1], "r0") || Objects.equals(board[row - 1][col + 1], "rr") || Objects.equals(board[row - 1][col + 1], "br"))) {
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row - 1, col + 1);
                                res.add(cur + "-" + to);
                            }
                        }
                        //rot einzel figur
                        else {
                            if (!((col == 7 || col == 0) && row == 6) && board[row + 1][col] == null || Objects.equals(board[row + 1][col], "r0")) {
                                //zug nach vorne
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row + 1, col);
                                res.add(cur + "-" + to);
                            }
                            if (board[row][col + 1] == null || Objects.equals(board[row][col + 1], "r0")) {
                                //nach rechts (von blau aus gesehen)
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row, col + 1);
                                res.add(cur + "-" + to);
                            }
                            if (board[row][col - 1] == null || Objects.equals(board[row][col - 1], "r0")) {
                                //nach links (von blau aus gesehen)
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row, col - 1);
                                res.add(cur + "-" + to);
                            }

                            //Diagonal nach links (von blau aus gesehen)
                            if (col != 0 && (Objects.equals(board[row + 1][col - 1], "b0") || Objects.equals(board[row + 1][col - 1], "bb") || Objects.equals(board[row + 1][col - 1], "rb"))) {
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row + 1, col - 1);
                                res.add(cur + "-" + to);
                            }

                            //Diagonal nach rechts (von blau aus gesehen)
                            if (col != 7 && (Objects.equals(board[row + 1][col + 1], "b0") || Objects.equals(board[row + 1][col + 1], "bb") || Objects.equals(board[row + 1][col - 1], "rb"))) {
                                String cur = indextoboardfield(row, col);
                                String to = indextoboardfield(row + 1, col + 1);
                                res.add(cur + "-" + to);
                            }
                        }
                    } else {
                        //blau doppel figur
                        if (board[row][col].charAt(1) == 'b') {

                        }

                    }
                }
            }
        }
        return new ArrayList<>();
    }

    String indextoboardfield(int row, int col) {
        String ro;
        char co;
        ro = Integer.toString(8 - row);
        char[] alp = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        co = alp[col];
        return co + ro;
    }

    // plus ist true wenn spieler rot ist
    ArrayList<String> knightValidFields(int col, int row, String[][] board, boolean plus) {
        ArrayList<String> res = new ArrayList<>();
        if (!plus) {
            // blauer spieler
            //2 Sprünge nach vorne
            if (row - 2 >= 0) {
                //Sprung nach 2 vorne 1 rechts
                if (col + 1 <= 7 && !(col == 6 && row == 2) && !Objects.equals(board[row - 2][col + 1], "bb") && !Objects.equals(board[row - 2][col + 1], "rb")) {
                    String cur = indextoboardfield(row, col);
                    String to = indextoboardfield(row - 2, col + 1);
                    res.add(cur + "-" + to);
                }
            }
        }
    }


}
