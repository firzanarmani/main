package com.notably.logic.commands.suggestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.suggestion.SuggestionTestUtil;
import com.notably.model.Model;
import com.notably.model.suggestion.SuggestionItem;
import com.notably.model.suggestion.SuggestionItemImpl;
import com.notably.testutil.TypicalBlockModel;

public class SearchSuggestionCommandTest {
    private static Model model;

    @BeforeAll
    public static void setUp() {
        model = SuggestionTestUtil.getModel();
    }

    @Test
    public void constructor_nullKeyword_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SearchSuggestionCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        SearchSuggestionCommand searchSuggestionCommand = new SearchSuggestionCommand("keywords");
        assertThrows(NullPointerException.class, () -> searchSuggestionCommand.execute(null));
    }

    @Test
    public void execute_generatesResponseCorrectly() {
        SearchSuggestionCommand searchSuggestionCommand = new SearchSuggestionCommand("fALsE");
        searchSuggestionCommand.execute(model);

        List<SuggestionItem> suggestions = model.getSuggestions();

        SuggestionItem cs2103tTut1 = new SuggestionItemImpl(
                TypicalBlockModel.PATH_TO_CS2103T_TUTORIAL_1.getStringRepresentation(), 1, () -> {});
        SuggestionItem cs2103tTut2 = new SuggestionItemImpl(
                TypicalBlockModel.PATH_TO_CS2103T_TUTORIAL_2.getStringRepresentation(), 2, () -> {});
        SuggestionItem cs2106Tut1 = new SuggestionItemImpl(
                TypicalBlockModel.PATH_TO_CS2106_TUTORIAL_1.getStringRepresentation(), 1, () -> {});
        List<SuggestionItem> expectedSuggestions = List.of(cs2103tTut1, cs2103tTut2, cs2106Tut1);

        for (int i = 0; i < expectedSuggestions.size(); i++) {
            SuggestionItem suggestion = suggestions.get(i);
            SuggestionItem expectedSuggestion = expectedSuggestions.get(i);
            assertEquals(expectedSuggestion.getProperty("displayText"), suggestion.getProperty("displayText"));
            assertEquals(expectedSuggestion.getProperty("frequency"), suggestion.getProperty("frequency"));
        }

        List<AbsolutePath> expectedCurrentlyOpenPaths = List.of(TypicalBlockModel.PATH_TO_CS2103T_TUTORIAL_1,
                TypicalBlockModel.PATH_TO_CS2103T_TUTORIAL_2, TypicalBlockModel.PATH_TO_CS2106_TUTORIAL_1);

        for (int i = 0; i < expectedCurrentlyOpenPaths.size(); i++) {
            SuggestionItem suggestion = suggestions.get(i);
            suggestion.getAction().run();

            assertEquals(expectedCurrentlyOpenPaths.get(i), model.getCurrentlyOpenPath());
        }
    }
}
