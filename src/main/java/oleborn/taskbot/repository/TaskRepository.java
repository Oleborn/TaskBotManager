package oleborn.taskbot.repository;

import oleborn.taskbot.model.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.dateSending >= :time and t.sent = false")
    List<Task> findTasksToSend(LocalDateTime time);

    List<Task> findAllByOwnerId(Long ownerId);

    List<Task> findAllByCreatorId(Long creatorId);

}

