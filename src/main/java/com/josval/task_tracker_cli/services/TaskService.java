package com.josval.task_tracker_cli.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josval.task_tracker_cli.models.Task;

public class TaskService {
    private static final String FILE_PATH = "json/data.json";

    public TaskService() {
        List<Task> tasks = readTasksFromFile();
        initializeIdCounter(tasks);
    }

    public List<Task> listTasks() {
        return readTasksFromFile();
    }

    private void initializeIdCounter(List<Task> tasks) {
        if (tasks != null && !tasks.isEmpty()) {
            Task.setIdCounter(tasks.stream()
                    .mapToInt(Task::getId)
                    .max()
                    .orElse(0));
        }
    }

    public List<Task> listTasksByStatus(String status) {
        List<Task> tasks = readTasksFromFile();
        if (tasks != null) {
            return tasks.stream()
                    .filter(task -> task.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public Task findByID(int id) {
        List<Task> tasks = readTasksFromFile();
        if (tasks != null) {
            for (Task task : tasks) {
                if (task.getId() == id) {
                    return task;
                }
            }
        }
        return null;
    }

    public void addTask(Task newTask) {
        List<Task> tasks = readTasksFromFile();
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(newTask);
        writeTasksToFile(tasks);
    }

    public void updateTask(int id, String newTitle, String newStatus) {
        List<Task> tasks = readTasksFromFile();
        if (tasks != null) {
            for (Task task : tasks) {
                if (task.getId() == id) {
                    task.setTitle(newTitle);
                    task.setStatus(newStatus);
                    break;
                }
            }
            writeTasksToFile(tasks);
        }
    }

    public void deleteTask(int id) {
        List<Task> tasks = readTasksFromFile();
        if (tasks != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getId() != id)
                    .collect(Collectors.toList());
            writeTasksToFile(tasks);
        }
    }

    private List<Task> readTasksFromFile() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type taskListType = new TypeToken<List<Task>>() {
            }.getType();
            return gson.fromJson(reader, taskListType);
        } catch (IOException e) {
            System.out.println("[!] Error reading tasks from file.");
            return null;
        }
    }

    private void writeTasksToFile(List<Task> tasks) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
