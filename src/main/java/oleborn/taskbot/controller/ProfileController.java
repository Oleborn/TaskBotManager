package oleborn.taskbot.controller;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Resource
    private ProfileService profileService;

    @GetMapping("/{userId}/friends")
    public List<ProfileDto> getFriends(@PathVariable Long userId) {
        return profileService.getFriends(userId);
    }
}
