package oleborn.taskbot.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.handlers.interfaces.MessagesHandler;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.UrlWebForms;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessagesHandlerImpl implements MessagesHandler {

    @Resource
    private OutputsMethods outputsMethods;
    @Resource
    private TaskService taskService;

    @Value("${taskbot.provider}")
    private String provider;

    @Override
    public void messagesHandler(Update update) {
        if (update.getMessage().getText().startsWith("/")) {
            commandsHandler(update);
        }

    }

    private void commandsHandler(Update update) {
        switch (update.getMessage().getText()) {
            case "/start" -> outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                    update,
                    OutputMessages.START_MESSAGE.getTextMessage(),
                    RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                    new InlineKeyboardBuilder()
                            .addWebButton("Добавить напоминание", UrlWebForms.CREATE_TASK.getUrl().formatted(provider))
                            .nextRow()
                            .addButton("Сохраненные напоминания", "saveTasks")
                            .nextRow()
                            .addWebButton("Профиль", "https://1a07-5-44-173-0.ngrok-free.app/task-form.html")
                            .build()
            );
        }
    }
}
