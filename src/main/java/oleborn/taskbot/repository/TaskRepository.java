package oleborn.taskbot.repository;

import oleborn.taskbot.model.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.dateSending >= :time and t.sent = false")
    List<Task> findTasksToSend(OffsetDateTime time);
}
