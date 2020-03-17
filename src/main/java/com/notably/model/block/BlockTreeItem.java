package com.notably.model.block;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * API of the BlockNode component.
 */
public interface BlockTreeItem {
    Title getTitle();

    Body getBody();

    Block getBlock();

    TreeItem<Block> getTreeItem();

    /**
     * Gets the parent block of a block, if possible.
     * Returns an {@code Optional.empty()} if the block is the root block.
     */
    BlockTreeItem getBlockParent();

    /**
     * Gets a list of children blocks of a block.
     */
    ObservableList<TreeItem<Block>> getObservableChildren();

    /**
     * Replaces all the children of the block with a new list of children
     */
    void setChildren(List<TreeItem<Block>> newChildren);

    /**
     * Finds a child block of a block, with the given input.
     */
    BlockTreeItem getChild(Title title);

    /**
     * Replaces a child block of a block, that matches the title, with a new child block.
     */
    void setChild(Title title, Block newBlock);

    /**
     * Adds a single new child to a block.
     */
    void addChild(Block newBlock);

    /**
     * Removes a specified child block from a block.
     * @param toRemove
     */
    void removeChild(Block toRemove);

    /**
     * Checks if a block is a root block.
     */
    boolean isRoot();
}
