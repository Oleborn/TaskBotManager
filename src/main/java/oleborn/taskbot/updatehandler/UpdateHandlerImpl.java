package oleborn.taskbot.updatehandler;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.repository.TaskRepository;
import oleborn.taskbot.service.interfaces.ProfileService;
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
public class UpdateHandlerImpl implements UpdateHandler {

    @Value("${taskbot.provider}")
    private String provider;

    @Resource
    private ProfileService profileService;
    @Resource
    private OutputsMethods outputsMethods;
    @Resource
    private TaskService taskService;
    @Resource
    private TaskRepository taskRepository;

    @Override
    public void handler(Update update) {

       // ProfileDto profileDto = profileService.getProfileByID(update.getMessage().getFrom().getId());

        if (update.hasMessage() && update.getMessage().getText().startsWith("/")){
            handleCommand(update);
            return;
        }
        if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()){
                case "thanks" -> outputsMethods.outputMessageWithCapture(update, "Да пожалуйста \uD83E\uDEE1", RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture());
            }
        }

//        switch (profileDto.getCommunicationStatus()){
//            case DEFAULT -> as;
//
//            //Создание таски
//            case INPUT_TITLE -> as;
//            case INPUT_TEXT -> as;
//
//            //создание профиля
//            case INPUT_FRIEND -> as;
//            case INPUT_GMT -> as;
//            case INPUT_YOURSELF_NAME -> as;
//            case INPUT_DATE_BIRTHSDAY -> as;
//
//            //обновление таски
//            case UPDATE_TEXT -> as;
//            case UPDATE_TITLE -> as;
//            //обновление профиля
//            case UPDATE_GMT -> as;
//            case UPDATE_YOURSELF_NAME -> as;
//            case UPDATE_DATE_BIRTHSDAY -> as;
//
//            case BLOCKED -> as;
//        }
    }

    private void handleCommand(Update update) {

         switch (update.getMessage().getText()) {
             case "/start" -> outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                     update,
                     OutputMessages.START_MESSAGE.getTextMessage(),
                     RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                     new InlineKeyboardBuilder()
                             .addWebButton("Добавить напоминание", UrlWebForms.TASK.getUrl().formatted(provider))
                             .nextRow()
                             .addWebButton("Профиль", "https://1a07-5-44-173-0.ngrok-free.app/task-form.html")
                             .build()
                     );
         }
    }

    private void inputText(Update update) {
        if (update.hasMessage()) {
            TaskDto taskDto = TaskDto.builder()
                    .title(update.getMessage().getText())
                    .build();

        }
    }
}
