package tests;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
    // blau unten rot oben
    boolean blauIstDran = false;
    long blauzweiteebene = 0;
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
                } else if(acc.equals("b0")){
                    blauersteebene = blauersteebene | number;
                }else if(acc.equals(" b")){
                    blauIstDran = true;
                }else{
                    blauIstDran = false;
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
        int row= ((field-1) / 8)+1;
        return res+col[modCol]+row;
    }

    public static int stringToField(String position) {
        // Ensure the input is in uppercase
        position = position.toUpperCase();

        // Get the column and row characters
        char columnChar = position.charAt(0);
        char rowChar = position.charAt(1);

        // Convert column character to column index (0-7)
        int column = columnChar - 'A';

        // Convert row character to row index (0-7)
        int row = (rowChar - '0') -1;

        // Calculate the field number (1-64)
        int fieldNumber = row * 8 + (column +1);

        return fieldNumber;
    }

    String pickMove(ArrayList<String> allMoves) {
        int newMove = ThreadLocalRandom.current().nextInt(0, allMoves.size());
        return allMoves.get(newMove);
    }

    //gibt Player R zurück, wenn rot gewonnen hat; Player B zurück, wenn blau gewonnen hat; Player EMPTY zurück, wenn noch
    // niemand gewonnen hat
    String thisPlayerHasWon () {
        //gucken, ob rot gewonnen hat
        for (int field=1; field<8; field++) {
            if (getplayeratpos(field)==Player.R || getplayeratpos(field)==Player.RR || getplayeratpos(field)==Player.BR) {
                return "Player red has won!";
            }
        }
        //gucken, ob blau gewonnen hat
        for (int field=57; field<64; field++) {
            if (getplayeratpos(field)==Player.B || getplayeratpos(field)==Player.BB || getplayeratpos(field)==Player.RB) {
                return "Player blue has won!";
            }
        }
        //wenn noch niemand gewonnen hat
        return "No one has won yet!";
    }

    ArrayList<String> possiblemoves() {
        ArrayList<String> res = new ArrayList<>();
        for (int field = 1; field < 64; field++) {
            //blau einzel figur
            if (this.blauIstDran && getplayeratpos(field)==Player.B) {
                //zug nach vorne
                if ((field + 8) != 57 && (field + 8) != 64 && field<57 && (getplayeratpos(field + 8) == Player.EMPTY || getplayeratpos(field + 8) == Player.B )) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field+8);
                    res.add(cur + "-" + to);
                }
                //nach rechts
                if (!Arrays.asList(7, 16, 24, 32, 40, 48, 56, 63).contains(field) && (getplayeratpos(field + 1) == Player.EMPTY || getplayeratpos(field + 1) == Player.B )) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field+1);
                    res.add(cur + "-" + to);
                }
                //nach links
                if (!Arrays.asList(2, 9, 17, 25, 33, 41, 49, 58).contains(field) && (getplayeratpos(field - 1) == Player.EMPTY || getplayeratpos(field - 1) == Player.B )) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field-1);
                    res.add(cur + "-" + to);
                }

                //Diagonal nach links
                if (!Arrays.asList(9, 10, 17, 25, 33, 41, 49, 50).contains(field) && (getplayeratpos(field +7) == Player.RR || getplayeratpos(field +7) == Player.R || getplayeratpos(field +7) == Player.BR)) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field+7);
                    res.add(cur + "-" + to);
                }

                //Diagonal nach rechts
                if (!Arrays.asList(16, 24, 32, 40, 48, 56, 55).contains(field) && (getplayeratpos(field +9) == Player.RR || getplayeratpos(field +9) == Player.R || getplayeratpos(field +9) == Player.BR)) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field+9);
                    res.add(cur + "-" + to);
                }
            }

            //blau doppelt
            if (this.blauIstDran && (getplayeratpos(field)==Player.BB || getplayeratpos(field)==Player.RB)) {
                // 2 nach rechts 1 nach oben
                if (!Arrays.asList(7, 15, 16, 23, 24, 31, 32, 39, 40, 47, 48, 54, 55, 56).contains(field) && getplayeratpos(field+10)!=Player.BB) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field+10);
                    res.add(cur + "-" + to);
                }
                // 1 nach rechts 2 nach oben
                if (!Arrays.asList(16, 24, 32, 40, 47, 48, 56).contains(field) && field<49 && getplayeratpos(field+17)!=Player.BB) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field+17);
                    res.add(cur + "-" + to);
                }
                // 2 nach links 1 nach oben
                if (!Arrays.asList(2, 9, 10, 17, 18, 25, 26, 33, 34, 41, 42, 49, 50, 51).contains(field) && getplayeratpos(field+6)!=Player.BB) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field+6);
                    res.add(cur + "-" + to);
                }
                // 1 nach links 2 nach oben
                if (!Arrays.asList(9, 17, 25, 33, 41, 49, 42).contains(field) && field<49 && getplayeratpos(field+15)!=Player.BB) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field+15);
                    res.add(cur + "-" + to);
                }
            }

            //rot einzel figur
            if (!this.blauIstDran && getplayeratpos(field)==Player.R) {
                //zug nach vorne
                if ((field-8)>1 && (field-8)!=8 && (getplayeratpos(field - 8) == Player.EMPTY || getplayeratpos(field - 8) == Player.R )) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field-8);
                    res.add(cur + "-" + to);
                }
                //nach rechts (von blau aus gesehen)
                if (!Arrays.asList(7, 16, 24, 32, 40, 48, 56, 63).contains(field) && (getplayeratpos(field + 1) == Player.EMPTY || getplayeratpos(field + 1) == Player.R )) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field+1);
                    res.add(cur + "-" + to);
                }
                //nach links (von blau aus gesehen)
                if (!Arrays.asList(2, 9, 17, 25, 33, 41, 49, 58).contains(field) && (getplayeratpos(field - 1) == Player.EMPTY || getplayeratpos(field - 1) == Player.R )) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field-1);
                    res.add(cur + "-" + to);
                }

                //Diagonal nach links (von blau aus gesehen)
                if (!Arrays.asList(9, 17, 25, 33, 41, 49, 10).contains(field) && (getplayeratpos(field - 9)==Player.BB || getplayeratpos(field - 9)==Player.B || getplayeratpos(field - 9)==Player.RB )) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field-9);
                    res.add(cur + "-" + to);
                }

                //Diagonal nach rechts (von blau aus gesehen)
                if (!Arrays.asList(16, 24, 32, 40, 48, 56, 15).contains(field) && (getplayeratpos(field - 7)==Player.BB || getplayeratpos(field - 7)==Player.B || getplayeratpos(field - 7)==Player.RB )) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field-7);
                    res.add(cur + "-" + to);
                }
            }

            // rot doppelt
            if (!this.blauIstDran && getplayeratpos(field)==Player.RR || getplayeratpos(field)==Player.BR) {
                //Links außen (von Blau gesehen)
                if (!Arrays.asList(9, 10, 11, 17, 18, 25, 26, 33, 34, 41, 42, 49, 50, 58).contains(field) && (getplayeratpos(field - 10)!=Player.RR)) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field-10);
                    res.add(cur + "-" + to);
                }

                //Links innen (von Blau gesehen)
                if (!Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 25, 33, 41, 49).contains(field) && (getplayeratpos(field - 17)!=Player.RR)) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field-17);
                    res.add(cur + "-" + to);
                }

                //Rechts außen (von Blau gesehen)
                if (!Arrays.asList(14, 15, 16, 23, 24, 31, 32, 39, 40, 47, 48, 55, 56, 63).contains(field) && (getplayeratpos(field - 6)!=Player.RR)) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field-6);
                    res.add(cur + "-" + to);
                }

                //Rechts innen (von Blau gesehen)
                if (!Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 23, 24, 32, 40, 48, 56).contains(field) && (getplayeratpos(field - 15)!=Player.RR)) {
                    String cur = fieldToString(field);
                    String to = fieldToString(field-15);
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

    void updateBoard(String fromposition, String toposition) {
        int frompo = stringToField(fromposition);
        int topo = stringToField(toposition);

        Player frompos = getplayeratpos(frompo);
        Player topos = getplayeratpos(topo);

        if(frompos == Player.RR){
            rotzweiteebene &= ~(1L << (frompo-1));
        }else if(frompos == Player.BB){
            blauzweiteebene &= ~(1L << (frompo-1));
        }else if(frompos == Player.RB){
            blauzweiteebene &= ~(1L << (frompo-1));
        }else if(frompos == Player.BR){
            rotzweiteebene &= ~(1L << (frompo-1));
        }else if(frompos == Player.R){
            rotersteebene &= ~(1L << (frompo-1));
        }else if(frompos == Player.B){
            blauersteebene &= ~(1L << (frompo-1));
        }

        if(topos == Player.RR){
            rotzweiteebene &= ~(1L << (topo-1));
            blauzweiteebene |= 1L << (topo-1);
        }else if(topos == Player.BB){
            rotzweiteebene |= 1L << (topo-1);
            blauzweiteebene &= ~(1L << (topo-1));
        }else if(topos == Player.RB){
            rotzweiteebene |= 1L << (topo-1);
            blauzweiteebene &= ~(1L << (topo-1));
        }else if(topos == Player.BR){
            rotzweiteebene &= ~(1L << (topo-1));
            blauzweiteebene |= 1L << (topo-1);
        }else if(topos == Player.R){
            if(frompos == Player.RR || frompos == Player.R || frompos == Player.BR){
                rotzweiteebene |= 1L << (topo-1);
            }else if(frompos == Player.RB || frompos == Player.BB){
                blauzweiteebene |= 1L << (topo-1);
            }
            else{
                rotersteebene &= ~(1L << (topo-1));
                blauersteebene |= 1L << (topo-1);
            }
        }else if(topos == Player.B){
            if(frompos == Player.BB || frompos == Player.B || frompos == Player.RB){
                blauzweiteebene |= 1L << (topo-1);
            }else if(frompos == Player.BR || frompos == Player.RR){
                rotzweiteebene |= 1L << (topo-1);
            }
            else{
                blauersteebene &= ~(1L << (topo-1));
                rotersteebene |= 1L << (topo-1);
            }

        } else if (topos == Player.EMPTY) {
            if(frompos == Player.BB || frompos == Player.B || frompos == Player.RB){
                blauersteebene |= 1L << (topo-1);
            }else if(frompos == Player.BR || frompos == Player.RR || frompos == Player.R){
                rotersteebene |= 1L << (topo-1);
            }
            else{
                blauersteebene &= ~(1L << (topo-1));
                rotersteebene |= 1L << (topo-1);
            }
        }
    }

    String boardToFEN(){
        StringBuilder res = new StringBuilder();
        int countempty = 0;
        int row = 1;

        for(int i = 2; i < 64; i++) {
            if (i != 8 && i != 57) {
                if(getplayeratpos(i) == Player.R){
                    if(countempty > 0){
                        res.append(countempty);
                        countempty = 0;
                    }
                    res.append("r0");
                } else if (getplayeratpos(i) == Player.RR) {
                    if(countempty > 0){
                        res.append(countempty);
                        countempty = 0;
                    }
                    res.append("rr");
                }else if(getplayeratpos(i) == Player.BR){
                    if(countempty > 0){
                        res.append(countempty);
                        countempty = 0;
                    }
                    res.append("br");
                }else if(getplayeratpos(i) == Player.BB){
                    if(countempty > 0){
                        res.append(countempty);
                        countempty = 0;
                    }
                    res.append("bb");
                }else if(getplayeratpos(i) == Player.RB){
                    if(countempty > 0){
                        res.append(countempty);
                        countempty = 0;
                    }
                    res.append("rb");
                }else if(getplayeratpos(i) == Player.B){
                    if(countempty > 0){
                        res.append(countempty);
                        countempty = 0;
                    }
                    res.append("b0");
                }else{
                    countempty++;
                }
            }

                if(i%8 == 0){
                    if(countempty > 0){
                        res.append(countempty);
                        countempty = 0;
                    }
                    res.append("/");
                }
                if(i == 63 && blauIstDran){
                    if(countempty > 0) {
                        res.append(countempty);
                        //countempty = 0;
                    }
                    blauIstDran = false;
                    res.append(" r");
                }else if (i == 63){
                    if(countempty > 0) {
                        res.append(countempty);
                        //countempty = 0;
                    }
                    blauIstDran = true;
                    res.append(" b");

                }
        }
        return res.toString();
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
enum Player{
    R, B, RB, BR, RR, BB, EMPTY;
}
