import java.util.ArrayList;

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
        //gucken, ob noch possibleMoves hat
        if (Move.possibleMoves(board).isEmpty()) {
            if (board.blauIstDran){
                return Player.R;
            } else {
                return Player.B;
            }
        }
        //wenn noch niemand gewonnen hat
        return Player.EMPTY;
    }

    private static boolean useAlphaBeta = false; // Change this to switch between algorithms

    private static long numOfSearchedZustand = 0;

    public static MoveEvaluation iterativeDeepening(Board startBoard, int maxDepth, boolean useAlphaBeta) {
        /*MoveEvaluation bestMoveEvaluation = new MoveEvaluation(0, "");
        bestMoveEvaluation = minimaxAlphaBeta(startBoard, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, useAlphaBeta, "");
        System.out.println("Anzhal Zustände: " + numOfSearchedZustand);
        return bestMoveEvaluation;

         */

        MoveEvaluation bestMoveEvaluation = new MoveEvaluation(0, "");
        for (int depth = 1; depth <= maxDepth; depth++) {
            bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, useAlphaBeta, "");
        }
        return bestMoveEvaluation;
    }

    public static MoveEvaluation minimaxAlphaBeta(Board board, int depth, int alpha, int beta, boolean useAlphaBeta, String lastMove) {
        if (depth == 0 || thisPlayerHasWon(board) != Player.EMPTY) {
            return new MoveEvaluation(board.evaluate(), lastMove);
        }

        ArrayList<int[]> allPossibleMoves = Move.possibleMoves(board);
        MoveEvaluation bestMove = new MoveEvaluation(board.blauIstDran ? Integer.MIN_VALUE : Integer.MAX_VALUE, "");

        for (int[] move : allPossibleMoves) {
            Board nextBoard = new Board(board.blauzweiteebene, board.rotzweiteebene, board.blauersteebene, board.rotersteebene);
            nextBoard.updateBoard(move[0], move[1]);
            nextBoard.blauIstDran = !board.blauIstDran;
            String moveString = Move.moveToString(move);
            MoveEvaluation eval = minimaxAlphaBeta(nextBoard, depth - 1, alpha, beta,  useAlphaBeta, moveString);
            // numOfSearchedZustand++;

            if (board.blauIstDran) {
                if (eval.evaluation > bestMove.evaluation) {
                    bestMove.evaluation = eval.evaluation;
                    bestMove.move = moveString;
                }
                alpha = Math.max(alpha, eval.evaluation);
            } else {
                if (eval.evaluation < bestMove.evaluation) {
                    bestMove.evaluation = eval.evaluation;
                    bestMove.move = moveString;
                }
                beta = Math.min(beta, eval.evaluation);
            }

            if (useAlphaBeta && beta <= alpha) {
                break; // Alpha-beta cut-off
            }
        }

        return bestMove;
    }
    public static void main(String[] args) {
        Board startBoard = new Board("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r");
        int maxDepth = 1;
        MoveEvaluation bestMoveEvaluation = iterativeDeepening(startBoard, maxDepth, useAlphaBeta);
        System.out.println("Best move: " + bestMoveEvaluation.move + " with value: " + bestMoveEvaluation.evaluation);
    }
}

class MoveEvaluation {
    int evaluation;
    String move;

    MoveEvaluation(int evaluation, String move) {
        this.evaluation = evaluation;
        this.move = move;
    }
}
