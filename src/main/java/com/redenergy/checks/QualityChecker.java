package com.redenergy.checks;

import com.redenergy.model.enums.Quality;

import static com.google.common.base.Preconditions.checkArgument;

public class QualityChecker implements CheckerInterface {
    @Override
    public void checkIfValidInput(String quality) throws IllegalArgumentException {
        checkArgument(Quality.valueOf(quality) != null, "Unexpected quality value: {}", quality);
    }
}
