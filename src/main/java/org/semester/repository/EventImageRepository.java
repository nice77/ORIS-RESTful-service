package org.semester.repository;

import org.semester.entity.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM event_image WHERE event_id = :eventId")
    void deleteByEventId(Long eventId);
}
