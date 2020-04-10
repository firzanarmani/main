package com.notably.model.block;

import static com.notably.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.model.block.exceptions.CannotModifyRootException;
import com.notably.model.block.exceptions.DuplicateBlockException;
import com.notably.model.block.exceptions.NoSuchBlockException;
import com.notably.testutil.TypicalBlockModel;

public class BlockTreeTest {
    private static AbsolutePath toRoot;
    private static AbsolutePath toCs2103;
    private static AbsolutePath toCs2103Week1;
    private static AbsolutePath toCs2103Week1Lecture;

    private BlockTree blockTree = new BlockTreeImpl();

    @BeforeAll
    public static void setUp() {
        toRoot = AbsolutePath.fromString("/");
        toCs2103 = AbsolutePath.fromString("/CS2103");
        toCs2103Week1 = AbsolutePath.fromString("/CS2103/Week1");
        toCs2103Week1Lecture = AbsolutePath.fromString("/CS2103/Week1/Lecture");
    }

    @BeforeEach
    private void setUpTestTree() {
        Block newBlock1 = new BlockImpl(new Title("CS2103"));
        Block newBlock2 = new BlockImpl(new Title("CS3230"));
        Block newBlock3 = new BlockImpl(new Title("Week1"));
        blockTree.add(toRoot, newBlock1);
        blockTree.add(toRoot, newBlock2);
        blockTree.add(toCs2103, newBlock3);
    }

    @AfterEach
    private void tearDownTestTree() {
        blockTree = new BlockTreeImpl();
    }

    @Test
    public void constructor() {
        assertTrue(blockTree.getRootBlock().isRootBlock());
    }

    @Test
    public void get_root() {
        assertEquals(blockTree.get(toRoot), blockTree.getRootBlock());
    }

    @Test
    public void get_pathDoesNotExist_throwsNoSuchBlockException() {
        AbsolutePath nonExistentPath = AbsolutePath.fromString("/SomeNonExistentPath");
        assertThrows(NoSuchBlockException.class, () -> blockTree.get(nonExistentPath));
    }

    @Test
    public void add_block() {
        Block newBlock = new BlockImpl(new Title("Week2"));
        AbsolutePath toCs2103Week2 = AbsolutePath.fromString("/CS2103/Week2");
        blockTree.add(toCs2103, newBlock);
        assertEquals(blockTree.get(toCs2103Week2).getBlock(), newBlock);
    }

    @Test
    public void add_block_throwsDuplicateBlockException() {
        Block newBlock = new BlockImpl(new Title("CS2103"));
        assertThrows(DuplicateBlockException.class, () -> blockTree.add(toRoot, newBlock));
    }

    @Test
    public void add_block_deeperNestedBlock() {
        Block newBlock = new BlockImpl(new Title("Lecture"));
        blockTree.add(toCs2103Week1, newBlock);
        assertEquals(blockTree.get(toCs2103Week1Lecture).getBlock(), newBlock);
    }

    @Test
    public void remove_block() {
        blockTree.remove(toCs2103Week1);
        assertThrows(NoSuchBlockException.class, () -> blockTree.get(toCs2103Week1));
    }

    @Test
    public void remove_root_throwsCannotModifyRootException() {
        assertThrows(CannotModifyRootException.class, () -> blockTree.remove(toRoot));
    }

    @Test
    public void set_root_throwsCannotModifyRootException() {
        Block newBlock = new BlockImpl(new Title("CS2107"));
        assertThrows(CannotModifyRootException.class, () -> blockTree.set(toRoot, newBlock));
    }

    @Test
    public void set_block_editBlock() {
        Block editedBlock = new BlockImpl(new Title("Week2"));
        AbsolutePath editedPath = AbsolutePath.fromString("/CS2103/Week2");
        blockTree.set(toCs2103Week1, editedBlock);
        assertEquals(blockTree.get(editedPath).getBlock(), editedBlock);
    }

    @Test
    public void testRootBodyContent() {
        blockTree = new BlockTreeImpl();
        assertEquals(blockTree.getRootBlock().getBody().getText(), "Create a new note to get started!");

        blockTree.add(TypicalBlockModel.PATH_TO_ROOT, TypicalBlockModel.Y2S2);
        assertEquals(blockTree.getRootBlock().getBody().getText(), "Open a note to get started!");

        blockTree.remove(TypicalBlockModel.PATH_TO_Y2S2);
        assertEquals(blockTree.getRootBlock().getBody().getText(), "Create a new note to get started!");
    }
}
