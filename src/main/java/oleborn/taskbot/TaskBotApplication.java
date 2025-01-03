package oleborn.taskbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class TaskBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskBotApplication.class, args);

		/*
		TODO:
		Убрать получение часового пояса с создания таски
		Заменить расчет даты в сервисе с OffsetDateTime на простое сложение
		Возможно заменить OffsetDateTime на LocalDateTime
		Продумать получение часового пояса, потому что получить его с устройства может быть проблематично,
		логичнее всего при первом запуске мини регистрация, в которой указать и часовой пояс в том числе
			что сделать:
				- сделать форму, все поля кроме часового пояса необязательные
				- добавить в CommandHandlerImpl проверку на наличие профиля, при отсутствии загрузка формы
				- переделать контроллер для получение всех данных

		Возможно добавить в репозитори метод на получение конкретного часового пояса
		Переделать TaskController убрать логику

		попробовать все ответы сделать исчезающими
		вынести в отдельный класс все методы ответов
		 */
	}
}
