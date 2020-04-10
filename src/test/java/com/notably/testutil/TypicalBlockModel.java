package com.notably.testutil;

import com.notably.commons.path.AbsolutePath;
import com.notably.model.block.Block;
import com.notably.model.block.BlockImpl;
import com.notably.model.block.BlockModel;
import com.notably.model.block.BlockModelImpl;
import com.notably.model.block.BlockTree;
import com.notably.model.block.BlockTreeImpl;
import com.notably.model.block.Body;
import com.notably.model.block.Title;

/**
 * A utility class containing a list of {@code Block} objects to be used in tests.
 */
public class TypicalBlockModel {
    public static final AbsolutePath PATH_TO_ROOT = AbsolutePath.fromString("/");
    public static final AbsolutePath PATH_TO_Y2S2 = AbsolutePath.fromString("/Y2S2");
    public static final AbsolutePath PATH_TO_CS2103T = AbsolutePath.fromString("/Y2S2/CS2103T");
    public static final AbsolutePath PATH_TO_CS2103T_LECTURES = AbsolutePath.fromString("/Y2S2/CS2103T/Lecture Notes");
    public static final AbsolutePath PATH_TO_CS2103T_TUTORIALS = AbsolutePath.fromString("/Y2S2/CS2103T/Tutorials");
    public static final AbsolutePath PATH_TO_CS2103T_TUTORIAL_1 =
            AbsolutePath.fromString("/Y2S2/CS2103T/Tutorials/Tutorial 1");
    public static final AbsolutePath PATH_TO_CS2103T_TUTORIAL_2 =
            AbsolutePath.fromString("/Y2S2/CS2103T/Tutorials/Tutorial 2");
    public static final AbsolutePath PATH_TO_CS2106 = AbsolutePath.fromString("/Y2S2/CS2106");
    public static final AbsolutePath PATH_TO_CS2106_LECTURES = AbsolutePath.fromString("/Y2S2/CS2106/Lecture Notes");
    public static final AbsolutePath PATH_TO_CS2106_TUTORIALS = AbsolutePath.fromString("/Y2S2/CS2106/Tutorials");
    public static final AbsolutePath PATH_TO_CS2106_TUTORIAL_1 =
            AbsolutePath.fromString("/Y2S2/CS2106/Tutorials/Tutorial 1");
    public static final AbsolutePath PATH_TO_CS2107 = AbsolutePath.fromString("/Y2S2/CS2107");
    public static final AbsolutePath PATH_TO_CS3230 = AbsolutePath.fromString("/Y2S2/CS3230");

    public static final AbsolutePath INITIAL_OPEN_PATH = PATH_TO_Y2S2;

    public static final Block Y2S2 = new BlockImpl(new Title("Y2S2"));
    public static final Block CS2103T = new BlockImpl(new Title("CS2103T"), new Body("Software Engineering"));
    public static final Block CS2103T_LECTURES = new BlockImpl(new Title("Lecture Notes"));
    public static final Block CS2103T_TUTORIALS = new BlockImpl(new Title("Tutorials"));
    public static final Block CS2103T_TUTORIAL_1 = new BlockImpl(new Title("Tutorial 1"),
            new Body("Q1: True, Q2: False"));
    public static final Block CS2103T_TUTORIAL_2 = new BlockImpl(new Title("Tutorial 2"),
            new Body("Q1: False, Q2: False"));
    public static final Block CS2106 = new BlockImpl(new Title("CS2106"),
            new Body("Introduction to Operating Systems"));
    public static final Block CS2106_LECTURES = new BlockImpl(new Title("Lecture Notes"));
    public static final Block CS2106_TUTORIALS = new BlockImpl(new Title("Tutorials"));
    public static final Block CS2106_TUTORIAL_1 = new BlockImpl(new Title("Tutorial 1"),
            new Body("Q1: True, Q2: False"));
    public static final Block CS2107 = new BlockImpl(new Title("CS2107"),
            new Body("Introduction to Computer Security"));
    public static final Block CS3230 = new BlockImpl(new Title("CS3230"),
            new Body("Design and Analysis of Algorithms"));

    /**
     * Returns an {@code BlockModel} with the typical blocks.
     */
    public static BlockModel getTypicalBlockModel() {
        BlockModel blockModel = new BlockModelImpl();
        BlockTree blockTree = new BlockTreeImpl();
        blockTree.add(PATH_TO_ROOT, Y2S2);
        blockTree.add(PATH_TO_Y2S2, CS2103T);
        blockTree.add(PATH_TO_CS2103T, CS2103T_LECTURES);
        blockTree.add(PATH_TO_CS2103T, CS2103T_TUTORIALS);
        blockTree.add(PATH_TO_CS2103T_TUTORIALS, CS2103T_TUTORIAL_1);
        blockTree.add(PATH_TO_CS2103T_TUTORIALS, CS2103T_TUTORIAL_2);
        blockTree.add(PATH_TO_Y2S2, CS2106);
        blockTree.add(PATH_TO_CS2106, CS2106_LECTURES);
        blockTree.add(PATH_TO_CS2106, CS2106_TUTORIALS);
        blockTree.add(PATH_TO_CS2106_TUTORIALS, CS2106_TUTORIAL_1);

        blockModel.setBlockTree(blockTree);
        blockModel.setCurrentlyOpenBlock(INITIAL_OPEN_PATH);

        return blockModel;
    }
}
