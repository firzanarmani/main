package com.notably.logic.parser;

import static com.notably.logic.parser.CliSyntax.PREFIX_BODY;
import static com.notably.logic.parser.CliSyntax.PREFIX_JUMP;
import static com.notably.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.ArrayList;
import java.util.List;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.Command;
import com.notably.logic.commands.NewCommand;
import com.notably.logic.commands.OpenCommand;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.Model;
import com.notably.model.block.Block;
import com.notably.model.block.BlockImpl;
import com.notably.model.block.Body;
import com.notably.model.block.Title;

/**
 * Represents a Parser for New Command.
 */
public class NewCommandParser implements CommandParser<Command> {
    private Model notablyModel;

    public NewCommandParser(Model notablyModel) {
        this.notablyModel = notablyModel;
    }
    /**
     * Parse input and create NewCommand and OpenCommand.
     * @param args parse userInput used to create block.
     * @return List of command to execute.
     * @throws ParseException when input is invalid.
     */
    public List<Command> parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TITLE, PREFIX_BODY, PREFIX_JUMP);

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_TITLE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format("Invalid Command"));
        }

        String title = argMultimap.getValue(PREFIX_TITLE).get();
        String body;
        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BODY)) {
            body = "";
        } else {
            body = argMultimap.getValue(PREFIX_BODY).get();
        }

        Block block;
        try {
            block = new BlockImpl(new Title(title), new Body(body));
        } catch (IllegalArgumentException ex) {
            throw new ParseException(ex.getMessage());
        }

        List<Command> commands = new ArrayList<>();
        commands.add(new NewCommand(block));

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_JUMP)) {
            return commands;
        }

        AbsolutePath path = ParserUtil.createAbsolutePath(title, notablyModel.getCurrentlyOpenPath());
        commands.add(new OpenCommand(path));
        return commands;
    }

}
