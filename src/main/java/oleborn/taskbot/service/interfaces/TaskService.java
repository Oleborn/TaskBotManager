package oleborn.taskbot.service.interfaces;

import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface TaskService {

    TaskDto createTask(TaskDto taskDto);

    TaskDto updateTask(TaskDto taskDto);

    void deleteTask(TaskDto taskDto);

    TaskDto getTaskByID(Long id);

    List<Task> findAllTasks(Long id);

    LocalDateTime createLocalDateTime(String localDate, String localTime);

    List<TaskDto> getTasksForSending(OffsetDateTime time);

    OffsetDateTime convertClientToServerTime(String localDate, String localTime, String clientTimeZone);

    void outputInMessageTask(TaskDto taskDto);

}
