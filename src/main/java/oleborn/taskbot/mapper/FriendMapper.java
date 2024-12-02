package oleborn.taskbot.mapper;

import oleborn.taskbot.model.dto.FriendDto;
import oleborn.taskbot.model.entities.Friend;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    FriendDto toFriendDto(Friend friend);

    Friend fromFriendDto(FriendDto friendDto);

    void updateFriendEntityFromDto(FriendDto friendDto, @MappingTarget Friend friend);

}
