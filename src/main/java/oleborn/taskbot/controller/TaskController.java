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
    private TaskMapper taskMapper;
    @Resource
    private ProfileService profileService;

    @Resource
    private TimeProcessingMethods timeProcessingMethods;

    @Resource
    @Lazy
    private OutputsMethods outputsMethods;

    @PostMapping
    public ResponseEntity<Void> createTask(@RequestBody TaskDto taskDto) {

        ProfileDto profileByID = profileService.getProfileByID(taskDto.getOwnerId());

        taskService.createTask(TaskDto.builder()
                .ownerId(taskDto.getOwnerId())
                .creatorId(taskDto.getCreatorId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dateSending(taskDto.getDateSending())
                .timeZoneOwner(profileByID.getTimeZone()) //TODO тут должен быть часовой пояс того кому придет уведомление
                .sent(false)
                .updated(false)
                .build());

        // Форматирование в строку с нужным паттерном
        String formattedTime = taskDto.getDateSending().format(DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy 'года'"));

        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                taskDto.getCreatorId(),
                OutputMessages.TASK_CREATED.getTextMessage().formatted(taskDto.getTitle(), formattedTime),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Сказать \"спасибо\"!", "thanks") //TODO тут можно поменять
                        .build()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateTask(@RequestBody TaskDto taskDto) {


        ProfileDto profileByID = profileService.getProfileByID(taskDto.getOwnerId());

        taskService.updateTask(TaskDto.builder()
                .id(taskDto.getId())
                .ownerId(taskDto.getOwnerId())
                .creatorId(taskDto.getCreatorId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dateSending(taskDto.getDateSending())
                .timeZoneOwner(profileByID.getTimeZone())
                .sent(false)
                .updated(true)
                .build());

        // Форматирование в строку с нужным паттерном
        String formattedTime = taskDto.getDateSending().format(DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy 'года'"));

        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                taskDto.getCreatorId(),
                OutputMessages.TASK_UPDATED.getTextMessage().formatted(taskDto.getTitle(), formattedTime),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Сказать \"спасибо\"!", "thanks") //TODO тут можно поменять
                        .build()
        );
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
