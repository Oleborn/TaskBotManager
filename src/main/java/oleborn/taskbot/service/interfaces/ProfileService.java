package oleborn.taskbot.service.interfaces;


import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileSelfDataDto;
import oleborn.taskbot.model.entities.Profile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


public interface ProfileService {

    void createProfile(Update update);

    ProfileDto updateProfile(ProfileDto profile);

    void deleteProfile(ProfileDto profileDto);

    ProfileDto getProfileByID(Long id);

    List<ProfileDto> getFriends(Long id);

    void setSelfDateProfile(ProfileSelfDataDto dto);

}
