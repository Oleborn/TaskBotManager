package oleborn.taskbot.updatehandler;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateHandlerImpl implements UpdateHandler {

    @Resource
    private ProfileService profileService;

    @Override
    public void handler(Update update) {

        ProfileDto profileDto = profileService.getProfileByID(update.getMessage().getFrom().getId());

        if (update.hasMessage()) {
            switch (profileDto.getCommunicationStatus()){

            }
        }else if (update.hasCallbackQuery()) {

        }else {

        }
    }
}
