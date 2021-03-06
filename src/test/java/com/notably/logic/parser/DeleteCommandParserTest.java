package com.notably.logic.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.DeleteCommand;
import com.notably.logic.commands.exceptions.CommandException;
import com.notably.logic.correction.AbsolutePathCorrectionEngine;
import com.notably.logic.correction.CorrectionEngine;
import com.notably.logic.correction.CorrectionEngineParameters;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.Model;
import com.notably.model.ModelManager;
import com.notably.model.block.BlockModel;
import com.notably.model.suggestion.SuggestionModel;
import com.notably.model.suggestion.SuggestionModelImpl;
import com.notably.model.viewstate.ViewStateModel;
import com.notably.model.viewstate.ViewStateModelImpl;
import com.notably.testutil.TypicalBlockModel;

class DeleteCommandParserTest {
    private static Model model;
    private static DeleteCommandParser deleteCommandParser;

    @BeforeEach
    public void setUp() {
        // Set up model
        BlockModel blockModel = TypicalBlockModel.getTypicalBlockModel();
        SuggestionModel suggestionModel = new SuggestionModelImpl();
        ViewStateModel viewStateModel = new ViewStateModelImpl();
        model = new ModelManager(blockModel, suggestionModel, viewStateModel);
        CorrectionEngine<AbsolutePath> pathCorrectionEngine = new AbsolutePathCorrectionEngine(model,
                new CorrectionEngineParameters().setForwardMatching(false).setDistanceThreshold(2));
        deleteCommandParser = new DeleteCommandParser(model, pathCorrectionEngine);

    }

    @Test
    void parse_relativePathWithPrefix_validDeleteCommand() throws ParseException, CommandException {
        final AbsolutePath toDeletePath = TypicalBlockModel.PATH_TO_Y2S2;
        final DeleteCommand deleteCommand = deleteCommandParser.parse(" -t /Y2S2").get(0);

        assertTrue(model.hasPath(toDeletePath));
        deleteCommand.execute(model);
        assertFalse(model.hasPath(toDeletePath));
    }

    @Test
    void parse_absolutePathWithPrefix_validDeleteCommand() throws ParseException, CommandException {
        final AbsolutePath toDeletePath = TypicalBlockModel.PATH_TO_CS2106;
        final DeleteCommand deleteCommand = deleteCommandParser.parse(" -t /Y2S2/CS2106").get(0);

        assertTrue(model.hasPath(toDeletePath));
        deleteCommand.execute(model);
        assertFalse(model.hasPath(toDeletePath));
    }

    @Test
    void parse_relativePathWithPrefixTypo_validDeleteCommand() throws ParseException, CommandException {
        final AbsolutePath toDeletePath = TypicalBlockModel.PATH_TO_Y2S2;
        final DeleteCommand deleteCommand = deleteCommandParser.parse(" -t /YaS2").get(0);

        assertTrue(model.hasPath(toDeletePath));
        deleteCommand.execute(model);
        assertFalse(model.hasPath(toDeletePath));
    }

    @Test
    void parse_absolutePathWithPrefixTypo_validDeleteCommand() throws ParseException, CommandException {
        final AbsolutePath toDeletePath = TypicalBlockModel.PATH_TO_CS2106;
        final DeleteCommand deleteCommand = deleteCommandParser.parse(" -t /YbS2/CS2106").get(0);

        assertTrue(model.hasPath(toDeletePath));
        deleteCommand.execute(model);
        assertFalse(model.hasPath(toDeletePath));
    }

    @Test
    void parse_relativePathWithoutPrefix_validDeleteCommand() throws ParseException, CommandException {
        final AbsolutePath toDeletePath = TypicalBlockModel.PATH_TO_CS2106;
        final DeleteCommand deleteCommand = deleteCommandParser.parse(" CS2106").get(0);

        assertTrue(model.hasPath(toDeletePath));
        deleteCommand.execute(model);
        assertFalse(model.hasPath(toDeletePath));
    }

    @Test
    void parse_relativePathWithoutPrefixTypo_validDeleteCommand() throws ParseException, CommandException {
        final AbsolutePath toDeletePath = TypicalBlockModel.PATH_TO_CS2106;
        final DeleteCommand deleteCommand = deleteCommandParser.parse(" CS210a").get(0);

        assertTrue(model.hasPath(toDeletePath));
        deleteCommand.execute(model);
        assertFalse(model.hasPath(toDeletePath));
    }

    @Test
    void parse_absolutePathWithoutPrefix_validDeleteCommand() throws ParseException, CommandException {
        final AbsolutePath toDeletePath = TypicalBlockModel.PATH_TO_CS2106;
        final DeleteCommand deleteCommand = deleteCommandParser.parse(" /Y2S2/CS2106").get(0);

        assertTrue(model.hasPath(toDeletePath));
        deleteCommand.execute(model);
        assertFalse(model.hasPath(toDeletePath));
    }

    @Test
    void parse_absolutePathWithoutPrefixTypo_validDeleteCommand() throws ParseException, CommandException {
        final AbsolutePath toDeletePath = TypicalBlockModel.PATH_TO_CS2106;
        final DeleteCommand deleteCommand = deleteCommandParser.parse(" /Y2S2/CS2109").get(0);

        assertTrue(model.hasPath(toDeletePath));
        deleteCommand.execute(model);
        assertFalse(model.hasPath(toDeletePath));
    }
    @Test
    void parse_invalidBlock_throwParseException() {
        assertThrows(ParseException.class, () -> deleteCommandParser.parse(" -t /nonExistent"));
    }

    @Test
    void parse_deleteRootBlock_throwParseException() {
        assertThrows(CommandException.class, () -> deleteCommandParser.parse(" -t /").get(0).execute(model));
    }
}
