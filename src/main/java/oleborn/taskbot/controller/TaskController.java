package oleborn.taskbot.controller;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.service.interfaces.TaskService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    // POST-запрос для получения данных задачи
    @PostMapping
    public ResponseEntity<Void> createTask(@RequestParam long user_id,
                                             @RequestParam String title,
                                             @RequestParam String description,
                                             @RequestParam String localDate,
                                             @RequestParam String localTime,
                                             @RequestParam String timeZone) {



        taskService.createTask(
                TaskDto.builder()
                        .ownerId(user_id)
                        .title(title)
                        .description(description)
                        .dateSending(taskService.convertClientToServerTime(localDate, localTime, timeZone))
                        .sent(false)
                        .updated(false)
                        .build()
        );
        return null;
    }
}
