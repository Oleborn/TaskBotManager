package oleborn.taskbot.utils.outputMethods;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class InlineKeyboardBuilder {
    private List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    private List<InlineKeyboardButton> currentRow = null;


    // Добавить кнопку в текущую строку
    public InlineKeyboardBuilder addButton(String text, String callbackData) {
        if (currentRow == null || currentRow.size() >= 9) {
            currentRow = new ArrayList<>();
            keyboard.add(currentRow);
        }
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);
        currentRow.add(inlineKeyboardButton);
        return this;
    }
    // Добавить веб-кнопку в текущую строку
    public InlineKeyboardBuilder addWebButton(String text, String url) {
        if (currentRow == null || currentRow.size() >= 9) {
            currentRow = new ArrayList<>();
            keyboard.add(currentRow);
        }
        InlineKeyboardButton webAppButton = new InlineKeyboardButton();
        webAppButton.setText(text);
        webAppButton.setWebApp(new WebAppInfo(url));
        currentRow.add(webAppButton);
        return this;
    }

    // Закончить текущую строку и начать новую
    public InlineKeyboardBuilder nextRow() {
        currentRow = new ArrayList<>();
        keyboard.add(currentRow);
        return this;
    }

    // Собрать и вернуть готовую клавиатуру
    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }
}
