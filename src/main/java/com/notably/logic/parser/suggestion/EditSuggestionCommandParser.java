package com.notably.logic.parser.suggestion;

import java.util.Optional;

import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.model.Model;

/**
 * Represents a parser for the command word "edit".
 */
public class EditSuggestionCommandParser implements SuggestionCommandParser<SuggestionCommand> {
    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_SHORTHAND = "e";

    private static final String RESPONSE_MESSAGE = "Edit this note";

    private Model model;

    public EditSuggestionCommandParser(Model model) {
        this.model = model;
    }

    @Override
    public Optional<SuggestionCommand> parse(String userInput) {
        model.setResponseText(RESPONSE_MESSAGE);
        return Optional.empty();
    }
}
