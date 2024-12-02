package oleborn.taskbot.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.LocalTime;

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

    @Column(nullable = false, length = 20)
    @Size(min = 1, max = 20)
    @NotNull
    private String title;

    @Column(nullable = false, length = 300)
    @Size(min = 1, max = 300)
    @NotNull
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column
    private LocalDateTime dateModified;

    @Column
    private OffsetDateTime dateSending; // Замена long на OffsetDateTime для гибкости работы с временными зонами

    @Column
    private boolean sent;

    @Column
    private boolean updated;

    @PrePersist
    protected void onCreate() {
        dateCreated = LocalDateTime.now();
        dateModified = dateCreated;
    }

    @PreUpdate
    protected void onUpdate() {
        dateModified = LocalDateTime.now();
    }
}
