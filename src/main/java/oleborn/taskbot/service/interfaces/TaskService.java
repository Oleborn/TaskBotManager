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

    void deleteTask(Long id);

    TaskDto getTaskByID(Long id);

    List<Task> findAllTasks(Long id);

    LocalDateTime createLocalDateTime(String localDate, String localTime);

    List<TaskDto> getTasksForSending(LocalDateTime time);

    void outputInMessageTask(TaskDto taskDto);

}
