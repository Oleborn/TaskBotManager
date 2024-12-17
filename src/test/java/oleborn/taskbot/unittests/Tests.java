package oleborn.taskbot.unittests;

import jakarta.annotation.Resource;
import oleborn.taskbot.repository.TaskRepository;
import oleborn.taskbot.service.interfaces.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Tests {

    @Resource
    private TaskService taskService;


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
}
