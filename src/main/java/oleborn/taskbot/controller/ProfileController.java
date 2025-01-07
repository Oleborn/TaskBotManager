package oleborn.taskbot.controller;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.ProfileDto;
import oleborn.taskbot.model.dto.ProfileSelfDataDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import oleborn.taskbot.utils.OutputMessages;
import oleborn.taskbot.utils.RandomPictures;
import oleborn.taskbot.utils.outputMethods.InlineKeyboardBuilder;
import oleborn.taskbot.utils.outputMethods.OutputsMethods;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Resource
    private ProfileService profileService;
    @Resource
    private OutputsMethods outputsMethods;

//    @PostMapping("/saveData")
//    public ResponseEntity<Void> saveProfile(@RequestBody ProfileSelfDataDto dto) {
//        profileService.saveSelfDateProfile(dto);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileDto profileDto) {
        profileService.updateProfile(profileDto);

        outputsMethods.outputMessageWithCaptureAndInlineKeyboard(
                profileDto.getTelegramId(),
                OutputMessages.PROFILE_UPDATED.getTextMessage(),
                RandomPictures.RANDOM_BOT_THUMBS_UP.getRandomNamePicture(),
                new InlineKeyboardBuilder()
                        .addButton("Начать работу!", "/start")
                        .build()
        );

        return ResponseEntity.ok().build();
    }


    //--------Запросы форм ---------//
    @GetMapping("/{telegramId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable long telegramId) {
        ProfileDto profile = profileService.getProfileByID(telegramId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{userId}/friends")
    public List<ProfileDto> getFriends(@PathVariable Long userId) {
        return profileService.getFriends(userId);
    }
}
