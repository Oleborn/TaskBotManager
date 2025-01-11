package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.updatehandler.UpdateHandlerImpl;
import oleborn.taskbot.updatehandler.handlers.interfaces.CommandHandler;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.UrlWebForms;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static oleborn.taskbot.utils.OutputMessages.RETURN_PROFILE;

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

                ProfileDto profileDto = profileService.getProfileByID(UpdateHandlerImpl.searchId(update));

                if (profileDto == null) {
                    profileService.createProfile(update);
                    outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                            update,
                            OutputMessages.PROFILE_CREATED.getTextMessage(),
                            RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                            new InlineKeyboardBuilder()
                                    .addWebButton("Ввести свои данные", UrlWebForms.SELF_DATA.getUrl().formatted(provider))
                                    .build()

                    );
                } else {
                    outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                            update,
                            OutputMessages.START_MESSAGE.getTextMessage(),
                            RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                            new InlineKeyboardBuilder()
                                    .addWebButton("Добавить напоминание", UrlWebForms.CREATE_TASK.getUrl().formatted(provider))
                                    .nextRow()
                                    .addButton("Сохраненные напоминания", "saveTasks")
                                    .nextRow()
                                    .addButton("Профиль", "profile")
                                    .build()
                    );
                }
            }
            default -> outputsMethods.outputMessage(id, "Неизвестная команда!");
        }

    }
}
