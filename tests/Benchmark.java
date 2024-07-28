
public class Benchmark {
    public void benchmarkRuntime(String board, String gameState) { // Early Game
        System.out.println("Benchmark " + gameState);
        System.out.println(" ");
        Board b = new Board(board);

        // Zuggenerator
        long startTimeZug = System.nanoTime();
        for (int i = 0; i < 10000; i++)  {
            Move.possibleMoves(b);
        }
        long endTimeZug = System.nanoTime();
        float resultZuggenerator = (float) (endTimeZug - startTimeZug) / 1_000_000;

        System.out.println("Zuggenerator");
        System.out.println("    10000 Iterationen: " + resultZuggenerator + "ms");
        System.out.println("    Durchschnittliche Laufzeit: " + resultZuggenerator / 10000 + "ms");
        System.out.println(" ");



        // Bewertungsfunktion
        MoveEvaluation res = null;
        long startTimeBew = System.nanoTime();
        for (int i = 0; i < 10000; i++)  {
            res = Game.iterativeDeepening(b, 1, true);
        }
        long endTimeBew = System.nanoTime();
        float resultBewertungsfunktion = (float) (endTimeBew - startTimeBew) / 1_000_000;

        System.out.println("Bewertungsfunktion");
        System.out.println("    10000 Iterationen: " + resultBewertungsfunktion + "ms");
        System.out.println("    Durchschnittliche Laufzeit: " + resultBewertungsfunktion / 10000 + "ms");
        System.out.println("    Bester Zug: " + Move.moveToString(res.move));
        System.out.println(" ");


        System.out.println("Min-Max");
        // Min-Max
        for (int depth = 2; depth <= 5; depth++) {
            long startTimeMinMax = System.nanoTime();

            res = Game.iterativeDeepening(b, depth, false);

            long endTimeMinMax= System.nanoTime();
            float resultMinMax = (float) (endTimeMinMax - startTimeMinMax) / 1_000_000;

            long numOfSearchedZustandMM = Game.numOfSearchedZustand;
            float zustandProMs = numOfSearchedZustandMM / resultMinMax;

            System.out.println("    Gesamtlaufzeit Tiefe " + depth +": " + resultMinMax + "ms");
            System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM);
            System.out.println("    Zustände pro ms: " + zustandProMs);
            System.out.println("    Bester Zug: " + Move.moveToString(res.move));
            System.out.println(" ");
        }

        System.out.println("Alpha-Beta");
        // AlphaBeta
        for (int depth = 1; depth <= 5; depth++) {
            long startTimeMinMax = System.nanoTime();

            res = Game.iterativeDeepening(b, depth, true);

            long endTimeMinMax= System.nanoTime();
            float resultMinMax = (float) (endTimeMinMax - startTimeMinMax) / 1_000_000;

            long numOfSearchedZustandMM = Game.numOfSearchedZustand;
            float zustandProMs = numOfSearchedZustandMM / resultMinMax;

            System.out.println("    Gesamtlaufzeit Tiefe " + depth +": " + resultMinMax + "ms");
            System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM);
            System.out.println("    Zustände pro ms: " + zustandProMs);
            System.out.println("    Bester Zug: " + Move.moveToString(res.move));
            System.out.println(" ");
        }

    }

    public static void main(String[] args) {
        Benchmark b = new Benchmark();

        b.benchmarkRuntime("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r", "Early-Game");
        b.benchmarkRuntime("b02b01b0/3b01b02/b02b02b01/b01b05/5r02/1r02r02r0/2rrr02r01/r03r01 b","Mid-Game");
        b.benchmarkRuntime("b01b01b01/8/2b03b01/1b06/1r01b01b02/3r04/2r03r01/4r01 r", "Late-Game");
    }
 }
