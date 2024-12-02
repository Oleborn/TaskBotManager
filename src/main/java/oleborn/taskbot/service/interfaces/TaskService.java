package oleborn.taskbot.service.interfaces;

import oleborn.taskbot.model.dto.TaskDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public interface TaskService {

    TaskDto createTask(TaskDto taskDto);

    TaskDto updateTask(TaskDto taskDto);

    void deleteTask(TaskDto taskDto);

    TaskDto getTaskByID(Long id);

    LocalDateTime createLocalDateTime(String localDate, String localTime);

    OffsetDateTime convertClientToServerTime(String localDate, String localTime, String clientTimeZone);

}
