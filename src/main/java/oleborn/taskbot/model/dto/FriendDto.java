package oleborn.taskbot.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendDto {

    private long id;

    private long telegramId;

    private String telegramNickname;

    private Boolean isAllowedSendTasks;

    private String timeZoneId;
}
