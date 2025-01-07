package oleborn.taskbot.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UrlWebForms {


    CREATE_TASK("https://%s/task-form.html"),
    UPDATE_TASK("https://%s/updateTask_form.html?task_id=%s"),
    SELF_DATA("https://%s/self-data-form.html"),
    SELF_DATA_UPDATE("https://%s/self-data-form_update.html");


    private final String url;
}
