package com.notably.logic.parser.suggestion;

import static com.notably.logic.parser.CliSyntax.PREFIX_SEARCH;

import java.util.Optional;

import com.notably.logic.commands.suggestion.SearchSuggestionCommand;
import com.notably.logic.parser.ArgumentMultimap;
import com.notably.logic.parser.ArgumentTokenizer;
import com.notably.logic.parser.ParserUtil;
import com.notably.model.Model;

/**
 * Represents a Parser for SearchSuggestionCommand.
 */
public class SearchSuggestionCommandParser implements SuggestionCommandParser<SearchSuggestionCommand> {
    public static final String COMMAND_WORD = "search";
    public static final String COMMAND_SHORTHAND = "s";

    private static final String RESPONSE_MESSAGE = "Search through all notes based on keyword";
    private static final String RESPONSE_MESSAGE_WITH_KEYWORD = "Search through all notes based on keyword \"%s\"";

    private Model model;

    public SearchSuggestionCommandParser(Model model) {
        this.model = model;
    }

    /**
     * Parses user input in the context of the SearchSuggestionCommand.
     *
     * @param userInput The user's input.
     * @return An optional SearchSuggestionCommand object
     */
    @Override
    public Optional<SearchSuggestionCommand> parse(String userInput) {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_SEARCH);

        String keyword;
        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_SEARCH)
                || !argMultimap.getPreamble().isEmpty()) {
            keyword = userInput.trim();
        } else {
            keyword = argMultimap.getValue(PREFIX_SEARCH).get();
        }

        if (keyword.isEmpty()) {
            model.setResponseText(RESPONSE_MESSAGE);
            return Optional.empty();
        } else {
            model.setResponseText(String.format(RESPONSE_MESSAGE_WITH_KEYWORD, keyword));
            return Optional.of(new SearchSuggestionCommand(keyword));
        }
    }
}
