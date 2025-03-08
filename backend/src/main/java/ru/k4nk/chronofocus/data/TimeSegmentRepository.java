package ru.k4nk.chronofocus.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TimeSegmentRepository extends JpaRepository<TimeSegment, Integer> {

    Optional<TimeSegment> findByCategoryAndStartTimestampBeforeAndEndTimestampIsNull(Category category, LocalDateTime dateTime);

    Optional<TimeSegment> findByEndTimestampIsNull();


    @Query("""
                select ts from TimeSegment as ts
                join ts.category as c
                join c.user as u
                where u.login = :login
                and ts.startTimestamp < :toDateTime
                and (ts.endTimestamp > :fromDateTime OR ts.endTimestamp is null)
            """)
    List<TimeSegment> findSegmentsByLoginAndTimestamps(String login, LocalDateTime fromDateTime, LocalDateTime toDateTime);


    @Query("""
                 select ts from TimeSegment as ts
                 join ts.category as c
                 join c.user as u
                 where ts in :timeSegments
                 and u.login != :login
            """)
    Set<TimeSegment> findAllSegmentsBelongToOtherLogin(String login, List<TimeSegment> timeSegments);

    @Query("""
                select ts from TimeSegment as ts
                join ts.category as c
                join c.user as u
                where u.login = :login
                and ts.endTimestamp is null
            """)
    List<TimeSegment> findAllByLoginAndEndTimestampIsNull(String login);

}
