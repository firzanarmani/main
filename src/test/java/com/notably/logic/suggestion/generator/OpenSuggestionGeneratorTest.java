package com.notably.logic.suggestion.generator;

import static com.notably.commons.parser.CliSyntax.PREFIX_TITLE;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.suggestion.SuggestionTestUtil;
import com.notably.logic.suggestion.handler.OpenSuggestionArgHandler;
import com.notably.model.Model;
import com.notably.model.suggestion.SuggestionItem;

public class OpenSuggestionGeneratorTest {
    private static AbsolutePath toCs2103t;
    private static List<AbsolutePath> paths;
    private static Model model;

    @BeforeAll
    public static void setUp() {
        toCs2103t = SuggestionTestUtil.getToCs2103t();
        paths = new ArrayList<>();
        paths.add(toCs2103t);
        model = SuggestionTestUtil.getModel();
    }

    @AfterEach
    public void clearSuggestions() {
        model.clearSuggestions();
    }

    @Test
    public void constructor_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OpenSuggestionGenerator(null, "title"));
    }

    @Test
    public void constructor_nullTitle_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OpenSuggestionGenerator(paths,
                null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        OpenSuggestionGenerator openSuggestionCommand = new OpenSuggestionGenerator(paths,
                toCs2103t.getStringRepresentation());
        assertThrows(NullPointerException.class, () -> openSuggestionCommand.execute(null));
    }

    @Test
    public void execute_blankOldTitle_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new OpenSuggestionGenerator(paths, "   "));
    }

    @Test
    public void execute_correctAbsolutePathWithPrefix_generatesResponseCorrectly() {
        String userInputWithoutPath = OpenSuggestionArgHandler.COMMAND_WORD + " " + PREFIX_TITLE + " ";
        model.setInput(userInputWithoutPath + toCs2103t.getStringRepresentation());
        OpenSuggestionGenerator openSuggestionCommand = new OpenSuggestionGenerator(paths,
                toCs2103t.getStringRepresentation());
        openSuggestionCommand.execute(model);

        List<SuggestionItem> suggestions = model.getSuggestions();

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();

        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);

        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void execute_correctAbsolutePathWithoutPrefix_generatesResponseCorrectly() {
        String userInputWithoutPath = OpenSuggestionArgHandler.COMMAND_WORD + " ";
        model.setInput(userInputWithoutPath + toCs2103t.getStringRepresentation());
        OpenSuggestionGenerator openSuggestionCommand = new OpenSuggestionGenerator(paths,
                toCs2103t.getStringRepresentation());
        openSuggestionCommand.execute(model);

        List<SuggestionItem> suggestions = model.getSuggestions();

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();

        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);

        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }
}
