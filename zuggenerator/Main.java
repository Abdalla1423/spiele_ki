package tests;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Board b = new Board();
        b.readfen("b0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 b");
        while (true) {
            System.out.println(b.boardToFEN());
            String hasWon = b.thisPlayerHasWon();
            if (hasWon.equals("Player blue has won!") || hasWon.equals("Player red has won!")){
                System.out.println(hasWon);
                break;
            }
            //noch niemand hat gewonnen
            String currentMove = b.pickMove(b.possiblemoves());
            System.out.println(currentMove);
            String[] curr = currentMove.split("-");
            b.updateBoard(curr[0], curr[1]);
        }
    }
}
