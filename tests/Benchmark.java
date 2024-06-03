package tests;

public class Benchmark {
    public void benchmarkOneRuntime() { // Early Game
        System.out.println("Benchmark Early Game");
        System.out.println(" ");
        Board b = new Board("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r");

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
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");



        // Min-Max
        // Tiefe 2
        long startTimeMinMax2 = System.nanoTime();

        res = Game.iterativeDeepening(b, 2, false);

        long endTimeMinMax2= System.nanoTime();
        float resultMinMax2 = (float) (endTimeMinMax2 - startTimeMinMax2) / 1_000_000;

        long numOfSearchedZustandMM2 = Game.numOfSearchedZustand;
        float zustandProMs2 = numOfSearchedZustandMM2 / resultMinMax2;

        System.out.println("Min-Max");
        System.out.println("    Gesamtlaufzeit Tiefe 2: " + resultMinMax2 + "ms");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM2);
        System.out.println("    Zustände pro ms: " + zustandProMs2);
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 3
        long startTimeMinMax3 = System.nanoTime();

        res = Game.iterativeDeepening(b, 3, false);

        long endTimeMinMax3= System.nanoTime();
        float resultMinMax3 = (float) (endTimeMinMax3 - startTimeMinMax3) / 1_000_000;

        long numOfSearchedZustandMM3 = Game.numOfSearchedZustand;
        float zustandProMs3 = numOfSearchedZustandMM3 / resultMinMax3;

