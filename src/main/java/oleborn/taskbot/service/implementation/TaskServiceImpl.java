package oleborn.taskbot.service.implementation;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.TaskMapper;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;
import oleborn.taskbot.repository.TaskRepository;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.utils.TimeProcessingMethods;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private TimeProcessingMethods timeProcessingMethods;


    @Override
    public TaskDto createTask(TaskDto taskDto) {
        //переводим дату отправки в МСК
        taskDto.setDateSending(timeProcessingMethods.processLocalTimeToMSKTime(taskDto.getDateSending()));
        Task task = taskMapper.toEntity(taskDto);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto) {

        Task taskEntity = taskRepository.findById(taskDto.getId()).orElseThrow(() -> new RuntimeException("ПОКА ТЕСТ"));
        //переводим дату отправки в МСК
        taskDto.setDateSending(timeProcessingMethods.processLocalTimeToMSKTime(taskDto.getDateSending()));

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
    public List<Task> findAllTasks(Long id){
        return new ArrayList<>(taskRepository.findAllByOwnerId(id));
    }

    @Override
    public LocalDateTime createLocalDateTime(String localDate, String localTime) {
        return LocalDateTime.of(LocalDate.parse(
                localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalTime.parse(localTime, DateTimeFormatter.ofPattern("HH:mm"))
        );
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
                .dateSending(LocalDateTime.now())
                .sent(true)
                .updated(true)
                .build();

        taskRepository.save(taskMapper.toEntity(buildTaskDto));
    }
}
