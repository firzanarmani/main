package com.notably.model.block;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Implementation class of Block.
 */
public class BlockImpl implements Block {
    private Title title;
    private Body body;

    /**
     * Initializes a block without the body.
     * Used when creating a root block or a block without the optional body argument.
     */
    public BlockImpl(Title title) {
        this(title, new Body(""));
    }

    /**
     * Initializes a block with body content.
     * Used when creating a block with additional body argument.
     */
    public BlockImpl(Title title, Body body) {
        requireNonNull(title);
        requireNonNull(body);
        this.title = title;
        this.body = body;
    }

    /**
     * Static method to create and return a root block.
     */
    public static Block createRootBlock() {
        return new BlockImpl(new Title("Root"), new Body("Create a new note to get started!"));
    }

    @Override
    public Title getTitle() {
        return this.title;
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Block // instanceof handles nulls
                && title.getText().equals(((Block) other).getTitle().getText())
                && body.getText().equals(((Block) other).getBody().getText())); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(title.getText());
    }
}
