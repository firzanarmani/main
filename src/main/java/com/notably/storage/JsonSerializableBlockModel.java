package com.notably.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.notably.commons.exceptions.IllegalValueException;
import com.notably.commons.path.AbsolutePath;
import com.notably.model.block.BlockModel;
import com.notably.model.block.BlockModelImpl;
import com.notably.model.block.BlockTree;
import com.notably.model.block.BlockTreeImpl;
import com.notably.model.block.BlockTreeItem;

/**
 * A BlockModel that is serializable to JSON format.
 */
@JsonRootName(value = "blockdata")
class JsonSerializableBlockModel {
    public static final String MISSING_LAST_OPENED_PATH_FIELD = "lastOpenedPath field is missing.";
    public static final String MESSAGE_DUPLICATE_BLOCK_CHILD = "Block's children list contains duplicate children.";

    private final List<JsonAdaptedBlockTreeItem> root = new ArrayList<>();

    private String lastOpenedPath;

    /**
     * Constructs a {@code JsonSerializableBlockModel} with the given model root's childblocks.
     */
    @JsonCreator
    public JsonSerializableBlockModel(@JsonProperty("root") List<JsonAdaptedBlockTreeItem> items,
        @JsonProperty("lastOpenedPath") String lastOpenedPath) {
        root.addAll(items);
        this.lastOpenedPath = lastOpenedPath;
    }

    /**
     * Converts a given {@code BlockModel} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableBlockModel}.
     */
    public JsonSerializableBlockModel(BlockModel source) {
        root.addAll(source.getBlockTree()
            .getRootBlock()
            .getBlockChildren()
            .stream()
            .map(JsonAdaptedBlockTreeItem::new)
            .collect(Collectors.toList()));
        lastOpenedPath = source.getCurrentlyOpenPath().getStringRepresentation();
    }

    /**
     * Converts this block model into the model's {@code BlockModel} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public BlockModel toModelType() throws IllegalValueException {
        BlockModel blockModel = new BlockModelImpl();

        // Generate the tree
        BlockTree blockTree = new BlockTreeImpl();
        List<BlockTreeItem> rootChildren = new ArrayList<>();
        for (JsonAdaptedBlockTreeItem jsonAdaptedBlockTreeItem : root) {
            BlockTreeItem blockTreeItem = jsonAdaptedBlockTreeItem.toModelType();
            if (rootChildren.contains(blockTreeItem)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_BLOCK_CHILD);
            }
            rootChildren.add(blockTreeItem);
        }
        if (!rootChildren.isEmpty()) {
            blockTree.getRootBlock()
                .setBlockChildren(rootChildren);
        }
        blockModel.setBlockTree(blockTree);

        // Get the last opened path
        if (lastOpenedPath == null) {
            throw new IllegalValueException(MISSING_LAST_OPENED_PATH_FIELD);
        }
        final AbsolutePath lastOpenedPath = AbsolutePath.fromString(this.lastOpenedPath);
        if (blockModel.hasPath(lastOpenedPath)) {
            blockModel.setCurrentlyOpenBlock(lastOpenedPath);
            return blockModel;
        }
        if (rootChildren.isEmpty()) {
            blockModel.setCurrentlyOpenBlock(AbsolutePath.fromString("/"));
            return blockModel;
        }
        BlockTreeItem firstChild = rootChildren.get(0);
        List<String> pathComponentsToFirstChild = new ArrayList<String>();
        pathComponentsToFirstChild.add(firstChild.getTitle().getText());
        blockModel.setCurrentlyOpenBlock(AbsolutePath.fromComponents(pathComponentsToFirstChild));
        return blockModel;
    }
}
