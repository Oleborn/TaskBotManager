package oleborn.taskbot.service.interfaces;

import oleborn.taskbot.model.dto.FriendDto;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.entities.Friend;

import java.util.List;


public interface ProfileService {

    ProfileDto createProfile(ProfileDto profile);

    ProfileDto updateProfile(ProfileDto profile);

    void deleteProfile(ProfileDto profileDto);

    ProfileDto getProfileByID(Long id);

    List<FriendDto> getFriends(Long id);

}
