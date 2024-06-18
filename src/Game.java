import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.System.currentTimeMillis;

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

    public static double timeManagment(Board board) {
        //abgelaufene Zeit herausfinden
        long time = TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis() - starttime);
        //early Game
        if(time < 21){
            if(board.numofPlayers() > 19){
                //wenig Zeit
            }else{

            }
        }
        //Middle Game
        else if(time < 91){
            if(board.numofPlayers() > 19){
                //wenig Zeit
            }else{

            }
        }
        //end Game
        else if(time < 110){
            if(board.numofPlayers() > 10){
                //wenig Zeit
            }else{

            }
        }
        //almost no time
        else{
            //wenig Zeit
        }
        return 0.0;
    }

    public static int timeToDepth(double time) {
        return Math.max(4, Math.min(6, (int) time));
    }

    private static boolean useAlphaBeta = true; // Change this to switch between algorithms

    //starttime in currenttimemillis
    static long starttime;
    public static long numOfSearchedZustand = 0;
    static HashMap<Integer, int[]> alphabetacutoffmove = new LinkedHashMap<>();

    static  int maxdepth;

    public static MoveEvaluation iterativeDeepening(Board startBoard, int maxDepth, boolean useAlphaBeta) {
        /*MoveEvaluation bestMoveEvaluation = new MoveEvaluation(0, "");
        bestMoveEvaluation = minimaxAlphaBeta(startBoard, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, useAlphaBeta, "");
        System.out.println("Anzhal Zustände: " + numOfSearchedZustand);
        return bestMoveEvaluation;

         */
        numOfSearchedZustand = 0;
        MoveEvaluation bestMoveEvaluation = new MoveEvaluation(0, new int[20]);
        for (int depth = 1; depth <= maxDepth; depth++) {

            maxdepth = depth;
            bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, useAlphaBeta, new int[20], new ArrayList<int[]>());
            MoveEvaluation finalBestMoveEvaluation = bestMoveEvaluation;

            linkedmoves = linkedmoves.stream().filter(x -> x[19] == finalBestMoveEvaluation.evaluation).toList();
            var bestmovepfadtrash = linkedmoves.stream().filter(i -> Arrays.toString(i).contains("" + finalBestMoveEvaluation.move[0]
                    + ", " + finalBestMoveEvaluation.move[1])).findFirst().get();

            int z = 0;
            for(int i = bestmovepfadtrash.length-2; i > 0; i--){
                if(bestmovepfadtrash[i] != 0 ){
                    bestmovepfad[z] = bestmovepfadtrash[i-1];
                    bestmovepfad[z+1] = bestmovepfadtrash[i];
                    i--;
                    z+=2;
                }
            }
            linkedmoves = new LinkedList<>();
        }

        return bestMoveEvaluation;
    }


    static int[] bestmovepfad = new int[20];
    static List<int[]> linkedmoves = new ArrayList<>();

    public static MoveEvaluation minimaxAlphaBeta(Board board, int depth, int alpha, int beta, boolean useAlphaBeta, int[] lastMove, ArrayList<int[]> currMoves) {
        if (depth == 0 || thisPlayerHasWon(board) != Player.EMPTY) {
            int evaluation = board.evaluate(currMoves.size() * 20);
            lastMove[19] = evaluation;

            linkedmoves.add(lastMove);

            return new MoveEvaluation(evaluation, lastMove);
        }

        ArrayList<int[]> allPossibleMoves = Move.possibleMoves(board);
        MoveEvaluation bestMove = new MoveEvaluation(board.blauIstDran ? Integer.MIN_VALUE : Integer.MAX_VALUE, new int[20]);

        if(allPossibleMoves.contains(alphabetacutoffmove.get(depth))){
            allPossibleMoves.remove(alphabetacutoffmove.get(depth));
            allPossibleMoves.add(0, alphabetacutoffmove.get(depth));
        }

        int frompo = bestmovepfad[(maxdepth-depth)*2];
        int topo = bestmovepfad[(maxdepth-depth)*2+1];
        int z2 = 0;

        if(bestmovepfad[(maxdepth-depth)*2] != 0){
            allPossibleMoves.add(0, new int[]{bestmovepfad[(maxdepth-depth)*2], bestmovepfad[(maxdepth-depth)*2+1]});
            bestmovepfad[(maxdepth-depth)*2] = 0;
        }

        for (int[] moves : allPossibleMoves) {
            if(moves[0] == frompo && moves[1] == topo & z2!= 0) {

            }else{
            z2++;
            int[] move = new int[20];
            move[0] = moves[0];
            move[1] = moves[1];
            int z = 0;
            int z1 = 2;
            if(lastMove[0] != 0){
                while(lastMove[z] != 0){
                    move[z1] = lastMove[z];
                    move[z1+1] = lastMove[z+1];
                    z += 2;
                    z1 += 2;
                }
            }
            Board nextBoard = new Board(board.blauzweiteebene, board.rotzweiteebene, board.blauersteebene, board.rotersteebene);
            nextBoard.updateBoard(move[0], move[1]);
            nextBoard.blauIstDran = !board.blauIstDran;
            currMoves.add(move);
            MoveEvaluation eval = minimaxAlphaBeta(nextBoard, depth - 1, alpha, beta,  useAlphaBeta, move, currMoves);

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
                alphabetacutoffmove.put(depth, move);
                break; // Alpha-beta cut-off
            }
        }}

        return bestMove;
    }

    public static String getMove(String b) {
        Board board = new Board(b);
        MoveEvaluation res = Game.iterativeDeepening(board, 5, true);
        return Move.moveToString(res.move);
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
    int[] move;

    MoveEvaluation(int evaluation, int[] move) {
        this.evaluation = evaluation;
        this.move = move;
    }
}
