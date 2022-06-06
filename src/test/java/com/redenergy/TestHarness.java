// Copyright Red Energy Limited 2017

package com.redenergy;

import com.opencsv.exceptions.CsvException;
import com.redenergy.checks.EnergyUnitChecker;
import com.redenergy.checks.NMILengthChecker;
import com.redenergy.checks.QualityChecker;
import com.redenergy.model.MeterRead;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Simple test harness for trying out SimpleNem12Parser implementation
 */
public class TestHarness {

  public static void main(String[] args) throws IOException, CsvException {
    ClassLoader classLoader = TestHarness.class.getClassLoader();
    File simpleNem12File = new File(classLoader.getResource("SimpleNem12.csv").getFile());
    EnergyUnitChecker energyUnitChecker = new EnergyUnitChecker();
    NMILengthChecker nmiLengthChecker = new NMILengthChecker();
    QualityChecker qualityChecker = new QualityChecker();

    // Uncomment below to try out test harness.
    Collection<MeterRead> meterReads = new SimpleNem12ParserImpl(energyUnitChecker, nmiLengthChecker,qualityChecker).parseSimpleNem12(simpleNem12File);

    MeterRead read6123456789 = meterReads.stream().filter(mr -> mr.getNmi().equals("6123456789")).findFirst().get();
    System.out.println(String.format("Total volume for NMI 6123456789 is %f", read6123456789.getTotalVolume()));  // Should be -36.84

    MeterRead read6987654321 = meterReads.stream().filter(mr -> mr.getNmi().equals("6987654321")).findFirst().get();
    System.out.println(String.format("Total volume for NMI 6987654321 is %f", read6987654321.getTotalVolume()));  // Should be 14.33
  }
}
