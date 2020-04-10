package com.notably.logic.parser.suggestion;

import static com.notably.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Optional;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.suggestion.DeleteSuggestionCommand;
import com.notably.logic.correction.CorrectionEngine;
import com.notably.logic.correction.CorrectionResult;
import com.notably.logic.correction.CorrectionStatus;
import com.notably.logic.parser.ArgumentMultimap;
import com.notably.logic.parser.ArgumentTokenizer;
import com.notably.logic.parser.ParserUtil;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.Model;

/**
 * Represents a Parser for DeleteSuggestionCommand.
 */
public class DeleteSuggestionCommandParser implements SuggestionCommandParser<DeleteSuggestionCommand> {
    public static final String COMMAND_WORD = "delete";

    private static final String RESPONSE_MESSAGE = "Delete a note";
    private static final String RESPONSE_MESSAGE_WITH_TITLE = "Delete a note titled \"%s\"";
    private static final String ERROR_MESSAGE_CANNOT_DELETE_NOTE = "Cannot delete \"%s\" as it is an invalid path";

    private Model model;
    private CorrectionEngine<AbsolutePath> pathCorrectionEngine;

    public DeleteSuggestionCommandParser(Model model, CorrectionEngine<AbsolutePath> pathCorrectionEngine) {
        this.model = model;
        this.pathCorrectionEngine = pathCorrectionEngine;
    }

    /**
     * Parses user input in the context of the DeleteSuggestionCommand.
     *
     * @param userInput The user's input.
     * @return An optional DeleteSuggestionCommand object with a corrected absolute path.
     */
    @Override
    public Optional<DeleteSuggestionCommand> parse(String userInput) {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_TITLE);

        String title;
        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_TITLE)
                || !argMultimap.getPreamble().isEmpty()) {
            title = userInput.trim();
        } else {
            title = argMultimap.getValue(PREFIX_TITLE).get();
        }

        if (title.isEmpty()) {
            model.setResponseText(RESPONSE_MESSAGE);
            return Optional.empty();
        }

        AbsolutePath uncorrectedPath;
        try {
            uncorrectedPath = ParserUtil.createAbsolutePath(title, model.getCurrentlyOpenPath());
        } catch (ParseException pe) {
            model.setResponseText(String.format(ERROR_MESSAGE_CANNOT_DELETE_NOTE, title));
            return Optional.empty();
        }

        model.setResponseText(String.format(RESPONSE_MESSAGE_WITH_TITLE, title));

        CorrectionResult<AbsolutePath> correctionResult = pathCorrectionEngine.correct(uncorrectedPath);
        if (correctionResult.getCorrectionStatus() == CorrectionStatus.FAILED) {
            return Optional.empty();
        }

        // TODO: Pass in the list of corrected items and create suggestions based on that
        return Optional.of(new DeleteSuggestionCommand(correctionResult.getCorrectedItems().get(0), title));
    }
}
