package com.redenergy;

import com.redenergy.checks.EnergyUnitChecker;
import com.redenergy.checks.NMILengthChecker;
import com.redenergy.checks.QualityChecker;
import com.redenergy.exceptions.RuntimeValidationException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Nem12RowValidatorTest {

    private Nem12RowValidator nem12RowValidator;

    @BeforeTest
    public void init() {
        EnergyUnitChecker energyUnitChecker = new EnergyUnitChecker();
        NMILengthChecker nmiLengthChecker = new NMILengthChecker();
        QualityChecker qualityChecker = new QualityChecker();

        nem12RowValidator = new Nem12RowValidator(energyUnitChecker, nmiLengthChecker, qualityChecker);
    }


    @Test
    public void testnem12rowvalidator_when_row_empty() {
        try {
            String[] strings = new String[0];
            nem12RowValidator.isValid(strings);
        } catch (RuntimeValidationException ex) {
            Assert.assertEquals("Row length is less than 0", ex.getMessage());
        }
    }

    @Test
    public void testnem12rowvalidator_when_row_100() {
        try {
            String[] strings = new String[]{"100", "dummy"};
            nem12RowValidator.isValid(strings);
        } catch (RuntimeValidationException ex) {
            Assert.assertEquals("Validation problems for row with record type 100", ex.getMessage());
        }
    }

    @Test
    public void testnem12rowvalidator_when_row_900() {
        try {
            String[] strings = new String[]{"900", "dummy"};
            nem12RowValidator.isValid(strings);
        } catch (RuntimeValidationException ex) {
            Assert.assertEquals("Validation problems for row with record type 900", ex.getMessage());
        }
    }

    @Test
    public void testnem12rowvalidator_when_row_200() {
        // when length of row is not correct
        try {
            String[] strings = new String[]{"200", "dummy"};
            nem12RowValidator.isValid(strings);
        } catch (RuntimeValidationException ex) {
            Assert.assertEquals("Validation problems for row with record type 200", ex.getMessage());
        }

        // when length of nem is not 10
        try {
            String[] strings = new String[]{"200", "dummy", "KWH"};
            nem12RowValidator.isValid(strings);
        } catch (RuntimeValidationException ex) {
            Assert.assertTrue(ex.getMessage().contains("Invalid length of NMI"));
        }

        // when energy unit is not correct
        try {
            String[] strings = new String[]{"200", "0123456789", "KH"};
            nem12RowValidator.isValid(strings);
        } catch (RuntimeValidationException ex) {
            Assert.assertTrue(ex.getMessage().contains("No enum constant"));
        }
    }

    @Test
    public void testnem12rowvalidator_when_row_300() {
        // when length of row is not correct
        try {
            String[] strings = new String[]{"300", "dummy"};
            nem12RowValidator.isValid(strings);
        } catch (RuntimeValidationException ex) {
            Assert.assertEquals("Validation problems for row with record type 300", ex.getMessage());
        }

        // when incorrect quality
        try {
            String[] strings = new String[]{"300", "20161113", "32.0", "B"};
            nem12RowValidator.isValid(strings);
        } catch (RuntimeValidationException ex) {
            Assert.assertTrue(ex.getMessage().contains("No enum constant"));
        }
    }
}
