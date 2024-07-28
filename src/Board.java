import java.util.Objects;

public class Board {
    public boolean blauIstDran = false; // Indicates if it's blue's turn
    long blauzweiteebene = 0; // Blue second layer bitboard
    long rotzweiteebene = 0; // Red second layer bitboard
    long blauersteebene = 0; // Blue first layer bitboard
    long rotersteebene = 0; // Red first layer bitboard

    /**
     * Constructor to initialize the board from a FEN string.
     *
     * @param fen The FEN string representing the board state.
     */
    public Board(String fen) {
        constructBitBoard(fen);
    }

    /**
     * Constructor to initialize the board with specific bitboards.
     *
     * @param blauzweiteebene  Blue second layer bitboard.
     * @param rotzweiteebene   Red second layer bitboard.
     * @param blauersteebene   Blue first layer bitboard.
     * @param rotersteebene    Red first layer bitboard.
     */
    public Board(long blauzweiteebene, long rotzweiteebene, long blauersteebene, long rotersteebene) {
        this.blauzweiteebene = blauzweiteebene;
        this.rotzweiteebene = rotzweiteebene;
        this.blauersteebene = blauersteebene;
        this.rotersteebene = rotersteebene;
    }

    /**
     * Construct the bitboards from a FEN string.
     *
     * @param fen The FEN string representing the board state.
     */
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

    /**
     * Get the player at a specific position.
     *
     * @param pos The position on the board (1-64).
     * @return The player at the specified position.
     */
    Player getplayeratpos(int pos) {
        long number = 1L << (pos - 1);

        boolean isBlueFirstLevel = (number & blauersteebene) != 0;
        boolean isRedFirstLevel = (number & rotersteebene) != 0;
        boolean isBlueSecondLevel = (number & blauzweiteebene) != 0;
        boolean isRedSecondLevel = (number & rotzweiteebene) != 0;

        if (isBlueFirstLevel) {
            if (isBlueSecondLevel) {
                return Player.BB;
            } else if (isRedSecondLevel) {
                return Player.BR;
            } else {
                return Player.B;
            }
        } else if (isRedFirstLevel) {
            if (isBlueSecondLevel) {
                return Player.RB;
            } else if (isRedSecondLevel) {
                return Player.RR;
            } else {
                return Player.R;
            }
        } else {
            return Player.EMPTY;
        }
    }

    /**
     * Convert field number to string representation.
     *
     * @param field The field number (1-64).
     * @return The string representation of the field.
     */
    public static String fieldToString(int field) {
        String res = "";
        int modCol = field % 8;
        char[] col = {'H', 'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        int row = ((field - 1) / 8) + 1;
        return res + col[modCol] + row;
    }

    /**
     * Convert string representation to field number.
     *
     * @param position The string representation of the field (e.g., "A1").
     * @return The field number (1-64).
     */
    public static int stringToField(String position) {
        // Ensure the input is in uppercase
        position = position.toUpperCase();

        // Get the column and row characters
        char columnChar = position.charAt(0);
        char rowChar = position.charAt(1);

        // Convert column character to column index (0-7)
        int column = columnChar - 'A';

        // Convert row character to row index (0-7)
        int row = (rowChar - '0') - 1;

        // Calculate the field number (1-64)
        int fieldNumber = row * 8 + (column + 1);

        return fieldNumber;
    }

    /**
     * Update the board after a move.
     *
     * @param frompo The starting position of the move.
     * @param topo   The ending position of the move.
     */
    void updateBoard(int frompo, int topo) {
        Player frompos = getplayeratpos(frompo);
        Player topos = getplayeratpos(topo);

        // Clear the bit at the from position
        switch (frompos) {
            case RR, BR -> rotzweiteebene &= ~(1L << (frompo - 1));
            case BB, RB -> blauzweiteebene &= ~(1L << (frompo - 1));
            case R -> rotersteebene &= ~(1L << (frompo - 1));
            case B -> blauersteebene &= ~(1L << (frompo - 1));
        }

        // Set the bit at the to position
        switch (topos) {
            case RR, BR -> {
                rotzweiteebene &= ~(1L << (topo - 1));
                blauzweiteebene |= 1L << (topo - 1);
            }
            case BB, RB -> {
                rotzweiteebene |= 1L << (topo - 1);
                blauzweiteebene &= ~(1L << (topo - 1));
            }
            case R -> {
                switch (frompos) {
                    case BB, RB -> blauzweiteebene |= 1L << (topo - 1);
                    case RR, R, BR -> rotzweiteebene |= 1L << (topo - 1);
                    default -> {
                        rotersteebene &= ~(1L << (topo - 1));
                        blauersteebene |= 1L << (topo - 1);
                    }
                }
            }
            case B -> {
                switch (frompos) {
                    case BB, B, RB -> blauzweiteebene |= 1L << (topo - 1);
                    case RR, BR -> rotzweiteebene |= 1L << (topo - 1);
                    default -> {
                        blauersteebene &= ~(1L << (topo - 1));
                        rotersteebene |= 1L << (topo - 1);
                    }
                }
            }
            case EMPTY -> {
                switch (frompos) {
                    case BB, B, RB -> blauersteebene |= 1L << (topo - 1);
                    case RR, R, BR -> rotersteebene |= 1L << (topo - 1);
                    default -> {
                        blauersteebene &= ~(1L << (topo - 1));
                        rotersteebene |= 1L << (topo - 1);
                    }
                }
            }
        }
    }

    /**
     * Count the number of players on the board.
     *
     * @return The number of players on the board.
     */
    int numofPlayers() {
        int num = 0;
        for (int i = 1; i < 9; i++) {
            for (int field = 8 * i - 7; field < i * 8; field++) {
                if (getplayeratpos(field) != Player.EMPTY) num++;
            }
        }
        return num;
    }

    /**
     * Evaluate the board's current state.
     *
     * @param depth The depth of the current evaluation.
     * @return The evaluation score of the board.
     */
    int evaluate(int depth) {
        int result = 0;
        for (int i = 1; i < 9; i++) {
            for (int field = 8 * i - 7; field < i * 8; field++) {
                switch (getplayeratpos(field)) {
                    case B -> {
                        if (field < 58) result += (9 + i) * i;
                        else return 10000 - depth;
                    }
                    case R -> {
                        if (field > 8) result -= (18 - i) * (9 - i);
                        else return -10000 + depth;
                    }
                    case BB -> {
                        if (field < 58) result += (2 * (9 + i) + 1) * i;
                        else return 10000 - depth;
                    }
                    case RR -> {
                        if (field > 8) result -= (2 * (18 - i) + 1) * (9 - i);
                        else return -10000 + depth;
                    }
                    case RB -> {
                        if (field < 58) result += ((9 + i) + 1) * i;
                        else return 10000 - depth;
                    }
                    case BR -> {
                        if (field > 8) result -= ((18 - i) + 1) * (9 - i);
                        else return -10000 + depth;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Convert the board state to FEN notation.
     *
     * @return The FEN string representing the board state.
     */
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

    /**
     * Override equals method for transposition table.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return this.blauzweiteebene == board.blauzweiteebene && this.blauersteebene == board.blauersteebene && this.rotzweiteebene == board.rotzweiteebene && this.rotersteebene == board.rotersteebene;
    }

    /**
     * Override hashCode method for transposition table.
     *
     * @return The hash code of the board.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.blauzweiteebene, this.blauersteebene, this.rotzweiteebene, this.rotersteebene);
    }
}
