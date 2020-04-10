package com.notably.logic.parser.suggestion;

import static com.notably.logic.parser.CliSyntax.PREFIX_SEARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.logic.commands.suggestion.SearchSuggestionCommand;
import com.notably.logic.suggestion.SuggestionTestUtil;
import com.notably.model.Model;
import com.notably.model.suggestion.SuggestionItem;

public class SearchSuggestionCommandParserTest {
    private static SearchSuggestionCommandParser searchSuggestionCommandParser;
    private static Model model;

    private static final String RESPONSE_MESSAGE = "Search through all notes based on keyword";
    private static final String RESPONSE_MESSAGE_WITH_KEYWORD = "Search through all notes based on keyword \"%s\"";

    @BeforeAll
    public static void setUp() {
        model = SuggestionTestUtil.getModel();
        searchSuggestionCommandParser = new SearchSuggestionCommandParser(model);
    }

    @AfterEach
    public void clearResponseTextAndSuggestions() {
        model.clearResponseText();
        model.clearSuggestions();
    }

    @Test
    public void parse_emptyKeyword_returnsEmptySuggestions() {
        String keyword = "";
        String userInput = " " + PREFIX_SEARCH + " " + keyword;
        Optional<? extends SearchSuggestionCommand> command = searchSuggestionCommandParser.parse(userInput);

        // Test response text
        assertEquals(Optional.of(RESPONSE_MESSAGE), model.responseTextProperty().getValue());

        // Test command
        assertFalse(command.isPresent());
    }

    @Test
    public void parse_withPrefixSearch_returnsSearchSuggestionCommand() {
        String keyword = "fAlSe";
        String userInput = " " + PREFIX_SEARCH + " " + keyword;
        Optional<? extends SearchSuggestionCommand> command = searchSuggestionCommandParser.parse(userInput);
        assertTrue(command.get() instanceof SearchSuggestionCommand);

        command.get().execute(model);

        // Test response text
        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_KEYWORD, keyword)),
                model.responseTextProperty().getValue());

        // Expected suggestions
        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSearchSugForKeywordFalse();

        // Actual suggestions
        List<SuggestionItem> suggestions = model.getSuggestions();

        // Test suggestions
        SuggestionTestUtil.testSearchSuggestions(expectedSuggestions, suggestions);
    }

    @Test
    public void parse_withoutPrefixSearch_returnsSearchSuggestionCommand() {
        String keyword = "fAlSe";
        String userInput = keyword;
        Optional<? extends SearchSuggestionCommand> command = searchSuggestionCommandParser.parse(userInput);
        assertTrue(command.get() instanceof SearchSuggestionCommand);

        command.get().execute(model);

        // Test response text
        assertEquals(Optional.of(String.format(RESPONSE_MESSAGE_WITH_KEYWORD, keyword)),
                model.responseTextProperty().getValue());

        // Expected suggestions
        List<SuggestionItem> expectedSuggestions = SuggestionTestUtil.getExpectedSearchSugForKeywordFalse();

        // Actual suggestions
        List<SuggestionItem> suggestions = model.getSuggestions();

        // Test suggestions
        SuggestionTestUtil.testSearchSuggestions(expectedSuggestions, suggestions);
    }
}
