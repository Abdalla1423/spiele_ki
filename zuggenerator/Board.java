import java.util.ArrayList;
import java.util.Arrays;

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

    public static int StringToField(String position) {
        // Ensure the input is in uppercase
        position = position.toUpperCase();
        
        // Get the column and row characters
        char columnChar = position.charAt(0);
        char rowChar = position.charAt(1);
        
        // Convert column character to column index (0-7)
        int column = columnChar - 'A';
        
        // Convert row character to row index (0-7)
        int row = 8 - (rowChar - '0');
        
        // Calculate the field number (1-64)
        int fieldNumber = row * 8 - (8 - column);
        
        return fieldNumber;
    }


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
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && playerAtPos == Player.R ||  playerAtPos == Player.B || playerAtPos == Player.EMPTY) {
                // Convert coordinates to chess notation
                String start = columns[column] + (row + 1);
                String end = columns[newX] + (newY + 1);
                possibleMoves.add(start + " - " + end);
            }
        }

        return possibleMoves;
    }

    //gibt Player R zurück, wenn rot gewonnen hat; Player B zurück, wenn blau gewonnen hat; Player EMPTY zurück, wenn noch
    // niemand gewonnen hat
    Player thisPLayerHasWon () {
        //gucken, ob rot gewonnen hat
        for (int field=1; field<8; field++) {
            if (getplayeratpos(field)==Player.R || getplayeratpos(field)==Player.RR || getplayeratpos(field)==Player.BR) {
                return Player.R;
            }
        }
        //gucken, ob blau gewonnen hat
        for (int field=57; field<64; field++) {
            if (getplayeratpos(field)==Player.B || getplayeratpos(field)==Player.BB || getplayeratpos(field)==Player.RB) {
                return Player.B;
            }
        }
        //wenn noch niemand gewonnen hat
        return Player.EMPTY;
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
                    if (!Arrays.asList(7, 16, 24, 32, 40, 48, 56, 63).contains(field) && (getplayeratpos(field + 8) == Player.EMPTY || getplayeratpos(field + 8) == Player.B )) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field+1);
                        res.add(cur + "-" + to);
                    }
                    //nach links
                    if (!Arrays.asList(2, 9, 17, 25, 33, 41, 49, 58).contains(field) && (getplayeratpos(field + 8) == Player.EMPTY || getplayeratpos(field + 8) == Player.B )) {
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

                //rot einzel figur
                if (!this.blauIstDran && getplayeratpos(field)==Player.R) {
                    //zug nach vorne
                    if ((field-8)>1 && (field-8)!=8 && (getplayeratpos(field + 8) == Player.EMPTY || getplayeratpos(field + 8) == Player.R )) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field-8);
                        res.add(cur + "-" + to);
                    }
                    //nach rechts (von blau aus gesehen)
                    if (!Arrays.asList(7, 16, 24, 32, 40, 48, 56, 63).contains(field) && (getplayeratpos(field + 8) == Player.EMPTY || getplayeratpos(field + 8) == Player.R )) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field+1);
                        res.add(cur + "-" + to);
                    }
                    //nach links (von blau aus gesehen)
                    if (!Arrays.asList(2, 9, 17, 25, 33, 41, 49, 58).contains(field) && (getplayeratpos(field + 8) == Player.EMPTY || getplayeratpos(field + 8) == Player.R )) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field-1);
                        res.add(cur + "-" + to);
                    }

                    //Diagonal nach links (von blau aus gesehen)
                    if (!Arrays.asList(9, 17, 25, 33, 41, 49, 10).contains(field) && (getplayeratpos(field -9)==Player.BB || getplayeratpos(field -9)==Player.B || getplayeratpos(field -9)==Player.RB )) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field-9);
                        res.add(cur + "-" + to);
                    }

                    //Diagonal nach rechts (von blau aus gesehen)
                    if (!Arrays.asList(16, 24, 32, 40, 48, 56, 15).contains(field) && (getplayeratpos(field -7)==Player.BB || getplayeratpos(field -7)==Player.B || getplayeratpos(field -7)==Player.RB )) {
                        String cur = fieldToString(field);
                        String to = fieldToString(field-7);
                        res.add(cur + "-" + to);
                    }
                }

                if (blauIstDran && getplayeratpos(field) == Player.BB || getplayeratpos(field) == Player.RB) {
                    //blau doppellt
                    res.addAll(getPossibleKnightMoves(true, field));
                }

                if (blauIstDran && getplayeratpos(field) == Player.RR || getplayeratpos(field) == Player.BR) {
                    //rot doppellt
                    res.addAll(getPossibleKnightMoves(true, field));
                }
        }
        return res;
    }
}
enum Player{
    R, B, RB, BR, RR, BB, EMPTY;
}
