package com.example.demo.repository;

import com.example.demo.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Integer> {
    List<MissionEntity> findByMemberId(String memberId);

    Optional<MissionEntity> findByMissionId(int missionId);
    Optional<MissionEntity> findByMemberIdAndStartDate(String memberId, String startDate);
    Optional<MissionEntity> findByMemberIdAndNow(String memberId, String now);
    Optional<MissionEntity> findByStartDate(String startDate);

    Optional<MissionEntity> findByNow(String now);

    //Optional<MissionEntity> findByMemberIdAndNow(String memberId, String now);
}
