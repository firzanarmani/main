package com.notably.logic.correction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.correction.distance.EditDistanceCalculator;
import com.notably.logic.correction.distance.LevenshteinDistanceCalculator;
import com.notably.model.Model;
import com.notably.model.block.BlockTree;
import com.notably.model.block.BlockTreeItem;

/**
 * Represents a correction engine that works on {@link AbsolutePath}s
 */
public class AbsolutePathCorrectionEngine implements CorrectionEngine<AbsolutePath> {
    private final EditDistanceCalculator editDistanceCalculator;
    private final Model model;
    private final int distanceThreshold;
    private final boolean forwardMatch;

    /**
     * Creates an {@link AbsolutePathCorrectionEngine}.
     *
     * @param model App's model
     * @param distanceThreshold Edit distance threshold between paths
     * @param forwardMatch Whether or not to do forward matching. Forward matching refers to the idea
     * that two paths will be regarded similar if one is an incomplete representation of another.
     * For example, "/path/to/no" will forward match with "/path/to/note".
     */
    public AbsolutePathCorrectionEngine(Model model, int distanceThreshold, boolean forwardMatch) {
        this(new LevenshteinDistanceCalculator(false), model, distanceThreshold, forwardMatch);
    }

    /**
     * Creates an {@link AbsolutePathCorrectionEngine}.
     *
     * @param editDistanceCalculator Edit distance calculator instance
     * @param model App's model
     * @param distanceThreshold Edit distance threshold between paths
     * @param forwardMatch Whether or not to do forward matching. Forward matching refers to the idea
     * that two paths will be regarded similar if one is an incomplete representation of another.
     * For example, "/path/to/no" will forward match with "/path/to/note".
     */
    public AbsolutePathCorrectionEngine(EditDistanceCalculator editDistanceCalculator,
            Model model, int distanceThreshold, boolean forwardMatch) {
        Objects.requireNonNull(editDistanceCalculator);
        Objects.requireNonNull(model);

        if (distanceThreshold < 0) {
            throw new IllegalArgumentException("\"distanceThreshold\" must be greater than 0");
        }

        this.editDistanceCalculator = editDistanceCalculator;
        this.model = model;
        this.distanceThreshold = distanceThreshold;
        this.forwardMatch = forwardMatch;
    }

    /**
     * Corrects a given path.
     *
     * @param uncorrected An uncorrected path
     * @return Result of the correction
     */
    @Override
    public CorrectionResult<AbsolutePath> correct(AbsolutePath uncorrected) {
        Objects.requireNonNull(uncorrected);

        List<AbsolutePath> possiblePaths = getPossiblePaths();

        int closestDistance = Integer.MAX_VALUE;
        for (AbsolutePath possiblePath : possiblePaths) {
            int distance = calculatePathDistance(uncorrected, possiblePath);
            if (distance < closestDistance) {
                closestDistance = distance;
            }
        }

        List<AbsolutePath> correctedItems = new ArrayList<>();
        for (AbsolutePath possiblePath : possiblePaths) {
            int distance = calculatePathDistance(uncorrected, possiblePath);
            if (distance == closestDistance) {
                correctedItems.add(possiblePath);
            }
        }

        if (closestDistance > distanceThreshold) {
            return new CorrectionResult<>(CorrectionStatus.FAILED);
        }

        if (correctedItems.size() == 1 && correctedItems.get(0).equals(uncorrected)) {
            return new CorrectionResult<>(CorrectionStatus.UNCHANGED, correctedItems);
        }

        return new CorrectionResult<>(CorrectionStatus.CORRECTED, correctedItems);
    }

    /**
     * Generates all possible paths from the app's {@link BlockTree}
     *
     * @return List of all possible paths
     */
    private List<AbsolutePath> getPossiblePaths() {
        List<AbsolutePath> possiblePaths = new ArrayList<>();

        Queue<AbsolutePath> pathQueue = new LinkedList<>();
        pathQueue.offer(AbsolutePath.fromString("/"));

        while (!pathQueue.isEmpty()) {
            AbsolutePath currentPath = pathQueue.poll();

            List<BlockTreeItem> childrenBlocks = model.getBlockTree().get(currentPath).getBlockChildren();
            List<AbsolutePath> childrenPaths = childrenBlocks
                    .stream()
                    .map(item -> {
                        List<String> combinedComponents = new ArrayList<>(currentPath.getComponents());
                        combinedComponents.add(item.getTitle().getText());
                        return AbsolutePath.fromComponents(combinedComponents);
                    })
                    .collect(Collectors.toList());
            pathQueue.addAll(childrenPaths);

            possiblePaths.add(currentPath);
        }

        return possiblePaths;
    }

    /**
     * Calculate the edit distance between two {@link AbsolutePath}s.
     *
     * @param input Input {@link AbsolutePath}
     * @param reference Reference {@link AbsolutePath}
     * @return Edit distance between {@code input} and {@code reference}
     */
    private int calculatePathDistance(AbsolutePath input, AbsolutePath reference) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(reference);

        List<String> inputComponents = input.getComponents();
        List<String> referenceComponents = reference.getComponents();

        // Calculate the cumulative distance between the two paths component-by-component
        int i = 0;
        int distance = 0;
        while (i < inputComponents.size() && i < referenceComponents.size()) {
            String inputComponent = inputComponents.get(i);
            String referenceComponent = referenceComponents.get(i);

            if (forwardMatch && i == inputComponents.size() - 1
                    && inputComponent.length() < referenceComponent.length()) {
                distance += calculateForwardMatchingDistance(inputComponent, referenceComponent);
            } else {
                distance += editDistanceCalculator.calculateDistance(inputComponent, referenceComponent);
            }

            i++;
        }

        // If one path is longer than another, increase cumulative distance by the size of
        // each extra component's length.
        while (i < inputComponents.size()) {
            distance += inputComponents.get(i).length();
            i++;
        }
        while (i < referenceComponents.size()) {
            distance += referenceComponents.get(i).length();
            i++;
        }

        return distance;
    }

    /**
     * Calculates the forward matching distance between to path components.
     *
     * @param inputComponent Input path component
     * @param referenceComponent Reference path component
     * @return Forward matching distance between the two components
     */
    private int calculateForwardMatchingDistance(String inputComponent, String referenceComponent) {
        Objects.requireNonNull(inputComponent);
        Objects.requireNonNull(referenceComponent);

        int forwardMatchDistance = Integer.MAX_VALUE;
        for (int stopIndex = 0; stopIndex <= referenceComponent.length(); stopIndex++) {
            int currentForwardMatchDistance = editDistanceCalculator.calculateDistance(inputComponent,
                    referenceComponent.substring(0, stopIndex));
            if (currentForwardMatchDistance < forwardMatchDistance) {
                forwardMatchDistance = currentForwardMatchDistance;
            }
        }

        return forwardMatchDistance;
    }
}

