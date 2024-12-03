package oleborn.taskbot.controller;

import oleborn.taskbot.model.dto.FriendDto;
import oleborn.taskbot.model.entities.Friend;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping("/{userId}/friends")
    public List<FriendDto> getFriends(@PathVariable Long userId) {
        // Заглушка списка друзей
        // Пример списка друзей
        //TODO переделать на получение настоящих друзей из PROFILE
        List<FriendDto> friends = List.of(
                new FriendDto(1L, 10001L, "john_doe", true),
                new FriendDto(2L, 10002L, "jane_smith", true)
        );
        return friends;
    }
}
