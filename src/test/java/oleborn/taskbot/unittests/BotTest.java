package oleborn.taskbot.unittests;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.TaskMapper;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.model.entities.Task;

import oleborn.taskbot.repository.FriendRepository;
import oleborn.taskbot.repository.ProfileRepository;
import oleborn.taskbot.repository.TaskRepository;
import oleborn.taskbot.service.interfaces.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@EnableAutoConfiguration(
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
        }
)
public class BotTest {

    @Resource
    private TaskService taskService;

    @Resource
    private TaskMapper taskMapper;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private ProfileRepository profileRepository;
    @MockitoBean
    private FriendRepository friendRepository;


    @Test
    public void testReturnDate() {

        // Объединение LocalDate и LocalTime в LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.parse("2024-12-17", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalTime.parse("12:15", DateTimeFormatter.ofPattern("HH:mm"))
        );

        // Форматирование в строку с нужным паттерном
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy 'года'"));

        System.out.println(formattedTime);

    }

    @Test
    public void testMapper(){
        Task task = Task.builder()
                .id(1L)
                .ownerId(12345L)
                .creatorId(22222L)
                .title("test")
                .description("test1")
                .dateCreated(LocalDateTime.parse("2024-12-23T10:39:22.294928900"))
                .dateModified(LocalDateTime.parse("2024-12-23T10:39:22.294928900"))
                .dateSending(OffsetDateTime.parse("2024-12-23T10:39:22.294928900+07:00"))
                .sent(false)
                .updated(false)
                .build();

        TaskDto taskDto = TaskDto.builder()
                .id(1L)
                .ownerId(12345L)
                .creatorId(22222L)
                .title("test")
                .description("test1")
                .dateCreated(LocalDateTime.parse("2024-12-23T10:39:22.294928900"))
                .dateModified(LocalDateTime.parse("2024-12-23T10:39:22.294928900"))
                .dateSending(OffsetDateTime.parse("2024-12-23T10:39:22.294928900+07:00"))
                .sent(false)
                .updated(false)
                .build();

        TaskDto taskDto1 = taskMapper.toDto(task);
        Task task1 = taskMapper.toEntity(taskDto);

        assertEquals(task, task1);
        assertEquals(taskDto, taskDto1);

    }
}
