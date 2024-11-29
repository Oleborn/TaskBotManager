package oleborn.taskbot.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friends")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotNull
    private long telegramId;

    @Column(nullable = false)
    @NotNull
    private String telegramNickname;

    @Column(nullable = false)
    @NotNull
    private Boolean isAllowedSendTasks;

}
