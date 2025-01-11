package oleborn.taskbot.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID.
    private long id;

    @NotNull
    @Column(nullable = false)
    private long ownerId;

    @NotNull
    @Column(nullable = false)
    private long creatorId;

    @Column(nullable = false, length = 60)
    @Size(min = 1, max = 60)
    @NotNull
    private String title;

    @Column(nullable = false, length = 300)
    @Size(max = 300)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column
    private LocalDateTime dateModified;

    @Column
    private LocalDateTime dateSending;

    @Column
    private String timeZoneOwner;

    @Column
    private boolean sent;

    @Column
    private boolean updated;

}
