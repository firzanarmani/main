package com.notably.model.block;

/**
 * API of the Block component.
 */
public interface Block {
    /**
     * Gets the title of a block.
     */
    Title getTitle();

    /**
     * Gets the body content of a block.
     */
    Body getBody();
}
