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


    public static int iterativeDeepening(Board startBoard, int maxDepth, boolean useAlphaBeta) {
        int bestValue = 0;
        for (int depth = 1; depth <= maxDepth; depth++) {
            bestValue = minimaxAlphaBeta(startBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, useAlphaBeta, "");
        }
        return bestValue;
    }

    public static int minimaxAlphaBeta(Board board, int depth, int alpha, int beta, boolean maximizingPlayer, boolean useAlphaBeta, String lastMove) {
        if (depth == 0 || thisPlayerHasWon(board) != Player.EMPTY) {
            return board.evaluate();
        }

        ArrayList <String> allPossibleMoves = Move.possiblemoves(board);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (String move : allPossibleMoves) {
                Board nextBoard = new Board(board.blauzweiteebene, board.rotzweiteebene, board.blauersteebene, board.rotersteebene);
                String[] curr = move.split("-");
                board.updateBoard(curr[0], curr[1]);
                int eval = minimaxAlphaBeta(nextBoard, depth - 1, alpha, beta, false, useAlphaBeta, move);
                maxEval = Math.max(maxEval, eval);
                if (useAlphaBeta) {
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break; // Beta cut-off
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (String move : allPossibleMoves) {
                Board nextBoard = new Board(board.blauzweiteebene, board.rotzweiteebene, board.blauersteebene, board.rotersteebene);
                String[] curr = move.split("-");
                board.updateBoard(curr[0], curr[1]);
                int eval = minimaxAlphaBeta(nextBoard, depth - 1, alpha, beta, true, useAlphaBeta, move);
                minEval = Math.min(minEval, eval);
                if (useAlphaBeta) {
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break; // Alpha cut-off
                    }
                }
            }
            return minEval;
        }
    }

    public static void main(String[] args) {
        Board startBoard = new Board("b0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 b");
        int maxDepth = 3;
        int bestValue = iterativeDeepening(startBoard, maxDepth, useAlphaBeta);
        System.out.println("Best value: " + bestValue);
    }
}