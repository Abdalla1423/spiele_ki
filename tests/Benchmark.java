package tests;

public class Benchmark {

    public long benchmarkOne() { // Early Game
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++)  {
            Board b = new Board();
            b.readfen("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r");
            b.possiblemoves();
        }
        long endTime = System.nanoTime();

        return (endTime - startTime);
    }

    public long benchmarkTwo() { // Mid Game
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++)  {
            Board b = new Board();
            b.readfen("b02b01b0/3b01b02/b02b02b01/b01b05/5r02/1r02r02r0/2rrr02r01/r03r01 b");
            b.possiblemoves();
        }
        long endTime = System.nanoTime();

        return (endTime - startTime);
    }

    public long benchmarkThree() { // End Game
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++)  {
            Board b = new Board();
            b.readfen("b01b01b01/8/2b03b01/1b06/1r01b01b02/3r04/2r03r01/4r01 r");
            b.possiblemoves();
        }
        long endTime = System.nanoTime();

        return (endTime - startTime);
    }

    public static void main(String[] args) {
        Benchmark b = new Benchmark();
        System.out.println(b.benchmarkOne());
        System.out.println(b.benchmarkTwo());
        System.out.println(b.benchmarkThree());
    }
 }
