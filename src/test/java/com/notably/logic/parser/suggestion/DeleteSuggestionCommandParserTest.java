package com.notably.logic.parser.suggestion;

import static com.notably.logic.parser.CliSyntax.PREFIX_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.logic.correction.AbsolutePathCorrectionEngine;
import com.notably.logic.correction.CorrectionEngine;
import com.notably.logic.suggestion.SuggestionTestUtil;
import com.notably.model.Model;
import com.notably.model.suggestion.SuggestionItem;

public class DeleteSuggestionCommandParserTest {
    private static AbsolutePath toCs2103t;
    private static String stringRelativePathToCs2103t;
    private static DeleteSuggestionCommandParser deleteSuggestionCommandParser;
    private static Model model;

    private static final String COMMAND_WORD = "delete";
    private static final String RESPONSE_MESSAGE = "Delete a note";
    private static final String RESPONSE_MESSAGE_WITH_TITLE = "Delete a note titled \"%s\"";
    private static final String ERROR_MESSAGE_CANNOT_DELETE_NOTE = "Cannot delete \"%s\" as it is an invalid path";
    private static final int CORRECTION_THRESHOLD = 2;
    private static final boolean USE_FORWARD_MATCHING = true;

    @BeforeAll
    public static void setUp() {
        model = SuggestionTestUtil.getModel();
        toCs2103t = SuggestionTestUtil.getToCs2103t();
        stringRelativePathToCs2103t = SuggestionTestUtil.getStringRelativePathToCs2103t();

        CorrectionEngine<AbsolutePath> pathCorrectionEngine = new AbsolutePathCorrectionEngine(model,
                CORRECTION_THRESHOLD, USE_FORWARD_MATCHING);
        deleteSuggestionCommandParser = new DeleteSuggestionCommandParser(model, pathCorrectionEngine);
    }

    @Test
    public void parse_correctedCmdWithoutPath_returnsOptionalEmpty() {
        String userInput = "dele";
        String path = "";

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(path);

        assertEquals(Optional.of(RESPONSE_MESSAGE), model.responseTextProperty().getValue());

        assertFalse(command.isPresent());
    }

    @Test
    public void parse_correctCmdWithoutPath_returnsOptionalEmpty() {
        String userInput = "delete";
        String path = "";

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(path);

        assertEquals(Optional.of(RESPONSE_MESSAGE), model.responseTextProperty().getValue());

        assertFalse(command.isPresent());
    }

