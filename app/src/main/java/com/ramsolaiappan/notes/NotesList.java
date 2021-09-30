package com.ramsolaiappan.notes;

public class NotesList {
    String title;
    String description;

    public NotesList(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
