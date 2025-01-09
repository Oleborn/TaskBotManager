package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.ProfileMapper;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileToSendTaskDto;
import oleborn.taskbot.model.entities.Profile;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.updatehandler.UpdateHandlerImpl;
import oleborn.taskbot.updatehandler.handlers.interfaces.CommandHandler;
import oleborn.taskbot.updatehandler.handlers.interfaces.MessagesHandler;
import oleborn.taskbot.service.interfaces.TaskService;
import oleborn.taskbot.utils.CommunicationStatus;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.UrlWebForms;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Component
public class MessagesHandlerImpl implements MessagesHandler {

    @Resource
    private OutputsMethods outputsMethods;
    @Resource
    private ProfileService profileService;
    @Resource
    private ProfileMapper profileMapper;

    @Resource
    private CommandHandler commandHandler;

    @Value("${taskbot.provider}")
    private String provider;

    @Override
    public void messagesHandler(Update update) {

        ProfileDto profileDto = profileService.getProfileByID(update.getMessage().getFrom().getId());

        if (update.getMessage().getText().startsWith("/")) {
            commandHandler.handleCommand(update);
            return;
        }

        switch (profileDto.getCommunicationStatus()){
            case DEFAULT -> {}
            case INPUT_FRIEND -> {
                ProfileDto profileFriend = profileService.getProfileByTelegramName(update.getMessage().getText());
                ProfileDto profileClient = profileService.getProfileByID(update.getMessage().getFrom().getId());

                if (profileFriend == null) {
                    outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                            update.getMessage().getFrom().getId(),
                            OutputMessages.FRIEND_NOT_FOUND.getTextMessage(),
                            RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                            new InlineKeyboardBuilder().addButton("В начало", "/start").build()
                    );
                    profileClient.setCommunicationStatus(CommunicationStatus.DEFAULT);
                    profileService.updateProfile(profileClient);
                    return;
                }


                List<ProfileToSendTaskDto> listProfilesWhoCanSendMessages = profileClient.getListProfilesWhoCanSendMessages();

                listProfilesWhoCanSendMessages.add(profileMapper.toShortDto(profileMapper.fromDto(profileFriend)));
                profileClient.setListProfilesWhoCanSendMessages(listProfilesWhoCanSendMessages);
                profileClient.setCommunicationStatus(CommunicationStatus.DEFAULT);
                profileService.updateProfile(profileClient);

                outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                        update.getMessage().getFrom().getId(),
                        OutputMessages.FRIEND_ADDED.getTextMessage(),
                        RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                        new InlineKeyboardBuilder().addButton("В начало", "/start").build()
                );

                outputsMethods.outputMessageWithCapture(
                        profileFriend.getTelegramId(),
                        OutputMessages.MESSAGES_FRIEND_ADDED.getTextMessage().formatted(profileClient.getNickName()),
                        RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture()
                );
            }
        }
    }
}
