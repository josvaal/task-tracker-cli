package com.josval.task_tracker_cli.models;

public class Task {
    private static int idCounter = 0;
    private int id;
    private String title;
    private String status;

    public Task(String title, String status) {
        this.id = ++idCounter;
        this.title = title;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Task.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
