package oleborn.taskbot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

    private long id;

    private long ownerId;

    private String title;

    private String description;

    private LocalDateTime dateCreated;

    private LocalDateTime dateModified;

    private OffsetDateTime dateSending;

    private boolean sent;

    private boolean updated;
}
