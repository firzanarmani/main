package com.notably.logic.parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.Command;
import com.notably.logic.commands.DeleteCommand;
import com.notably.logic.commands.EditCommand;
import com.notably.logic.commands.ExitCommand;
import com.notably.logic.commands.HelpCommand;
import com.notably.logic.commands.NewCommand;
import com.notably.logic.commands.OpenCommand;
import com.notably.logic.correction.AbsolutePathCorrectionEngine;
import com.notably.logic.correction.CorrectionEngine;
import com.notably.logic.correction.CorrectionResult;
import com.notably.logic.correction.CorrectionStatus;
import com.notably.logic.correction.StringCorrectionEngine;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.Model;

/**
 * Parse in users input and generate the respective Commands
 */
public class NotablyParser {

    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final List<String> COMMAND_LIST = List.of("new", "edit", "delete", "open", "help", "exit");
    private static final int CORRECTION_THRESHOLD = 2;
    private static final boolean USE_PATH_FORWARD_MATCHING = false;

    private Model notablyModel;
    private final CorrectionEngine<String> commandCorrectionEngine;
    private final CorrectionEngine<AbsolutePath> pathCorrectionEngine;

    public NotablyParser(Model notablyModel) {
        this.notablyModel = notablyModel;
        this.commandCorrectionEngine = new StringCorrectionEngine(COMMAND_LIST, CORRECTION_THRESHOLD);
        this.pathCorrectionEngine = new AbsolutePathCorrectionEngine(notablyModel, CORRECTION_THRESHOLD,
                USE_PATH_FORWARD_MATCHING);
    }
    /**
     * Create list of different Commands base on user input.
     * @param userInput supplied by the user.
     * @return list of commands.
     * @throws ParseException when there is a invalid input string.
     */
    public List<? extends Command> parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format("Invalid Command"));
        }

        String commandWord = matcher.group("commandWord");
        if (commandWord.length() > 1) {
            CorrectionResult<String> correctionResult = commandCorrectionEngine.correct(commandWord);
            if (correctionResult.getCorrectionStatus() == CorrectionStatus.FAILED) {
                throw new ParseException("Invalid command");
            }
            commandWord = correctionResult.getCorrectedItems().get(0);
        }

        final String arguments = matcher.group("arguments");

        switch (commandWord) {
        case NewCommand.COMMAND_WORD:
        case NewCommand.COMMAND_SHORTHAND :
            return new NewCommandParser(notablyModel).parse(arguments);

        case OpenCommand.COMMAND_WORD:
        case OpenCommand.COMMAND_SHORTHAND :
            return new OpenCommandParser(notablyModel, pathCorrectionEngine).parse(arguments);

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_SHORTHAND:
            return new DeleteCommandParser(notablyModel, pathCorrectionEngine).parse(arguments);

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_SHORTHAND:
            return List.of(new EditCommand());

        case HelpCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_SHORTHAND:
            return List.of(new HelpCommand());

        case ExitCommand.COMMAND_WORD:
            return List.of(new ExitCommand());

        default:
            throw new ParseException("Invalid Command Word");
        }
    }

}

