package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.updatehandler.handlers.interfaces.CallbackQueryHandler;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.updatehandler.handlers.interfaces.CommandHandler;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.UrlWebForms;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;

@Component
public class CallbackQueryHandlerImpl implements CallbackQueryHandler {

    @Resource
    private OutputsMethods outputsMethods;
    @Resource
    private TaskService taskService;

    @Resource
    private CommandHandler commandHandler;

    @Resource
    private ProfileService profileService;

    @Value("${taskbot.provider}")
    private String provider;


    @Override
    public void handlerCallbackQuery(Update update) {

        if (update.getCallbackQuery().getData().startsWith("/")) {
            commandHandler.handleCommand(update);
            return;
        }

        if (update.getCallbackQuery().getData().startsWith("savedTask")) {
            tasksHandler(update);
            return;
        }
        if (update.getCallbackQuery().getData().startsWith("deleteTask")) {
            deleteMethod(update);
            return;
        }

        switch (update.getCallbackQuery().getData()) {
            case "saveTasks" -> outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                    update,
                    OutputMessages.RETURN_SAVES_TASKS.getTextMessage(),
                    RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                    outputsMethods.createButtonInColumnSavedTasks(
                            taskService.findAllTasks(update.getCallbackQuery().getFrom().getId())
                    )
            );
            case "thanks" ->
                    outputsMethods.outputMessageWithCapture(update, "Да пожалуйста \uD83E\uDEE1", RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture());
        }
    }

    private void tasksHandler(Update update) {
        String[] name = update.getCallbackQuery().getData().split("_");
        TaskDto taskDto = taskService.getTaskByID(Long.valueOf(name[1]));
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                update,
                OutputMessages.RETURN_UPDATE_TASK.getTextMessage().formatted(
                        taskDto.getTitle(),
                        taskDto.getDescription(),
                        taskDto.getDateCreated().format(DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy 'года'")),
                        taskDto.getDateSending().format(DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy 'года'")),
                        taskDto.isSent() ? "Отправлено!" : "Не отправлено"
//                        profileService.getProfileByID(taskDto.getCreatorId()).getNickName(), //TODO доделать добавление профилей
//                        profileService.getProfileByID(taskDto.getOwnerId()).getNickName()
                ),
                RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addWebButton("Изменить напоминание", UrlWebForms.UPDATE_TASK.getUrl().formatted(provider, name[1]))
                        .nextRow()
                        .addButton("Удалить напоминание", "deleteTask_"+name[1])
                        .build()
        );
        System.out.println(taskDto.getDateSending());
    }
    private void deleteMethod(Update update) {
        String[] name = update.getCallbackQuery().getData().split("_");
        TaskDto taskDto = taskService.getTaskByID(Long.valueOf(name[1]));
        taskService.deleteTask(taskDto);
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                update,
                OutputMessages.TASK_DELETED.getTextMessage().formatted(taskDto.getTitle()),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }
}