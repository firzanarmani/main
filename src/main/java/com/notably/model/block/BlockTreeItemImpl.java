package com.notably.model.block;

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import java.util.List;

import com.notably.model.block.exceptions.DuplicateBlockException;
import com.notably.model.block.exceptions.NoSuchBlockException;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Represents a BlockTreeItem in the Notably data structure.
 */
public class BlockTreeItemImpl extends TreeItem<Block> implements BlockTreeItem {
    public BlockTreeItemImpl(Block block) {
        super(requireNonNull(block));
    }

    public static BlockTreeItem createRootBlockNode() {
        return new BlockTreeItemImpl(BlockImpl.createRootBlock());
    }

    @Override
    public TreeItem<Block> getTreeItem() {
        return this;
    }

    @Override
    public Title getTitle() {
        return this.getValue().getTitle();
    }

    @Override
    public Body getBody() {
        return this.getValue().getBody();
    }

    @Override
    public Block getBlock() {
        return this.getValue();
    }

    @Override
    public BlockTreeItem getBlockParent() {
        return (BlockTreeItem) this.getParent();
    }

    @Override
    public ObservableList<TreeItem<Block>> getObservableChildren() {
        return this.getChildren();
    }

    @Override
    public void setChildren(List<TreeItem<Block>> newChildren) {
        requireNonNull(newChildren);
        this.getChildren().setAll(newChildren);
    }

    @Override
    public BlockTreeItem getChild(Title title) throws NoSuchBlockException {
        requireNonNull(title);
        TreeItem<Block> child = this.getChildren()
            .stream()
            .filter(block -> block.getValue()
                .getTitle()
                .equals(title))
            .findFirst()
            .orElseThrow(() -> new NoSuchBlockException(title.getText()));
        return (BlockTreeItem) child;
    }

    @Override
    public void setChild(Title title, Block newBlock) {
        requireNonNull(title);
        requireNonNull(newBlock);
        BlockTreeItem child = getChild(title);
        child.getTreeItem().setValue(newBlock);
    }

    @Override
    public void addChild(Block block) throws DuplicateBlockException {
        boolean hasMatchingChild = this.getChildren()
            .stream()
            .anyMatch(child -> child.getValue()
                .getTitle()
                .equals(block.getTitle()));
        if (hasMatchingChild) {
            throw new DuplicateBlockException();
        } else {
            this.getChildren().add(new TreeItem<Block>(block));
        }
    }

    @Override
    public void removeChild(Block toRemove) {
        BlockTreeItem child = getChild(toRemove.getTitle());
        this.getChildren().remove(child.getTreeItem());
    }

    @Override
    public boolean isRoot() {
        return this.getValue()
                .getTitle()
                .getText()
                .equals("Root")
            && this.getParent() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof BlockTreeItem)) {
            return false;
        }

        BlockTreeItem otherBlock = (BlockTreeItem) obj;
        return otherBlock.getTitle().equals(this.getTitle())
            && otherBlock.getBlockParent().equals(this.getBlockParent())
            && otherBlock.getObservableChildren().equals(this.getObservableChildren());
    }

    @Override
    public int hashCode() {
        return hash(this.getParent(), this.getTitle(), this.getObservableChildren());
    }

}
