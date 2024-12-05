package oleborn.taskbot.service;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.stereotype.Component;

@Component
public class OutputService {

    @Resource
    private OutputsMethods outputsMethods;
    @Resource
    private TaskService taskService;

    public void setOutputsBotMessageForTask(TaskDto taskDto){
        taskService.outputInMessageTask(taskDto);
        outputsMethods.outputMessageWithCapture(
                taskDto.getOwnerId(),
                OutputMessages.OUTPUT_MESSAGE_FOR_TASK.getTextMessage().formatted(taskDto.getTitle(), taskDto.getDescription()),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture()
        );

    }

}
