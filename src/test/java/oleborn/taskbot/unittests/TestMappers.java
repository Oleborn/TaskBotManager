package oleborn.taskbot.unittests;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.TaskMapper;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;
import oleborn.taskbot.repository.TaskRepository;
import oleborn.taskbot.service.interfaces.TaskService;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestMappers {

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private TaskService taskService;

    @MockitoBean
    private TaskRepository taskRepository;

    @Test
    public void testMapperTask() {

//        Task newTask = Task.builder()
//                .id(1L)
//                .title("TestTitle1")
//                .description("TestDescription1")
//                .dateCreated(LocalDateTime.now())
//                .sent(false)
//                .build();
//
//        TaskDto newTaskDto = TaskDto.builder()
//                .id(1L)
//                .title("TestTitle1")
//                .description("TestDescription1")
//                .dateCreated(LocalDateTime.now())
//                .sent(false)
//                .build();
//
//        TaskDto testingTaskDto = taskMapper.toDto(newTask);
//        Task testingTask = taskMapper.toEntity(testingTaskDto);
//
//        assertEquals(newTask.getId(), testingTask.getId());
//        assertEquals(newTask.getTitle(), testingTask.getTitle());
//
//        assertEquals(newTaskDto.getId(), testingTaskDto.getId());
//        assertEquals(newTaskDto.getDescription(), testingTaskDto.getDescription());
    }
}
