package oleborn.taskbot.service.implementation;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.ProfileMapper;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileToSendTaskDto;
import oleborn.taskbot.model.entities.Profile;
import oleborn.taskbot.repository.ProfileRepository;
import oleborn.taskbot.service.interfaces.OutputService;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.updatehandler.UpdateHandlerImpl;
import oleborn.taskbot.utils.CommunicationStatus;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.UrlWebForms;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static oleborn.taskbot.utils.CommunicationStatus.INPUT_FRIEND;
import static oleborn.taskbot.utils.OutputMessages.*;


@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    @Resource
    private ProfileRepository profileRepository;

    @Resource
    private ProfileMapper profileMapper;

    @Resource
    @Lazy
    private OutputService outputService;

    @Value("${taskbot.provider}")
    private String provider;


    @Override
    public void createProfile(Update update) {
        profileRepository.save(profileMapper.fromDto(
                ProfileDto.builder()
                        .nickName(update.getMessage().getFrom().getUserName())
                        .telegramId(update.getMessage().getFrom().getId())
                        .communicationStatus(CommunicationStatus.DEFAULT)
                        .build()
        ));
    }

    @Override
    public void updateProfile(ProfileDto profileDto) {
        Profile profileEntity = profileRepository.findById(profileDto.getTelegramId()).orElse(null);
        //обновляем статус так как в форме он не получается
        if (profileDto.getCommunicationStatus() == null) {
            profileDto.setCommunicationStatus(profileEntity.getCommunicationStatus());
        }
        //обновляем список друзей так как в форме он не получается
        if (profileDto.getListProfilesWhoCanSendMessages() == null) {
            profileDto.setListProfilesWhoCanSendMessages(profileMapper.toShortDtoList(profileEntity.getListProfilesWhoCanSendMessages()));
        }
        profileMapper.updateProfileEntityFromDto(profileDto, profileEntity);

        profileRepository.save(profileEntity);
    }

    @Override
    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    @Override
    public ProfileDto getProfileByID(Long id) {
        return profileMapper.toDto(profileRepository.findById(id).orElse(null));
    }

    @Override
    public ProfileDto getProfileByTelegramName(String telegramNickName) {
        return profileMapper.toDto(profileRepository.findProfileByNickName(telegramNickName).orElse(null));
    }

    @Override
    public List<ProfileDto> getFriends(Long id) {
        return profileRepository.findProfilesWhoICanSendMessages(id).stream()
                .map(profile -> profileMapper.toDto(profile))
                .toList();
    }

    @Override
    public List<ProfileDto> getSenderMeTasks(Long id) {
        return profileRepository.findProfilesWhoCanReceiveMessages(id).stream()
                .map(profile -> profileMapper.toDto(profile))
                .toList();
    }

    @Override
    public void viewProfile(Update update) {
        ProfileDto profileDto = getProfileByID(update.getCallbackQuery().getFrom().getId());

        String outputMessages;

        if (profileDto.getListProfilesWhoCanSendMessages().isEmpty()) {
            outputMessages = RETURN_PROFILE_NO_FRIENDS.getTextMessage();
        } else {
            outputMessages = RETURN_PROFILE_RECEIVE_MESSAGE.getTextMessage().formatted(
                    profileDto.getListProfilesWhoCanSendMessages().stream()
                            .map(ProfileToSendTaskDto::getNickName) // Извлекаем nickName
                            .filter(Objects::nonNull)    // Убираем возможные null значения
                            .collect(Collectors.joining("\n")) // Соединяем через перенос строки
            );
        }
        outputService.returningProfile(profileDto, update, outputMessages);
    }

    @Override
    public void addFriendSetStatus(Update update) {
        ProfileDto profileDto = getProfileByID(UpdateHandlerImpl.searchId(update));
        profileDto.setCommunicationStatus(INPUT_FRIEND);
        updateProfile(profileDto);

        outputService.returnAddedFriendMessage(update);
    }

    @Override
    public void deleteFriend(Update update) {
        ProfileDto profileDto = getProfileByID(UpdateHandlerImpl.searchId(update));

        List<ProfileToSendTaskDto> listSenders = profileDto.getListProfilesWhoCanSendMessages();

        boolean isEmpty = listSenders.isEmpty();

        String outputMessage = isEmpty ? RETURN_NO_SAVES_FRIENDS.getTextMessage() : RETURN_SAVES_FRIENDS.getTextMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = isEmpty
                ? new InlineKeyboardBuilder().addButton("В начало", "/start").build()
                : outputService.returnButtonInColumnSavedSenders(listSenders);

        outputService.returnMessageForSavedFriends(update, outputMessage, inlineKeyboardMarkup);
    }
}
