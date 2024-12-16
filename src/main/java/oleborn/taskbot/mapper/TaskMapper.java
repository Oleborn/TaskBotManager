package oleborn.taskbot.mapper;

import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")  // Интеграция с Spring
public interface TaskMapper {

    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);

    void updateTaskEntityFromDto(TaskDto taskDto, @MappingTarget Task task);
}