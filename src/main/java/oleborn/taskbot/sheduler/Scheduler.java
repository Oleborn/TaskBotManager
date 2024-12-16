package oleborn.taskbot.sheduler;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.service.OutputService;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class Scheduler {

    @Resource
    private TaskService taskService;

    @Resource
    @Lazy
    private OutputService outputService;

    private int count = 0;

    //TODO можно пошаманить с частотой шедулера и временем проверки
    @Scheduled(fixedRate = 10000) // Запуск каждые 10 секунд
    public void checkTasks() {
        OffsetDateTime now = OffsetDateTime.now();

        OffsetDateTime thresholdTime = now.plusSeconds(10);

        // Получаем список задач, которые нужно выполнить
        List<TaskDto> tasksToSend = taskService.getTasksForSending(now);

        System.out.printf("Итерация №%d. Количество неотправленных тасков всего: %d \n", count, tasksToSend.size());

        tasksToSend.stream()
                .filter(taskDto ->
                        taskDto.getDateSending().isBefore(thresholdTime) ||
                                taskDto.getDateSending().isEqual(thresholdTime)// Проверка: дата отправки <= thresholdTime
                )
                .forEach(this::processTaskAsync); // Асинхронная обработка каждой задачи
        count++;
    }

    @Async
    public void processTaskAsync(TaskDto taskDto) {
        outputService.setOutputsBotMessageForTask(taskDto);
    }
}

