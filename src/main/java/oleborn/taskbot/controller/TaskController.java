package oleborn.taskbot.controller;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.TaskMapper;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.TimeProcessingMethods;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    @Resource
    private TimeProcessingMethods timeProcessingMethods;

    @PostMapping
    public ResponseEntity<Void> createTask(@RequestBody TaskDto taskDto) {
        taskService.createTaskInController(taskDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateTask(@RequestBody TaskDto taskDto) {
        taskService.updateTaskInController(taskDto);
        return ResponseEntity.ok().build();
    }

    //--------Запросы форм ---------//

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {

        TaskDto task = taskService.getTaskByID(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        task.setDateSending(timeProcessingMethods.processMSKTimeToLocalTimeForProfile(task));

        return ResponseEntity.ok(task);
    }


}
