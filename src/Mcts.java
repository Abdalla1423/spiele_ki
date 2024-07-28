import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

class MonteCarloTreeSearch {
    static final int TIME_LIMIT = 6000;
    static  final int WIN_SCORE = 1;
    public class Node {
        Node parent;
        ArrayList<Node> childNodes;
        boolean blauIstDran; //true, wenn Spieler blau dran ist, zum Zeitpunkt wo der move dieses Nodes ausgeführt wird
        int score;
        int visitCount;
        int [] move;
        Board currentBoard; //Board, nachdem der move dieser node ausgeführt wurde

        public Node(Node parent, boolean blauIstDran, int [] move, Board currentBoard) {
            this.parent = parent;
            childNodes = new ArrayList<>();
            this.blauIstDran = blauIstDran;
            score = 0;
            visitCount = 0;
            this.move = move;
            this.currentBoard= currentBoard;
        }
    }

    public static String getMctsResult(Board board){
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch();
        Node rootnode= mcts.new Node(null, !board.blauIstDran, null, board);
        Node winnerNode= mcts.monteCarloTreeSearch(rootnode);
        return Move.moveToString(winnerNode.move);
    }

    public Node monteCarloTreeSearch(Node rootNode) {
        double timeLimit = System.currentTimeMillis() + TIME_LIMIT;
        addChildNodes(rootNode);

        while (System.currentTimeMillis() < timeLimit) {
            Node promisingNode;
            promisingNode = getPromisingNode(rootNode);

            if (promisingNode.childNodes.size() == 0) {
                addChildNodes(promisingNode);
            }

            simulateRandomPlay(promisingNode);
        }

        return getWinnerNode(rootNode);
    }

    public Board deepCopyBoard(Board original){
        Board nextBoard = new Board(original.blauzweiteebene, original.rotzweiteebene, original.blauersteebene, original.rotersteebene);
        nextBoard.blauIstDran = original.blauIstDran;
        return nextBoard;
    }

    public void addChildNodes(Node node) {
        if (Game.thisPlayerHasWon(node.currentBoard) == Player.EMPTY){ //wenn ein Spieler schon gewonnen hat, muss die Suche abgebrochen werden (Test 9 failed ohne diese Bedingung)
            ArrayList<int[]> possibleMoves = Move.possibleMoves(node.currentBoard);
            for (int i = 0; i < possibleMoves.size(); i++) {
                int[] chosenMove= possibleMoves.get(i);
                Board updatedBoard = deepCopyBoard(node.currentBoard);
                updatedBoard.updateBoard(chosenMove[0], chosenMove[1]);
                updatedBoard.blauIstDran= !node.currentBoard.blauIstDran;
                node.childNodes.add(new Node(node, !node.blauIstDran, chosenMove, updatedBoard));
            }
        }
    }
    public Node getPromisingNode(Node rootNode) {
        Node promisingNode = rootNode;
        while (promisingNode.childNodes.size() != 0) {
            double uctIndex = Double.MIN_VALUE;
            Node bestNode = null;

            for (int i = 0; i < promisingNode.childNodes.size(); i++) {
                Node childNode = promisingNode.childNodes.get(i);
                double uctTemporary;

                if (childNode.visitCount == 0) {
                    bestNode = childNode;
                    break;
                }

                uctTemporary = ((double) childNode.score / (double) childNode.visitCount) + 1.41 * Math.sqrt(Math.log(promisingNode.visitCount) / (double) childNode.visitCount);

                if (uctTemporary > uctIndex) {
                    uctIndex = uctTemporary;
                    bestNode = childNode;
                }
            }
            if(bestNode == null) {
                break;
            }
            promisingNode = bestNode;
        }
        return promisingNode;
    }

    public void simulateRandomPlay(Node promisingNode) {
        Board gameState = deepCopyBoard(promisingNode.currentBoard);
        boolean BlueWon;
        while (true) {
            ArrayList<int[]> possibleMoves = Move.possibleMoves(gameState);
            if (!possibleMoves.isEmpty()){
                Player hasWon = Game.thisPlayerHasWon(gameState);
                if (hasWon != Player.EMPTY) {
                    BlueWon = (hasWon == Player.B);
                    break;
                }
                int[] currentMove = Move.pickRandomMove(possibleMoves);
                gameState.updateBoard(currentMove[0], currentMove[1]);
                gameState.blauIstDran= !gameState.blauIstDran;
            } else {
                BlueWon = !gameState.blauIstDran;
                break;
            }
        }
        backPropagation(promisingNode, BlueWon);
    }

    public void backPropagation(Node current, Boolean BlueWon){
        current.visitCount++;
        if (current.blauIstDran == BlueWon) {
            current.score += WIN_SCORE;
        } else {
            current.score -= WIN_SCORE;
        }
        if (current.parent == null){
            return;
        }
        backPropagation(current.parent, BlueWon);
    }

    public Node getWinnerNode(Node rootNode) {
        return Collections.max(rootNode.childNodes, Comparator.comparing(c -> c.score));
    }

    public void printScores(Node rootNode) {
        System.out.println("N.\tScore\t\tVisits");
        for (int i = 0; i < rootNode.childNodes.size(); i++) {
            System.out.printf("%02d\t%d\t\t%d%n", i + 1, rootNode.childNodes.get(i).score, rootNode.childNodes.get(i).visitCount);
            System.out.println(Move.moveToString(rootNode.childNodes.get(i).move));
        }
    }
}
