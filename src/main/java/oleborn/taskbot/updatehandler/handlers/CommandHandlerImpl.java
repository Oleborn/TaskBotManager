package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.updatehandler.handlers.interfaces.CommandHandler;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.UrlWebForms;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CommandHandlerImpl implements CommandHandler {

    @Resource
    private OutputsMethods outputsMethods;

    @Resource
    private ProfileService profileService;

    @Value("${taskbot.provider}")
    private String provider;

    @Override
    public void handleCommand(Update update) {

        String command = null;
        long id = 0;

        if (update.hasMessage()) {
            command = update.getMessage().getText();
            id = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            command = update.getCallbackQuery().getData();
            id = update.getCallbackQuery().getMessage().getChatId();
        }

        switch (command) {
            case "/start" -> {
                outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
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
                profileService.createProfile(update);
            }
            default -> outputsMethods.outputMessage(id, "Неизвестная команда!");
        }

    }
}
