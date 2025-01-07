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

    @Query("SELECT p.listProfilesWhoCanSendMessages FROM Profile p WHERE p.telegramId = :id")
    List<Profile> findFriendsById(@Param("id") Long id);

    Optional<Profile> findProfileByNickName(String nickName);

}
