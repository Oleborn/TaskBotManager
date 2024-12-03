package oleborn.taskbot.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OutputMessages {

    START_MESSAGE(
            """
                    <b>Привет!</b>
                    
                    <i>Я бот, который записывает напоминания и присылает их Вам в указанное время!</i>
                    
                    <i>Я еще многого не умею, но учусь и поэтому не судите строго! \uD83D\uDE09</i>
                    """
    ),
    TASK_CREATED(
            """
                    Напоминание с заголовком - "%s" записано в базу данных.
                    
                    Я верну Вам напоминание - %s!
                    """
    );

    private final String textMessage;
}
