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


    void updateBoard(String fromposition, String toposition) {
        int frompo = stringToField(fromposition);
        int topo = stringToField(toposition);

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

    int evaluate() {
        int result=0;
        // this.blauIstDran = false;

        /*if (this.blauIstDran) {
            for (int i = 8; i > 0; i--) {
                for (int field = 8 * i - 7; field < i * 8; field++) {
                    if (Player.B == getplayeratpos(field))  {
                        return i;
                    }
                }
            }
        } else {
            for (int i = 1; i < 9; i++) {
                for (int field = 8 * i - 7; field < i * 8; field++) {
                    if (Player.R == getplayeratpos(field))  {
                        return -i;
                    }
                }
            }
        }

         */


        // verlieren -10, verlieren -11, gewinnen -23 --> -2
        //  verlieren -15, verliern -16, gewinnen -33 --> -2
        for (int i = 1; i < 9; i++) {
            for (int field = 8*i-7; field < i*8; field++) {
                int factor = (int) Math.pow(i, 2);
                switch (getplayeratpos(field)) {
                    case B -> result+=9+factor;
                    case R -> result-=18-factor;
                    case BB -> result+=2*(9+factor)+1;
                    case RR -> result-=2*(18-factor)+1;
                    case RB -> result+=(9+factor)+1;
                    case BR -> result-=(18-factor)+1;
                }
            }
        }


        /*

        //Reihe 1
        for (int field=2; field<8; field++) {
           switch (getplayeratpos(field)) {
               case Player.B -> result+=10;
               case Player.R, Player.BR, Player.RR -> result = -10000;
               case Player.BB -> result+=22;
               case Player.RB -> result+=11;
           }
        }
        //Reihe 2 (blau + 1 zu reihe davor)
        for (int field=9; field<16; field++) {
            switch (getplayeratpos(field)) {
                case Player.B -> result+=11;
                case Player.R -> result-=25;
                case Player.BB -> result+=24;
                case Player.RR, Player.BR -> result-=60;
                case Player.RB -> result+=12;
            }
        }
        //Reihe 3 (blau+1 im vergleich zu reihe davor)
        for (int field=17; field<24; field++) {
            switch (getplayeratpos(field)) {
                case Player.B -> result+=13;
                case Player.R -> result-=16;
                case Player.BB -> result+=26;
                case Player.RR, Player.BR -> result-=50;
                case Player.RB -> result+=13;
            }
        }
        //Reihe 4 (blau + 1 zu reihe davor)
        for (int field=25; field<32; field++) {
            switch (getplayeratpos(field)) {
                case Player.B -> result+=16;
                case Player.R -> result-=15;
                case Player.BB -> result+=28;
                case Player.RR -> result-=32;
                case Player.RB -> result+=16;
                case Player.BR -> result-=16;
            }
        }
        //Reihe 5 (blau+2 im vergleich zu reihe davor)
        for (int field=33; field<40; field++) {
            switch (getplayeratpos(field)) {
                case Player.B -> result+=15;
                case Player.R -> result-=13;
                case Player.BB -> result+=32;
                case Player.RR -> result-=28;
                case Player.RB -> result+=16;
                case Player.BR -> result-=14;
            }
        }
        //Reihe 6 (blau+1 im vergleich zu reihe davor)
        for (int field=41; field<48; field++) {
            switch (getplayeratpos(field)) {
                case Player.B -> result+=16;
                case Player.R -> result-=12;
                case Player.BB -> result+=50; //gewinnt das spiel im nächsten zug
                case Player.RR -> result-=26;
                case Player.RB -> result+=50; //gewinnt das spiel im nächsten zug
                case Player.BR -> result-=13;
            }
        }
        //Reihe 7 (blau+11 im vergleich zu reihe davor)
        for (int field=49; field<56; field++) {
            switch (getplayeratpos(field)) {
                case Player.B -> result+=25;
                case Player.BB -> result+=60; //gewinnt das spiel im nächsten zug
                case Player.R -> result-=11;
                case Player.RR -> result-=24;
                case Player.RB -> result+=60; //gewinnt das spiel im nächsten zug
                case Player.BR -> result-=12;
            }
        }
        //Reihe 8
        for (int field=57; field<63; field++) {
            switch (getplayeratpos(field)) {
                case Player.B -> result=10000;
                case Player.BB -> result=10000;
                case Player.R -> result-=10;
                case Player.RR -> result-=22;
                case Player.RB -> result=10000;
                case Player.BR -> result-=11;
            }
        }

         */

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

