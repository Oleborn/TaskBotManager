package oleborn.taskbot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class TaskBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskBotApplication.class, args);
	}
}
