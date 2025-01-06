package oleborn.taskbot.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oleborn.taskbot.utils.CommunicationStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {


    //---- Информация которую пользователь вводит по желанию ----//

    @Column(length = 40)
    @Size(max = 60)
    private String yourselfName;

    @Column(length = 20)
    private String yourselfDateOfBirth;

    @Column(length = 400)
    private String yourselfDescription;

    @ManyToMany
    @JoinTable(
            name = "profile_messages",
            joinColumns = @JoinColumn(name = "sender_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver_profile_id")
    )
    @Builder.Default
    private List<Profile> listProfilesWhoCanSendMessages = new ArrayList<>();

    //---- Telegram data ----//
    @Column
    private String nickName;

    @Id
    private Long telegramId;

    // ---- Utils ----//
    @Column
    @Enumerated(EnumType.STRING)
    private CommunicationStatus communicationStatus;

    @Column
    private String timeZone;

}
