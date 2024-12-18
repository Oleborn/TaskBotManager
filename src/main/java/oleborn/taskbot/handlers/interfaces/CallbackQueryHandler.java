package oleborn.taskbot.handlers.interfaces;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackQueryHandler {

    void handlerCallbackQuery(Update update);

}
