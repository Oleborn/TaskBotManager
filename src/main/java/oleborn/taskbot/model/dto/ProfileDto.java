package oleborn.taskbot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oleborn.taskbot.model.entities.Friend;
import oleborn.taskbot.utils.CommunicationStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {

    private long id;
    private String yourselfName;
    private String yourselfDateOfBirth;
    private String yourselfDescription;
    private int yourselfGmt;
    private List<Friend> listFriends;
    private String nickName;
    private Long telegramId;
    private CommunicationStatus communicationStatus;
}
