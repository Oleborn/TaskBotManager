package oleborn.taskbot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.Collections;


@Component
public class Bot extends TelegramLongPollingBot {


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

        System.out.println(update);

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Откройте форму для создания задачи:");

            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            InlineKeyboardButton webAppButton = new InlineKeyboardButton();
            webAppButton.setText("Открыть форму");
            webAppButton.setWebApp(new WebAppInfo("https://1460-5-44-173-0.ngrok-free.app/task-form.html"));

            keyboardMarkup.setKeyboard(Collections.singletonList(Collections.singletonList(webAppButton)));
            message.setReplyMarkup(keyboardMarkup);

            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
