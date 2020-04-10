package com.notably.logic.parser.suggestion;

import static com.notably.logic.parser.CliSyntax.PREFIX_JUMP;
import static com.notably.logic.parser.CliSyntax.PREFIX_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.logic.suggestion.SuggestionTestUtil;
import com.notably.model.Model;

public class NewSuggestionCommandParserTest {
    private static NewSuggestionCommandParser newSuggestionCommandParser;
    private static Model model;

    private static final String RESPONSE_MESSAGE = "Create a new note";
    private static final String RESPONSE_MESSAGE_WITH_TITLE = "Create a new note titled \"%s\".";
    private static final String RESPONSE_MESSAGE_WITH_TITLE_AND_OPEN = "Create a new note titled \"%s\" and open it.";
    private static final String ERROR_MESSAGE_INVALID_COMMAND = "\"%s\" is an invalid creation format. "
            + "The correct format is \"new -t TITLE [-o]\"";
    private static final String ERROR_MESSAGE_INVALID_TITLE = "Title \"%s\" is invalid. "
            + "Titles should only contain alphanumeric characters and symbols except - and /";

    @BeforeAll
    public static void setUp() {
        model = SuggestionTestUtil.getModel();

        newSuggestionCommandParser = new NewSuggestionCommandParser(model);
    }

    @Test
    public void parse_emptyArg() {
        String arg = "";

        Optional<? extends SuggestionCommand> command = newSuggestionCommandParser.parse(arg);

        assertEquals(Optional.of(RESPONSE_MESSAGE), model.responseTextProperty().getValue());
    }

    @Test
    public void parse_prefixTitleValidTitleNoPrefixJump() {
        String title = "Aa123!@#$%^&*()";
        String arg = " " + PREFIX_TITLE + " " + title;

        Optional<? extends SuggestionCommand> command = newSuggestionCommandParser.parse(arg);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE, title)),
                model.responseTextProperty().getValue());
    }

    @Test
    public void parse_prefixTitleValidTitlePrefixJump() {
        String title = "Aa123!@#$%^&*()";
        String arg = " " + PREFIX_TITLE + " " + title + " " + PREFIX_JUMP;

        Optional<? extends SuggestionCommand> command = newSuggestionCommandParser.parse(arg);

        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_TITLE_AND_OPEN, title)),
                model.responseTextProperty().getValue());
    }

    @Test
    public void parse_prefixTitleInvalidTitle() {
        String title = "aA-";
        String arg = " " + PREFIX_TITLE + " " + title + " " + PREFIX_JUMP;

        Optional<? extends SuggestionCommand> command = newSuggestionCommandParser.parse(arg);

        assertEquals(Optional.of(String.format(ERROR_MESSAGE_INVALID_TITLE, title)),
                model.responseTextProperty().getValue());
    }

    @Test
    public void parse_noPrefixTitleValidTitle() {
        String title = "Aa123!@#$%^&*()";
        String arg = " " + title + " " + PREFIX_JUMP;
        String userInput = "nw" + arg;
        model.setInput(userInput);

        Optional<? extends SuggestionCommand> command = newSuggestionCommandParser.parse(arg);

        assertEquals(Optional.of(String.format(ERROR_MESSAGE_INVALID_COMMAND, userInput)),
                model.responseTextProperty().getValue());
    }
}
