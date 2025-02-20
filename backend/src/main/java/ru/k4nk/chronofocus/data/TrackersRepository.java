package ru.k4nk.chronofocus.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrackersRepository extends JpaRepository<Tracker, Integer> {

    @Query("""
    select t from Tracker as t
    where t.startTimestamp >= :from
    and t.stopTimestamp <= :to
""")
    List<Tracker> getSummary(LocalDateTime from, LocalDateTime to);


    @Query("""
    select t from Tracker as t
    where t.category = :categoryId
    and t.stopTimestamp is null
""")
    Optional<Tracker> findActiveTracker(Integer categoryId, LocalDateTime from);

    Optional<Tracker> findByCategoryAndStopTimestampIsNull(Category category);

    Optional<Tracker> findByCategoryAndStartTimestampBeforeAndStopTimestampIsNull(Category category, LocalDateTime dateTime);

    Optional<Tracker> findByStopTimestampIsNull();

}
