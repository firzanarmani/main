package com.notably.logic.parser.suggestion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.logic.suggestion.SuggestionTestUtil;
import com.notably.model.Model;

public class EditSuggestionCommandParserTest {
    private static EditSuggestionCommandParser editSuggestionCommandParser;
    private static Model model;

    private static final String RESPONSE_MESSAGE = "Edit this note";

    @BeforeAll
    public static void setUp() {
        model = SuggestionTestUtil.getModel();

        editSuggestionCommandParser = new EditSuggestionCommandParser(model);
    }

    @Test
    public void parse() {
        String arg = "";

        Optional<? extends SuggestionCommand> command = editSuggestionCommandParser.parse(arg);

        assertEquals(Optional.of(RESPONSE_MESSAGE),
                model.responseTextProperty().getValue());
    }
}
