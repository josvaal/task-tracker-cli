package com.josval.task_tracker_cli;

import java.util.List;
import java.util.concurrent.Callable;
import com.josval.task_tracker_cli.models.Task;
import com.josval.task_tracker_cli.services.TaskService;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "task-cli", mixinStandardHelpOptions = true, version = "task-cli 1.0", description = "Tracks tasks via the command line.")
public class App implements Callable<Integer> {

    TaskService taskService = new TaskService();

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Use '-h' or '--help' for list commands.");
        return 0;
    }

    @Command(name = "list", description = "Lists all tasks or tasks by status")
    void list(
            @Parameters(index = "0", arity = "0..1", description = "Filter tasks by status") String status) {
        List<Task> tasks = (status == null)
                ? taskService.listTasks()
                : taskService.listTasksByStatus(status);
        if (tasks.isEmpty()) {
            System.out.println("[!] No tasks available");
            return;
        }

        System.out.println(String.format("%-5s %-20s %-15s", "ID", "Title", "Status"));

        tasks.forEach(task -> System.out
                .println(String.format("%-5d %-20s %-15s", task.getId(), task.getTitle(), task.getStatus())));
    }

    @Command(name = "add", description = "Adds a new task")
    void add(
            @Parameters(index = "0", description = "The title of the task") String title) {
        Task task = new Task(title, "todo");
        taskService.addTask(task);
        System.out.println(String.format("%-5s %-20s %-15s", "ID", "Title", "Status"));
        System.out.println(String.format("%-5s %-20s %-15s", task.getId(), task.getTitle(), task.getStatus()));
    }

    @Command(name = "update", description = "Update a task by ID")
    void updateById(
            @Parameters(index = "0", description = "The ID of the task") int id,
            @Parameters(index = "1", description = "The new title of the task") String title) {
        Task task = taskService.findByID(id);
        if (task == null) {
            System.out.println("[!] Task with ID " + id + " does not exist.");
            return;
        }
        taskService.updateTask(id, title, task.getStatus());
        System.out.println("[*] Task updated successfully!");
    }

    @Command(name = "delete", description = "Delete a task by ID")
    void deletByID(
            @Parameters(index = "0", description = "The ID of the task") int id) {
        Task task = taskService.findByID(id);
        if (task == null) {
            System.out.println("[!] Task with ID " + id + " does not exist.");
            return;
        }
        taskService.deleteTask(id);
        System.out.println("[*] Task deleted successfully!");
    }

    @Command(name = "mark-in-progress", description = "Mark a task in progress")
    void markInProgress(
            @Parameters(index = "0", description = "The ID of the task") int id) {
        Task task = taskService.findByID(id);
        if (task == null) {
            System.out.println("[!] Task with ID " + id + " does not exist.");
            return;
        }
        taskService.updateTask(id, task.getTitle(), "in-progress");
        System.out.println("[*] Task changed to in-progress successfully!");
    }

    @Command(name = "mark-done", description = "Mark a task as done")
    void markDone(
            @Parameters(index = "0", description = "The ID of the task") int id) {
        Task task = taskService.findByID(id);
        if (task == null) {
            System.out.println("[!] Task with ID " + id + " does not exist.");
            return;
        }
        taskService.updateTask(id, task.getTitle(), "done");
        System.out.println("[*] Task changed to done successfully!");
    }
}
