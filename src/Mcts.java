import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class MonteCarloTreeSearch {
    // Constants and variables
    static final int TIME_LIMIT = 3000; //Zeit für die Suche pro Zug
    static  final int WIN_SCORE = 1;

    /**
     * Diese klasse repräsentiert einen Konten (Spielzug) im zu durchsuchenden Baum (der alle möglichen Züge enthält)
     */
    public class Node {
        // Constants and variables
        Node parent;
        ArrayList<Node> childNodes;
        boolean blauIstDran; //true, wenn Spieler blau dran ist, zum Zeitpunkt wo der move dieses Nodes ausgeführt wird
        int score;
        int visitCount;
        int [] move; //Zug, den diese Node darstellt
        Board currentBoard; //Board, nachdem der Move dieser Node ausgeführt wurde

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

    /**
     * Diese Methode dient als einfachen Aufruf der MCTS-Suche, insb. zum Testen und für den MCTS-Client wurde dies genutzt
     * @param board  Der Anfangszustand des Spielboards, für das ein Zug gefunden werden soll
     * @return Der beste (laut MCTS-Suche) Spielzug als String dargestellt.
     */
    public static String getMctsResult(Board board){
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch();
        Node rootnode= mcts.new Node(null, !board.blauIstDran, null, board);
        Node winnerNode= mcts.monteCarloTreeSearch(rootnode);
        return Move.moveToString(winnerNode.move);
    }

    /**
     * Dies ist die eigentliche MCTS-Suche
     * @param rootNode  Enthält nur das Spielfeld im Anfangszustand und als childNodes werden in dieser Methode alle
     *                 möglichen züge hinzugfügt, alle anderen variablen sollten nicht beachtet werden
     * @return Der beste (laut MCTS-Suche) Spielzug als Node dargestellt.
     */
    public Node monteCarloTreeSearch(Node rootNode) {
        double timeLimit = System.currentTimeMillis() + TIME_LIMIT;
        addChildNodes(rootNode);

        while (System.currentTimeMillis() < timeLimit) {
            Node promisingNode;
            promisingNode = getPromisingNode(rootNode); //Selectionphase

            if (promisingNode.childNodes.size() == 0) {
                addChildNodes(promisingNode);    //Expansionphase
            }

            simulateRandomPlay(promisingNode);   //Simulationsphase und Backpropagation
        }

        return getWinnerNode(rootNode);
    }

    /**
     * Erstellt eine exakte Kopie vom übergebenen Board, allerdings ein neues Objekt mit neuer Referenz, sodass
     * Änderungen am neuen Board nicht das originale Board verändern
     * @param original  Das originale Spielboard, das kopiert aber nicht verändert werden soll
     * @return die exakte Kopie von original
     */
    public Board deepCopyBoard(Board original){
        Board nextBoard = new Board(original.blauzweiteebene, original.rotzweiteebene, original.blauersteebene, original.rotersteebene);
        nextBoard.blauIstDran = original.blauIstDran;
        return nextBoard;
    }

    /**
     * Erstellt die Kindknoten des übergebenem Knoten, also alle möglichen Züge, nachdem der Zug des übergebenen
     * Knotens ausgeführt wurde.
     * @param node  Knoten, dem Kindknoten angehängt werden sollen
     */
    public void addChildNodes(Node node) {
        if (Game.thisPlayerHasWon(node.currentBoard) == Player.EMPTY ){ //wenn ein Spieler schon gewonnen hat, soll kein weiterer Move ausgeführt werden (Test 9 failed ohne diese Bedingung)
            ArrayList<int[]> possibleMoves = Move.possibleMoves(node.currentBoard);
            for (int i = 0; i < possibleMoves.size(); i++) {
                int[] chosenMove= possibleMoves.get(i);
                Board updatedBoard = deepCopyBoard(node.currentBoard); // das Board von node selbst soll natürlich nicht verändert werden, deshalb deepCopy
                updatedBoard.updateBoard(chosenMove[0], chosenMove[1]);
                updatedBoard.blauIstDran= !node.currentBoard.blauIstDran; //Da dieser Zug direkt nach dem Zug des Elternknotens ausgeführt wird, ist der andere Spieler dran
                node.childNodes.add(new Node(node, !node.blauIstDran, chosenMove, updatedBoard));
            }
        }
    }

    /**
     * Sucht aus, welcher Zug vertieft werden soll. Die Suche findet nach dem "UCT"-Prinzip statt.
     * @param rootNode  die rootNode des gesamten Baums als Startpunkt der Suche
     * @return der nach dem "UCT"-Prinzip ausgewählte Knoten
     */
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

    /**
     * Simuliert ein rein zufälliges Spiel basierend auf dem aktuellen Spielzustand, damit dieser bewertet werden kann
     * @param promisingNode der knoten der in der Selectionphase ausgewählt wurde
     */
    public void simulateRandomPlay(Node promisingNode) {
        Board gameState = deepCopyBoard(promisingNode.currentBoard); //das Board des KNotens selbst soll natürlich unverändert bleiben
        boolean BlueWon; //speichert, welcher Spieler Gewinner ist
        while (true) {
            ArrayList<int[]> possibleMoves = Move.possibleMoves(gameState);
            if (!possibleMoves.isEmpty()){
                Player hasWon = Game.thisPlayerHasWon(gameState);
                if (hasWon != Player.EMPTY) { // Spiel ist beendet
                    BlueWon = (hasWon == Player.B);
                    break;
                }
                int[] currentMove = Move.pickRandomMove(possibleMoves);
                gameState.updateBoard(currentMove[0], currentMove[1]);
                gameState.blauIstDran= !gameState.blauIstDran;
            } else { // kein möglicher Spielzug mehr, Spiel ist vorbei
                BlueWon = !gameState.blauIstDran; // wenn blau dran ist, hat er verloren, sonst rot
                break;
            }
        }
        backPropagation(promisingNode, BlueWon);
    }

    /**
     * Aktualisiert score und visitCount (Backpropagation).
     * @param current Knoten, dessen score und visitCount aktualisiert werden soll
     * @param BlueWon gibt an, welcher Spieler bei der Simulation gewonnen hat
     */
    public void backPropagation(Node current, Boolean BlueWon){
        current.visitCount++; //egal ob sieg oder Niederlage, visitCount wird aufjedenfall erhöht
        if (current.blauIstDran == BlueWon) {
            current.score += WIN_SCORE;
        }
        if (current.parent == null){ // Ende der Rekursion
            return;
        }
        backPropagation(current.parent, BlueWon);
    }

    /**
     * @param rootNode  die rootNode des gesamten Baums
     * @return die Node, die den bestbewertesten Zug enthält
     */
    public Node getWinnerNode(Node rootNode) {
        return Collections.max(rootNode.childNodes, Comparator.comparing(c -> c.score));
    }

    /**
     * Printet die Ergebnisse der Suche aauf die Konsole; hilfreich zum Verständnis.
     * @param rootNode  die rootNode des gesamten Baums
     */
    public void printScores(Node rootNode) {
        System.out.println("N.\tScore\t\tVisits");
        for (int i = 0; i < rootNode.childNodes.size(); i++) {
            System.out.printf("%02d\t%d\t\t%d%n", i + 1, rootNode.childNodes.get(i).score, rootNode.childNodes.get(i).visitCount);
            System.out.println(Move.moveToString(rootNode.childNodes.get(i).move));
        }
    }
}
