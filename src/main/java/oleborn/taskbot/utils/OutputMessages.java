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
    TASK_UPDATED(
            """
                    <b>Напоминание с заголовком - "%s" обновлено в базе данных.</b>
                    
                    <b>Я верну Вам напоминание в</b> <i>%s.</i> 
                    
                    <i>Не забудьте отключить бесшумный режим!</i> \uD83D\uDE09
                    """
    ),
    RETURN_TASK(
            """
                    <b>Возвращаю Вам сохраненное напоминание</b>
                    
                    <b>Заголовок:</b> <i>%s</i>
                    
                    <b>Текст напоминания:</b> <i>%s</i>
                    """
    ),
    RETURN_UPDATE_TASK(
            """
                    <b>Сохраненное напоминание</b>
                    
                    <b>Заголовок:</b> <i>%s</i>
                    
                    <b>Текст напоминания:</b> <i>%s</i>
                    
                    <b>Дата создания:</b> <i>%s</i>   
                    
                    <b>Дата отправки:</b> <i>%s</i>
                    
                    <b>Статус отправки:</b> <i>%s</i>
                    """
    ),
    //    <b>Кем создано:</b> <i>%s</i>
//
//    <b>Кому будет направлено:</b> <i>%s</i>
    RETURN_SAVES_TASKS("""
            Вот весь список ваших напоминаний.
            
            Чтобы отредактировать выберите нажатием напоминание.
            """),
    RETURN_NO_SAVES_TASKS("""
            У тебя нет напоминаний!
            
            Наверно это даже хорошо... 🤷‍♂️ 🤪
            """),

    TASK_DELETED("""
            Напоминание с заголовком: "%s", удалено.
            """),

    RETURN_PROFILE(
            """
                    <b>Вот что я о тебе знаю:</b>
                    
                    <b>Информация для других пользователей:</b>
                    <b>Имя:</b> <i>%s</i>
                    <b>Дата рождения:</b> <i>%s</i>
                    <b>О себе:</b> <i>%s</i>
                    
                    <b>Неизменяемые данные Telegram:</b>
                    <b>Телеграм-никнейм:</b> <i>%s</i>
                    <b>ID:</b> <i>%s</i>
                    
                    %s
                    
                    Так же ты можешь изменить данные, которые увидят другие пользователи бота.
                    """
    ),
    RETURN_PROFILE_RECEIVE_MESSAGE(
            """
                    <b>Вот список тех кто может установить тебе напоминания:</b>
                    <i>%s</i>
                    """
    ),
    RETURN_PROFILE_NO_FRIENDS(
            """
                    <b>Пока Вам никто не может устанавливать напоминания. </b>
                    
                    <b>Но вы всегда можете добавите кого-нибудь!</b>
                    """
    ),
    PROFILE_CREATED(
            """
                    <b>Привет!</b>
                    
                    <b>Мы с тобой еще не знакомы, поэтому в предложенной форме введи что-нибудь, а я запомню \uD83D\uDE09</b>
                    """
    ),
    PROFILE_UPDATED(
            """
                    <b>Ваш профиль обновлен. \uD83D\uDE09</b>
                    """
    ),
    FRIEND_MESSAGE_FOR_ADDED(
            """
                    <b>Введите в панель Telegram Nickname человека, который сможет устанавливать тебе напоминания.</b>
                    
                    <i>Если его нет в этом боте, ничего не получится, увы... 🤷‍♂️</i>
                    """
    ),
    FRIEND_NOT_FOUND(
            """
                    <b>Мне не известен человек с таким именем! 😞</b>
                    
                    <i>Скорее всего он просто ни разу не заходил ко мне. После того как я с ним познакомлюсь, попробуйте еще раз! 💪</i>
                    """
    ),
    FRIEND_ADDED(
            """
                    <b>Друг добавлен! 👍</b>
                    
                    <i>Теперь он может отправлять тебе сообщения 💪</i>
                    """
    );


    private final String textMessage;
}
