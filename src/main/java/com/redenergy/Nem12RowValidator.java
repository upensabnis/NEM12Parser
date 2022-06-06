package com.redenergy;

import com.opencsv.validators.RowValidator;
import com.redenergy.checks.EnergyUnitChecker;
import com.redenergy.checks.NMILengthChecker;
import com.redenergy.checks.QualityChecker;
import com.redenergy.exceptions.RuntimeValidationException;

import static com.google.common.base.Preconditions.checkArgument;

public class Nem12RowValidator implements RowValidator {

    private NMILengthChecker nmiLengthChecker;
    private EnergyUnitChecker energyUnitChecker;
    private QualityChecker qualityChecker;

    public Nem12RowValidator(EnergyUnitChecker energyUnitChecker, NMILengthChecker nmiLengthChecker,
                             QualityChecker qualityChecker) {
        this.nmiLengthChecker = nmiLengthChecker;
        this.energyUnitChecker = energyUnitChecker;
        this.qualityChecker = qualityChecker;
    }

    @Override
    public boolean isValid(String[] strings) {
        boolean valid = false;

        try {
            checkArgument(strings != null && strings.length > 0, "Row length is less than 0");
            int firstColumnInRow = Integer.parseInt(strings[0]);
            if (firstColumnInRow == 100 || firstColumnInRow == 900) {
                checkArgument(strings.length == 1, "Validation problems for row with record type " + firstColumnInRow);
                valid = true;
            } else if (firstColumnInRow == 200) {
                checkArgument(strings.length == 3, "Validation problems for row with record type " + firstColumnInRow);
                nmiLengthChecker.checkIfValidInput(strings[1]);
                energyUnitChecker.checkIfValidInput(strings[2]);
                valid = true;
            } else if (firstColumnInRow == 300) {
                checkArgument(strings.length == 4, "Validation problems for row with record type " + firstColumnInRow);
                qualityChecker.checkIfValidInput(strings[3]);
                valid = true;
            } else {
                throw new IllegalArgumentException("Invalid record type: " + firstColumnInRow);
            }
        } catch (IllegalArgumentException ex) {
            throw new RuntimeValidationException(ex.getMessage());
        }

        return valid;
    }

    @Override
    public void validate(String[] strings) {
        if (!isValid(strings)) {
            throw new RuntimeValidationException("Input CSV contains unexpected data");
        }
    }
}
