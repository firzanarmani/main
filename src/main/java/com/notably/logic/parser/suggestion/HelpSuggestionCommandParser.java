package com.notably.logic.parser.suggestion;

import java.util.Optional;

import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.model.Model;

/**
 * Represents a parser for the command word "edit".
 */
public class HelpSuggestionCommandParser implements SuggestionCommandParser<SuggestionCommand> {
    public static final String COMMAND_WORD = "help";
    public static final String COMMAND_SHORTHAND = "h";

    private static final String RESPONSE_MESSAGE = "Display a list of available commands";

    private Model model;

    public HelpSuggestionCommandParser(Model model) {
        this.model = model;
    }

    @Override
    public Optional<SuggestionCommand> parse(String userInput) {
        model.setResponseText(RESPONSE_MESSAGE);
        return Optional.empty();
    }
}
