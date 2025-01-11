package oleborn.taskbot.service.interfaces;

import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileToSendTaskDto;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public interface OutputService {

    void setOutputsBotMessageForTask(TaskDto taskDto);

    void returningProfile(ProfileDto profileDto, Update update, String outputMessages);

    void returnAddedFriendMessage(Update update);

    InlineKeyboardMarkup returnButtonInColumnSavedSenders(List<ProfileToSendTaskDto> listSenders);

    void returnMessageForSavedFriends(Update update, String outputMessage, InlineKeyboardMarkup inlineKeyboardMarkup);

    void returnMessageTaskCreated(TaskDto taskDto, String formattedTime);

    void returnMessageTaskUpdated(TaskDto taskDto, String formattedTime);

    InlineKeyboardMarkup returnButtonInColumnReceivedSavedTasks(List<Task> allTasks, String received);

    void returnMessageReceivedSavedTasks(Update update, String outputMessage, InlineKeyboardMarkup inlineKeyboardMarkup);

    InlineKeyboardMarkup returnButtonInColumnCreatedSavedTasks(List<Task> allTasks, String received);

    void returnMessageCreatedSavedTasks(Update update, String outputMessage, InlineKeyboardMarkup inlineKeyboardMarkup);

    void returnMessageTaskViewReceived(Update update, TaskDto taskDto, String[] name);

    void notificationRemovingTaskSetToYourself(Long id, TaskDto taskDto);

    void returnMessageToProfileFriendDeleted(Update update, ProfileToSendTaskDto profileDtoDeletingSender);

    void returnMessageToFriendDeleted(ProfileDto profileDto, ProfileToSendTaskDto profileDtoDeletingSender);

    void returnMessageTaskViewCreated(Update update, TaskDto taskDto, String[] name);

    void notificationOwnerAboutTaskRemovalSetByInitiator(Long id, TaskDto taskDto);

    void notificationToCreatorAboutDeletionByInitiatorOfTaskSetToInitiatorByCreator(Long id, TaskDto taskDto);

    void notificationToInitiatorAboutTaskRemovalInstalledOwner(Long id, TaskDto taskDto);

    void notificationToInitiatorAboutTaskDeletionSetByCreatorToInitiator(Long id, TaskDto taskDto);

    void noticeMessageHandler(long id, TaskDto taskDto, String typeNotice, String formattedTime);

    void notificationToInitiatorOfTaskCreatedForOwner(Long id, TaskDto taskDto);

    void notificationToOwnerOfTaskCreatedForHimByInitiator(Long id, TaskDto taskDto);

    void notificationToInitiatorOfTaskUpdatedForOwner(Long id, TaskDto taskDto);

    void notificationToOwnerOfTaskUpdatedForHimByInitiator(Long id, TaskDto taskDto);
}
