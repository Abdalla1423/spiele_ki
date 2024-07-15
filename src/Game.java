
import java.util.*;


public class Game {
    //gibt Player R zurück, wenn rot gewonnen hat; Player B zurück, wenn blau gewonnen hat; Player EMPTY zurück, wenn noch
    // niemand gewonnen hat
    // TODO: Player also wins if other one has no moves or players left (same as no moves)
    public static Player thisPlayerHasWon (Board board) {
        //gucken, ob rot gewonnen hat
        for (int field=1; field<8; field++) {
            if (board.getplayeratpos(field)==Player.R || board.getplayeratpos(field)==Player.RR || board.getplayeratpos(field)==Player.BR) {
                return Player.R;
            }
        }
        //gucken, ob blau gewonnen hat
        for (int field=57; field<64; field++) {
            if (board.getplayeratpos(field)==Player.B || board.getplayeratpos(field)==Player.BB || board.getplayeratpos(field)==Player.RB) {
                return Player.B;
            }
        }
        //wenn noch niemand gewonnen hat
        return Player.EMPTY;
    }

    public static int timeManagment(Board board, long time) {
        //abgelaufene Zeit herausfinden
        return 6;
    }

    public static long numOfSearchedZustand = 0;
    static HashMap<BoardDepthKey, MoveEvaluation> transpositionTable = new HashMap<>();

    public static MoveEvaluation iterativeDeepening(Board startBoard, int maxDepth, boolean useAlphaBeta) {

        numOfSearchedZustand = 0;
        MoveEvaluation bestMoveEvaluation = new MoveEvaluation(0, new int[20]);
        for (int depth = 1; depth <= maxDepth; depth++) {
            bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, useAlphaBeta, new int[20], new ArrayList<int[]>());

        }

        return bestMoveEvaluation;
    }


    public static MoveEvaluation minimaxAlphaBeta(Board board, int depth, int alpha, int beta, boolean useAlphaBeta, int[] lastMove, ArrayList<int[]> currMoves) {
        int moveFactor = currMoves.size() * 20;
        // jemand gewonnen
        if (depth == 0 || thisPlayerHasWon(board) != Player.EMPTY) {
            int evaluation = board.evaluate(moveFactor);
            return new MoveEvaluation(evaluation, lastMove);
        }

        // keine Moves mehr
        ArrayList<int[]> allPossibleMoves = Move.possibleMoves(board);
        if (allPossibleMoves.isEmpty()){
            if (board.blauIstDran) {
                return new MoveEvaluation(-10000 + moveFactor,lastMove);
            } else {
                return new MoveEvaluation(10000 - moveFactor, lastMove);
            }
        }

        MoveEvaluation bestMove = new MoveEvaluation(board.blauIstDran ? Integer.MIN_VALUE : Integer.MAX_VALUE, new int[20]);

        for (int[] move : allPossibleMoves) {
                Board nextBoard = new Board(board.blauzweiteebene, board.rotzweiteebene, board.blauersteebene, board.rotersteebene);
                nextBoard.updateBoard(move[0], move[1]);
                nextBoard.blauIstDran = !board.blauIstDran;
                currMoves.add(move);
                MoveEvaluation eval;

                // Transpositiontable
                BoardDepthKey key = new BoardDepthKey(board, depth);
                if (transpositionTable.containsKey(key)) {
                    eval = transpositionTable.get(key);
                } else {
                    eval = minimaxAlphaBeta(nextBoard, depth - 1, alpha, beta,  useAlphaBeta, move, currMoves);
                }
                numOfSearchedZustand++;
                currMoves.remove(currMoves.size()-1);

                if (board.blauIstDran) {
                    if (eval.evaluation > bestMove.evaluation) {
                        bestMove.evaluation = eval.evaluation;
                        bestMove.move = move;

                    }
                    alpha = Math.max(alpha, eval.evaluation);
                } else {
                    if (eval.evaluation < bestMove.evaluation) {
                        bestMove.evaluation = eval.evaluation;
                        bestMove.move = move;
                    }
                    beta = Math.min(beta, eval.evaluation);
                }

                if (useAlphaBeta && beta <= alpha) {
                    break; // Alpha-beta cut-off
                }
            }

        // Transpositiontable
        transpositionTable.put(new BoardDepthKey(board, depth), bestMove);

        return bestMove;
    }

    public static String getMove(String b) {
        Board board = new Board(b);
        MoveEvaluation res = Game.iterativeDeepening(board, 6, true);
        return Move.moveToString(res.move);
    }

    public static void main(String[] args) {
        String move = getMove("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r");
        System.out.println("Best move: " + move );
    }
}

class MoveEvaluation {
    int evaluation;
    int[] move;

    MoveEvaluation(int evaluation, int[] move) {
        this.evaluation = evaluation;
        this.move = move;
    }
}

class BoardDepthKey {
    Board board;
    int depth;


    BoardDepthKey(Board board, int depth) {
        this.board = board;
        this.depth = depth;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardDepthKey key = (BoardDepthKey) o;
        return this.depth == key.depth && Objects.equals(board, key.board);
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(depth, board);
    }
}
