package oleborn.taskbot.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oleborn.taskbot.utils.CommunicationStatus;

import java.util.List;


@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 40)
    @Size(min = 3, max = 40)
    @NotNull
    private String yourselfName;

    @Column(length = 20)
    private String yourselfDateOfBirth;

    @Column(length = 400)
    private String yourselfDescription;

    @Column(nullable = false)
    private String timeZoneId;

    @ManyToMany
    @JoinTable(
            name = "profile_friends", // Название таблицы для связи
            joinColumns = @JoinColumn(name = "profile_id"), // Внешний ключ для Profile
            inverseJoinColumns = @JoinColumn(name = "friend_id") // Внешний ключ для Friend
    )
    private List<Friend> listFriends;

    //---- telegram data ----//
    @Column
    private String nickName;

    @Column
    private Long telegramId;

    // ---- utils ----//
    @Column
    private CommunicationStatus communicationStatus;

}
