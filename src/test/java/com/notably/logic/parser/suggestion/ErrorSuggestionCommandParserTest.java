package com.notably.logic.parser.suggestion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.logic.suggestion.SuggestionTestUtil;
import com.notably.model.Model;

public class ErrorSuggestionCommandParserTest {
    private static ErrorSuggestionCommandParser errorSuggestionCommandParser;
    private static Model model;

    private static final String ERROR_MESSAGE = "\"%s\" is an invalid command format. "
            + "To see the list of available commands, type: help";

    @BeforeAll
    public static void setUp() {
        model = SuggestionTestUtil.getModel();

        errorSuggestionCommandParser = new ErrorSuggestionCommandParser(model);
    }

    @Test
    public void parse() {
        String arg = "opensesame";

        Optional<? extends SuggestionCommand> command = errorSuggestionCommandParser.parse(arg);

        assertEquals(Optional.of(String.format(ERROR_MESSAGE, arg)),
                model.responseTextProperty().getValue());
    }
}
