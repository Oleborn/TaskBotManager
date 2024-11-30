package oleborn.taskbot.service.implementation;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.ProfileMapper;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.entities.Profile;
import oleborn.taskbot.repository.ProfileRepository;
import oleborn.taskbot.service.interfaces.ProfileService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ProfileServiceImpl implements ProfileService {

    @Resource
    private ProfileRepository profileRepository;

    @Resource
    private ProfileMapper profileMapper;


    @Override
    public ProfileDto createProfile(ProfileDto profileDto) {
        Profile profileEntity = profileMapper.fromDto(profileDto);
        return profileMapper.toDto(profileRepository.save(profileEntity));
    }

    @Override
    public ProfileDto updateProfile(ProfileDto profileDto) {
        Profile profileEntity = profileRepository.findById(profileDto.getId())
                .orElseThrow(() -> new RuntimeException("ПОКА ТЕСТ"));

        profileMapper.updateEntityFromDto(profileDto, profileEntity);

        return profileMapper.toDto(profileRepository.save(profileEntity));
    }

    @Override
    public void deleteProfile(ProfileDto profileDto) {
        profileRepository.delete(profileMapper.fromDto(profileDto));
    }

    @Override
    public ProfileDto getProfileByID(Long id) {

        Optional<Profile> profileEntity = profileRepository.findById(id);

        return profileEntity.map(profile -> profileMapper.toDto(profile))
                .orElseThrow(() -> new RuntimeException("ПОКА ТЕСТ"));
    }
}
