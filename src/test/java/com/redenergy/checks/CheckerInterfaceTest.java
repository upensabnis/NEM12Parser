package com.redenergy.checks;


import org.testng.annotations.Test;

public class CheckerInterfaceTest {

    @Test
    public void testenergyunitchecker_when_valid_input() {
        CheckerInterface checkerInterface = new EnergyUnitChecker();
        checkerInterface.checkIfValidInput("KWH");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testenergyunitchecker_when_invalid_input() {
        CheckerInterface checkerInterface = new EnergyUnitChecker();
        checkerInterface.checkIfValidInput("KMRE");
    }

    @Test
    public void testnmilengthchecker_when_valid_input() {
        CheckerInterface checkerInterface = new NMILengthChecker();
        checkerInterface.checkIfValidInput("0123456789");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testnmilengthchecker_when_invalid_input() {
        CheckerInterface checkerInterface = new NMILengthChecker();
        checkerInterface.checkIfValidInput("12345");
    }

    @Test
    public void testqualitychecker_with_valid_input() {
        CheckerInterface checkerInterface = new QualityChecker();
        checkerInterface.checkIfValidInput("A");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testqualitychecker_with_invalid_input() {
        CheckerInterface checkerInterface = new QualityChecker();
        checkerInterface.checkIfValidInput("B");
    }

}
