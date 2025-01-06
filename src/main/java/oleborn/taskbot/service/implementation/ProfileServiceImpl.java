package oleborn.taskbot.service.implementation;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import oleborn.taskbot.mapper.ProfileMapper;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileSelfDataDto;
import oleborn.taskbot.model.entities.Profile;
import oleborn.taskbot.repository.ProfileRepository;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.utils.CommunicationStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    @Resource
    private ProfileRepository profileRepository;

    @Resource
    private ProfileMapper profileMapper;


    @Override
    public void createProfile(Update update) {
        profileRepository.save(profileMapper.fromDto(
                ProfileDto.builder()
                        .nickName(update.getMessage().getFrom().getUserName())
                        .telegramId(update.getMessage().getChatId())
                        .communicationStatus(CommunicationStatus.DEFAULT)
                        .build()
        ));
    }

    @Override
    public Optional<ProfileDto> updateProfile(ProfileDto profileDto) {
        System.out.println(profileDto);
        Profile profileEntity = profileRepository.findById(profileDto.getTelegramId())
                .orElseThrow(() -> new RuntimeException("ПОКА ТЕСТ"));

        profileMapper.updateProfileEntityFromDto(profileDto, profileEntity);

        return Optional.ofNullable(profileMapper.toDto(profileRepository.save(profileEntity)));
    }

    @Override
    public void deleteProfile(ProfileDto profileDto) {
        profileRepository.deleteById(profileDto.getTelegramId());
    }

    @Override
    public Optional<ProfileDto> getProfileByID(Long id) {

        Optional<Profile> profileEntity = profileRepository.findById(id);

        return profileEntity.map(profileMapper::toDto);
                //.orElseThrow(() -> new EntityNotFoundException("Профиль с ID " + id + " не найден")));
    }

    @Override
    public List<ProfileDto> getFriends(Long id) {
        return profileRepository.findFriendsById(id).stream()
                .map(profile -> profileMapper.toDto(profile))
                .toList();
    }

    @Override
    public void setSelfDateProfile(ProfileSelfDataDto dto) {
        Optional<Profile> profileEntity = profileRepository.findById(dto.getTelegramId());

        if (profileEntity.isPresent()) {
            ProfileDto profileDto = ProfileDto.builder()
                    .yourselfName(dto.getYourselfName())
                    .yourselfDateOfBirth(dto.getYourselfDateOfBirth())
                    .yourselfDescription(dto.getYourselfDescription())
                    .timeZone(dto.getTimeZone())
                    .build();

            profileRepository.save(profileMapper.fromDto(profileDto));
        }
    }
}
