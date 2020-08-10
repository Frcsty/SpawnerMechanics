package com.github.frcsty.spawnermechanics.api.attribute;

import java.util.HashMap;
import java.util.Map;

public final class Attribute {

    private final Map<String, Object> attributes = new HashMap<>();

    public void setAttribute(final String key, final Object value) {
        this.attributes.put(key, value);
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public Object getAttribute(final String key) {
        return attributes.get(key);
    }
}
