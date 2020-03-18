package com.notably.commons.core.path;

import static com.notably.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.notably.commons.core.path.exceptions.InvalidPathException;

/**
 * Test for RelativePath.
 */
class RelativePathTest {

    @Test
    public void fromString_validInputString_generateAbsolutePath() throws InvalidPathException {
        final RelativePath testInput = RelativePath.fromString("CS2103/notes");
        List<String> paths = new ArrayList<>();
        paths.add("CS2103");
        paths.add("notes");
        assertEquals(paths, testInput.getComponents());
    }

    @Test
    public void fromString_invalidInputString_exceptionThrown() {
        assertThrows(InvalidPathException.class, () -> RelativePath.fromString("/CS2103/notes"));
    }

    @Test
    public void toAbsolutePath_validInput_correctedPath() throws InvalidPathException {
        final RelativePath inputRelativePath = RelativePath.fromString("CS2103/notes/hello");
        final AbsolutePath inputCurrPath = AbsolutePath.fromString("/CS2103");

        final AbsolutePath expectedOutput = AbsolutePath.fromString("/CS2103/CS2103/notes/hello");

        assertEquals(expectedOutput, inputRelativePath.toAbsolutePath(inputCurrPath));
    }

    @Test
    public void toAbsolutePath_invalidInput_exceptionThrown() throws InvalidPathException {
        final RelativePath inputRelativePath = RelativePath.fromString("../../notes/hello");
        final AbsolutePath inputCurrPath = AbsolutePath.fromString("/CS2103");

        assertThrows(InvalidPathException.class, () -> inputRelativePath.toAbsolutePath(inputCurrPath));
    }

    @Test
    public void equals_similarPath_pathAreEqual() throws InvalidPathException {
        final RelativePath inputRelativePath1 = RelativePath.fromString("CS2103/../CS2103");
        final RelativePath inputRelativePath2 = RelativePath.fromString("CS2103");

        assertEquals(inputRelativePath2, inputRelativePath1);
    }
}
