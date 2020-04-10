package com.notably.commons.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.notably.commons.path.exceptions.InvalidPathException;

/**
 * Utility class for path operations.
 */
public class PathUtil {
    /**
     * Normalises the components of a {@link RelativePath}.
     * For instance, the path "./CS2103/../CS2103" is normalised as "CS2103".
     *
     * @param base Base of normalisation
     * @param components Components to normalise
     * @return Normalised components
     */
    public static List<String> normaliseRelativeComponents(List<String> base, List<String> components) {
        Objects.requireNonNull(base);
        Objects.requireNonNull(components);

        List<String> normalised = new ArrayList<>(base);
        for (String component : components) {
            if (component.equals("..")) {
                if (normalised.isEmpty()) {
                    throw new InvalidPathException("Invalid relative path components");
                }
                normalised.remove(normalised.size() - 1);
            } else if (!component.equals(".")) {
                normalised.add(component);
            }
        }

        return normalised;
    }

    /**
     * Normalises the components of a {@link RelativePath}.
     * For instance, the path "./CS2103/../CS2103" is normalised as "CS2103".
     *
     * @param components Components to normalise
     * @return Normalised components
     */
    public static List<String> normaliseRelativeComponents(List<String> components) {
        Objects.requireNonNull(components);

        return normaliseRelativeComponents(List.of(), components);
    }
}
