package com.notably.view.blockcontent;

import static com.notably.commons.util.CollectionUtil.requireAllNonNull;

import com.notably.logic.Logic;
import com.notably.model.Model;

import javafx.scene.layout.StackPane;

/**
 * View of the currently open block's content.
 */
public class BlockContent {

    private final BlockContentDisplayView blockContentDisplayView;
    private final BlockContentEditView blockContentEditView;

    public BlockContent(StackPane blockContentPlaceholder, Logic logic, Model model) {
        requireAllNonNull(blockContentPlaceholder, model, logic);

        blockContentDisplayView = new BlockContentDisplayView(model);
        blockContentEditView = new BlockContentEditView(logic, model);

        // TODO: Integrate BlockContentEditView
        blockContentPlaceholder.getChildren().addAll(blockContentDisplayView.getRoot());
    }
}

