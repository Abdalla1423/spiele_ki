import java.util.*;

public class Game {
    // Constants and variables
    public static long numOfSearchedZustand = 0;
    static HashMap<String, int[]> alphabetacutoffmove = new LinkedHashMap<>();
    static int maxdepth;
    static HashMap<BoardDepthKey, MoveEvaluation> transpositionTable = new HashMap<>();
    static int[] bestmovepfad = new int[20];

    /**
     * Determines if a player has won the game.
     *
     * @param board The current state of the board.
     * @return Player.R if red has won, Player.B if blue has won, Player.EMPTY if no one has won.
     */
    public static Player thisPlayerHasWon(Board board) {
        // Check if red has won
        for (int field = 1; field < 8; field++) {
            if (board.getplayeratpos(field) == Player.R || board.getplayeratpos(field) == Player.RR || board.getplayeratpos(field) == Player.BR) {
                return Player.R;
            }
        }
        // Check if blue has won
        for (int field = 57; field < 64; field++) {
            if (board.getplayeratpos(field) == Player.B || board.getplayeratpos(field) == Player.BB || board.getplayeratpos(field) == Player.RB) {
                return Player.B;
            }
        }
        // If no one has won
        return Player.EMPTY;
    }

    /**
     * Determines the maximum search depth based on the remaining time.
     *
     * @param board The current state of the board.
     * @param time  The remaining time in milliseconds.
     * @return The maximum search depth.
     */
    public static int timeManagment(Board board, long time) {
        // Calculate remaining time
        return 6;
    }

