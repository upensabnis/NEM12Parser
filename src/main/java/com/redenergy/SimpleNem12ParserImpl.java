package com.redenergy;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.redenergy.checks.EnergyUnitChecker;
import com.redenergy.checks.NMILengthChecker;
import com.redenergy.checks.QualityChecker;
import com.redenergy.exceptions.RuntimeParsingException;
import com.redenergy.exceptions.RuntimeValidationException;
import com.redenergy.model.MeterRead;
import com.redenergy.model.MeterVolume;
import com.redenergy.model.enums.EnergyUnit;
import com.redenergy.model.enums.Quality;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleNem12ParserImpl implements SimpleNem12Parser {

    private EnergyUnitChecker energyUnitChecker;
    private NMILengthChecker nmiLengthChecker;
    private QualityChecker qualityChecker;

    public SimpleNem12ParserImpl(EnergyUnitChecker energyUnitChecker,
                                 NMILengthChecker nmiLengthChecker, QualityChecker qualityChecker) {
        this.energyUnitChecker = energyUnitChecker;
        this.nmiLengthChecker = nmiLengthChecker;
        this.qualityChecker = qualityChecker;
    }

    @Override
    public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) throws IOException, CsvException, RuntimeValidationException {
        Nem12RowValidator validator = new Nem12RowValidator(energyUnitChecker, nmiLengthChecker, qualityChecker);
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(simpleNem12File))
                .withRowValidator(validator)
                .build();

        // read all rows in memory (assuming file is not big)
        List<String[]> csvRows = csvReader.readAll();
        List<MeterRead> allReadMeters = parseCsvRows(csvRows);

        return allReadMeters;
    }

    /**
     *
     * @param csvRows
     * @return list of meters read from csv
     */
    private List<MeterRead> parseCsvRows(List<String[]> csvRows) {
        isFileStartWith100(csvRows);
        isFileEndWith900(csvRows);

        removeRowsWith100And900(csvRows);

        // rows after this should not start with either 100 or 900
        List<MeterRead> readMeters = new ArrayList<>();
        boolean is200RowSet = false;

        for(String[] row: csvRows) {
            if (String.valueOf(100).equals(row[0]) || String.valueOf(900).equals(row[0])) {
                throw new RuntimeParsingException("Record type 100 or 900 appears more than once");
            }
            is200RowSet = parseRowsStartingWith200Or300(readMeters, row, is200RowSet);
        }

        return readMeters;
    }

    private void isFileStartWith100(List<String[]> csvRows) {
        String[] firstRow = csvRows.get(0);
        if(!String.valueOf(100).equals(firstRow[0])) {
            throw new RuntimeParsingException("File does not start with 100");
        }
    }

    private void isFileEndWith900(List<String[]> csvRows) {
        String[] lastRow = csvRows.get(csvRows.size() - 1);
        if(!String.valueOf(900).equals(lastRow[0])) {
            throw new RuntimeParsingException("File does not end with 900");
        }
    }

    private List<String[]> removeRowsWith100And900(List<String[]> csvRows) {
        csvRows.remove(csvRows.size() - 1);
        csvRows.remove(0);
        return csvRows;
    }

    private boolean parseRowsStartingWith200Or300(List<MeterRead> meterReads, String[] row, boolean is200RowSet) {
        if (String.valueOf(200).equals(row[0])) {
            parseRowsStartingWith200(meterReads, row[1], row[2]);
            is200RowSet = true;
        }
        if (String.valueOf(300).equals(row[0])) {
            if (is200RowSet) {
                parseRowsStartingWith300(meterReads, row[1], row[2], row[3]);
            } else {
                throw new RuntimeParsingException("Row 300 is before row 200");
            }
        }
        return is200RowSet;
    }

    private boolean parseRowsStartingWith200(List<MeterRead> meterReads, String nmi, String energyUnit) {
        MeterRead meterRead = new MeterRead(nmi, EnergyUnit.valueOf(energyUnit));
        meterReads.add(meterRead);
        return true;
    }

    private void parseRowsStartingWith300(List<MeterRead> meterReads, String date, String reading, String quality) {
        MeterRead meterRead = meterReads.get(meterReads.size() - 1);
        MeterVolume meterVolume = new MeterVolume(BigDecimal.valueOf(Double.parseDouble(reading)), Quality.valueOf(quality));
        meterRead.appendVolume(parseDateTime(date), meterVolume);
    }

    private LocalDate parseDateTime(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            return LocalDate.parse(dateTime, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            throw new RuntimeParsingException(ex.getMessage());
        }
    }

}
