package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.ProfileMapper;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileToSendTaskDto;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.updatehandler.UpdateHandlerImpl;
import oleborn.taskbot.updatehandler.handlers.interfaces.CallbackQueryHandler;
import oleborn.taskbot.updatehandler.handlers.interfaces.CommandHandler;
import oleborn.taskbot.utils.*;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static oleborn.taskbot.utils.CommunicationStatus.INPUT_FRIEND;
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

        if (update.getCallbackQuery().getData().startsWith("savedTask")) {
            taskViewHandler(update);
            return;
        }
        if (update.getCallbackQuery().getData().startsWith("deleteTask")) {
            deleteTaskMethod(update);
            return;
        }

        if (update.getCallbackQuery().getData().startsWith("deleteSenders")) {
            deleteSendersMethod(update);
            return;
        }

        switch (update.getCallbackQuery().getData()) {
            case "saveTasks" -> {
                //Для верного отображения ответа проверяем пустой ли список тасков
                List<Task> allTasks = taskService.findAllTasksByOwnerId(update.getCallbackQuery().getFrom().getId());
                boolean isEmpty = allTasks.isEmpty();

                String outputMessage = isEmpty ? RETURN_NO_SAVES_TASKS.getTextMessage() : RETURN_SAVES_TASKS.getTextMessage();
                InlineKeyboardMarkup inlineKeyboardMarkup = isEmpty
                        ? new InlineKeyboardBuilder().addButton("В начало", "/start").build()
                        : outputsMethods.createButtonInColumnSavedTasks(allTasks);

                outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                        update,
                        outputMessage,
                        RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                        inlineKeyboardMarkup
                );
            }
            case "thanks" ->
                    outputsMethods.outputMessageWithCapture(update, "Да пожалуйста \uD83E\uDEE1", RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture());
            case "profile" -> {
                ProfileDto profileDto = profileService.getProfileByID(update.getCallbackQuery().getFrom().getId());

                String outputMessages;

                if (profileDto.getListProfilesWhoCanSendMessages().isEmpty()) {
                    outputMessages = RETURN_PROFILE_NO_FRIENDS.getTextMessage();
                } else {
                    outputMessages = RETURN_PROFILE_RECEIVE_MESSAGE.getTextMessage().formatted(
                            profileDto.getListProfilesWhoCanSendMessages().stream()
                                    .map(ProfileToSendTaskDto::getNickName) // Извлекаем nickName
                                    .filter(Objects::nonNull)    // Убираем возможные null значения
                                    .collect(Collectors.joining("\n")) // Соединяем через перенос строки
                    );
                }

                outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                        update,
                        RETURN_PROFILE.getTextMessage().formatted(
                                profileDto.getYourselfName() != null ? profileDto.getYourselfName() : "Пока не заполнено",
                                profileDto.getYourselfDateOfBirth() != null ? profileDto.getYourselfDateOfBirth() : "Пока не заполнено",
                                profileDto.getYourselfDescription() != null ? profileDto.getYourselfDescription() : "Пока не заполнено",
                                update.getCallbackQuery().getFrom().getUserName(),
                                update.getCallbackQuery().getFrom().getId(),
                                outputMessages
                        ),
                        RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                        new InlineKeyboardBuilder()
                                .addWebButton("Изменить личные данные", UrlWebForms.SELF_DATA_UPDATE.getUrl().formatted(provider))
                                .nextRow()
                                .addButton("Добавить друзей в список", "list_friend_add")
                                .nextRow()
                                .addButton("Удалить друзей из списка", "list_friend_delete")
                                .build()

                );
            }
            case "list_friend_add" -> {

                ProfileDto profileDto = profileService.getProfileByID(UpdateHandlerImpl.searchId(update));
                profileDto.setCommunicationStatus(INPUT_FRIEND);
                profileService.updateProfile(profileDto);

                outputsMethods.outputMessageWithCapture(
                        update,
                        FRIEND_MESSAGE_FOR_ADDED.getTextMessage(),
                        RandomPictures.RANDOM_BOT_START.getRandomNamePicture());
            }
            case "list_friend_delete" -> {
                ProfileDto profileDto = profileService.getProfileByID(UpdateHandlerImpl.searchId(update));

                List<ProfileToSendTaskDto> listSenders = profileDto.getListProfilesWhoCanSendMessages();

                boolean isEmpty = listSenders.isEmpty();

                String outputMessage = isEmpty ? RETURN_NO_SAVES_FRIENDS.getTextMessage() : RETURN_SAVES_FRIENDS.getTextMessage();
                InlineKeyboardMarkup inlineKeyboardMarkup = isEmpty
                        ? new InlineKeyboardBuilder().addButton("В начало", "/start").build()
                        : outputsMethods.createButtonInColumnSavedSenders(listSenders);

                outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                        update,
                        outputMessage,
                        RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                        inlineKeyboardMarkup
                );
            }
            // выходит список друзей, тех кто может направлять напоминания тебе
            // 2 кнопки - добавить и редактировать
            // при добавлении строка ждет ник телеграмма, проверяет в БД на наличие и добавляет
            // при редактировании выходит текст и кнопками список лиц
            // при выборе одного из списка загружается инфа и кнопки удалить и назад
        }
    }

    private void taskViewHandler(Update update) {
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
                        .addButton("Удалить напоминание", "deleteTask_" + name[1])
                        .build()
        );
    }
    /* ADDED
    У каждого профиля хранится часовой пояс + к МСК
    сервер имеет свой часовой пояс и вычисляет разницу с МСК у себя
    когда человек делает таску сервер должен:
        - записать в бд время отправки по МСК
        - при возврате получение из БД часового пояса и расчет в соответствии
        с часовым поясом получателя
     */


    private void deleteTaskMethod(Update update) {
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

    private void deleteSendersMethod(Update update) {
        String[] name = update.getCallbackQuery().getData().split("_");
        ProfileToSendTaskDto profileDtoDeletingSender = profileMapper.toShortDto(profileService.getProfileByID(Long.valueOf(name[1])));
        ProfileDto profileDtoClient = profileService.getProfileByID(UpdateHandlerImpl.searchId(update));

        List<ProfileToSendTaskDto> listProfilesWhoCanSendMessages = profileDtoClient.getListProfilesWhoCanSendMessages();

        if (listProfilesWhoCanSendMessages.contains(profileDtoDeletingSender)){
            profileDtoClient.getListProfilesWhoCanSendMessages().remove(profileDtoDeletingSender);
        }

        profileService.updateProfile(profileDtoClient);

        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                update,
                OutputMessages.FRIEND_DELETED.getTextMessage().formatted(profileDtoDeletingSender.getNickName()),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );

        outputsMethods.outputMessageWithCapture(
                profileDtoDeletingSender.getTelegramId(),
                OutputMessages.MESSAGES_FRIEND_DELETED.getTextMessage().formatted(profileDtoClient.getNickName()),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture()
        );
    }
}
