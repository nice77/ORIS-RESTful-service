package org.semester.repository;

import org.semester.entity.Comment;
import org.semester.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNameContaining(String name, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = """
                    select * from fetch_events(:eventId, :page) as events
                    """
    )
    List<Event> fetchEventRecommendations(Long eventId, Integer page);
}
