package seedu.address.model.block.exceptions;

/**
 * Signals that the operation will result in duplicate Block (Blocks are considered duplicates if they have the same
 * title in the same directory).
 */
public class DuplicateBlockException extends RuntimeException {
    public DuplicateBlockException() {
        super("Operation would result in duplicate blocks in the same directory");
    }
}
