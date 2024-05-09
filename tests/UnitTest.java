package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class UnitTest {
    @Test
    public void testEarlyGameOne() { // Gruppe G Early Game
        Board b = new Board();
        b.readfen("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0 r");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("B8-B7", "B8-C8", "C8-B8", "C8-C7", "C8-D8", "D8-C8", "D8-D7", "D8-E8", "E8-D8", "E8-E7", "E8-F8", "F8-E8", "F8-F7", "F8-G8", "G8-F8", "G8-G7", "B7-A7", "B7-B6", "B7-C7", "C6-B6", "C6-C5", "C6-D6", "D6-C6", "D6-D5", "D6-E6", "G7-F7", "G7-G6", "G7-H7", "F6-D5", "F6-E4", "F6-G4", "F6-H5"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEarlyGameTwo() { // Gruppe AI Mid Game
        Board b = new Board();
        b.readfen("2bbbb1b0/1b06/1b01b04/4b03/4r03/3r02b01/1r0r02rr2/2rr2r0 b");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("B2-A2", "B2-B3", "B2-C2", "B3-A3", "B3-B4", "B3-C3", "D1-B2", "D1-C3", "D1-E3", "D1-F2", "D3-C3", "D3-D4", "D3-E3", "E1-C2", "E1-D3", "E1-F3", "E1-G2", "E4-D4", "E4-F4", "G1-F1", "G1-G2", "G6-F6", "G6-F7", "G6-G7", "G6-H6"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEarlyGameThree() { // Gruppe AF Mid Game
        Board b = new Board();
        b.readfen("b02b01b0/3b01b02/b02b02b01/b01b05/5r02/1r02r02r0/2rrr02r01/r03r01 b");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("B1-B2", "B1-C1", "E1-E2", "E1-D1", "E1-F1", "G1-G2", "G1-F1", "D2-D3", "D2-C2", "D2-E2", "F2-F3", "F2-E2", "F2-G2", "A3-A4", "A3-B3", "D3-D4", "D3-C3", "D3-E3", "G3-G4", "G3-F3", "G3-H3", "A4-A5", "A4-B4", "C4-C5", "C4-B4", "C4-D4"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEarlyGameFour() { // Gruppe B Mid Game
        Board b = new Board();
        b.readfen("b01bbb01b0/1b02b03/3bbr01b01/8/3rr1b0b01/8/2r01r01rr1/r0r0r01r01 r");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("B8-B7", "B8-C8", "C8-C7", "C8-B8", "C8-D8", "D8-D7", "D8-C8", "D8-E8", "F8-F7", "F8-E8", "F8-G8", "C7-C6", "C7-B7", "C7-D7", "E7-E6", "E7-D7", "E7-F7", "G7-E6", "G7-F5", "G7-H5", "D5-B4", "D5-C3", "D5-E3", "D5-F4", "E3-F3"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEarlyGameFive() { // Gruppe R Mid Game
        Board b = new Board();
        b.readfen("1b0b01b0b0/3bb4/8/1r03b02/3b0rrr0b01/6r01/1r0r0r04/2r01r01 r");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("D8-C8", "D8-E8", "D8-D7", "F8-E8", "F8-G8", "F8-F7", "B7-A7", "B7-C7", "B7-B6", "C7-B7", "C7-D7", "C7-C6", "D7-C7", "D7-E7", "D7-D6", "G6-F6", "G6-H6", "E5-C4", "E5-G4", "E5-D3", "E5-F3", "B4-A4", "B4-C4", "B4-B3"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLateGameOne() { // Gruppe G Late Game
        Board b = new Board();
        b.readfen("b01b01b01/8/2b03b01/1b06/1r01b01b02/3r04/2r03r01/4r01 r");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("F8-E8", "F8-F7", "F8-G8", "C7-B7", "C7-C6", "C7-D7", "G7-F7", "G7-G6", "G7-H7", "D6-C6", "D6-E6", "B5-A5", "B5-C5"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLateGameTwo() { // Gruppe AI Late Game
        Board b = new Board();
        b.readfen("2b03/8/8/3b0b03/2b03b01/2r03r01/2r05/6 r");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("C6-B6", "C6-D6", "C7-B7", "C7-C6", "C7-D7", "G6-F6", "G6-H6"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLateGameThree() { // Gruppe AF Late Game
        Board b = new Board();
        b.readfen("6/1b03b02/3b01r0b01/bb2b04/1b01r02r0r0/1r0r02rbr01/1r06/6 r");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("F3-E3", "D5-C5", "D5-E5", "G5-G4", "G5-F5", "G5-H5", "H5-H4", "H5-G5", "B6-A6", "B6-C6", "C6-C5", "C6-B6", "C6-B5", "C6-D6", "G6-G5", "G6-H6", "B7-B6", "B7-A7", "B7-C7"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLateGameFour() { // Gruppe M End Game 1
        Board b = new Board();
        b.readfen("6/1b06/8/2b01bbb0rb1/1rbr0rr1r0r01/8/b07/6 b");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("B2-A2", "B2-B3", "B2-C2", "C4-B4", "C4-D4", "C4-D5", "E4-C5", "E4-D6", "E4-F6", "E4-G5", "F4-G5", "G4-E5", "G4-F6", "G4-H6", "B5-A7", "B5-C7", "B5-D6", "A7-B7"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLateGameFive() { // Gruppe M End Game 2
        Board b = new Board();
        b.readfen("6/8/6rr1/8/8/8/b0b0b05/6 r");
        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList("G3-F1", "G3-E2"));
        ArrayList<String> actualResult = b.possiblemoves();
        Collections.sort(expectedResult);
        Collections.sort(actualResult);
        Assertions.assertEquals(expectedResult, actualResult);
    }
}


