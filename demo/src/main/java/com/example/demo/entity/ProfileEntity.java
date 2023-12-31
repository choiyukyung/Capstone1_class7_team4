package com.example.demo.entity;

import com.example.demo.dto.ProfileDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "profile_table") //database에 해당 이름의 테이블 생성
public class ProfileEntity {
    @Id
    @Column
    private String memberId;

    @Column
    private Integer missionCnt;

    @Column
    private Integer successCnt;

    @Column
    private Integer level;

    @Column
    private String position;

    @Column
    private Integer weekTotalAmount;

    @Column
    private String goalEntry1;

    @Column
    private String goalEntry2;

    @Column
    private String goalEntry3;

    @Column
    private String successMission1;

    @Column
    private String successMission2;

    @Column
    private String successMission3;

    @Column
    private Integer totalSavingMoney;


//    @SuppressWarnings("JpaAttributeTypeInspection")
//    @Column
//    private List<String> goalEntry; //3개
//
//    @Convert(converter = StringListConverter.class)
//    @Column(columnDefinition = "json")
//    private List<String> successMission;

    public static ProfileEntity toProfileEntity(ProfileDTO profileDTO){
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setMemberId(profileDTO.getMemberId());
        profileEntity.setMissionCnt(profileDTO.getMissionCnt());
        profileEntity.setSuccessCnt(profileDTO.getSuccessCnt());
        profileEntity.setLevel(profileDTO.getLevel());
        profileEntity.setPosition(profileDTO.getPosition());
        profileEntity.setWeekTotalAmount(profileDTO.getWeekTotalAmount());
        profileEntity.setGoalEntry1(profileDTO.getGoalEntry1());
        profileEntity.setGoalEntry2(profileDTO.getGoalEntry2());
        profileEntity.setGoalEntry3(profileDTO.getGoalEntry3());
        profileEntity.setSuccessMission1(profileDTO.getSuccessMission1());
        profileEntity.setSuccessMission2(profileDTO.getSuccessMission2());
        profileEntity.setSuccessMission3(profileDTO.getSuccessMission3());
        profileEntity.setTotalSavingMoney(profileDTO.getTotalSavingMoney());

        return profileEntity;
    }


}
