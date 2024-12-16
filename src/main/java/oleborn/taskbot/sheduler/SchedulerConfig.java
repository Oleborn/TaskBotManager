package oleborn.taskbot.sheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling // Включаем планировщик задач
@EnableAsync      // Включаем поддержку асинхронности
public class SchedulerConfig {
}
