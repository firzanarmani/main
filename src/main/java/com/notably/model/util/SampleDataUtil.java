package com.notably.model.util;

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
 * Contains utility methods for populating {@code BlockTree} with sample data.
 */
public class SampleDataUtil {
    private static Block welcome = new BlockImpl(new Title("Welcome"),
        new Body("We, at Notably, are happy to welcome to this great application!\n\n"
            + "Let's quickly run through how to use our application!\n\n- Type 'open Step 1' to open the 'Step 1' "
            + "note.\n\nPay attention to the currently opened note in the sidebar on the left!"));
    private static Block step1 = new BlockImpl(new Title("Step 1"),
        new Body("Nice! You're good at this 'opening note' thing.\n\n"
            + "As you can see, the 'Step 1' note is a child of the 'Welcome' note. \n\nNow, let's go to the 'Step 2' "
            + "note. BUT WAIT! If you notice in the sidebar, 'Step 2' is not a child of the current note. "
            + "(Notes can work like folders too!)\n"
            + "We can do this in 2 ways:\n"
            + "Basic method:\n- Type 'open ../' to go back one note (the parent of the current note)\n"
            + "- Then type 'open Step 2'\n\nAdvanced method:\n- Type 'open ../Step 2\n\n"
            + "! If the note you want to go to is even further back, you can stack "
            + "the navigation paths, for example 'open ../../../Note1/Note1Child'"));
    private static Block step2 = new BlockImpl(new Title("Step 2"),
        new Body("You're a navigation master at this point! Well done!\n\nLet's add a new note here!\n\n- Type 'new"
            + " -t A new title -b Contents here' to create a child note here with the title 'A new title' and the "
            + "body content 'Contents here'\n\nOnce that's done, check the note you've just made and then navigate to "
            + "'Step 3'"));
    private static Block step3 = new BlockImpl(new Title("Step 3"),
        new Body("Good work on making a new note there. Now let's try deleting a note!\n\n- Type 'delete DeleteMe' to "
            + "delete the note with the title 'DeleteMe"));
    private static Block deleteMe = new BlockImpl(new Title("DeleteMe"),
        new Body("Please... Put me out of my misery... Delete me!!!!"));

    public static BlockModel getSampleBlockModel() {
        BlockModel sampleBm = new BlockModelImpl();
        BlockTree sampleBt = new BlockTreeImpl();
        sampleBt.add(AbsolutePath.fromString("/"), welcome);
        sampleBt.add(AbsolutePath.fromString("/Welcome"), step1);
        sampleBt.add(AbsolutePath.fromString("/Welcome/"), step2);
        sampleBt.add(AbsolutePath.fromString("/Welcome/"), step3);
        sampleBt.add(AbsolutePath.fromString("/Welcome/Step 3"), deleteMe);
        sampleBm.setBlockTree(sampleBt);
        sampleBm.setCurrentlyOpenBlock(AbsolutePath.fromString("/Welcome"));
        return sampleBm;
    }
}
