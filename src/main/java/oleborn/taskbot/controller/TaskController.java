package oleborn.taskbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.TaskMapper;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;
    @Resource
    @Lazy
    private OutputsMethods outputsMethods;

    // POST-запрос для получения данных задачи
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
        taskService.createTask(TaskDto.builder()
                .ownerId(recipient)
                .creatorId(user_id)
                .title(title)
                .description(description)
                .dateSending(taskService.convertClientToServerTime(localDate, localTime, timeZone))
                .sent(false)
                .updated(false)
                .build());

        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                user_id,
                OutputMessages.TASK_CREATED.getTextMessage().formatted(title, taskService.convertClientToServerTime(localDate, localTime, timeZone).toString()),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Test", "asd")
                        .build()
                );
        return null;
    }
}
