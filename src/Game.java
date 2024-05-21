import java.util.ArrayList;

public class Game {
    //gibt Player R zurück, wenn rot gewonnen hat; Player B zurück, wenn blau gewonnen hat; Player EMPTY zurück, wenn noch
    // niemand gewonnen hat
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

    private static boolean useAlphaBeta = true; // Change this to switch between algorithms


    public static MoveEvaluation iterativeDeepening(Board startBoard, int maxDepth, boolean useAlphaBeta) {
        MoveEvaluation bestMoveEvaluation = new MoveEvaluation(0, "");
        for (int depth = 1; depth <= maxDepth; depth++) {
            bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, useAlphaBeta, "");
        }
        return bestMoveEvaluation;
    }

    public static MoveEvaluation minimaxAlphaBeta(Board board, int depth, int alpha, int beta, boolean maximizingPlayer, boolean useAlphaBeta, String lastMove) {
        if (depth == 0 || thisPlayerHasWon(board) != Player.EMPTY) {
            return new MoveEvaluation(board.evaluate(), lastMove);
        }

        ArrayList<String> allPossibleMoves = Move.possiblemoves(board);
        MoveEvaluation bestMove = new MoveEvaluation(maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE, "");

        for (String move : allPossibleMoves) {
            Board nextBoard = new Board(board.blauzweiteebene, board.rotzweiteebene, board.blauersteebene, board.rotersteebene);
            String[] curr = move.split("-");
            nextBoard.updateBoard(curr[0], curr[1]);
            nextBoard.blauIstDran = !board.blauIstDran;
            MoveEvaluation eval = minimaxAlphaBeta(nextBoard, depth - 1, alpha, beta, !maximizingPlayer, useAlphaBeta, move);

            if (maximizingPlayer) {
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

        return bestMove;
    }
    public static void main(String[] args) {
        Board startBoard = new Board("b0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 b");
        int maxDepth = 3;
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
