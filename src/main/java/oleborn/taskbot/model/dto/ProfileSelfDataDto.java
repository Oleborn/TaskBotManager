package oleborn.taskbot.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ProfileSelfDataDto {
    private long telegramId;
    private String yourselfName;
    private String yourselfDateOfBirth;
    private String yourselfDescription;

}
