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
                    <b>Напоминание с заголовком - "%s" записано в базу данных.</b>
                    
                    <b>Я верну Вам напоминание в</b> <i>%s.</i> 
                    
                    <i>Не забудьте отключить бесшумный режим!</i> \uD83D\uDE09
                    """
    ),
    OUTPUT_MESSAGE_FOR_TASK(
            """
                    <b>Возвращаю Вам сохраненное напоминание</b>
                    
                    <b>Заголовок:</b> <i>%s</i>
                    
                    <b>Текст напоминания:</b> <i>%s</i>
                    """
    ),
    OUTPUT_MESSAGE_FOR_SAVES_TASKS("""
            Вот весь список ваших напоминаний.
            
            Чтобы отредактировать выберите нажатием напоминание.
            """);

    private final String textMessage;
}
