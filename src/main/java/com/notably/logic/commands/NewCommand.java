package com.notably.logic.commands;

import static java.util.Objects.requireNonNull;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.exceptions.CommandException;
import com.notably.model.Model;
import com.notably.model.block.Block;
import com.notably.model.block.exceptions.DuplicateBlockException;

/**
 * Represent a command used to add a new block.
 */
public class NewCommand extends Command {
    public static final String COMMAND_WORD = "new";
    public static final String COMMAND_SHORTHAND = "n";
    private final Block toAdd;
    private AbsolutePath path;

    public NewCommand(Block block, AbsolutePath path) {
        requireNonNull(block);
        this.toAdd = block;
        this.path = path;
    }

    /**
     * Add a new block to the tree structure.
     * @param notablyModel used to access the tree structure.
     * @throws CommandException when block of the same Title is detected.
     */
    public void execute(Model notablyModel) throws CommandException {
        requireNonNull(notablyModel);
        try {
            notablyModel.addBlockToCurrentPath(toAdd);
        } catch (IllegalArgumentException | DuplicateBlockException ex) {
            throw new CommandException(ex.getMessage());
        }
    }
}