    /**
     * Performs iterative deepening search to find the best move.
     *
     * @param startBoard  The starting state of the board.
     * @param maxDepth    The maximum search depth.
     * @param useAlphaBeta Indicates whether to use alpha-beta pruning.
     * @return The best move evaluation.
     */
    public static MoveEvaluation iterativeDeepening(Board startBoard, int maxDepth, boolean useAlphaBeta) {
        //Initialize window size and increment for aspiration windows
        int initialWindowSize = 50;
        int windowSizeIncrement = 70;
        int initialEvaluation = 0;

        //benchmark counter
        numOfSearchedZustand = 0;
        MoveEvaluation bestMoveEvaluation = new MoveEvaluation(0, new int[20]);

        for (int depth = 1; depth <= maxDepth; depth++) {
            maxdepth = depth;

            int alphaAspirationWindow = initialEvaluation - initialWindowSize;
            int betaAspirationWindow = initialEvaluation + initialWindowSize;

            // Try the initial window size
            bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, alphaAspirationWindow, betaAspirationWindow, useAlphaBeta, new int[20], new ArrayList<>());
            initialEvaluation = bestMoveEvaluation.evaluation;

            // Check if the result is outside the window
            if (bestMoveEvaluation.evaluation <= alphaAspirationWindow || bestMoveEvaluation.evaluation >= betaAspirationWindow) {
                // Increase the window size and try again
                alphaAspirationWindow = initialEvaluation - (initialWindowSize + windowSizeIncrement);
                betaAspirationWindow = initialEvaluation + (initialWindowSize + windowSizeIncrement);

                bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, alphaAspirationWindow, betaAspirationWindow, useAlphaBeta, new int[20], new ArrayList<>());
                initialEvaluation = bestMoveEvaluation.evaluation;

                // If still outside the window, fall back to full range alpha-beta
                if (bestMoveEvaluation.evaluation <= alphaAspirationWindow || bestMoveEvaluation.evaluation >= betaAspirationWindow) {
                    bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, useAlphaBeta, new int[20], new ArrayList<>());
                    initialEvaluation = bestMoveEvaluation.evaluation;
                }
            }

            // Reverse the array to get moves in correct order
            int[] bestmovepfadtrash = bestMoveEvaluation.move;
            int z = 0;
            for (int i = bestmovepfadtrash.length - 2; i > 0; i--) {
                if (bestmovepfadtrash[i] != 0) {
                    bestmovepfad[z] = bestmovepfadtrash[i - 1];
                    bestmovepfad[z + 1] = bestmovepfadtrash[i];
                    i--;
                    z += 2;
                }
            }

            bestMoveEvaluation.move[0] = bestmovepfad[0];
            bestMoveEvaluation.move[1] = bestmovepfad[1];
        }

        return bestMoveEvaluation;
    }

    /**
     * Performs the minimax search with alpha-beta pruning to find the best move.
     *
     * @param board        The current state of the board.
     * @param depth        The current search depth.
     * @param alpha        The alpha value for alpha-beta pruning.
     * @param beta         The beta value for alpha-beta pruning.
     * @param useAlphaBeta Indicates whether to use alpha-beta pruning.
     * @param lastMove     The last move made.
     * @param currMoves    The current list of moves.
     * @return The best move evaluation.
     */
    public static MoveEvaluation minimaxAlphaBeta(Board board, int depth, int alpha, int beta, boolean useAlphaBeta, int[] lastMove, ArrayList<int[]> currMoves) {
        int moveFactor = currMoves.size() * 20;

        // Check if a player has won
        if (depth == 0 || thisPlayerHasWon(board) != Player.EMPTY) {
            int evaluation = board.evaluate(moveFactor);
            return new MoveEvaluation(evaluation, lastMove);
        }

        // Generate all possible moves and check it the player has moves left
        ArrayList<int[]> allPossibleMoves = Move.possibleMoves(board);
        if (allPossibleMoves.isEmpty()) {
            if (board.blauIstDran) {
                return new MoveEvaluation(-10000 + moveFactor, lastMove);
            } else {
                return new MoveEvaluation(10000 - moveFactor, lastMove);
            }
        }

        MoveEvaluation bestMove = new MoveEvaluation(board.blauIstDran ? Integer.MIN_VALUE : Integer.MAX_VALUE, new int[20]);

        if (alphabetacutoffmove.containsKey(Arrays.toString(lastMove))) {
            int[] cutoffmove = alphabetacutoffmove.get(Arrays.toString(lastMove));
            List<int[]> allPossibleMove = allPossibleMoves.stream().filter(i -> i[0] != cutoffmove[0] && i[1] != cutoffmove[1]).toList();
            allPossibleMoves = new ArrayList<>(allPossibleMove);
            allPossibleMoves.add(0, cutoffmove);
        }

        int frompo = bestmovepfad[(maxdepth - depth) * 2];
        int topo = bestmovepfad[(maxdepth - depth) * 2 + 1];
        int z2 = 0;

        if (bestmovepfad[(maxdepth - depth) * 2] != 0) {
            allPossibleMoves.add(0, new int[]{bestmovepfad[(maxdepth - depth) * 2], bestmovepfad[(maxdepth - depth) * 2 + 1]});
            bestmovepfad[(maxdepth - depth) * 2] = 0;
        }

        for (int[] moves : allPossibleMoves) {
            if (moves[0] == frompo && moves[1] == topo & z2 != 0) {
                // Move was the best move and was set first
            } else {
                z2++;
                int[] move = new int[20];
                move[0] = moves[0];
                move[1] = moves[1];
                int z = 0;
                int z1 = 2;
                if (lastMove[0] != 0) {
                    while (lastMove[z] != 0) {
                        move[z1] = lastMove[z];
                        move[z1 + 1] = lastMove[z + 1];
                        z += 2;
                        z1 += 2;
                    }
                }

                Board nextBoard = new Board(board.blauzweiteebene, board.rotzweiteebene, board.blauersteebene, board.rotersteebene);
                nextBoard.updateBoard(move[0], move[1]);
                nextBoard.blauIstDran = !board.blauIstDran;
                currMoves.add(move);
                MoveEvaluation eval;

                // Check if Board existed at that depth already and was saved in the Transposition table
                BoardDepthKey key = new BoardDepthKey(board, depth);
                if (transpositionTable.containsKey(key)) {
                    eval = transpositionTable.get(key);
                } else {
                    eval = minimaxAlphaBeta(nextBoard, depth - 1, alpha, beta, useAlphaBeta, move, currMoves);
                }
                numOfSearchedZustand++;
                currMoves.remove(currMoves.size() - 1);

                if (board.blauIstDran) {
                    if (eval.evaluation > bestMove.evaluation) {
                        bestMove.evaluation = eval.evaluation;
                        bestMove.move = eval.move;
                    }
                    alpha = Math.max(alpha, eval.evaluation);
                } else {
                    if (eval.evaluation < bestMove.evaluation) {
                        bestMove.evaluation = eval.evaluation;
                        bestMove.move = eval.move;
                    }
                    beta = Math.min(beta, eval.evaluation);
                }

                if (useAlphaBeta && beta <= alpha) {
                    // Alpha-beta cut-off
                    break;
                }
            }
        }

        // save Board inside Transposition table
        transpositionTable.put(new BoardDepthKey(board, depth), bestMove);

        return bestMove;
    }

    /**
     * Generates the best move based on the given board and remaining time.
     *
     * @param b    The FEN string representing the board state.
     * @param time The remaining time in milliseconds.
     * @return The best move as a string.
     */
    public static String getMove(String b, int time) {
        Board board = new Board(b);
        int maxDepth = timeManagment(board, time);
        MoveEvaluation res = Game.iterativeDeepening(board, maxDepth, true);
        return Move.moveToString(res.move);
    }

    public static void main(String[] args) {
        String move = getMove("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r", 100000);
        System.out.println("Best move: " + move);
    }
}

/**
 * This class represents the evaluation of a move.
 */
class MoveEvaluation {
    int evaluation; // The evaluation score of the move
    int[] move; // The move represented as an array of integers

    /**
     * Constructor to initialize the move evaluation.
     *
     * @param evaluation The evaluation score of the move.
     * @param move       The move represented as an array of integers.
     */
    MoveEvaluation(int evaluation, int[] move) {
        this.evaluation = evaluation;
        this.move = move;
    }
}

/**
 * This class represents a key for the transposition table.
 */
class BoardDepthKey {
    Board board; // The board state
    int depth; // The search depth

    /**
     * Constructor to initialize the board depth key.
     *
     * @param board The board state.
     * @param depth The search depth.
     */
    BoardDepthKey(Board board, int depth) {
        this.board = board;
        this.depth = depth;
    }

    /**
     * Checks if this object is equal to another object.
     *
     * @param o The other object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardDepthKey key = (BoardDepthKey) o;
        return this.depth == key.depth && Objects.equals(board, key.board);
    }

    /**
     * Returns the hash code of this object.
     *
     * @return The hash code of this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(depth, board);
    }
}
