package com.notably.model.suggestion;

/**
 * Implementation class for Suggestion Item.
 */
public class SuggestionItemImpl implements SuggestionItem {
    private String displayText;
    private Runnable action;

    public SuggestionItemImpl(String displayText, Runnable action) {
        this.displayText = displayText;
        this.action = action;
    }

    @Override
    public String getDisplayText() {
        return displayText;
    }

    @Override
    public Runnable getAction() {
        return action;
    }
}
