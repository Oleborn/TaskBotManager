package oleborn.taskbot.mapper;

import oleborn.taskbot.model.dto.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.scheduling.config.Task;

@Mapper(componentModel = "spring")  // Интеграция с Spring
public interface TaskMapper {
    
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);
}