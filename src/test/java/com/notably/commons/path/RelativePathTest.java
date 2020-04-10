package com.notably.commons.path;

import static com.notably.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.notably.commons.path.exceptions.InvalidPathException;

/**
 * Test for RelativePath.
 */
class RelativePathTest {

    @Test
    public void fromString_validInputString_generateAbsolutePath() {
        final RelativePath testInput = RelativePath.fromString("CS2103/notes~");
        List<String> paths = new ArrayList<>();
        paths.add("CS2103");
        paths.add("notes~");
        assertEquals(paths, testInput.getComponents());
    }

    @Test
    public void fromString_invalidInputString_exceptionThrown() {
        assertThrows(InvalidPathException.class, () -> RelativePath.fromString("/CS2103!/notes"));
    }

    @Test
    public void toAbsolutePath_validInput_correctedPath() {
        final RelativePath inputRelativePath = RelativePath.fromString("CS2103/%notes%/hello");
        final AbsolutePath inputCurrPath = AbsolutePath.fromString("/CS2103");

        final AbsolutePath expectedOutput = AbsolutePath.fromString("/CS2103/CS2103/%notes%/hello");

        assertEquals(expectedOutput, inputRelativePath.toAbsolutePath(inputCurrPath));
    }

    @Test
    public void toAbsolutePath_invalidInput_exceptionThrown() {
        final RelativePath inputRelativePath = RelativePath.fromString("../../notes/hello!");
        final AbsolutePath inputCurrPath = AbsolutePath.fromString("/CS2103");

        assertThrows(InvalidPathException.class, () -> inputRelativePath.toAbsolutePath(inputCurrPath));
    }

    @Test
    public void compareTo_equalPaths() {
        final RelativePath relativePath1 = RelativePath.fromString("./notes/../notes");
        final RelativePath relativePath2 = RelativePath.fromString("NOtes");

        assertTrue(relativePath1.compareTo(relativePath2) == 0);
    }

    @Test
    public void compareTo_equalSizePathsWithDifferentComponents() {
        final RelativePath relativePath1 = RelativePath.fromString("Hello/notes");
        final RelativePath relativePath2 = RelativePath.fromString("Hello/./New notes");

        assertTrue(relativePath1.compareTo(relativePath2) > 0);
    }

    @Test
    public void compareTo_differentSizePathsWithSamePrefix() {
        final RelativePath relativePath1 = RelativePath.fromString("Hello/../hellO/notes/again!");
        final RelativePath relativePath2 = RelativePath.fromString("./hello/notes");

        assertTrue(relativePath1.compareTo(relativePath2) > 0);
    }

    @Test
    public void equals_similarPathWithSameCasing_pathAreEqual() {
        final RelativePath inputRelativePath1 = RelativePath.fromString("@CS2103/../@CS2103");
        final RelativePath inputRelativePath2 = RelativePath.fromString("@CS2103");

        assertEquals(inputRelativePath2, inputRelativePath1);
    }

    @Test
    public void equals_similarPathWithDifferentCase_pathAreEqual() {
        final RelativePath inputRelativePath1 = RelativePath.fromString("cs2103");
        final RelativePath inputRelativePath2 = RelativePath.fromString("CS2103");

        assertEquals(inputRelativePath2, inputRelativePath1);
    }
}
