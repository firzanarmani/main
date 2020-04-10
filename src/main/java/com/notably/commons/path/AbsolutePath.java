package com.notably.commons.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.notably.commons.path.exceptions.InvalidPathException;
import com.notably.model.block.Title;

/**
 * Represents the Path to a Block, starting from the Root node.
 */
public class AbsolutePath implements Path {
    public static final String INVALID_ABSOLUTE_PATH = "Invalid absolute path";
    public static final String VALIDATION_REGEX = "\\/|(\\/" + Title.VALIDATION_REGEX + ")+\\/?";

    private final List<String> components;

    private AbsolutePath(String absolutePathString) {
        this.components = new ArrayList<>();
        for (String obj : absolutePathString.split("/")) {
            if (!obj.trim().equals("")) {
                this.components.add(obj.trim());
            }
        }
    }

    public AbsolutePath(List<String> absolutePathList) {
        this.components = absolutePathList;
    }

    /**
     * Check if a supplied string is a ValidAbsolutePath
     * @param absolutePathString
     * @return
     */
    public static boolean isValidAbsolutePath(String absolutePathString) {
        return absolutePathString.matches(VALIDATION_REGEX);
    }

    /**
     * Creates an absolute path from a string.
     * @param absolutePathString used to create an absolutePath.
     * @return Absolute Path.
     */
    public static AbsolutePath fromString(String absolutePathString) {
        if (!isValidAbsolutePath(absolutePathString)) {
            throw new InvalidPathException(INVALID_ABSOLUTE_PATH);
        }
        return new AbsolutePath(absolutePathString);
    }

    /**
     * Creates an absolute path from a list of components.
     * @param absoluteComponents used to create an absolutePath.
     * @return Absolute Path.
     */
    public static AbsolutePath fromComponents(List<String> absoluteComponents) {
        return new AbsolutePath(absoluteComponents);
    }


    /**
     * Convert from relative path to absolute path.
     * @param relativePath used to convert to absolute path.
     * @param currentWorkingPath of the current working directory.
     * @return the converted AbsolutePath.
     */
    public static AbsolutePath fromRelativePath(RelativePath relativePath,
            AbsolutePath currentWorkingPath) {
        List<String> temp = new ArrayList<>(relativePath.getComponents());
        List<String> temp2 = new ArrayList<>(currentWorkingPath.getComponents());
        for (String obj : temp) {
            if (obj.equals("..")) {
                if (temp2.size() == 0) {
                    throw new InvalidPathException("Empty directory");
                }
                temp2.remove(temp2.size() - 1);
            } else if (obj.equals(".")) {
                //Do nothing
            } else {
                temp2.add(obj);
            }
        }
        return new AbsolutePath(temp2);
    }

    /**
     * Convert Absolute path to Relative path.
     * @param currentWorkingPath of the current working directory.
     * @return the converted RelativePath
     */
    public RelativePath toRelativePath(AbsolutePath currentWorkingPath) {
        return RelativePath.fromAbsolutePath(this, currentWorkingPath);
    }

    @Override
    public List<String> getComponents() {
        return this.components;
    }

    @Override
    public String getStringRepresentation() {
        return "/" + String.join("/", this.components);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AbsolutePath)) {
            return false;
        }

        AbsolutePath another = (AbsolutePath) object;
        List<String> temp = another.getComponents()
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());;
        return this.components
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList())
                .equals(temp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.components);
    }

    @Override
    public String toString() {
        return getStringRepresentation();
    }
}

