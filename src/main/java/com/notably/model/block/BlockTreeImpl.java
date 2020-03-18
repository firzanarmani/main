package com.notably.model.block;

import static java.util.Objects.requireNonNull;

import com.notably.commons.core.path.Path;
import com.notably.model.block.exceptions.NoSuchBlockException;

/**
 * Custom tree-like data structure that uses the Path object
 * to obtain the Block needed for manipulation.
 */
public class BlockTreeImpl implements BlockTree {
    private BlockTreeItem root;

    public BlockTreeImpl() {
        this.root = BlockTreeItemImpl.createRootBlockTreeItem();
    }

    @Override
    public BlockTreeItem getRootBlock() {
        return this.root;
    }

    @Override
    public void add(Path path, Block newBlock) throws NoSuchBlockException {
        requireNonNull(path);
        requireNonNull(newBlock);
        BlockTreeItem currentBlock = get(path);
        for (String component : path.getComponents()) {
            currentBlock = currentBlock.getBlockChild(new Title(component));
        }
        currentBlock.addBlockChild(newBlock);
    }

    @Override
    public void set(Path path, Block newBlock) {
        requireNonNull(path);
        requireNonNull(newBlock);
        BlockTreeItem currentBlock = get(path);
        BlockTreeItem parentBlock;
        if (!currentBlock.isRootBlock()) {
            parentBlock = currentBlock.getBlockParent();
            int index = parentBlock.getBlockChildren()
                .indexOf(currentBlock.getTreeItem());
            parentBlock.getBlockChildren().get(index).setValue(newBlock);
        } else {
            // TODO: Error - Cannot replace root
        }
    }

    @Override
    public void remove(Path path) {
        requireNonNull(path);
        BlockTreeItem currentBlock = get(path);
        BlockTreeItem parentBlock;
        if (!currentBlock.isRootBlock()) {
            parentBlock = currentBlock.getBlockParent();
            int index = parentBlock.getBlockChildren()
                .indexOf(currentBlock.getTreeItem());
            parentBlock.getBlockChildren().remove(index);
        } else {
            // TODO: Error - Cannot delete root
        }
    }

    @Override
    public BlockTreeItem get(Path path) {
        requireNonNull(path);
        BlockTreeItem currentBlock = root;
        for (String component : path.getComponents()) {
            currentBlock = currentBlock.getBlockChild(new Title(component));
        }
        return currentBlock;
    }
}
