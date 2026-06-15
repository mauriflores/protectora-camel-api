package com.uminas.protectora.model;

public class MetaChange {
    private MetaValue value;
    private String field;

    public MetaValue getValue() {
        return value;
    }

    public void setValue(MetaValue value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}