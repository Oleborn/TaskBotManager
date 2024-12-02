package oleborn.taskbot.updatehandler;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.repository.TaskRepository;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.service.interfaces.TaskService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateHandlerImpl implements UpdateHandler {

    @Resource
    private ProfileService profileService;
    @Resource
    private TaskService taskService;
    @Resource
    private TaskRepository taskRepository;

    @Override
    public void handler(Update update) {

        ProfileDto profileDto = profileService.getProfileByID(update.getMessage().getFrom().getId());

        if (update.hasMessage() && update.getMessage().getText().startsWith("/")){
//            hanleCommand();
            return;
        }

        switch (profileDto.getCommunicationStatus()){
//            case DEFAULT -> as;
//
//            //Создание таски
//            case INPUT_TITLE -> as;
//            case INPUT_TEXT -> as;
//
//            //создание профиля
//            case INPUT_FRIEND -> as;
//            case INPUT_GMT -> as;
//            case INPUT_YOURSELF_NAME -> as;
//            case INPUT_DATE_BIRTHSDAY -> as;
//
//            //обновление таски
//            case UPDATE_TEXT -> as;
//            case UPDATE_TITLE -> as;
//            //обновление профиля
//            case UPDATE_GMT -> as;
//            case UPDATE_YOURSELF_NAME -> as;
//            case UPDATE_DATE_BIRTHSDAY -> as;
//
//            case BLOCKED -> as;
        }
    }

    private void inputText(Update update) {
        if (update.hasMessage()) {
            TaskDto taskDto = TaskDto.builder()
                    .title(update.getMessage().getText())
                    .build();

        }
    }
}
