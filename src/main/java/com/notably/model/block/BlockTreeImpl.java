package com.notably.model.block;

import com.notably.commons.core.path.Path;
import com.notably.model.block.exceptions.NoSuchBlockException;

import javafx.scene.control.TreeItem;

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
        BlockNode currentBlock = get(path);
        for (String component : path.getComponents()) {
            currentBlock = currentBlock.getChild(new Title(component));
        }
        currentBlock.addChild(newBlock);
        // currentBlock.getChildren().add(new TreeItem<Block>(newBlock));
    }

    @Override
    public void set(Path path, Block newBlock) {
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
        BlockNode currentBlock = root;
        for (String component : path.getComponents()) {
            currentBlock = currentBlock.getChild(new Title(component));
        }
        return currentBlock;
    }
}
