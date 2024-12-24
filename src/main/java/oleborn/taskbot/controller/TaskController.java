package oleborn.taskbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.TaskMapper;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.*;
import java.time.format.DateTimeFormatter;

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
    @Lazy
    private OutputsMethods outputsMethods;

    @PostMapping
    public ResponseEntity<Void> createTask(@RequestParam long user_id,
                                           @RequestParam long recipient,
                                           @RequestParam String title,
                                           @RequestParam String description,
                                           @RequestParam String localDate,
                                           @RequestParam String localTime,
                                           @RequestParam String timeZone
    )
    {

        OffsetDateTime resultTime = taskService.convertClientToServerTime(localDate, localTime, timeZone);

        taskService.createTask(TaskDto.builder()
                .ownerId(recipient)
                .creatorId(user_id)
                .title(title)
                .description(description)
                .dateSending(resultTime)
                //profileService.getProfileByID(recipient).getTimeZoneId();
                .timeZoneOwner(timeZone) //TODO тут должен быть часовой пояс того кому придет уведомление
                .sent(false)
                .updated(false)
                .build());

        // Объединение LocalDate и LocalTime в LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalTime.parse(localTime, DateTimeFormatter.ofPattern("HH:mm"))
        );

        // Форматирование в строку с нужным паттерном
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy 'года'"));

        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                user_id,
                OutputMessages.TASK_CREATED.getTextMessage().formatted(title, formattedTime),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Сказать \"спасибо\"!", "thanks") //TODO тут можно поменять
                        .build()
                );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateTask(@RequestParam long user_id,
                                           @RequestParam long task_id,
                                           @RequestParam long recipient,
                                           @RequestParam String title,
                                           @RequestParam String description,
                                           @RequestParam String localDate,
                                           @RequestParam String localTime,
                                           @RequestParam String timeZone
    ){

        OffsetDateTime resultTime = taskService.convertClientToServerTime(localDate, localTime, timeZone);

        taskService.updateTask(TaskDto.builder()
                .id(task_id)
                .ownerId(recipient)
                .creatorId(user_id)
                .title(title)
                .description(description)
                .dateSending(resultTime)
                //profileService.getProfileByID(recipient).getTimeZoneId();
                .timeZoneOwner(timeZone) //TODO тут должен быть часовой пояс того кому придет уведомление
                .sent(false)
                .updated(true)
                .build());

        // Объединение LocalDate и LocalTime в LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalTime.parse(localTime, DateTimeFormatter.ofPattern("HH:mm"))
        );

        // Форматирование в строку с нужным паттерном
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy 'года'"));

        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                user_id,
                OutputMessages.TASK_UPDATED.getTextMessage().formatted(title, formattedTime),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Сказать \"спасибо\"!", "thanks") //TODO тут можно поменять
                        .build()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        TaskDto task = taskService.getTaskByID(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
