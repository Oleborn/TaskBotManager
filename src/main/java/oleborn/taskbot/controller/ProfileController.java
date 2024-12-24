package oleborn.taskbot.controller;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileSelfDataDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/data")
    public ResponseEntity<Void> getProfile(@RequestBody ProfileSelfDataDto dto) {
        profileService.setSelfDateProfile(dto);
        return ResponseEntity.ok().build();
    }
}
