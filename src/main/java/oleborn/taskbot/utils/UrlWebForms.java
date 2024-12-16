package oleborn.taskbot.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@AllArgsConstructor
@Getter
public enum UrlWebForms {


    TASK("http://%s/task-form.html");

    private final String url;
}
