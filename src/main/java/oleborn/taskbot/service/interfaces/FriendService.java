package oleborn.taskbot.service.interfaces;

import oleborn.taskbot.model.dto.FriendDto;


public interface FriendService {

    FriendDto createFriend(FriendDto friendDto);

    FriendDto updateFriend(FriendDto friendDto);

    void deleteFriend(FriendDto friendDto);

    FriendDto getFriendByID(Long id);

}
