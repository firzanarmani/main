package com.notably.model.block;

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.notably.model.block.exceptions.DuplicateBlockException;
import com.notably.model.block.exceptions.NoSuchBlockException;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Represents a BlockTreeItem in the Notably data structure.
 */
public class BlockTreeItemImpl implements BlockTreeItem {
    private TreeItem<Block> blockTreeItem;

    public BlockTreeItemImpl(Block block) {
        requireNonNull(block);
        blockTreeItem = new TreeItem<Block>(block);
    }

    public static BlockTreeItem createRootBlockTreeItem() {
        return new BlockTreeItemImpl(BlockImpl.createRootBlock());
    }

    @Override
    public TreeItem<Block> getTreeItem() {
        return blockTreeItem;
    }

    @Override
    public Title getTitle() {
        return blockTreeItem.getValue().getTitle();
    }

    @Override
    public Body getBody() {
        return blockTreeItem.getValue().getBody();
    }

    @Override
    public Block getBlock() {
        return blockTreeItem.getValue();
    }

    @Override
    public BlockTreeItem getBlockParent() {
        return (BlockTreeItem) blockTreeItem.getParent();
    }

    @Override
    public ObservableList<TreeItem<Block>> getBlockChildren() {
        return blockTreeItem.getChildren();
    }

    /*
    TODO: Implement and test after storage is implemented
    @Override
    public void setChildren(List<TreeItem<Block>> newChildren) {
        requireNonNull(newChildren);
        blockTreeItem.getChildren().setAll(newChildren);
    } */

    @Override
    public BlockTreeItem getBlockChild(Title title) throws NoSuchBlockException {
        requireNonNull(title);
        TreeItem<Block> child = blockTreeItem.getChildren()
            .stream()
            .filter(block -> block.getValue()
                .getTitle()
                .equals(title))
            .findFirst()
            .orElseThrow(() -> new NoSuchBlockException(title.getText()));
        return (BlockTreeItem) child;
    }

    @Override
    public void setBlockChild(Title title, Block newBlock) {
        requireNonNull(title);
        requireNonNull(newBlock);
        BlockTreeItem child = getBlockChild(title);
        child.getTreeItem().setValue(newBlock);
    }

    @Override
    public void addBlockChild(Block block) throws DuplicateBlockException {
        requireNonNull(block);
        boolean hasMatchingChild = blockTreeItem.getChildren()
            .stream()
            .anyMatch(child -> child.getValue()
                .getTitle()
                .equals(block.getTitle()));
        if (hasMatchingChild) {
            throw new DuplicateBlockException();
        } else {
            blockTreeItem.getChildren().add(new TreeItem<Block>(block));
        }
    }

    @Override
    public void removeBlockChild(Block toRemove) {
        requireNonNull(toRemove);
        BlockTreeItem child = getBlockChild(toRemove.getTitle());
        blockTreeItem.getChildren().remove(child.getTreeItem());
    }

    @Override
    public boolean isRootBlock() {
        return blockTreeItem.getValue()
                .getTitle()
                .getText()
                .equals("Root")
            && blockTreeItem.getParent() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (blockTreeItem == obj) {
            return true;
        }

        if (!(obj instanceof BlockTreeItem)) {
            return false;
        }

        BlockTreeItem otherBlock = (BlockTreeItem) obj;
        return otherBlock.getTitle().equals(this.getTitle())
            && otherBlock.getBlockParent().equals(this.getBlockParent())
            && otherBlock.getBlockChildren().equals(this.getBlockChildren());
    }

    @Override
    public int hashCode() {
        return hash(this.getTitle(), this.getBlockParent(), this.getBlockChildren());
    }

}
