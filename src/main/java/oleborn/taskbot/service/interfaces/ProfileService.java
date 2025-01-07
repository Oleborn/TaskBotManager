package oleborn.taskbot.service.interfaces;


import oleborn.taskbot.model.dto.ProfileDto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


public interface ProfileService {

    void createProfile(Update update);

    void updateProfile(ProfileDto profile);

    void deleteProfile(Long id);

    ProfileDto getProfileByID(Long id);

    ProfileDto getProfileByTelegramName(String telegramNickName);

    List<ProfileDto> getFriends(Long id);

//    void saveSelfDateProfile(ProfileSelfDataDto dto);

}
