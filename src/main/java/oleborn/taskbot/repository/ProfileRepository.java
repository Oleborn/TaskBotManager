package oleborn.taskbot.repository;

import oleborn.taskbot.model.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findProfileByNickName(String nickName);

    // Запрос списка тех кто может отправлять Profile сообщения
    @Query(value = "SELECT p.* FROM profile p " +
            "JOIN profile_messages pm ON p.telegram_id = pm.receiver_profile_id " +
            "WHERE pm.sender_profile_id = ?1", nativeQuery = true)
    List<Profile> findProfilesWhoCanReceiveMessages(Long senderId);

    // Запрос списка тех кому Profile может отправлять сообщения
    @Query("SELECT p.listProfilesWhoCanSendMessages FROM Profile p WHERE p.telegramId = :id")
    List<Profile> findProfilesWhoICanSendMessages(@Param("id") Long id);

}
