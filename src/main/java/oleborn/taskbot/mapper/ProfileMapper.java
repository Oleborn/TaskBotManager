package oleborn.taskbot.mapper;

import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.entities.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto toDto(Profile profile);

    Profile fromDto(ProfileDto profileDto);

    // Метод для обновления существующего объекта
    void updateProfileEntityFromDto(ProfileDto profileDto, @MappingTarget Profile profileEntity);

}
