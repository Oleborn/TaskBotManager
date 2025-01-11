package oleborn.taskbot.service.interfaces;

import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    TaskDto createTask(TaskDto taskDto);

    TaskDto updateTask(TaskDto taskDto);

    void deleteTask(TaskDto taskDto);

    void deleteTask(Long id);

    TaskDto getTaskByID(Long id);

    List<Task> findAllTasksByOwnerId(Long id);

    List<Task> findAllTasksByCreatorId(Long id);

    List<TaskDto> getTasksForSending(LocalDateTime time);

    void outputInMessageTask(TaskDto taskDto);

    void createTaskInController(TaskDto taskDto);

    void updateTaskInController(TaskDto taskDto);

    void processSaveTasksReceived(Update update);

    void processSaveTasksCreated(Update update);

    void taskViewReceived(Update update);

    void deleteTaskMethod(Update update);

    void deleteSendersMethod(Update update);

    void taskViewCreated(Update update);

}
