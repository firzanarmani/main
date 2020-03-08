package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODY;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Block;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "new";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a block to the current directory. "
            + "Parameters: "
            + PREFIX_TITLE+ "TITLE "
            + PREFIX_BODY+ "BODY "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TITLE+ "CS2103"
            + PREFIX_BODY+ "Take some notes here";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Block toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Block block) {
        requireNonNull(block);
        toAdd = block;
    }

    @Override
    public void execute(Model model) throws CommandException {
        requireNonNull(model);

        model.addBlock(toAdd);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
