package com.notably.model.suggestion;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The implementation class of SuggestionModel.
 */
public class SuggestionModelImpl implements SuggestionModel {
    private ObservableList<SuggestionItem> suggestions;
    private Property<Optional<String>> responseTextProperty;

    public SuggestionModelImpl() {
        suggestions = FXCollections.observableArrayList();
        responseTextProperty = new SimpleObjectProperty(Optional.empty());
    }

    @Override
    public ObservableList<SuggestionItem> getSuggestions() {
        return suggestions;
    }

    @Override
    public void setSuggestions(List<SuggestionItem> suggestions) {
        Objects.requireNonNull(suggestions);
        this.suggestions.setAll(suggestions);
    }

    @Override
    public Property<Optional<String>> responseTextProperty() {
        return responseTextProperty;
    }

    @Override
    public void setResponseText(String responseText) {
        Objects.requireNonNull(responseText);
        responseTextProperty.setValue(Optional.of(responseText));
    }

    @Override
    public void clearResponseText() {
        responseTextProperty.setValue(Optional.empty());
    }

    @Override
    public void clearSuggestions() {
        suggestions.clear();
    }
}
