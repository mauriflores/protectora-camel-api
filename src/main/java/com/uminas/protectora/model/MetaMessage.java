package com.uminas.protectora.model;

public class MetaMessage {
    private String from;
    private MetaText text;
    private String type;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public MetaText getText() {
        return text;
    }

    public void setText(MetaText text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}