package ru.kuznetsov.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.domain.Task;
import ru.kuznetsov.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String taskss(Model model,
                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {

        List<Task> tasks = taskService.getAll((page - 1) * limit, limit);
        model.addAttribute("tasks", tasks);
        model.addAttribute("current_page", page);
        int allCount = taskService.getAllCount();
        System.out.println(allCount);
        int totalPages = (int) Math.ceil(1.0 * allCount / limit);
        if (totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        return "tasks";
    }

    @PostMapping("/{id}")
    public String edit(Model model,
                       @PathVariable Integer id,
                       @RequestBody TaskInfo info) {
        if (isNull(id) || (id <= 0)) {
            throw new RuntimeException("Invalid id");
        }
        Task task = taskService.edit(id, info.getDescription(), info.getStatus());
        return taskss(model, 1, 10);
    }

    @PostMapping
    public String add(
            Model model,
            @RequestBody TaskInfo info
    ) {
        Task task = taskService.create(info.getDescription(), info.getStatus());
        return taskss(model, 1, 10);
    }

    @DeleteMapping("/{id}")
    public String delete(
            Model model,
            @PathVariable("id") Integer id //??
    ) {
        if (isNull(id) || id <= 0) {
            throw new RuntimeException("Invalid id");
        }
        taskService.delete(id);
        return taskss(model, 1, 10);
    }
}