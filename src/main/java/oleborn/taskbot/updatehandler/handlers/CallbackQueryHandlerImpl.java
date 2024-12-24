package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.updatehandler.handlers.interfaces.CallbackQueryHandler;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.updatehandler.handlers.interfaces.CommandHandler;
import oleborn.taskbot.utils.FormatDate;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.UrlWebForms;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
            case "profile" ->
                outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                        update,
                        OutputMessages.RETURN_PROFILE.getTextMessage()
                                .formatted(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getFrom().getId()),
                        RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                        new InlineKeyboardBuilder()
                                .addWebButton("Добавить личные данные", UrlWebForms.SELF_DATA.getUrl())
                                .nextRow()
                                .addButton("Список друзей", "list_friend")
                                .build()
                        // выходит список друзей, тех кто может направлять напоминания
                        // 2 кнопки - добавить и редактировать
                        // при добавлении строка ждет ник телеграмма, проверяет в БД на наличие и добавляет
                        // при редактировании выходит текст и кнопками список лиц
                        // при выборе одного из списка загружается инфа и кнопки удалить и назад
                );
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
                        taskDto.getDateCreated().atZone(ZoneId.systemDefault())
                                .withZoneSameInstant(ZoneId.of(taskDto.getTimeZoneOwner()))
                                .format(DateTimeFormatter.ofPattern(FormatDate.PRIME_FORMAT_DATE.getFormat())),
                        taskDto.getDateSending().atZoneSameInstant(ZoneId.of(taskDto.getTimeZoneOwner()))
                                .format(DateTimeFormatter.ofPattern(FormatDate.PRIME_FORMAT_DATE.getFormat())),
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
