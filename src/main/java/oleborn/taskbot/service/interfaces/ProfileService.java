package oleborn.taskbot.service.interfaces;

import oleborn.taskbot.model.dto.ProfileDto;


public interface ProfileService {

    ProfileDto createProfile(ProfileDto profile);

    ProfileDto updateProfile(ProfileDto profile);

    void deleteProfile(ProfileDto profileDto);

    ProfileDto getProfileByID(Long id);

}
