package com.redenergy.checks;

import static com.google.common.base.Preconditions.checkArgument;

public class NMILengthChecker implements CheckerInterface {
    @Override
    public void checkIfValidInput(String nmi) throws IllegalArgumentException {
        checkArgument(nmi.length() == 10, "Invalid length of NMI: {}, expectedLength: {}, actualLength: {}",
                nmi, String.valueOf(10), nmi);
    }
}