        System.out.println("    Gesamtlaufzeit Tiefe 3: " + resultMinMax3 + "ms");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM3);
        System.out.println("    Zustände pro ms: " + zustandProMs3);
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 4
        long startTimeMinMax4 = System.nanoTime();

        res = Game.iterativeDeepening(b, 4, false);

        long endTimeMinMax4 = System.nanoTime();
        float resultMinMax4 = (float) (endTimeMinMax4 - startTimeMinMax4) / 1_000_000;

        long numOfSearchedZustandMM4 = Game.numOfSearchedZustand;
        float zustandProMs4 = numOfSearchedZustandMM4 / resultMinMax4;

        System.out.println("    Gesamtlaufzeit Tiefe 4: " + resultMinMax4 + "ms");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM4);
        System.out.println("    Zustände pro ms: " + zustandProMs4);
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // AlphaBeta
        // Tiefe 2
        long startTimeAB2 = System.nanoTime();

        res = Game.iterativeDeepening(b, 2, true);

        long endTimeAB2 = System.nanoTime();
        float resultAB2 = (float) (endTimeAB2 - startTimeAB2) / 1_000_000;

        long numOfSearchedZustandAB2 = Game.numOfSearchedZustand;
        float zustandProMsAB2 = numOfSearchedZustandAB2 / resultAB2;

        System.out.println("Alpha Beta");
        System.out.println("    Gesamtlaufzeit Tiefe 2: " + resultAB2 + "ms (" + (resultAB2 - resultMinMax2) + "ms)");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandAB2 + " (" + (numOfSearchedZustandAB2 - numOfSearchedZustandMM2) + ")");
        System.out.println("    Zustände pro ms: " + zustandProMsAB2 + " (" + (zustandProMsAB2 - zustandProMs2) + ")");
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 3
        long startTimeAB3 = System.nanoTime();

        res = Game.iterativeDeepening(b, 3, true);

        long endTimeAB3= System.nanoTime();
        float resultAB3 = (float) (endTimeAB3 - startTimeAB3) / 1_000_000;

        long numOfSearchedZustandAB3 = Game.numOfSearchedZustand;
        float zustandProMsAB3 = numOfSearchedZustandAB3 / resultAB3;

        System.out.println("    Gesamtlaufzeit Tiefe 3: " + resultAB3 + "ms (" + (resultAB3 - resultMinMax3) + "ms)");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandAB3 + " (" + (numOfSearchedZustandAB3 - numOfSearchedZustandMM3) + ")");
        System.out.println("    Zustände pro ms: " + zustandProMsAB3 + " (" + -(zustandProMsAB3 - zustandProMs3) + ")");
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 4
        long startTimeAB4 = System.nanoTime();

        res = Game.iterativeDeepening(b, 4, true);

        long endTimeAB4 = System.nanoTime();
        float resultAB4 = (float) (endTimeAB4 - startTimeAB4) / 1_000_000;

        long numOfSearchedZustandAB4 = Game.numOfSearchedZustand;
        float zustandProMsAB4 = numOfSearchedZustandAB4 / resultAB4;

        System.out.println("    Gesamtlaufzeit Tiefe 4: " + resultAB4 + "ms (" + (resultAB4 - resultMinMax4) + "ms)");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandAB4 + " (" + (numOfSearchedZustandAB4 - numOfSearchedZustandMM4) + ")");
        System.out.println("    Zustände pro ms: " + zustandProMsAB4 + " (" + (zustandProMsAB4 - zustandProMs4) + ")");
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");
    }

    public void benchmarkTwoRuntime() { // Mid Game
        System.out.println("Benchmark Mid Game");
        System.out.println(" ");
        Board b = new Board("b02b01b0/3b01b02/b02b02b01/b01b05/5r02/1r02r02r0/2rrr02r01/r03r01 b");

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
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");



        // Min-Max
        // Tiefe 2
        long startTimeMinMax2 = System.nanoTime();

        res = Game.iterativeDeepening(b, 2, false);

        long endTimeMinMax2= System.nanoTime();
        float resultMinMax2 = (float) (endTimeMinMax2 - startTimeMinMax2) / 1_000_000;

        long numOfSearchedZustandMM2 = Game.numOfSearchedZustand;
        float zustandProMs2 = numOfSearchedZustandMM2 / resultMinMax2;

        System.out.println("Min-Max");
        System.out.println("    Gesamtlaufzeit Tiefe 2: " + resultMinMax2 + "ms");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM2);
        System.out.println("    Zustände pro ms: " + zustandProMs2);
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 3
        long startTimeMinMax3 = System.nanoTime();

        res = Game.iterativeDeepening(b, 3, false);

        long endTimeMinMax3= System.nanoTime();
        float resultMinMax3 = (float) (endTimeMinMax3 - startTimeMinMax3) / 1_000_000;

        long numOfSearchedZustandMM3 = Game.numOfSearchedZustand;
        float zustandProMs3 = numOfSearchedZustandMM3 / resultMinMax3;

        System.out.println("    Gesamtlaufzeit Tiefe 3: " + resultMinMax3 + "ms");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM3);
        System.out.println("    Zustände pro ms: " + zustandProMs3);
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 4
        long startTimeMinMax4 = System.nanoTime();

        res = Game.iterativeDeepening(b, 4, false);

        long endTimeMinMax4 = System.nanoTime();
        float resultMinMax4 = (float) (endTimeMinMax4 - startTimeMinMax4) / 1_000_000;

        long numOfSearchedZustandMM4 = Game.numOfSearchedZustand;
        float zustandProMs4 = numOfSearchedZustandMM4 / resultMinMax4;

        System.out.println("    Gesamtlaufzeit Tiefe 4: " + resultMinMax4 + "ms");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM4);
        System.out.println("    Zustände pro ms: " + zustandProMs4);
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // AlphaBeta
        // Tiefe 2
        long startTimeAB2 = System.nanoTime();

        res = Game.iterativeDeepening(b, 2, true);

        long endTimeAB2 = System.nanoTime();
        float resultAB2 = (float) (endTimeAB2 - startTimeAB2) / 1_000_000;

        long numOfSearchedZustandAB2 = Game.numOfSearchedZustand;
        float zustandProMsAB2 = numOfSearchedZustandAB2 / resultAB2;

        System.out.println("Alpha Beta");
        System.out.println("    Gesamtlaufzeit Tiefe 2: " + resultAB2 + "ms (" + (resultAB2 - resultMinMax2) + "ms)");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandAB2 + " (" + (numOfSearchedZustandAB2 - numOfSearchedZustandMM2) + ")");
        System.out.println("    Zustände pro ms: " + zustandProMsAB2 + " (" + (zustandProMsAB2 - zustandProMs2) + ")");
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 3
        long startTimeAB3 = System.nanoTime();

        res = Game.iterativeDeepening(b, 3, true);

        long endTimeAB3= System.nanoTime();
        float resultAB3 = (float) (endTimeAB3 - startTimeAB3) / 1_000_000;

        long numOfSearchedZustandAB3 = Game.numOfSearchedZustand;
        float zustandProMsAB3 = numOfSearchedZustandAB3 / resultAB3;

        System.out.println("    Gesamtlaufzeit Tiefe 3: " + resultAB3 + "ms (" + (resultAB3 - resultMinMax3) + "ms)");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandAB3 + " (" + (numOfSearchedZustandAB3 - numOfSearchedZustandMM3) + ")");
        System.out.println("    Zustände pro ms: " + zustandProMsAB3 + " (" + -(zustandProMsAB3 - zustandProMs3) + ")");
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 4
        long startTimeAB4 = System.nanoTime();

        res = Game.iterativeDeepening(b, 4, true);

        long endTimeAB4 = System.nanoTime();
        float resultAB4 = (float) (endTimeAB4 - startTimeAB4) / 1_000_000;

        long numOfSearchedZustandAB4 = Game.numOfSearchedZustand;
        float zustandProMsAB4 = numOfSearchedZustandAB4 / resultAB4;

        System.out.println("    Gesamtlaufzeit Tiefe 4: " + resultAB4 + "ms (" + (resultAB4 - resultMinMax4) + "ms)");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandAB4 + " (" + (numOfSearchedZustandAB4 - numOfSearchedZustandMM4) + ")");
        System.out.println("    Zustände pro ms: " + zustandProMsAB4 + " (" + (zustandProMsAB4 - zustandProMs4) + ")");
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");

    }

    public void benchmarkThreeRuntime() { // End Game
        System.out.println("Benchmark Late Game");
        System.out.println(" ");
        Board b = new Board("b01b01b01/8/2b03b01/1b06/1r01b01b02/3r04/2r03r01/4r01 r");

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
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");



        // Min-Max
        // Tiefe 2
        long startTimeMinMax2 = System.nanoTime();

        res = Game.iterativeDeepening(b, 2, false);

        long endTimeMinMax2= System.nanoTime();
        float resultMinMax2 = (float) (endTimeMinMax2 - startTimeMinMax2) / 1_000_000;

        long numOfSearchedZustandMM2 = Game.numOfSearchedZustand;
        float zustandProMs2 = numOfSearchedZustandMM2 / resultMinMax2;

        System.out.println("Min-Max");
        System.out.println("    Gesamtlaufzeit Tiefe 2: " + resultMinMax2 + "ms");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM2);
        System.out.println("    Zustände pro ms: " + zustandProMs2);
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 3
        long startTimeMinMax3 = System.nanoTime();

        res = Game.iterativeDeepening(b, 3, false);

        long endTimeMinMax3= System.nanoTime();
        float resultMinMax3 = (float) (endTimeMinMax3 - startTimeMinMax3) / 1_000_000;

        long numOfSearchedZustandMM3 = Game.numOfSearchedZustand;
        float zustandProMs3 = numOfSearchedZustandMM3 / resultMinMax3;

        System.out.println("    Gesamtlaufzeit Tiefe 3: " + resultMinMax3 + "ms");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM3);
        System.out.println("    Zustände pro ms: " + zustandProMs3);
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 4
        long startTimeMinMax4 = System.nanoTime();

        res = Game.iterativeDeepening(b, 4, false);

        long endTimeMinMax4 = System.nanoTime();
        float resultMinMax4 = (float) (endTimeMinMax4 - startTimeMinMax4) / 1_000_000;

        long numOfSearchedZustandMM4 = Game.numOfSearchedZustand;
        float zustandProMs4 = numOfSearchedZustandMM4 / resultMinMax4;

        System.out.println("    Gesamtlaufzeit Tiefe 4: " + resultMinMax4 + "ms");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandMM4);
        System.out.println("    Zustände pro ms: " + zustandProMs4);
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // AlphaBeta
        // Tiefe 2
        long startTimeAB2 = System.nanoTime();

        res = Game.iterativeDeepening(b, 2, true);

        long endTimeAB2 = System.nanoTime();
        float resultAB2 = (float) (endTimeAB2 - startTimeAB2) / 1_000_000;

        long numOfSearchedZustandAB2 = Game.numOfSearchedZustand;
        float zustandProMsAB2 = numOfSearchedZustandAB2 / resultAB2;

        System.out.println("Alpha Beta");
        System.out.println("    Gesamtlaufzeit Tiefe 2: " + resultAB2 + "ms (" + (resultAB2 - resultMinMax2) + "ms)");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandAB2 + " (" + (numOfSearchedZustandAB2 - numOfSearchedZustandMM2) + ")");
        System.out.println("    Zustände pro ms: " + zustandProMsAB2 + " (" + (zustandProMsAB2 - zustandProMs2) + ")");
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 3
        long startTimeAB3 = System.nanoTime();

        res = Game.iterativeDeepening(b, 3, true);

        long endTimeAB3= System.nanoTime();
        float resultAB3 = (float) (endTimeAB3 - startTimeAB3) / 1_000_000;

        long numOfSearchedZustandAB3 = Game.numOfSearchedZustand;
        float zustandProMsAB3 = numOfSearchedZustandAB3 / resultAB3;

        System.out.println("    Gesamtlaufzeit Tiefe 3: " + resultAB3 + "ms (" + (resultAB3 - resultMinMax3) + "ms)");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandAB3 + " (" + (numOfSearchedZustandAB3 - numOfSearchedZustandMM3) + ")");
        System.out.println("    Zustände pro ms: " + zustandProMsAB3 + " (" + -(zustandProMsAB3 - zustandProMs3) + ")");
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


        // Tiefe 4
        long startTimeAB4 = System.nanoTime();

        res = Game.iterativeDeepening(b, 4, true);

        long endTimeAB4 = System.nanoTime();
        float resultAB4 = (float) (endTimeAB4 - startTimeAB4) / 1_000_000;

        long numOfSearchedZustandAB4 = Game.numOfSearchedZustand;
        float zustandProMsAB4 = numOfSearchedZustandAB4 / resultAB4;

        System.out.println("    Gesamtlaufzeit Tiefe 4: " + resultAB4 + "ms (" + (resultAB4 - resultMinMax4) + "ms)");
        System.out.println("    Untersuchte Stellungen: " + numOfSearchedZustandAB4 + " (" + (numOfSearchedZustandAB4 - numOfSearchedZustandMM4) + ")");
        System.out.println("    Zustände pro ms: " + zustandProMsAB4 + " (" + (zustandProMsAB4 - zustandProMs4) + ")");
        System.out.println("    Bester Zug: " + res.move);
        System.out.println(" ");


    }

    public static void main(String[] args) {
        Benchmark b = new Benchmark();

        b.benchmarkOneRuntime();
        b.benchmarkTwoRuntime();
        b.benchmarkThreeRuntime();
    }
 }
