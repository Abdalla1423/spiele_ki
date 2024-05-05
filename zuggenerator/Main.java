import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Board b = new Board();
        b.readfen("2rr3/5r02/1rr1rr2r0r0/2rb3b01/2r0b04/5b0bb1/2bb2b02/3b02");
        System.out.println((b.indextoboardfield(0,0)));
    }
}