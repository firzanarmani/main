package com.notably.model.suggestion;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation class for Suggestion Item.
 */
public class SuggestionItemImpl implements SuggestionItem {
    private Runnable action;
    private HashMap<String, String> hmap;

    public SuggestionItemImpl(String displayText, Runnable action) {
        Objects.requireNonNull(displayText);
        Objects.requireNonNull(action);

        this.action = action;

        hmap = new HashMap<>();
        hmap.put("displayText", displayText);
    }

    public SuggestionItemImpl(String displayText, int frequency, Runnable action) {
        Objects.requireNonNull(displayText);
        Objects.requireNonNull(frequency);
        Objects.requireNonNull(action);

        this.action = action;

        hmap = new HashMap<>();
        hmap.put("displayText", displayText);
        hmap.put("frequency", Integer.toString(frequency));
    }

    @Override
    public Optional<String> getProperty(String key) {
        return Optional.ofNullable(hmap.get(key));
    }

    @Override
    public Runnable getAction() {
        return action;
    }
}
