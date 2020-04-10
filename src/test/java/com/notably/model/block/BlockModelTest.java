package com.notably.model.block;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.model.block.exceptions.CannotModifyRootException;

import javafx.collections.FXCollections;

public class BlockModelTest {
    private BlockModel blockModel;

    @BeforeEach
    public void setUpBeforeEach() {
        blockModel = new BlockModelImpl();
        blockModel.addBlockToCurrentPath(new BlockImpl(new Title("CS2103")));
        blockModel.addBlockToCurrentPath(new BlockImpl(new Title("CS3230")));
    }

    @Test
    public void constructor() {
        blockModel = new BlockModelImpl();
        assertEquals(blockModel.getBlockTree()
            .getRootBlock()
            .getTreeItem()
            .getChildren(), FXCollections.emptyObservableList());
        assertEquals(blockModel.getCurrentlyOpenPath(), AbsolutePath.fromString("/"));
    }

    @Test
    public void addBlockToCurrentPath_addToRoot() {
        Block newBlock = new BlockImpl(new Title("CS2107"));
        blockModel.addBlockToCurrentPath(newBlock);
        assertTrue(blockModel.hasPath(AbsolutePath.fromString("/CS2107")));
    }

    @Test
    public void removeBlock_removeRoot() {
        assertThrows(CannotModifyRootException.class, () -> blockModel.removeBlock(AbsolutePath.fromString("/")));
    }

    @Test
    public void updateCurrentlyOpenBlockBody_setRootBody_throwsCannotModifyRootException () {
        assertThrows(CannotModifyRootException.class, () -> blockModel.updateCurrentlyOpenBlockBody(
                new Body("New Body")));
    }

    @Test
    public void setCurrentlyOpenBlock_changePath() {
        blockModel.setCurrentlyOpenBlock(AbsolutePath.fromString("/CS2103"));
        assertEquals(blockModel.getCurrentlyOpenPath(), AbsolutePath.fromString("/CS2103"));
    }

    @Test
    public void addBlockToCurrentPath_addToNonRootPath() {
        blockModel.setCurrentlyOpenBlock(AbsolutePath.fromString("/CS3230"));
        blockModel.addBlockToCurrentPath(new BlockImpl(new Title("Week1")));
        blockModel.addBlockToCurrentPath(new BlockImpl(new Title("Week2")));
        blockModel.setCurrentlyOpenBlock(AbsolutePath.fromString("/"));
        assertTrue(blockModel.hasPath(AbsolutePath.fromString("/CS3230/Week1")));
        assertTrue(blockModel.hasPath(AbsolutePath.fromString("/CS3230/Week2")));
    }

    @Test
    public void removeBlock_nonRootPath() {
        blockModel.setCurrentlyOpenBlock(AbsolutePath.fromString("/CS3230"));
        blockModel.addBlockToCurrentPath(new BlockImpl(new Title("Week1")));
        blockModel.setCurrentlyOpenBlock(AbsolutePath.fromString("/"));
        blockModel.removeBlock(AbsolutePath.fromString("/CS3230/Week1"));
    }

    @Test
    public void updateCurrentlyOpenBlockBody_nonRootPath() {
        blockModel.setCurrentlyOpenBlock(AbsolutePath.fromString("/CS3230"));
        assertEquals(blockModel.getBlockTree()
            .get(blockModel.getCurrentlyOpenPath())
            .getBody()
            .getText(), "");
        blockModel.updateCurrentlyOpenBlockBody(new Body("These are notes."));
        assertEquals(blockModel.getBlockTree()
            .get(blockModel.getCurrentlyOpenPath())
            .getBody()
            .getText(), "These are notes.");
    }
}
