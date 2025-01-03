package oleborn.taskbot.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FormatDate {

    PRIME_FORMAT_DATE("HH:mm, dd.MM.yyyy 'года'");

    private final String format;

}
