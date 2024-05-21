import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Board board = new Board("b0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 b");
        while (true) {
            System.out.println(board.boardToFEN());
            Player hasWon = Game.thisPlayerHasWon(board);
            if (hasWon == Player.R){
                System.out.println("Red has won!");
                break;
            } 
            if(hasWon == Player.B){
                System.out.println("Blue has won!");
                break;
            } 
            //noch niemand hat gewonnen
            ArrayList<String> possbleMoves = Move.possiblemoves(board);
            String currentMove = Move.pickMove(possbleMoves);
            System.out.println(currentMove);
            String[] curr = currentMove.split("-");
            board.updateBoard(curr[0], curr[1]);
        }
    }
}
