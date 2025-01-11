package oleborn.taskbot.updatehandler.handlers;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.ProfileMapper;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileToSendTaskDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.updatehandler.handlers.interfaces.CommandHandler;
import oleborn.taskbot.updatehandler.handlers.interfaces.MessagesHandler;
import oleborn.taskbot.utils.CommunicationStatus;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

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


        switch (profileDto.getCommunicationStatus()) {
            case INPUT_FRIEND -> {
                String friendName = update.getMessage().getText().replace("@", ""); // Удаляем символ @
                ProfileDto profileFriend = profileService.getProfileByTelegramName(friendName);
                ProfileDto profileClient = profileService.getProfileByID(update.getMessage().getFrom().getId());

                if (profileFriend == null) {
                    sendMessage(profileClient.getTelegramId(), OutputMessages.FRIEND_NOT_FOUND);
                    profileClient.setCommunicationStatus(CommunicationStatus.DEFAULT);
                    profileService.updateProfile(profileClient);
                    return;
                }

                List<ProfileToSendTaskDto> listProfilesWhoCanSendMessages = profileClient.getListProfilesWhoCanSendMessages();

                if (listProfilesWhoCanSendMessages.stream().anyMatch(p -> p.getNickName().equals(profileFriend.getNickName()))) {
                    sendMessage(profileClient.getTelegramId(), OutputMessages.FRIEND_EXISTS);
                    return;
                }

                listProfilesWhoCanSendMessages.add(profileMapper.toShortDto(profileMapper.fromDto(profileFriend)));
                profileClient.setListProfilesWhoCanSendMessages(listProfilesWhoCanSendMessages);
                profileClient.setCommunicationStatus(CommunicationStatus.DEFAULT);
                profileService.updateProfile(profileClient);

                sendMessage(profileClient.getTelegramId(), OutputMessages.FRIEND_ADDED);
                sendMessage(profileFriend.getTelegramId(), OutputMessages.MESSAGES_FRIEND_ADDED, profileClient.getNickName());
            }
        }


    }

    private void sendMessage(long chatId, OutputMessages message, Object... args) {
        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                chatId,
                message.getTextMessage().formatted(args),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder().addButton("В начало", "/start").build()
        );
    }
}
