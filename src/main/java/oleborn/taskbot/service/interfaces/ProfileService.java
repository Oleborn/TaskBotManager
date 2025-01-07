package oleborn.taskbot.service.interfaces;


import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileSelfDataDto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;


public interface ProfileService {

    void createProfile(Update update);

    void updateProfile(ProfileDto profile);

    void deleteProfile(ProfileDto profileDto);

    ProfileDto getProfileByIDForStart(Long id);

    ProfileDto getProfileByID(Long id);

    ProfileDto getProfileByTelegramName(String telegramNickName);

    List<ProfileDto> getFriends(Long id);

    void setSelfDateProfile(ProfileSelfDataDto dto);

}
