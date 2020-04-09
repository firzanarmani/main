package com.notably.view;

import java.util.Optional;

import com.notably.model.suggestion.SuggestionItem;

import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


/**
 * A View-Controller for the Suggestions List that appears in the View.
 */
public class SuggestionsWindowView extends ViewPart<Region> {

    private static final String FXML = "SuggestionsWindowView.fxml";
    private static final int LIST_CELL_HEIGHT = 36;

    @FXML
    private VBox suggestionsBox;

    @FXML
    private Label responseText;

    @FXML
    private ListView<SuggestionItem> suggestionsListPanel;

    public SuggestionsWindowView(ObservableList<SuggestionItem> suggestionsList,
                                 Property<Optional<String>> responseText) {
        super(FXML);
        autoUpdateSuggestionsDisplay(suggestionsList, responseText);
        initializeSuggestionsList(suggestionsList);
    }

    /**
     * Sets listeners to update the visibility of the {@code SuggestionsWindow}, and its components.
     * @param suggestionsList The current list of suggestions from the model.
     * @param responseTextProperty The info text to be displayed above the suggestions list, if any.
     */
    private void autoUpdateSuggestionsDisplay(ObservableList<SuggestionItem> suggestionsList,
                                              Property<Optional<String>> responseTextProperty) {
        responseTextProperty.addListener((observable, oldValue, newValue) -> {
            Optional<String> response = responseTextProperty.getValue();
            if (response.isPresent()) {
                responseText.setText(response.get());
                setSuggestionsTextRenderingStatus(true);
            } else {
                setSuggestionsTextRenderingStatus(false);
            }
        });

        suggestionsList.addListener((Observable observable) -> {
            suggestionsListPanel.setPrefHeight(computePrefListHeight(suggestionsList.size()));
            if (suggestionsList.size() == 0) {
                setSuggestionsListRenderingStatus(false);
            } else {
                setSuggestionsListRenderingStatus(true);
            }
        });
    }

    private void initializeSuggestionsList(ObservableList<SuggestionItem> suggestionsList) {
        suggestionsListPanel.setItems(suggestionsList);
        suggestionsListPanel.setCellFactory(listView -> new SuggestionsListViewCell());
    }

    private void setSuggestionsListRenderingStatus(boolean bool) {
        suggestionsListPanel.setManaged(bool);
        suggestionsListPanel.setVisible(bool);
    }

    private void setSuggestionsTextRenderingStatus(boolean bool) {
        responseText.setManaged(bool);
        responseText.setVisible(bool);
    }

    private int computePrefListHeight(int listSize) {
        return listSize * LIST_CELL_HEIGHT;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code SuggestionItem}.
     */
    class SuggestionsListViewCell extends ListCell<SuggestionItem> {
        @Override
        protected void updateItem(SuggestionItem suggestionItem, boolean empty) {
            super.updateItem(suggestionItem, empty);
            setGraphic(null);

            if (empty || suggestionItem == null) {
                setText("");
            } else {
                Optional<String> displayProperty = getItem().getProperty("displayText");
                String displayString = displayProperty.orElse("");
                setText(displayString);
            }
        }
    }
}
