package com.uminas.protectora.model;

import java.util.List;

public class MetaWebhook {
    private String object;
    private List<MetaEntry> entry;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<MetaEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<MetaEntry> entry) {
        this.entry = entry;
    }
}