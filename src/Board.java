public class Board {
    public boolean blauIstDran = false;
    long blauzweiteebene = 0;
    long rotzweiteebene = 0;
    long blauersteebene = 0;
    long rotersteebene = 0;
    // blau unten rot oben

    public Board(String fen) {
        constructBitBoard(fen);
    }

    public Board(long blauzweiteebene,long rotzweiteebene, long blauersteebene,long rotersteebene) {
        this.blauzweiteebene = blauzweiteebene;
        this.rotzweiteebene = rotzweiteebene;
        this.blauersteebene = blauersteebene;
        this.rotersteebene = rotersteebene;
    }

    void constructBitBoard(String fen) {
        long number = 2;
        int row = 1;
        for (int i = 0; i < fen.length(); i++) {
            char ch = fen.charAt(i);
            if (Character.isDigit(ch)) {
                number <<= Character.getNumericValue(ch);
            } else if (ch == '/') {
                if (row == 7 || row == 1) number <<= 1;
                row++;
            } else {
                String acc = fen.substring(i, i + 2);
                switch (acc) {
                    case "r0" -> rotersteebene |= number;
                    case "rr" -> {
                        rotersteebene |= number;
                        rotzweiteebene |= number;
                    }
                    case "br" -> {
                        blauersteebene |= number;
                        rotzweiteebene |= number;
                    }
                    case "rb" -> {
                        rotersteebene |= number;
                        blauzweiteebene |= number;
                    }
                    case "bb" -> {
                        blauersteebene |= number;
                        blauzweiteebene |= number;
                    }
                    case "b0" -> blauersteebene |= number;
                    case " b" -> blauIstDran = true;
                    default -> blauIstDran = false;
                }
                number <<= 1;
                i++;
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

    public static String fieldToString (int field) {
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


    void updateBoard(int frompo, int topo) {

        Player frompos = getplayeratpos(frompo);
        Player topos = getplayeratpos(topo);

        switch (frompos) {
            case RR, BR -> rotzweiteebene &= ~(1L << (frompo-1));
            case BB, RB -> blauzweiteebene &= ~(1L << (frompo-1));
            case R -> rotersteebene &= ~(1L << (frompo-1));
            case B -> blauersteebene &= ~(1L << (frompo-1));
        }

        switch (topos) {
            case RR, BR -> {
                rotzweiteebene &= ~(1L << (topo-1));
                blauzweiteebene |= 1L << (topo-1);
            }
            case BB, RB -> {
                rotzweiteebene |= 1L << (topo-1);
                blauzweiteebene &= ~(1L << (topo-1));
            }
            case R -> {
                switch (frompos) {
                    case BB, RB -> blauzweiteebene |= 1L << (topo-1);
                    case RR, R, BR -> rotzweiteebene |= 1L << (topo-1);
                    default -> {
                        rotersteebene &= ~(1L << (topo-1));
                        blauersteebene |= 1L << (topo-1);
                    }
                }
            }
            case B -> {
                switch (frompos) {
                    case BB, B, RB -> blauzweiteebene |= 1L << (topo-1);
                    case RR, BR -> rotzweiteebene |= 1L << (topo-1);
                    default -> {
                        blauersteebene &= ~(1L << (topo-1));
                        rotersteebene |= 1L << (topo-1);
                    }
                }
            }
            case EMPTY -> {
                switch (frompos) {
                    case BB, B, RB -> blauersteebene |= 1L << (topo-1);
                    case RR, R, BR -> rotersteebene |= 1L << (topo-1);
                    default -> {
                        blauersteebene &= ~(1L << (topo-1));
                        rotersteebene |= 1L << (topo-1);
                    }
                }
            }
        }
    }

    int numofPlayers () {
        int num = 0;
        for (int i = 1; i < 9; i++) {
            for (int field = 8 * i - 7; field < i * 8; field++) {
                if (getplayeratpos(field) != Player.EMPTY) num++;
            }
        }
       return num;
    }

    int evaluate(int depth) {
        if (Move.possibleMoves(this).isEmpty()){
            if (this.blauIstDran) {
                return -10000 + depth;
            } else {
                return 10000 - depth;
            }
        }

        int result=0;
        for (int i = 1; i < 9; i++) {
            for (int field = 8*i-7; field < i*8; field++) {

                //int factor = i;
                switch (getplayeratpos(field)) {
                    case B -> {
                        if (field < 58) result += (9 + i) * i;
                        else return 10000 - depth;
                    }
                    case R -> {
                        if (field > 8) result -= (18-i) * (9 - i);
                        else return -10000 + depth;
                    }
                    case BB -> {
                        if (field < 58) result  += (2*(9+i)+1) * i;
                        else return 10000 - depth;
                    }
                    case RR -> {
                        if (field > 8) result -= (2*(18-i)+1) * (9 - i);
                        else return -10000 + depth;
                    }
                    case RB -> {
                        if (field < 58) result += ((9+i)+1) * i;
                        else return 10000 - depth;
                    }
                    case BR -> {
                        if (field > 8) result -= ((18-i)+1) * (9 - i);
                        else return -10000 + depth;
                    }
                }
            }
        }


        return result;
    }

    String boardToFEN() {
        StringBuilder res = new StringBuilder();
        int countEmpty = 0;

        for (int i = 2; i < 64; i++) {
            if (i != 8 && i != 57) {
                Player player = getplayeratpos(i);
                if (player != Player.EMPTY) {
                    if (countEmpty > 0) {
                        res.append(countEmpty);
                        countEmpty = 0;
                    }
                    res.append(switch (player) {
                        case R -> "r0";
                        case RR -> "rr";
                        case BR -> "br";
                        case BB -> "bb";
                        case RB -> "rb";
                        case B -> "b0";
                        default -> "";
                    });
                } else {
                    countEmpty++;
                }
            }

            if (i % 8 == 0) {
                if (countEmpty > 0) {
                    res.append(countEmpty);
                    countEmpty = 0;
                }
                res.append("/");
            }
            if (i == 63) {
                if (countEmpty > 0) {
                    res.append(countEmpty);
                }
                res.append(blauIstDran ? " b" : " r");
            }
        }
        return res.toString();
    }

}
