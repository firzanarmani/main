package seedu.address.model.block;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Body {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String blockBody;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Body(String body) {
        requireNonNull(body);
        checkArgument(isValidBody(body), MESSAGE_CONSTRAINTS);
        blockBody = body;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidBody(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return blockBody;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Body // instanceof handles nulls
                && blockBody.equals(((Body) other).blockBody)); // state check
    }

    @Override
    public int hashCode() {
        return blockBody.hashCode();
    }

}
