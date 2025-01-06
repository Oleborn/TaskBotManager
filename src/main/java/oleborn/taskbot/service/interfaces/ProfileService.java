package oleborn.taskbot.service.interfaces;


import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileSelfDataDto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;


public interface ProfileService {

    void createProfile(Update update);

    Optional<ProfileDto> updateProfile(ProfileDto profile);

    void deleteProfile(ProfileDto profileDto);

    Optional<ProfileDto> getProfileByID(Long id);

    List<ProfileDto> getFriends(Long id);

    void setSelfDateProfile(ProfileSelfDataDto dto);

}
