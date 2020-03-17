package com.notably.model.block;

import java.util.List;
import java.util.Objects;

import com.notably.model.block.exceptions.DuplicateBlockException;
import com.notably.model.block.exceptions.NoSuchBlockException;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Represents a BlockNode in the Notably data structure.
 */
public class BlockNodeImpl extends TreeItem<Block> implements BlockNode {
    private TreeItem<Block> blockNode;

    public BlockNodeImpl(Block block) {
        blockNode = new TreeItem<Block>(block);
    }

    public BlockNodeImpl(TreeItem<Block> treeItem) {
        blockNode = treeItem;
    }

    public static BlockNode createRootBlockNode() {
        return new BlockNodeImpl(BlockImpl.createRootBlock());
    }

    public static BlockNode createBlockNode(Block block) {
        return new BlockNodeImpl(block);
    }

    @Override
    public TreeItem<Block> getTreeItem() {
        return blockNode;
    }

    @Override
    public Title getTitle() {
        return blockNode.getValue().getTitle();
    }

    @Override
    public Body getBody() {
        return blockNode.getValue().getBody();
    }

    @Override
    public Block getBlock() {
        return blockNode.getValue();
    }

    @Override
    public BlockNode getBlockParent() {
        return new BlockNodeImpl(blockNode.getParent());
    }

    @Override
    public ObservableList<TreeItem<Block>> getObservableChildren() {
        return blockNode.getChildren();
    }

    @Override
    public void setChildren(List<TreeItem<Block>> newChildren) {
        Objects.requireNonNull(newChildren);
        blockNode.getChildren().setAll(newChildren);
    }

    @Override
    public BlockNode getChild(Title title) throws NoSuchBlockException {
        Objects.requireNonNull(title);
        TreeItem<Block> child = blockNode.getChildren()
            .stream()
            .filter(block -> block.getValue()
                .getTitle()
                .equals(title))
            .findFirst()
            .orElseThrow(() -> new NoSuchBlockException(title.getText()));
        return new BlockNodeImpl(child);
    }

    @Override
    public void setChild(Title title, Block newBlock) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(newBlock);
        BlockNode child = getChild(title);
        child.getTreeItem().setValue(newBlock);
    }

    @Override
    public void addChild(Block block) throws DuplicateBlockException {
        boolean hasMatchingChild = blockNode.getChildren()
            .stream()
            .anyMatch(child -> child.getValue()
                .getTitle()
                .equals(block.getTitle()));
        if (hasMatchingChild) {
            throw new DuplicateBlockException();
        } else {
            blockNode.getChildren().add(new TreeItem<Block>(block));
        }
    }

    @Override
    public void removeChild(Block toRemove) {
        BlockNode child = getChild(toRemove.getTitle());
        blockNode.getChildren().remove(child.getTreeItem());
    }

    @Override
    public boolean isRoot() {
        return blockNode.getValue()
                .getTitle()
                .getText()
                .equals("Root")
            && blockNode.getParent() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof BlockNode)) {
            return false;
        }

        BlockNode otherBlock = (BlockNode) obj;
        return otherBlock.getTitle().equals(this.getTitle())
            && otherBlock.getBlockParent().equals(this.getBlockParent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getParent(), this.getTitle());
    }

}
