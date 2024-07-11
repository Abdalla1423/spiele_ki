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
        //wenn noch niemand gewonnen hat
        return Player.EMPTY;
    }

    public static int timeManagment(Board board, long time) {
        //abgelaufene Zeit herausfinden
        int numofplayers = board.numofPlayers();
        return 6;
        //early Game
        /*if(numofplayers > 22){
            return 5;
        } else if(numofplayers > 20) {
            return 6;
        //Mid-Game
        } else if(numofplayers > 18) {
            return 7;
        } else if(numofplayers > 16) {
            return 7;
        //End-Game
        } else if(numofplayers > 14) {
            return 6;
        } else if(numofplayers > 12) {
            return 5;
        }

         */
    }

    public static long numOfSearchedZustand = 0;
    static HashMap<Integer, int[]> alphabetacutoffmove = new LinkedHashMap<>();

    static  int maxdepth; // zu currentdepth umbennenen
    static HashMap<BoardDepthKey, MoveEvaluation> transpositionTable = new HashMap<>();

    public static MoveEvaluation iterativeDeepening(Board startBoard, int maxDepth, boolean useAlphaBeta) {
        /*MoveEvaluation bestMoveEvaluation = new MoveEvaluation(0, "");
        bestMoveEvaluation = minimaxAlphaBeta(startBoard, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, useAlphaBeta, "");
        System.out.println("Anzhal Zustände: " + numOfSearchedZustand);
        return bestMoveEvaluation;
         */

        //maxdepth soll vom timemanagement ausgewählt werden

        int initialWindowSize = 50;
        int windowSizeIncrement = 70;
        int initialEvaluation = 0;


        numOfSearchedZustand = 0;
        MoveEvaluation bestMoveEvaluation = new MoveEvaluation(0, new int[20]);
        for (int depth = 1; depth <= maxDepth; depth++) {
            maxdepth = depth;

            int alphaAspirationWindow = initialEvaluation - initialWindowSize;
            int betaAspirationWindow = initialEvaluation + initialWindowSize;

            // Try the initial window size
            bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, alphaAspirationWindow, betaAspirationWindow, useAlphaBeta, new int[20], new ArrayList<int[]>());
            initialEvaluation = bestMoveEvaluation.evaluation;

            //System.out.println("Alpha: " + alphaAspirationWindow + ", Beta: " + betaAspirationWindow + ", Evaluation: " + bestMoveEvaluation.evaluation + ", Move: " + Move.moveToString(bestMoveEvaluation.move));

            //initialEvaluation = bestMoveEvaluation.evaluation;

            // Check if the result is outside the window
            if (bestMoveEvaluation.evaluation <= alphaAspirationWindow || bestMoveEvaluation.evaluation >= betaAspirationWindow) {
                // Increase the window size and try again
                alphaAspirationWindow = initialEvaluation - (initialWindowSize + windowSizeIncrement);
                betaAspirationWindow = initialEvaluation + (initialWindowSize + windowSizeIncrement);

                bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, alphaAspirationWindow, betaAspirationWindow, useAlphaBeta, new int[20], new ArrayList<int[]>());
                initialEvaluation = bestMoveEvaluation.evaluation;
                //System.out.println("Alpha: " + alphaAspirationWindow + ", Beta: " + betaAspirationWindow + ", Evaluation: " + bestMoveEvaluation.evaluation + ", Move: " + Move.moveToString(bestMoveEvaluation.move));


                // If still outside the window, fall back to normal alpha-beta
                if (bestMoveEvaluation.evaluation <= alphaAspirationWindow || bestMoveEvaluation.evaluation >= betaAspirationWindow) {
                    bestMoveEvaluation = minimaxAlphaBeta(startBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, useAlphaBeta, new int[20], new ArrayList<int[]>());
                    initialEvaluation = bestMoveEvaluation.evaluation;
                    //System.out.println("Alpha: " + alphaAspirationWindow + ", Beta: " + betaAspirationWindow + ", Evaluation: " + bestMoveEvaluation.evaluation + ", Move: " + Move.moveToString(bestMoveEvaluation.move));
                }
            }
            //System.out.println("Alpha: " + alphaAspirationWindow + ", Beta: " + betaAspirationWindow + ", Evaluation: " + bestMoveEvaluation.evaluation + ", Move: " + Move.moveToString(bestMoveEvaluation.move));


            // Update der anfänglichen Schätzung für die nächste Iterationstiefe


            //Umdrehen des Arrays damit Züge in richtiger Reihenfolge
            int[] bestmovepfadtrash = bestMoveEvaluation.move;
            int z = 0;
            for(int i = bestmovepfadtrash.length-2; i > 0; i--) {
                if(bestmovepfadtrash[i] != 0 ){
                    bestmovepfad[z] = bestmovepfadtrash[i-1];
                    bestmovepfad[z+1] = bestmovepfadtrash[i];
                    i--;
                    z+=2;
                }
            }

            bestMoveEvaluation.move[0] = bestmovepfad[0];
            bestMoveEvaluation.move[1] = bestmovepfad[1];

        }

        return bestMoveEvaluation;
    }

    static int[] bestmovepfad = new int[20];

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
            if (moves[0] == frompo && moves[1] == topo & z2!= 0) {
                //Zug war der beste zug und wurde an erster Stelle gesetzt
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
                MoveEvaluation eval;

                // Transpositiontable
                BoardDepthKey key = new BoardDepthKey(board, depth, alpha, beta);
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
                    alphabetacutoffmove.put(depth, move);
                    break; // Alpha-beta cut-off
                }
            }
        }

        // Transpositiontable
        //System.out.println("Statements:" + depth + " " + alpha + " " + beta);
        transpositionTable.put(new BoardDepthKey(board, depth, alpha, beta), bestMove);

        return bestMove;
    }

    public static String getMove(String b, int time) {
        Board board = new Board(b);
        int maxDepth = timeManagment(board, time);
        MoveEvaluation res = Game.iterativeDeepening(board, maxDepth, true);
        return Move.moveToString(res.move);
    }

    public static void main(String[] args) {
        // Board startBoard = new Board("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r");
        // MoveEvaluation bestMoveEvaluation = iterativeDeepening(startBoard, 5, useAlphaBeta);
        String move = getMove("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r", 100000);
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
    int alpha;
    int beta;

    BoardDepthKey(Board board, int depth, int alpha, int beta) {
        this.board = board;
        this.depth = depth;
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardDepthKey key = (BoardDepthKey) o;
        return this.depth == key.depth && this.alpha == key.alpha && this.beta == key.beta && Objects.equals(board, key.board);
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(depth, board, alpha, beta);
    }
}