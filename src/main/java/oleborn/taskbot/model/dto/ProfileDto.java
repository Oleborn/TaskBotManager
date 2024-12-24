package oleborn.taskbot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oleborn.taskbot.model.entities.Profile;
import oleborn.taskbot.utils.CommunicationStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {

    private String yourselfName;
    private String yourselfDateOfBirth;
    private String yourselfDescription;
    private List<Profile> listProfilesWhoCanSendMessages;
    private String nickName;
    private Long telegramId;
    private CommunicationStatus communicationStatus;
    private String timeZone;
}
