package com.notably.view.blockcontent;

import java.util.Objects;

import com.notably.commons.compiler.Compiler;
import com.notably.commons.path.AbsolutePath;
import com.notably.model.Model;
import com.notably.model.block.BlockTreeItem;
import com.notably.view.ViewPart;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.web.WebView;

/**
 * Read-only and rendered view of the currently open block's content.
 */
public class BlockContentDisplayView extends ViewPart<WebView> {

    private static final String FXML = "blockcontent/BlockContentDisplayView.fxml";
    private final Model model;

    @FXML
    private WebView blockContentDisplay;

    public BlockContentDisplayView(Model model) {
        super(FXML);

        Objects.requireNonNull(model);

        this.model = model;

        setup();
    }

    /**
     * Sets up the view's initial data and wire up all required change listeners.
     */
    private void setup() {
        // Initialize content display
        reload(model);

        // Setup change listeners
        model.currentlyOpenPathProperty().addListener(observable -> reload(model));
        model.getBlockTree().getRootBlock().getTreeItem()
                .addEventHandler(TreeItem.treeNotificationEvent(), event -> reload(model));
    }

    /**
     * Reloads the {@link WebView}'s content with the currently open block's rendered HTML body.
     *
     * @param model App's model
     */
    private void reload(Model model) {
        Objects.requireNonNull(model);

        AbsolutePath currentlyOpenPath = model.getCurrentlyOpenPath();
        BlockTreeItem currentlyOpenBlock = model.getBlockTree().get(currentlyOpenPath);
        String markdownBody = currentlyOpenBlock.getBody().getText();

        String htmlBody = Compiler.compile(markdownBody);
        blockContentDisplay.getEngine().setUserStyleSheetLocation(getClass().getResource("/view/blockcontent/BlockContentDisplay.css").toExternalForm());
        blockContentDisplay.getEngine().loadContent(htmlBody);
    }
}

