package oleborn.taskbot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class TaskBotApplication {

	@Value("${DB_USERNAME}")
	private static String dbUsername;

	@Value("${DB_PASSWORD}")
	private static String dbPassword;

	@Value("${BOT_TOKEN}")
	private static String botToken;

	public static void main(String[] args) {
		SpringApplication.run(TaskBotApplication.class, args);
		logValues();
	}

	@PostConstruct
	public static void logValues() {
		System.out.println("DB_USERNAME: " + dbUsername);
		System.out.println("DB_PASSWORD: " + dbPassword);
		System.out.println("BOT_TOKEN: " + botToken);
	}

}
