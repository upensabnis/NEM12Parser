package com.redenergy.checks;

import com.redenergy.model.enums.EnergyUnit;

import static com.google.common.base.Preconditions.checkArgument;

public class EnergyUnitChecker implements CheckerInterface {
    @Override
    public void checkIfValidInput(String energyUnit) throws IllegalArgumentException {
        checkArgument(EnergyUnit.valueOf(energyUnit) != null, "Unexpected energy unit: {}", energyUnit);
    }
}
