package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.updatehandler.handlers.interfaces.CommandHandler;
import oleborn.taskbot.updatehandler.handlers.interfaces.MessagesHandler;
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

    @Resource
    private CommandHandler commandHandler;

    @Value("${taskbot.provider}")
    private String provider;

    @Override
    public void messagesHandler(Update update) {
        if (update.getMessage().getText().startsWith("/")) {
            commandHandler.handleCommand(update);
            return;
        }
    }
}
