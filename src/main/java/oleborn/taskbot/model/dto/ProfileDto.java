package oleborn.taskbot.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import oleborn.taskbot.model.entities.Profile;
import oleborn.taskbot.utils.CommunicationStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileDto {

    String yourselfName;
    String yourselfDateOfBirth;
    String yourselfDescription;
    List<ProfileToSendTaskDto> listProfilesWhoCanSendMessages;
    String nickName;
    Long telegramId;
    CommunicationStatus communicationStatus;
    String timeZone;
}
