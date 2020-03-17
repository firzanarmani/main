package com.notably.model.block;

import com.notably.commons.core.path.Path;

import javafx.scene.control.TreeItem;

/**
 * Custom tree-like data structure that uses the Path object
 * to obtain the Block needed for manipulation.
 */
public interface BlockTree {
    TreeItem<Block> getRootBlock();

    /**
     * Adds a new Block to the block at the path specified.
     * @param path Full path of the parent block
     * @param newBlock The block to be added
     */
    void add(Path path, Block newBlock);

    /**
     * Sets the block at the specified path to a new block.
     * @param path Full path of the block
     * @param newBlock The new block to be set
     */
    void set(Path path, Block newBlock);

    /**
     * Remove the block at the specified path.
     * @param path
     */
    void remove(Path path);

    BlockNode get(Path path);
}
