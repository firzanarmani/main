package com.notably.logic.correction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.notably.logic.correction.distance.EditDistanceCalculator;
import com.notably.logic.correction.distance.LevenshteinDistanceCalculator;

/**
 * Represents a correction engine that works on {@link String}s.
 */
public class StringCorrectionEngine implements CorrectionEngine<String> {
    private final EditDistanceCalculator editDistanceCalculator;
    private final List<String> options;
    private final int distanceThreshold;

    public StringCorrectionEngine(List<String> options, int distanceThreshold) {
        this(new LevenshteinDistanceCalculator(false), options, distanceThreshold);
    }

    public StringCorrectionEngine(EditDistanceCalculator editDistanceCalculator,
            List<String> options, int distanceThreshold) {
        Objects.requireNonNull(editDistanceCalculator);
        Objects.requireNonNull(options);

        if (options.isEmpty()) {
            throw new IllegalArgumentException("\"options\" must contain at least one element");
        }

        if (distanceThreshold < 0) {
            throw new IllegalArgumentException("\"distanceThreshold\" must be greater than 0");
        }

        this.editDistanceCalculator = editDistanceCalculator;
        this.options = options;
        this.distanceThreshold = distanceThreshold;
    }

    /**
     * Corrects a given string.
     *
     * @param uncorrected Uncorrected string
     * @return Result of the correction
     */
    @Override
    public CorrectionResult<String> correct(String uncorrected) {
        Objects.requireNonNull(uncorrected);

        int closestDistance = Integer.MAX_VALUE;
        for (String option : options) {
            int distance = editDistanceCalculator.calculateDistance(uncorrected, option);
            if (distance < closestDistance) {
                closestDistance = distance;
            }
        }

        List<String> correctedItems = new ArrayList<>();
        for (String option : options) {
            int distance = editDistanceCalculator.calculateDistance(uncorrected, option);
            if (distance == closestDistance) {
                correctedItems.add(option);
            }
        }

        if (closestDistance > distanceThreshold) {
            return new CorrectionResult<>(CorrectionStatus.FAILED);
        }

        if (correctedItems.size() == 1 && correctedItems.get(0).equalsIgnoreCase(uncorrected)) {
            return new CorrectionResult<>(CorrectionStatus.UNCHANGED, correctedItems);
        }

        return new CorrectionResult<String>(CorrectionStatus.CORRECTED, correctedItems);
    }
}

