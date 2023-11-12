package com.stat.statserver.repository;

import com.stat.statserver.model.StatsRecord;
import com.stat.statserver.model.UserActivityView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRecordJpaRepository extends JpaRepository<StatsRecord, Long> {

    @Query("SELECT s.userId AS userId, s.uri AS uri, COUNT(s) AS hitsQuantity " +
            "FROM StatsRecord s WHERE s.userId IN :userIds AND s.dateTime " +
            "BETWEEN :startDate AND :endDate GROUP BY s.userId, s.uri ORDER BY s.userId ASC, hitsQuantity DESC")
    List<UserActivityView> getStats(@Param("userIds")List<Long> userIds, @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate );

}
