package com.example.planify.model;

public class Task {

    private String id;
    private String title;
    private String description;
    private String deadline;

    public Task(String id,
                String title,
                String description,
                String deadline) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDeadline() {
        return deadline;
    }
}