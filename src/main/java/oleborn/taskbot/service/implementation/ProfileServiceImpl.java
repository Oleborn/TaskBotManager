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
                        .telegramId(update.getMessage().getFrom().getId())
                        .communicationStatus(CommunicationStatus.DEFAULT)
                        .build()
        ));
    }

    @Override
    public void updateProfile(ProfileDto profileDto) {
        Profile profileEntity = profileRepository.findById(profileDto.getTelegramId())
                .orElseThrow(() -> new RuntimeException("ПОКА ТЕСТ updateProfile"));
        //обновляем статус так как в форме он не получается
        profileDto.setCommunicationStatus(profileEntity.getCommunicationStatus());

        profileMapper.updateProfileEntityFromDto(profileDto, profileEntity);
        profileRepository.save(profileEntity);
    }

    @Override
    public void deleteProfile(ProfileDto profileDto) {
        profileRepository.deleteById(profileDto.getTelegramId());
    }

    @Override
    public ProfileDto getProfileByIDForStart(Long id) {
        Profile profile = profileRepository.findById(id).orElse(null);
        return profileMapper.toDto(profile);
    }

    @Override
    public ProfileDto getProfileByID(Long id) {
        return profileMapper.toDto(
                profileRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Профиль с id " + id + " не найден"))
        );
    }

    @Override
    public ProfileDto getProfileByTelegramName(String telegramNickName) {
        Profile profile = profileRepository.findProfileByNickName(telegramNickName)
                .orElseThrow(() -> new RuntimeException("Профиль с telegramNickName " + telegramNickName + " не найден"));
        return profileMapper.toDto(profile);
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
