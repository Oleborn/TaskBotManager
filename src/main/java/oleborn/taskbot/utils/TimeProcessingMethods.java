package oleborn.taskbot.utils;

import jakarta.annotation.Resource;
import oleborn.taskbot.model.dto.TaskDto;
import oleborn.taskbot.service.interfaces.ProfileService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeProcessingMethods {

    @Resource
    private ProfileService profileService;

    public LocalDateTime processingTimeToMSK(LocalDateTime time, int hoursOffset) {
        if (hoursOffset >= 0) {
            return time.minusHours(Math.abs(hoursOffset)); // Вычитаем часы
        } else {
            return time.plusHours(hoursOffset); // Добавляем часы

        }
    }

    public LocalDateTime processLocalTimeToMSKTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Europe/Moscow")).toLocalDateTime();
    }

    public LocalDateTime processMSKTimeToLocalTimeForProfile(TaskDto taskDto) {
        return taskDto.getDateSending()
                .atZone(ZoneId.of("Europe/Moscow"))
                //тут мы просто прибавляем часы к текущему времени в МСК
                .plusHours(Integer.parseInt(profileService.getProfileByID(taskDto.getOwnerId()).getTimeZone()))
                .toLocalDateTime();
    }
}
