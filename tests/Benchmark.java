import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Benchmark {

    public long benchmarkOne() { // Early Game
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++)  {
            Board b = new Board("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r");
            Move.convertMoves(Move.possibleMoves(b));
        }
        long endTime = System.nanoTime();

        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }

    public long benchmarkTwo() { // Mid Game
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++)  {
            Board b = new Board("b02b01b0/3b01b02/b02b02b01/b01b05/5r02/1r02r02r0/2rrr02r01/r03r01 b");
            Move.convertMoves(Move.possibleMoves(b));
        }
        long endTime = System.nanoTime();

        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }

    public long benchmarkThree() { // End Game
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++)  {
            Board b = new Board("b01b01b01/8/2b03b01/1b06/1r01b01b02/3r04/2r03r01/4r01 r");
            Move.convertMoves(Move.possibleMoves(b));
        }
        long endTime = System.nanoTime();

        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }

    public static void main(String[] args) {
        Benchmark b = new Benchmark();

        long benchOne = 0;
        for (int i = 0; i < 10; i++) {
            benchOne = benchOne + b.benchmarkOne();
        }
        System.out.println("Benchmark Early Game");
        System.out.println("10000 iterations in milliseconds: " + benchOne);
        System.out.println("Average time in milliseconds " + (float) benchOne / 10000);
        System.out.println(" ");

        long benchTwo = 0;
        for (int i = 0; i < 10; i++) {
            benchTwo = benchTwo + b.benchmarkTwo();
        }
        System.out.println("Benchmark Mid Game");
        System.out.println("10000 iterations in milliseconds: " + benchTwo);
        System.out.println("Average time in milliseconds " + (float) benchTwo / 10000);
        System.out.println(" ");

        long benchThree = 0;
        for (int i = 0; i < 10; i++) {
            benchThree = benchThree + b.benchmarkThree();
        }
        System.out.println("Benchmark Late Game");
        System.out.println("10000 iterations in milliseconds: " + benchThree);
        System.out.println("Average time in milliseconds " + (float) benchThree / 10000);
        System.out.println(" ");
    }
 }
