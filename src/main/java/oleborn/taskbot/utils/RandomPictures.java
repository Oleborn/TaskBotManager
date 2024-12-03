package oleborn.taskbot.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Getter
public enum RandomPictures {

    RANDOM_BOT_START(
            Arrays.asList("bot1", "bot2", "bot3", "bot4", "bot5", "bot6", "bot7", "bot8", "bot9")
    ),
    RANDOM_BOT_THUMBS_UP(
            Arrays.asList("BotThumbUp1", "BotThumbUp2", "BotThumbUp3", "BotThumbUp4", "BotThumbUp5", "BotThumbUp6", "BotThumbUp7", "BotThumbUp8")
    );

    private final List<String> namePicture;
    private static final Random RANDOM = new Random();

    /**
     * Возвращает случайное имя картинки из списка.
     */
    public String getRandomNamePicture() {
        return namePicture.get(RANDOM.nextInt(namePicture.size()));
    }

}
