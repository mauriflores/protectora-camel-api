package com.uminas.protectora.model;

import java.util.List;

public class MetaEntry {
    private String id;
    private List<MetaChange> changes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MetaChange> getChanges() {
        return changes;
    }

    public void setChanges(List<MetaChange> changes) {
        this.changes = changes;
    }
}