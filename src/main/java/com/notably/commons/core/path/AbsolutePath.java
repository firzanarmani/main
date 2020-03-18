package com.notably.commons.core.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.notably.commons.core.path.exceptions.InvalidPathException;

/**
 * Represents the Path to a Block, starting from the Root node.
 */
public class AbsolutePath implements Path {
    public static final String INVALID_ABSOLUTE_PATH = "Invalid absolute path";
    public static final String VALIDATION_REGEX = "(\\/\\p{Alnum}+)+\\/?";
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
     * @throws InvalidPathException if String provided is not a valid absolutePath.
     */
    public static AbsolutePath fromString(String absolutePathString) throws InvalidPathException {
        if (!isValidAbsolutePath(absolutePathString)) {
            throw new InvalidPathException(INVALID_ABSOLUTE_PATH);
        }
        return new AbsolutePath(absolutePathString);
    }

    /**
     * Creates an absolute path from a list of components.
     * @param absoluteComponents used to create an absolutePath.
     * @return Absolute Path.
     * @throws InvalidPathException if String provided is not a valid absolutePath.
     */
    public static AbsolutePath fromComponent(List<String> absoluteComponents) {
        return new AbsolutePath(absoluteComponents);
    }


    /**
     * Convert from relative path to absolute path.
     * @param relativePath used to convert to absolute path.
     * @param currentWorkingPath of the current working directory.
     * @return the converted AbsolutePath.
     * @throws InvalidPathException
     */
    public static AbsolutePath fromRelativePath(RelativePath relativePath,
            AbsolutePath currentWorkingPath) throws InvalidPathException {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getComponents() {
        return this.components;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Path)) {
            return false;
        }

        Path another = (Path) object;
        List<String> temp = another.getComponents();
        return this.components.equals(temp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.components);
    }

    @Override
    public String toString() {
        return "/" + String.join("/", this.components);
    }
}

