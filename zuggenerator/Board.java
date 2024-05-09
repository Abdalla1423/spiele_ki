import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board {
    l
    long rotzweiteebene = 0;
    long blauersteebene = 0;
    long rotersteebene = 0;

    void readfen(String fen) {
        long number = 2;
        int row = 1;
        for (int i = 0; i < fen.length(); i++) {
            if (Character.isDigit(fen.charAt(i))) {
                int skip = Integer.parseInt(String.valueOf(fen.charAt(i)));
                number = number << skip;
            } else if (fen.charAt(i) == '/') {
                if (row == 7) {
                    number = number << 1;
                } else if (row == 1) {
                    number = number << 1;
                }
                row = row + 1;
            } else {
                String acc = fen.substring(i, i + 2);
                if (acc.equals("r0")) {
                    rotersteebene = rotersteebene | number;
                } else if (acc.equals("rr")) {
                    rotersteebene = rotersteebene | number;
                    rotzweiteebene = rotzweiteebene | number;
                } else if (acc.equals("br")) {
                    blauersteebene = blauersteebene | number;
                    rotzweiteebene = rotzweiteebene | number;
                } else if (acc.equals("rb")) {
                    rotersteebene = rotersteebene | number;
                    blauzweiteebene = blauzweiteebene | number;
                } else if (acc.equals("bb")) {
                    blauersteebene = blauersteebene | number;
                    blauzweiteebene = blauzweiteebene | number;
                } else {//b0
                    blauersteebene = blauersteebene | number;
                }
                number = number << 1;
                i = i + 1;
            }
        }
    }

    Player getplayeratpos(int pos) {
        long number = 1;
        number = number << (pos - 1);

        if ((number & blauersteebene) == number) {
            if ((number & blauzweiteebene) == number) {
                return Player.BB;
            } else if ((number & rotzweiteebene) == number) {
                return Player.BR;
            } else {
                return Player.B;
            }
        } else if ((number & rotersteebene) == number) {
            if ((number & blauzweiteebene) == number) {
                return Player.RB;
            } else if ((number & rotzweiteebene) == number) {
                return Player.RR;
            } else {
                return Player.R;
            }
        } else {
            return Player.EMPTY;
        }
    }

    String fieldToString (int field) {
        String res = "";
        int modCol= field % 8;
        char [] col= {'H', 'A', 'B', 'C', 'D', 'E', 'F' , 'G'};
        int row= 8- ((field-1) / 8);
        return res+col[modCol]+row;
    }

    ArrayList<String> possiblemoves() {
        ArrayList<String> res = new ArrayList<>();
        for (int field = 1; field < 64; field++) {
                //blau einzel figur
                if (getplayeratpos(field)==Player.B) {
                    //zug nach vorne
                    if ((field - 8) > 1 && (field - 8) != 8 && getplayeratpos(field - 8) == Player.EMPTY) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field-8);
                        res.add(cur + "-" + to);
                    }
                    //nach rechts
                    if (!Arrays.asList(16, 24, 32, 40, 48, 56, 63).contains(field) && getplayeratpos(field + 1) == Player.EMPTY) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field+1);
                        res.add(cur + "-" + to);
                    }
                    //nach links
                    if (!Arrays.asList(9, 17, 25, 33, 41, 49, 58).contains(field) && getplayeratpos(field - 1) == Player.EMPTY) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field-1);
                        res.add(cur + "-" + to);
                    }

                    //Diagonal nach links
                    if (!Arrays.asList(2, 9, 10, 17, 25, 33, 41, 49).contains(field) && (getplayeratpos(field - 9) == Player.RR || getplayeratpos(field - 9) == Player.R || getplayeratpos(field - 9) == Player.BR)) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field-9);
                        res.add(cur + "-" + to);
                    }

                    //Diagonal nach rechts
                    if (!Arrays.asList(16, 24, 32, 40, 48, 56, 15).contains(field) && (getplayeratpos(field - 7) == Player.RR || getplayeratpos(field - 7) == Player.R || getplayeratpos(field - 7) == Player.BR)) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field-7);
                        res.add(cur + "-" + to);
                    }
                }

                //rot einzel figur
                if (getplayeratpos(field)==Player.B) {
                    //zug nach vorne
                    if ((field+8)<64 && (field+8)!=57 && getplayeratpos(field+8)==Player.EMPTY) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field+8);
                        res.add(cur + "-" + to);
                    }
                    //nach rechts (von blau aus gesehen)
                    if (!Arrays.asList(7, 16, 24, 32, 40, 48, 56).contains(field) && getplayeratpos(field +1)==Player.EMPTY) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field+1);
                        res.add(cur + "-" + to);
                    }
                    //nach links (von blau aus gesehen)
                    if (!Arrays.asList(9, 17, 25, 33, 41, 49, 2).contains(field) && getplayeratpos(field -1)==Player.EMPTY) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field-1);
                        res.add(cur + "-" + to);
                    }

                    //Diagonal nach links (von blau aus gesehen)
                    if (!Arrays.asList(9, 17, 25, 33, 41, 49, 50).contains(field) && (getplayeratpos(field +7)==Player.BB || getplayeratpos(field +7)==Player.B || getplayeratpos(field +7)==Player.RB )) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field+7);
                        res.add(cur + "-" + to);
                    }

                    //Diagonal nach rechts (von blau aus gesehen)
                    if (!Arrays.asList(16, 24, 32, 40, 48, 56, 55).contains(field) && (getplayeratpos(field +9)==Player.BB || getplayeratpos(field +9)==Player.B || getplayeratpos(field +9)==Player.RB )) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field+9);
                        res.add(cur + "-" + to);
                    }
                }
                //blau doppellt

                //rot doppellt
        }
        return res;
    }

/*
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

        return res;
    } */
}
enum Player{
    R, B, RB, BR, RR, BB, EMPTY;
}
