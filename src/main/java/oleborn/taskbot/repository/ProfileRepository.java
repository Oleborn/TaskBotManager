package oleborn.taskbot.repository;

import oleborn.taskbot.model.entities.Friend;
import oleborn.taskbot.model.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT p.listFriends FROM Profile p WHERE p.id = :id")
    List<Friend> findFriendsById(@Param("id") Long id);

}
