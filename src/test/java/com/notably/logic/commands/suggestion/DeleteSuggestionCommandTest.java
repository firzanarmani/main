package com.notably.logic.commands.suggestion;

import static com.notably.logic.parser.CliSyntax.PREFIX_TITLE;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.suggestion.SuggestionTestUtil;
import com.notably.model.Model;
import com.notably.model.suggestion.SuggestionItem;

public class DeleteSuggestionCommandTest {
    private static AbsolutePath toCs2103t;
    private static Model model;

    private static final String COMMAND_WORD = "delete";

    @BeforeAll
    public static void setUp() {
        toCs2103t = SuggestionTestUtil.getToCs2103t();
        model = SuggestionTestUtil.getModel();
    }

    @AfterEach
    public void clearSuggestions() {
        model.clearSuggestions();
    }

    @Test
    public void constructor_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeleteSuggestionCommand(null, "title"));
    }

    @Test
    public void constructor_nullTitle_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeleteSuggestionCommand(toCs2103t,
            null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        DeleteSuggestionCommand deleteSuggestionCommand = new DeleteSuggestionCommand(toCs2103t,
            toCs2103t.getStringRepresentation());
        assertThrows(NullPointerException.class, () -> deleteSuggestionCommand.execute(null));
    }

    @Test
    public void execute_blankOldTitle_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteSuggestionCommand(toCs2103t, "   "));
    }

    @Test
    public void execute_correctAbsolutePathWithPrefix_generatesResponseCorrectly() {
        String userInputWithoutPath = COMMAND_WORD + " " + PREFIX_TITLE + " ";
        model.setInput(userInputWithoutPath + toCs2103t.getStringRepresentation());
        DeleteSuggestionCommand deleteSuggestionCommand = new DeleteSuggestionCommand(toCs2103t,
            toCs2103t.getStringRepresentation());
        deleteSuggestionCommand.execute(model);

        List<SuggestionItem> suggestions = model.getSuggestions();

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();

        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);

        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void execute_correctAbsolutePathWithoutPrefix_generatesResponseCorrectly() {
        String userInputWithoutPath = COMMAND_WORD + " ";
        model.setInput(userInputWithoutPath + toCs2103t.getStringRepresentation());
        DeleteSuggestionCommand deleteSuggestionCommand = new DeleteSuggestionCommand(toCs2103t,
                toCs2103t.getStringRepresentation());
        deleteSuggestionCommand.execute(model);

        List<SuggestionItem> suggestions = model.getSuggestions();

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();

        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);

        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }
}
