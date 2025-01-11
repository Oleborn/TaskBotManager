package oleborn.taskbot.service.implementation;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.ProfileMapper;
import oleborn.taskbot.mapper.TaskMapper;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileToSendTaskDto;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;
import oleborn.taskbot.repository.TaskRepository;
import oleborn.taskbot.service.interfaces.OutputService;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.updatehandler.UpdateHandlerImpl;
import oleborn.taskbot.utils.*;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static oleborn.taskbot.utils.OutputMessages.*;
import static oleborn.taskbot.utils.OutputMessages.RETURN_CREATED_TASKS;

@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private TimeProcessingMethods timeProcessingMethods;

    @Resource
    @Lazy
    private OutputService outputService;

    @Resource
    private ProfileService profileService;

    @Resource
    private ProfileMapper profileMapper;



    @Override
    public TaskDto createTask(TaskDto taskDto) {
        //переводим дату отправки в МСК
        Task task = taskMapper.toEntity(taskDto);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto) {

        Task taskEntity = taskRepository.findById(taskDto.getId()).orElseThrow(() -> new RuntimeException("ПОКА ТЕСТ"));
        //переводим дату отправки в МСК
        taskMapper.updateTaskEntityFromDto(taskDto, taskEntity);
        return taskMapper.toDto(taskRepository.save(taskEntity));
    }

    @Override
    public void deleteTask(TaskDto taskDto) {
        taskRepository.deleteById(taskDto.getId());
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public TaskDto getTaskByID(Long id) {

        Optional<Task> taskEntity = taskRepository.findById(id);

        return taskEntity.map(taskMapper::toDto).orElseThrow(() -> new RuntimeException("ПОКА ТЕСТ"));
    }

    @Override
    public List<Task> findAllTasksByOwnerId(Long id){
        return new ArrayList<>(taskRepository.findAllByOwnerId(id));
    }

    @Override
    public List<Task> findAllTasksByCreatorId(Long id){
        return new ArrayList<>(taskRepository.findAllByCreatorId(id));
    }

    @Override
    public List<TaskDto> getTasksForSending(LocalDateTime time) {
        return taskRepository.findTasksToSend(time)
                .stream()
                .map(task -> taskMapper.toDto(task))
                .toList();
    }

    @Override
    public void outputInMessageTask(TaskDto taskDto) {
        TaskDto buildTaskDto = TaskDto.builder()
                .id(taskDto.getId())
                .ownerId(taskDto.getOwnerId())
                .creatorId(taskDto.getCreatorId())
                .title(taskDto.getTitle())
                .timeZoneOwner(taskDto.getTimeZoneOwner())
                .description(taskDto.getDescription())
                .dateCreated(taskDto.getDateCreated())
                .dateModified(LocalDateTime.now())
                .dateSending(
                      timeProcessingMethods.processLocalTimeToMSKTime(LocalDateTime.now())
                )
                .sent(true)
                .updated(taskDto.isUpdated())
                .build();

        taskRepository.save(taskMapper.toEntity(buildTaskDto));
    }

    @Override
    public void createTaskInController(TaskDto taskDto) {
        //Получаем профиль того кому устанавливается таска
        ProfileDto profileByIDOwner = profileService.getProfileByID(taskDto.getOwnerId());
        //Получаем профиль того кто установил таску
        ProfileDto profileByIDCreator = profileService.getProfileByID(taskDto.getCreatorId());

        createTask(TaskDto.builder()
                .ownerId(taskDto.getOwnerId())
                .creatorId(taskDto.getCreatorId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dateCreated(timeProcessingMethods.processLocalTimeToMSKTime(LocalDateTime.now())
                        //Тут КОСТЫЛЬ! приводим к поясному времени создателя простым сложением
                        .plusHours(Long.parseLong(profileByIDCreator.getTimeZone())))
                // тут надо приводить к МСК
                .dateSending(timeProcessingMethods.processingTimeToMSK(
                        taskDto.getDateSending(), Integer.parseInt(profileService.getProfileByID(taskDto.getCreatorId()).getTimeZone()))
                )
                .timeZoneOwner(profileByIDOwner.getTimeZone()) //тут часовой пояс того кому придет уведомление
                .sent(false)
                .updated(false)
                .build());

        // Форматирование даты в строку с нужным паттерном
        String formattedTime = taskDto.getDateSending().format(DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy 'года'"));

        outputService.noticeMessageHandler(taskDto.getCreatorId(), taskDto, "CREATED", formattedTime);
    }

    @Override
    public void updateTaskInController(TaskDto taskDto) {
        ProfileDto profileByID = profileService.getProfileByID(taskDto.getOwnerId());

        updateTask(TaskDto.builder()
                .id(taskDto.getId())
                .ownerId(taskDto.getOwnerId())
                .creatorId(taskDto.getCreatorId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dateSending(timeProcessingMethods.processingTimeToMSK(
                        taskDto.getDateSending(), Integer.parseInt(profileService.getProfileByID(taskDto.getCreatorId()).getTimeZone()))
                )
                .timeZoneOwner(profileByID.getTimeZone())
                .sent(false)
                .updated(true)
                .build());

        // Форматирование в строку с нужным паттерном
        String formattedTime = taskDto.getDateSending().format(DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy 'года'"));

        outputService.noticeMessageHandler(taskDto.getCreatorId(), taskDto, "UPDATED", formattedTime);
    }

    @Override
    public void processSaveTasksReceived(Update update) {
        //Для верного отображения ответа проверяем пустой ли список тасков
        List<Task> allTasks = findAllTasksByOwnerId(update.getCallbackQuery().getFrom().getId());
        boolean isEmpty = allTasks.isEmpty();

        String outputMessage = isEmpty ? RETURN_RECEIVED_TASKS_EMPTY.getTextMessage() : RETURN_RECEIVED_TASKS.getTextMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = isEmpty
                ? new InlineKeyboardBuilder().addButton("В начало", "/start").build()
                : outputService.returnButtonInColumnReceivedSavedTasks(allTasks, "received");

        outputService.returnMessageReceivedSavedTasks(update, outputMessage, inlineKeyboardMarkup);
    }

    @Override
    public void processSaveTasksCreated(Update update) {
        //Для верного отображения ответа проверяем пустой ли список тасков
        List<Task> allTasks = findAllTasksByCreatorId(update.getCallbackQuery().getFrom().getId());
        boolean isEmpty = allTasks.isEmpty();

        String outputMessage = isEmpty ? RETURN_CREATED_TASKS_EMPTY.getTextMessage() : RETURN_CREATED_TASKS.getTextMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = isEmpty
                ? new InlineKeyboardBuilder().addButton("В начало", "/start").build()
                : outputService.returnButtonInColumnCreatedSavedTasks(allTasks, "created");

        outputService.returnMessageCreatedSavedTasks(update, outputMessage, inlineKeyboardMarkup);
    }

    @Override
    public void deleteTaskMethod(Update update) {
        String[] name = update.getCallbackQuery().getData().split("_");
        TaskDto taskDto = getTaskByID(Long.valueOf(name[1]));
        deleteTask(taskDto);

        outputService.noticeMessageHandler(update.getCallbackQuery().getFrom().getId(), taskDto, "DELETED", "");
    }

    @Override
    public void deleteSendersMethod(Update update) {
        String[] name = update.getCallbackQuery().getData().split("_");
        ProfileToSendTaskDto profileDtoDeletingSender = profileMapper.toShortDto(profileService.getProfileByID(Long.valueOf(name[1])));
        ProfileDto profileDtoClient = profileService.getProfileByID(UpdateHandlerImpl.searchId(update));

        List<ProfileToSendTaskDto> listProfilesWhoCanSendMessages = profileDtoClient.getListProfilesWhoCanSendMessages();

        if (listProfilesWhoCanSendMessages.contains(profileDtoDeletingSender)){
            profileDtoClient.getListProfilesWhoCanSendMessages().remove(profileDtoDeletingSender);
        }

        profileService.updateProfile(profileDtoClient);

        outputService.returnMessageToProfileFriendDeleted(update, profileDtoDeletingSender);
        outputService.returnMessageToFriendDeleted(profileDtoClient, profileDtoDeletingSender);
    }

    @Override
    public void taskViewReceived(Update update) {
        String[] name = update.getCallbackQuery().getData().split("_");
        TaskDto taskDto = getTaskByID(Long.valueOf(name[1]));

        outputService.returnMessageTaskViewReceived(update, taskDto, name);
    }

    @Override
    public void taskViewCreated(Update update) {
        String[] name = update.getCallbackQuery().getData().split("_");
        TaskDto taskDto = getTaskByID(Long.valueOf(name[1]));

        outputService.returnMessageTaskViewCreated(update, taskDto, name);

    }
}
