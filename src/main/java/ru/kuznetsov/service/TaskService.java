package ru.kuznetsov.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kuznetsov.controller.dao.TaskDAO;
import ru.kuznetsov.domain.Status;
import ru.kuznetsov.domain.Task;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getAll(int offset, int limit) {
        return taskDAO.getAll(offset, limit);
    }

    public int getAllCount() {
        return taskDAO.getAllCount();
    }

    @Transactional
    public Task edit(int id, String description, Status status) {
        Task taskById = Optional.ofNullable(taskDAO.getById(id))
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        taskById.setDescription(description);
        taskById.setStatus(status);
        taskDAO.saveOrUpdate(taskById);
        return taskById;
    }

    public Task create(String description, Status status) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);
        return task;
    }

    @Transactional
    public void delete(int id) {
        Task taskById = Optional.ofNullable(taskDAO.getById(id))
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        taskDAO.delete(taskById);
    }
}