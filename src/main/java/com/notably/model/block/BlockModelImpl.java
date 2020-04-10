package com.notably.model.block;

import java.util.Objects;

import com.notably.commons.path.AbsolutePath;
import com.notably.model.block.exceptions.NoSuchBlockException;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The implementation class of BlockModel.
 */
public class BlockModelImpl implements BlockModel {
    private BlockTree blockTree;
    private Property<AbsolutePath> currentlyOpenPath;

    public BlockModelImpl() {
        blockTree = new BlockTreeImpl();
        currentlyOpenPath = new SimpleObjectProperty<AbsolutePath>(AbsolutePath.fromString("/"));
    }

    @Override
    public BlockTree getBlockTree() {
        return blockTree;
    }

    @Override
    public void setBlockTree(BlockTree blockTree) {
        this.blockTree = blockTree;
    }

    @Override
    public void resetData(BlockModel newData) {
        Objects.requireNonNull(newData);
        setBlockTree(newData.getBlockTree());
        setCurrentlyOpenBlock(newData.getCurrentlyOpenPath());
    }

    @Override
    public AbsolutePath getCurrentlyOpenPath() {
        return currentlyOpenPath.getValue();
    }

    @Override
    public Property<AbsolutePath> currentlyOpenPathProperty() {
        return currentlyOpenPath;
    }

    @Override
    public boolean hasPath(AbsolutePath p) {
        try {
            blockTree.get(p);
            return true;
        } catch (NoSuchBlockException e) {
            return false;
        }
    }

    @Override
    public void setCurrentlyOpenBlock(AbsolutePath p) {
        if (!hasPath(p)) {
            throw new NoSuchBlockException(p.getStringRepresentation());
        }
        currentlyOpenPath.setValue(p);
    }

    @Override
    public void addBlockToCurrentPath(Block b) {
        blockTree.add(getCurrentlyOpenPath(), b);
    }

    @Override
    public void removeBlock(AbsolutePath p) {
        blockTree.remove(p);
    }

    @Override
    public void updateCurrentlyOpenBlockBody(Body newBody) {
        Block newBlock = new BlockImpl(blockTree.get(getCurrentlyOpenPath()).getTitle(), newBody);
        blockTree.set(getCurrentlyOpenPath(), newBlock);
    }
}
