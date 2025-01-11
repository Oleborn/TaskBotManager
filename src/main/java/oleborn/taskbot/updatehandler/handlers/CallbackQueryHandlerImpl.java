package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.ProfileMapper;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.updatehandler.handlers.interfaces.CallbackQueryHandler;
import oleborn.taskbot.updatehandler.handlers.interfaces.CommandHandler;
import oleborn.taskbot.utils.*;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static oleborn.taskbot.utils.OutputMessages.*;

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

    @Resource
    private ProfileMapper profileMapper;

    @Resource
    private TimeProcessingMethods timeProcessingMethods;

    @Value("${taskbot.provider}")
    private String provider;


    @Override
    public void handlerCallbackQuery(Update update) {

        if (update.getCallbackQuery().getData().startsWith("/")) {
            commandHandler.handleCommand(update);
            return;
        }

        if (update.getCallbackQuery().getData().startsWith("savedCreatedTask")) {
            taskService.taskViewCreated(update);
            return;
        }

        if (update.getCallbackQuery().getData().startsWith("savedReceivedTask")) {
            taskService.taskViewReceived(update);
            return;
        }

        if (update.getCallbackQuery().getData().startsWith("deleteTask")) {
            taskService.deleteTaskMethod(update);
            return;
        }

        if (update.getCallbackQuery().getData().startsWith("deleteSenders")) {
            taskService.deleteSendersMethod(update);
            return;
        }

        switch (update.getCallbackQuery().getData()) {
            case "saveTasks" -> {
                outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                        update,
                        RETURN_SAVES_TASKS.getTextMessage(),
                        RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                        new InlineKeyboardBuilder()
                                .addButton("Созданные тобой", "saveTasks_created")
                                .nextRow()
                                .addButton("Созданные для тебя", "saveTasks_received")
                                .build()

                );
            }
            case "saveTasks_received" -> taskService.processSaveTasksReceived(update);
            case "saveTasks_created" -> taskService.processSaveTasksCreated(update);
            case "thanks" ->
                    outputsMethods.outputMessageWithCapture(update, "Да пожалуйста \uD83E\uDEE1", RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture());
            case "profile" -> profileService.viewProfile(update);

            case "list_friend_add" -> profileService.addFriendSetStatus(update);
            case "list_friend_delete" -> profileService.deleteFriend(update);
            // выходит список друзей, тех кто может направлять напоминания тебе
            // 2 кнопки - добавить и редактировать
            // при добавлении строка ждет ник телеграмма, проверяет в БД на наличие и добавляет
            // при редактировании выходит текст и кнопками список лиц
            // при выборе одного из списка загружается инфа и кнопки удалить и назад
        }
    }
}
