package com.notably.logic.parser.suggestion;

import java.util.Optional;

import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.model.Model;

/**
 * Represents a parser for the command word "edit".
 */
public class ErrorSuggestionCommandParser implements SuggestionCommandParser<SuggestionCommand> {
    private static final String ERROR_MESSAGE_INVALID_COMMAND = "\"%s\" is an invalid command format. "
            + "To see the list of available commands, type: help";

    private Model model;

    public ErrorSuggestionCommandParser(Model model) {
        this.model = model;
    }

    @Override
    public Optional<SuggestionCommand> parse(String userInput) {
        model.setResponseText(String.format(ERROR_MESSAGE_INVALID_COMMAND, userInput));
        return Optional.empty();
    }
}