    @Test
    public void parse_correctCmdcorrectAbsolutePathWithPrefix_returnsDeleteSuggestionCommand() {
        String userInputWithoutPath = COMMAND_WORD + " " + PREFIX_TITLE + " ";
        String userInput = userInputWithoutPath + toCs2103t.getStringRepresentation();
        String arg = " " + PREFIX_TITLE + " " + toCs2103t.getStringRepresentation();

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(arg);
        assertTrue(command.isPresent());

        command.get().execute(model);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, toCs2103t.getStringRepresentation())),
                model.responseTextProperty().getValue());

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();
        List<SuggestionItem> suggestions = model.getSuggestions();
        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);
        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void parse_correctedCmdcorrectAbsolutePathWithoutPrefix_returnsDeleteSuggestionCommand() {
        String userInputWithoutPath = "dele";
        String userInput = userInputWithoutPath + toCs2103t.getStringRepresentation();
        String arg = " " + toCs2103t.getStringRepresentation();

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(arg);
        assertTrue(command.isPresent());

        command.get().execute(model);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, toCs2103t.getStringRepresentation())),
                model.responseTextProperty().getValue());

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();
        List<SuggestionItem> suggestions = model.getSuggestions();
        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);
        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void parse_correctCmdcorrectRelativePathWithPrefix_returnsDeleteSuggestionCommand() {
        String userInputWithoutPath = COMMAND_WORD + " " + PREFIX_TITLE + " ";
        String userInput = userInputWithoutPath + stringRelativePathToCs2103t;
        String arg = " " + PREFIX_TITLE + " " + stringRelativePathToCs2103t;

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(arg);
        assertTrue(command.isPresent());

        command.get().execute(model);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, stringRelativePathToCs2103t)),
                model.responseTextProperty().getValue());

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();
        List<SuggestionItem> suggestions = model.getSuggestions();
        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);
        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void parse_correctedCmdcorrectRelativePathWithoutPrefix_returnsDeleteSuggestionCommand() {
        String userInputWithoutPath = "dele";
        String userInput = userInputWithoutPath + stringRelativePathToCs2103t;
        String arg = " " + stringRelativePathToCs2103t;

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(arg);
        assertTrue(command.isPresent());

        command.get().execute(model);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, stringRelativePathToCs2103t)),
                model.responseTextProperty().getValue());

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();
        List<SuggestionItem> suggestions = model.getSuggestions();
        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);
        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void parse_correctedCmdcorrectedAbsolutePathWithPrefix_returnsDeleteSuggestionCommand() {
        String userInputWithoutPath = "dele " + PREFIX_TITLE + " ";
        String path = "/Y2S2/CS2104";
        String userInput = userInputWithoutPath + path;
        String arg = " " + PREFIX_TITLE + " " + path;

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(arg);
        assertTrue(command.isPresent());

        command.get().execute(model);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, path)),
                model.responseTextProperty().getValue());

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();
        List<SuggestionItem> suggestions = model.getSuggestions();
        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);
        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void parse_correctCmdcorrectedAbsolutePathWithoutPrefix_returnsDeleteSuggestionCommand() {
        String userInputWithoutPath = COMMAND_WORD + " ";
        String path = "/Y2S2/CS2104";
        String userInput = userInputWithoutPath + path;
        String arg = " " + path;

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(arg);
        assertTrue(command.isPresent());

        command.get().execute(model);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, path)),
                model.responseTextProperty().getValue());

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();
        List<SuggestionItem> suggestions = model.getSuggestions();
        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);
        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void parse_correctedCmdcorrectedRelativePathWithPrefix_returnsDeleteSuggestionCommand() {
        String userInputWithoutPath = "dele " + PREFIX_TITLE + " ";
        String path = "CS2104";
        String userInput = userInputWithoutPath + path;
        String arg = " " + PREFIX_TITLE + " " + path;

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(arg);
        assertTrue(command.isPresent());

        command.get().execute(model);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, path)),
                model.responseTextProperty().getValue());

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();
        List<SuggestionItem> suggestions = model.getSuggestions();
        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);
        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void parse_correctCmdcorrectedRelativePathWithoutPrefix_returnsDeleteSuggestionCommand() {
        String userInputWithoutPath = COMMAND_WORD + " ";
        String path = "CS2104";
        String userInput = userInputWithoutPath + path;
        String arg = " " + path;

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(arg);
        assertTrue(command.isPresent());

        command.get().execute(model);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, path)),
                model.responseTextProperty().getValue());

        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSugForCs2103tPathInput();
        List<SuggestionItem> suggestions = model.getSuggestions();
        SuggestionTestUtil.assertSuggestions(expectedSuggestions, suggestions);

        List<String> expectedInputs = SuggestionTestUtil.getExpectedInputsForCs2103tPathInput(userInputWithoutPath);
        SuggestionTestUtil.assertInputs(expectedInputs, suggestions, model);
    }

    @Test
    public void parse_invalidPath_returnsOptionalEmpty() {
        String path = "-";
        String userInput = COMMAND_WORD + " " + path;

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(path);

        assertEquals(Optional.of(String.format(ERROR_MESSAGE_CANNOT_DELETE_NOTE, path)),
                model.responseTextProperty().getValue());

        assertFalse(command.isPresent());
    }

    @Test
    public void parse_uncorrectedPath_returnsOptionalEmpty() {
        String path = "random";
        String userInput = COMMAND_WORD + " " + path;

        model.setInput(userInput);
        Optional<? extends SuggestionCommand> command = deleteSuggestionCommandParser.parse(path);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, path)),
                model.responseTextProperty().getValue());

        assertFalse(command.isPresent());
    }
}
