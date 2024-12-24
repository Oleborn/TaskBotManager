package oleborn.taskbot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

    private long id;

    private long ownerId;

    private long creatorId;

    private String title;

    private String description;

    private LocalDateTime dateCreated;

    private LocalDateTime dateModified;

    private OffsetDateTime dateSending;

    private String timeZoneOwner;

    private boolean sent;

    private boolean updated;
}
