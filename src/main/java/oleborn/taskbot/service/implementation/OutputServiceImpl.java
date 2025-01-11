package oleborn.taskbot.service.implementation;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileToSendTaskDto;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;
import oleborn.taskbot.service.interfaces.OutputService;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.utils.*;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static oleborn.taskbot.utils.OutputMessages.FRIEND_MESSAGE_FOR_ADDED;
import static oleborn.taskbot.utils.OutputMessages.RETURN_PROFILE;

@Service
public class OutputServiceImpl implements OutputService {

    @Resource
    private OutputsMethods outputsMethods;

    @Resource
    private TaskService taskService;

    @Resource
    private TimeProcessingMethods timeProcessingMethods;

    @Resource
    private ProfileService profileService;

    @Value("${taskbot.provider}")
    private String provider;

    @Override
    public void setOutputsBotMessageForTask(TaskDto taskDto){
        taskService.outputInMessageTask(taskDto);
        outputsMethods.outputMessageWithCapture(
                taskDto.getOwnerId(),
                OutputMessages.RETURN_TASK.getTextMessage().formatted(taskDto.getTitle(), taskDto.getDescription()),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture()
        );

    }

    @Override
    public void returningProfile(ProfileDto profileDto, Update update, String outputMessages) {
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

    @Override
    public void returnAddedFriendMessage(Update update) {
        outputsMethods.outputMessageWithCapture(
                update,
                FRIEND_MESSAGE_FOR_ADDED.getTextMessage(),
                RandomPictures.RANDOM_BOT_START.getRandomNamePicture());
    }

    @Override
    public InlineKeyboardMarkup returnButtonInColumnSavedSenders(List<ProfileToSendTaskDto> listSenders) {
       return outputsMethods.createButtonInColumnSavedSenders(listSenders);
    }

    @Override
    public void returnMessageForSavedFriends(Update update, String outputMessage, InlineKeyboardMarkup inlineKeyboardMarkup) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                update,
                outputMessage,
                RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                inlineKeyboardMarkup
        );
    }

    @Override
    public void returnMessageTaskCreated(TaskDto taskDto, String formattedTime) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                taskDto.getCreatorId(),
                OutputMessages.TASK_CREATED.getTextMessage().formatted(taskDto.getTitle(), formattedTime),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Сказать \"спасибо\"!", "thanks") //TODO тут можно поменять
                        .build()
        );
    }

    @Override
    public void returnMessageTaskUpdated(TaskDto taskDto, String formattedTime) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                taskDto.getCreatorId(),
                OutputMessages.TASK_UPDATED.getTextMessage().formatted(taskDto.getTitle(), formattedTime),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Сказать \"спасибо\"!", "thanks") //TODO тут можно поменять
                        .build()
        );
    }

    @Override
    public InlineKeyboardMarkup returnButtonInColumnReceivedSavedTasks(List<Task> allTasks, String received) {
        return outputsMethods.createButtonInColumnSavedTasks(allTasks, received);
    }

    @Override
    public void returnMessageReceivedSavedTasks(Update update, String outputMessage, InlineKeyboardMarkup inlineKeyboardMarkup) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                update,
                outputMessage,
                RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                inlineKeyboardMarkup
        );
    }

    @Override
    public InlineKeyboardMarkup returnButtonInColumnCreatedSavedTasks(List<Task> allTasks, String created) {
        return outputsMethods.createButtonInColumnSavedTasks(allTasks, created);
    }

    @Override
    public void returnMessageCreatedSavedTasks(Update update, String outputMessage, InlineKeyboardMarkup inlineKeyboardMarkup) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                update,
                outputMessage,
                RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                inlineKeyboardMarkup
        );
    }

    @Override
    public void returnMessageTaskViewReceived(Update update, TaskDto taskDto, String[] name) {
        ProfileDto profileDtoCreator = profileService.getProfileByID(taskDto.getCreatorId());
        ProfileDto profileDtoOwner = profileService.getProfileByID(taskDto.getOwnerId());


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
                        profileService.getProfileByID(taskDto.getCreatorId()).getNickName(),
                        profileService.getProfileByID(taskDto.getOwnerId()).getNickName(),
                        taskDto.isSent() ? "Отправлено!" : "Не отправлено"
                ),
                RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Удалить напоминание", "deleteTask_" + name[1])
                        .build()
        );
    }

    @Override
    public void notificationRemovingTaskSetToYourself(Long id, TaskDto taskDto) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                id,
                OutputMessages.TASK_DELETED.getTextMessage().formatted(taskDto.getTitle()),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }

    @Override
    public void returnMessageToProfileFriendDeleted(Update update, ProfileToSendTaskDto profileDtoDeletingSender) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                update,
                OutputMessages.FRIEND_DELETED.getTextMessage().formatted(profileDtoDeletingSender.getNickName()),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }

    @Override
    public void returnMessageToFriendDeleted(ProfileDto profileDto, ProfileToSendTaskDto profileDtoDeletingSender) {
        outputsMethods.outputMessageWithCapture(
                profileDtoDeletingSender.getTelegramId(),
                OutputMessages.MESSAGES_FRIEND_DELETED.getTextMessage().formatted(profileDto.getNickName()),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture()
        );
    }

    @Override
    public void returnMessageTaskViewCreated(Update update, TaskDto taskDto, String[] name) {
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
                        profileService.getProfileByID(taskDto.getCreatorId()).getNickName(),
                        profileService.getProfileByID(taskDto.getOwnerId()).getNickName(),
                        taskDto.isSent() ? "Отправлено!" : "Не отправлено"
                ),
                RandomPictures.RANDOM_BOT_START.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addWebButton("Изменить напоминание", UrlWebForms.UPDATE_TASK.getUrl().formatted(provider, name[1]))
                        .nextRow()
                        .addButton("Удалить напоминание", "deleteTask_" + name[1])
                        .build()
        );
    }

    @Override
    public void notificationOwnerAboutTaskRemovalSetByInitiator(Long id, TaskDto taskDto) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                taskDto.getOwnerId(),
                OutputMessages.MSG_TO_OWNER_ABOUT_TASK_DEL_SET_INITIATOR.getTextMessage()
                        .formatted(
                                taskDto.getTitle(),
                                profileService.getProfileByID(id).getNickName()
                        ),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }

    @Override
    public void notificationToCreatorAboutDeletionByInitiatorOfTaskSetToInitiatorByCreator(Long id, TaskDto taskDto) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                taskDto.getCreatorId(),
                OutputMessages.MSG_TO_CREATOR_ABOUT_DEL_TASK_SET_TO_INITIATOR_BY_CREATOR.getTextMessage()
                        .formatted(
                                profileService.getProfileByID(id).getNickName(),
                                taskDto.getTitle()
                        ),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }

    @Override
    public void notificationToInitiatorAboutTaskRemovalInstalledOwner(Long id, TaskDto taskDto) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                id,
                OutputMessages.MSG_TO_INITIATOR_ABOUT_TASK_DEL_FOR_OWNER.getTextMessage()
                        .formatted(
                                taskDto.getTitle(),
                                profileService.getProfileByID(taskDto.getOwnerId()).getNickName()
                        ),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }

    @Override
    public void notificationToInitiatorAboutTaskDeletionSetByCreatorToInitiator(Long id, TaskDto taskDto) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                id,
                OutputMessages.MSG_INITIATOR_ABOUT_DEl_TASK_SET_TO_CREATOR_BY_INITIATOR.getTextMessage()
                        .formatted(
                                taskDto.getTitle(),
                                profileService.getProfileByID(taskDto.getCreatorId()).getNickName()
                        ),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }


    @Override
    public void notificationToInitiatorOfTaskCreatedForOwner(Long id, TaskDto taskDto) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                id,
                OutputMessages.MSG_TO_INITIATOR_ABOUT_TASK_CREATE_FOR_OWNER.getTextMessage()
                        .formatted(
                                taskDto.getTitle(),
                                profileService.getProfileByID(taskDto.getOwnerId()).getNickName()
                        ),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }

    @Override
    public void notificationToOwnerOfTaskCreatedForHimByInitiator(Long id, TaskDto taskDto) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                taskDto.getOwnerId(),
                OutputMessages.MSG_TO_OWNER_ABOUT_TASK_CREATE_SET_INITIATOR.getTextMessage()
                        .formatted(
                                profileService.getProfileByID(id).getNickName(),
                                taskDto.getTitle()
                        ),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }

    @Override
    public void notificationToInitiatorOfTaskUpdatedForOwner(Long id, TaskDto taskDto) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                id,
                OutputMessages.MSG_TO_INITIATOR_ABOUT_TASK_UPDATE_FOR_OWNER.getTextMessage()
                        .formatted(
                                taskDto.getTitle(),
                                profileService.getProfileByID(taskDto.getOwnerId()).getNickName()
                        ),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }

    @Override
    public void notificationToOwnerOfTaskUpdatedForHimByInitiator(Long id, TaskDto taskDto) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                taskDto.getOwnerId(),
                OutputMessages.MSG_TO_OWNER_ABOUT_TASK_UPDATE_SET_INITIATOR.getTextMessage()
                        .formatted(
                                profileService.getProfileByID(id).getNickName(),
                                taskDto.getTitle()
                        ),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Вернуться в начало", "/start")
                        .build()
        );
    }

    @Override
    public void noticeMessageHandler(long id, TaskDto taskDto, String typeNotice, String formattedTime){

        long idCreator = taskDto.getCreatorId();
        long idOwner = taskDto.getOwnerId();

        switch (typeNotice) {
            case "DELETED" -> {
                if (id == idCreator && id == idOwner) {
                    notificationRemovingTaskSetToYourself(id, taskDto);
                } else if (id == idCreator) {
                    notificationToInitiatorAboutTaskRemovalInstalledOwner(id, taskDto);
                    notificationOwnerAboutTaskRemovalSetByInitiator(id, taskDto);
                } else if (id == idOwner) {
                    notificationToCreatorAboutDeletionByInitiatorOfTaskSetToInitiatorByCreator(id, taskDto);
                    notificationToInitiatorAboutTaskDeletionSetByCreatorToInitiator(id, taskDto);
                }
            }
            case "UPDATED" -> {
                if (id == idCreator && id == idOwner) {
                    returnMessageTaskUpdated(taskDto, formattedTime);
                } else if (id == idCreator) {
                    notificationToInitiatorOfTaskUpdatedForOwner(id, taskDto);
                    notificationToOwnerOfTaskUpdatedForHimByInitiator(id, taskDto);
                }
            }
            case "CREATED" -> {
                if (id == idCreator && id == idOwner) {
                    returnMessageTaskCreated(taskDto, formattedTime);
                } else if (id == idCreator) {
                    notificationToInitiatorOfTaskCreatedForOwner(id, taskDto);
                    notificationToOwnerOfTaskCreatedForHimByInitiator(id, taskDto);
                }
            }
        }
    }
}
