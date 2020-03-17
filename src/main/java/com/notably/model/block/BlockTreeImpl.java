package com.notably.model.block;

import static java.util.Objects.requireNonNull;

import com.notably.commons.core.path.Path;
import com.notably.model.block.exceptions.NoSuchBlockException;

/**
 * Custom tree-like data structure that uses the Path object
 * to obtain the Block needed for manipulation.
 */
public class BlockTreeImpl implements BlockTree {
    private BlockNode root;

    public BlockTreeImpl() {
        this.root = BlockNodeImpl.createRootBlockNode();
    }

    @Override
    public BlockNode getRootBlock() {
        return this.root;
    }

    @Override
    public void add(Path path, Block newBlock) throws NoSuchBlockException {
        requireNonNull(path);
        requireNonNull(newBlock);
        BlockNode currentBlock = get(path);
        for (String component : path.getComponents()) {
            currentBlock = currentBlock.getChild(new Title(component));
        }
        currentBlock.addChild(newBlock);
    }

    @Override
    public void set(Path path, Block newBlock) {
        requireNonNull(path);
        requireNonNull(newBlock);
        BlockNode currentBlock = get(path);
        BlockNode parentBlock;
        if (!currentBlock.isRoot()) {
            parentBlock = currentBlock.getBlockParent();
            int index = parentBlock.getObservableChildren()
                .indexOf(currentBlock.getTreeItem());
            parentBlock.getObservableChildren().get(index).setValue(newBlock);
        } else {
            // TODO: Error - Cannot replace root
        }
    }

    @Override
    public void remove(Path path) {
        requireNonNull(path);
        BlockNode currentBlock = get(path);
        BlockNode parentBlock;
        if (!currentBlock.isRoot()) {
            parentBlock = currentBlock.getBlockParent();
            int index = parentBlock.getObservableChildren()
                .indexOf(currentBlock.getTreeItem());
            parentBlock.getObservableChildren().remove(index);
        } else {
            // TODO: Error - Cannot delete root
        }
    }

    @Override
    public BlockNode get(Path path) {
        requireNonNull(path);
        BlockNode currentBlock = root;
        for (String component : path.getComponents()) {
            currentBlock = currentBlock.getChild(new Title(component));
        }
        return currentBlock;
    }
}
