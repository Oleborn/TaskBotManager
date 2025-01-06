package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.entities.Profile;
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
import oleborn.taskbot.utils.outputMethods.TimeProcessingMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private TimeProcessingMethods timeProcessingMethods;

    @Value("${taskbot.provider}")
    private String provider;


    @Override
    public void handlerCallbackQuery(Update update) {

        if (update.getCallbackQuery().getData().startsWith("/")) {
            commandHandler.handleCommand(update);
            return;
        }

        if (update.getCallbackQuery().getData().startsWith("savedTask")) {
            tasksViewHandler(update);
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
            case "profile" -> {
                Optional<ProfileDto> profileDto = profileService.getProfileByID(update.getCallbackQuery().getFrom().getId());
                if (profileDto.isEmpty()) {
                    throw new RuntimeException(); //TODO тут кастомное исключение
                }

                String outputMessages;

                if (profileDto.get().getListProfilesWhoCanSendMessages().isEmpty()){
                    outputMessages = RETURN_PROFILE_NO_FRIENDS.getTextMessage();
                } else {
                    outputMessages = RETURN_PROFILE_FRIENDS.getTextMessage().formatted(
                            profileDto.get().getListProfilesWhoCanSendMessages().stream()
                                    .map((Profile t) -> profileDto.get().getNickName()) // Извлекаем nickName
                                    .filter(Objects::nonNull)    // Убираем возможные null значения
                                    .collect(Collectors.joining("\n,")) // Соединяем через перенос строки
                    );
                }

                outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                        update,
                        RETURN_PROFILE.getTextMessage().formatted(
                                profileDto.get().getYourselfName() != null ? profileDto.get().getYourselfName() : "Пока не заполнено",
                                profileDto.get().getYourselfDateOfBirth() != null ? profileDto.get().getYourselfDateOfBirth() : "Пока не заполнено",
                                profileDto.get().getYourselfDescription() != null ? profileDto.get().getYourselfDescription() : "Пока не заполнено",
                                update.getCallbackQuery().getFrom().getUserName(),
                                update.getCallbackQuery().getFrom().getId(),
                                outputMessages
                        ),
                        RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                        new InlineKeyboardBuilder()
                                .addWebButton("Изменить личные данные", UrlWebForms.SELF_DATA.getUrl().formatted(provider))
                                .nextRow()
                                .addButton("Добавить друзей в список","list_friend_add")
                                .nextRow()
                                .addButton("Изменить список друзей", "list_friend_update")
                                .build()

                );
            }
                // выходит список друзей, тех кто может направлять напоминания тебе
                // 2 кнопки - добавить и редактировать
                // при добавлении строка ждет ник телеграмма, проверяет в БД на наличие и добавляет
                // при редактировании выходит текст и кнопками список лиц
                // при выборе одного из списка загружается инфа и кнопки удалить и назад
        }
    }

    private void tasksViewHandler(Update update) {
        String[] name = update.getCallbackQuery().getData().split("_");
        TaskDto taskDto = taskService.getTaskByID(Long.valueOf(name[1]));
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                update,
                OutputMessages.RETURN_UPDATE_TASK.getTextMessage().formatted(
                        taskDto.getTitle(),
                        taskDto.getDescription(),
                        taskDto.getDateCreated()
                                .format(DateTimeFormatter.ofPattern(FormatDate.PRIME_FORMAT_DATE.getFormat())),
                        //перевести часовой пояс из string в int и прибавить к taskDto.getDateSending()
                        timeProcessingMethods.processMSKTimeToLocalTimeForProfile(taskDto)
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
    /*
    У каждого профиля хранится часовой пояс + к МСК
    сервер имеет свой часовой пояс и вычисляет разницу с МСК у себя
    когда человек делает таску сервер должен:
        - записать в бд время отправки по МСК
        - при возврате получение из БД часового пояса и расчет в соответствии
        с часовым поясом получателя
     */


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
