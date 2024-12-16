package oleborn.taskbot.bot;

import jakarta.annotation.Resource;
import oleborn.taskbot.updatehandler.UpdateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class Bot extends TelegramLongPollingBot {

    @Lazy
    @Resource
    private UpdateHandler updateHandler;

    @Value("${taskbot.name-bot}")
    private String nameBot;

    @Value("${taskbot.bot-token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return nameBot;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


    @Override
    public void onUpdateReceived(Update update) {

        updateHandler.handler(update);

    }
}
