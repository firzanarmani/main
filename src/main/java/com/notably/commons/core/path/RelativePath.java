package com.notably.commons.core.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.notably.commons.core.path.exceptions.InvalidPathException;

/**
 * Represents a path to the block relative to the current directory.
 */
public class RelativePath implements Path {
    public static final String INVALID_RELATIVE_PATH = "Invalid relative path";
    public static final String VALIDATION_REGEX = "(\\.|\\..|\\p{Alnum}+)(\\/(\\.|\\..|\\p{Alnum}+))*\\/?";
    private final List<String> components;
    private RelativePath(String relativePathString) {
        this.components = new ArrayList<>();
        for (String obj : relativePathString.split("/")) {
            if (!obj.trim().equals("")) {
                this.components.add(obj.trim());
            }
        }
    }

    private RelativePath(List<String> components) {
        this.components = components;
    }

    /**
     * Check if string is a valid relative path.
     * @param relativePathString String used to check validity.
     * @return returns true if String is a valid relative Path.
     */
    public static boolean isValidRelativePath(String relativePathString) {
        return relativePathString.matches(VALIDATION_REGEX);
    }

    /**
     * Instantiate a RelativePath object if the string is valid.
     * @param relativePathString used to create RelativePath.
     * @return the converted RelativePath object.
     * @throws InvalidPathException if the String is invalid (Not relative path).
     */
    public static RelativePath fromString(String relativePathString) throws InvalidPathException {
        if (!isValidRelativePath(relativePathString)) {
            throw new InvalidPathException("Not a relative path");
        }
        return new RelativePath(relativePathString);

    }

    /**
     * Instantiate a relativePath from components.
     * @param components to create a relativePath.
     * @return a relativePath object.
     */
    public static RelativePath fromComponents(List<String> components) {
        return new RelativePath(components);

    }

    /**
     * Convert from absolute path to relative path.
     * @param absolutePath to convert to relative path.
     * @param currentWorkingPath of the current working directory.
     * @return the converted relative path.
     * @throws InvalidPathException
     */
    public static RelativePath fromAbsolutePath(AbsolutePath absolutePath,
            AbsolutePath currentWorkingPath) {
        int sizeOfCurrent = currentWorkingPath.getComponents().size();
        int sizeOfAbsolute = absolutePath.getComponents().size();
        List<String> componentsToRelative;
        if (sizeOfCurrent > sizeOfAbsolute) {
            componentsToRelative = Collections.nCopies(sizeOfAbsolute - sizeOfCurrent, "..");
        } else {
            componentsToRelative = absolutePath.getComponents().subList(sizeOfCurrent, sizeOfAbsolute);
        }
        return fromComponents(componentsToRelative);
    }

    /**
     * Convert relative path to absolute path.
     * @param currentWorkingPath of the current working directory.
     * @return the absolute path of the relative path.
     */
    public AbsolutePath toAbsolutePath(AbsolutePath currentWorkingPath) throws InvalidPathException {
        return AbsolutePath.fromRelativePath(this, currentWorkingPath);
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
        List<String> temp = new ArrayList<>();
        List<String> anotherTemp = new ArrayList<>();
        for (String obj: this.getComponents()) {
            if (obj.equals("..")) {
                if (temp.size() == 0) {
                    temp.add(obj);
                } else {
                    temp.remove(temp.size() - 1);
                }
            } else if (!obj.equals(".")) {
                temp.add(obj);
            }
        }
        for (String obj: another.getComponents()) {
            if (obj.equals("..")) {
                if (anotherTemp.size() == 0) {
                    anotherTemp.add(obj);
                } else {
                    anotherTemp.remove(temp.size() - 1);
                }
            } else if (!obj.equals(".")) {
                anotherTemp.add(obj);
            }
        }
        return temp.equals(anotherTemp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.components);
    }

    @Override
    public String toString() {
        return String.join("/", this.components);
    }
}

