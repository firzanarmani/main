package com.notably.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.notably.commons.core.path.AbsolutePath;
import com.notably.commons.core.path.Path;

import com.notably.model.block.Block;
import com.notably.model.block.BlockTreeItem;
import com.notably.model.block.BlockTree;
import com.notably.model.block.BlockTreeImpl;
import com.notably.model.block.exceptions.NoSuchBlockException;

/**
 * Manages manipulation of Notably's BlockTree.
 */
public class BlockDriverManager implements BlockDriver {
    private BlockTree blockTree;
    private BlockTreeItem currentlyOpen;

    public BlockDriverManager() {
        blockTree = new BlockTreeImpl();
        currentlyOpen = blockTree.getRootBlock();
    }

    /*
    TODO: To implement after storage classes are ready
    public BlockDriverManager(BlockDriver toBeCopied) {
        this();
        resetData(toBeCopied);
    }
     */

    @Override
    public BlockTree getBlockTree() {
        return blockTree;
    }

    @Override
    public boolean hasBlock(Block b) {
		requireNonNull(b);
        try {
            currentlyOpen.getChild(b.getTitle());
            return true;
        } catch (NoSuchBlockException e) {
            return false;
        }
    }

    @Override
    public void addBlock(Path p, Block b) {
		requireNonNull(p);
		requireNonNull(b);
        if (p instanceof AbsolutePath) {
            blockTree.add(p, b);
        } else {
            currentlyOpen.addChild(b);
        }
    }

    @Override
    public void removeBlock(Path p) {
		requireNonNull(p);
        // TODO: Handle delete warning prompt in UI
        if (p instanceof AbsolutePath) {
            blockTree.remove(p);
        } else {
            // TODO: Handle RelativePath
        }
    }

    @Override
    public Block openBlock(Path p) {
		requireNonNull(p);
        if (p instanceof AbsolutePath) {
            currentlyOpen = blockTree.get(p);
        } else {
            // TODO: Handle RelativePath
        }
        return currentlyOpen.getBlock();
    }

    @Override
    public void setBlock(Block targetBlock, Block newBlock) {
		requireNonNull(targetBlock);
		requireNonNull(newBlock);
        currentlyOpen.getTreeItem().setValue(newBlock);
    }

    @Override
    public Path getBlockPath(Block b) {
		requireNonNull(b);
        List<String> absolutePathList = new ArrayList<String>();
        BlockTreeItem parentNode = currentlyOpen.getBlockParent();
        while (!parentNode.isRoot()) {
            absolutePathList.add(parentNode.getTitle().getText());
            parentNode = parentNode.getBlockParent();
        }
        Collections.reverse(absolutePathList);
        // TODO: After Path classes are complete
        // return new AbsolutePath(absolutePathList);
        return null;
    }

}
