package com.AppRun.RunningAppBackend.repository;

import com.AppRun.RunningAppBackend.entity.PersonalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalRecordRepository extends JpaRepository<PersonalRecord, Long> {

    List<PersonalRecord> findByUserId(Long userId);

    @Query("SELECT pr FROM PersonalRecord pr WHERE pr.user.id = :userId AND pr.distanceKm = :distance")
    Optional<PersonalRecord> findByUserIdAndDistance(@Param("userId") Long userId, @Param("distance") Double distance);

    void deleteByUserId(Long userId);
}