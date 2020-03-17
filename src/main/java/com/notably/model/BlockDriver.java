package com.notably.model;

import com.notably.commons.core.path.Path;

import com.notably.model.block.Block;
import com.notably.model.block.BlockTree;

/**
 * API for BlockDriver component.
 */
public interface BlockDriver {
    /**
     * Gets the BlockTree currently in use by Notably.
     */
    BlockTree getBlockTree();

    /**
     * Check if currently open block has the specified child block.
     */
    boolean hasBlock(Block b);

    /**
     * Adds a block to the specified path, if possible.
     */
    void addBlock(Path p, Block b);

    /**
     * Removes the block at the specified path, if possible.
     */
    void removeBlock(Path p);

    /**
     * Opens the block at the specified path, if possible.
     */
    Block openBlock(Path p);

    void setBlock(Block targetBlock, Block newBlock);

    Path getBlockPath(Block b);

    /*
    TODO: To implement after storage classes are ready
    void setBlockTree(List<Block> blocks);

    void resetData(BlockDriver newData);
    */
}
