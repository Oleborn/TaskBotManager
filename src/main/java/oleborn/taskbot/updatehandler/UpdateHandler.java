package oleborn.taskbot.updatehandler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    void handler(Update update);
}
