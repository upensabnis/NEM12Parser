package com.redenergy;

import com.opencsv.exceptions.CsvException;
import com.redenergy.checks.EnergyUnitChecker;
import com.redenergy.checks.NMILengthChecker;
import com.redenergy.checks.QualityChecker;
import com.redenergy.exceptions.RuntimeParsingException;
import com.redenergy.exceptions.RuntimeValidationException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class SimpleNem12ParserImplTest {

    private EnergyUnitChecker energyUnitChecker;
    private NMILengthChecker nmiLengthChecker;
    private QualityChecker qualityChecker;
    private  SimpleNem12ParserImpl simpleNem12Parser;

    @BeforeTest
    public void init() {
        energyUnitChecker = new EnergyUnitChecker();
        nmiLengthChecker = new NMILengthChecker();
        qualityChecker = new QualityChecker();
        simpleNem12Parser = new SimpleNem12ParserImpl(energyUnitChecker, nmiLengthChecker, qualityChecker);
    }

    @Test(expectedExceptions = RuntimeValidationException.class)
    public void testsimplenem12parserimpl_when_input_contains_unexpected_data() throws IOException, CsvException {
        ClassLoader classLoader = TestHarness.class.getClassLoader();
        File simpleNem12File = new File(
                classLoader.getResource("SimpleNem12_with_unknown_record_type.csv").getFile()
        );
        simpleNem12Parser.parseSimpleNem12(simpleNem12File);
    }

    @Test
    public void testsimplenem12parserimpl_when_input_with_no_100() {
        try {
            ClassLoader classLoader = TestHarness.class.getClassLoader();
            File simpleNem12File = new File(
                    classLoader.getResource("SimpleNem12_with_no_record_type_100.csv").getFile()
            );
            simpleNem12Parser.parseSimpleNem12(simpleNem12File);

        } catch (IOException e) {
        } catch (CsvException e) {
        } catch (RuntimeParsingException e) {
            Assert.assertEquals("File does not start with 100", e.getMessage());
        }
    }

    @Test
    public void testsimplenem12parserimpl_when_input_with_no_900() {
        try {
            ClassLoader classLoader = TestHarness.class.getClassLoader();
            File simpleNem12File = new File(
                    classLoader.getResource("SimpleNem12_with_no_record_type_900.csv").getFile()
            );
            simpleNem12Parser.parseSimpleNem12(simpleNem12File);

        } catch (IOException e) {
        } catch (CsvException e) {
        } catch (RuntimeParsingException e) {
            Assert.assertEquals("File does not end with 900", e.getMessage());
        }
    }

    @Test
    public void testsimplenem12parserimpl_when_row_300_before_200() {
        try {
            ClassLoader classLoader = TestHarness.class.getClassLoader();
            File simpleNem12File = new File(
                    classLoader.getResource("SimpleNem12_with_row_300_before_200.csv").getFile()
            );
            simpleNem12Parser.parseSimpleNem12(simpleNem12File);

        } catch (IOException e) {
        } catch (CsvException e) {
        } catch (RuntimeParsingException e) {
            Assert.assertEquals("Row 300 is before row 200", e.getMessage());
        }
    }

    @Test
    public void testsimplenem12parserimpl_when_wrong_date_time() {
        try {
            ClassLoader classLoader = TestHarness.class.getClassLoader();
            File simpleNem12File = new File(
                    classLoader.getResource("SimpleNem12_with_wrong_date_time.csv").getFile()
            );
            simpleNem12Parser.parseSimpleNem12(simpleNem12File);

        } catch (IOException e) {
        } catch (CsvException e) {
        } catch (RuntimeParsingException e) {
            Assert.assertTrue(e.getMessage().contains("could not be parsed"));
        }
    }
}
