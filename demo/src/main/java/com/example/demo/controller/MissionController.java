package com.example.demo.controller;

import com.example.demo.dto.AccountAnalyzeDTO;
import com.example.demo.dto.MissionDTO;
import com.example.demo.dto.SurveyDTO;
import com.example.demo.service.AccountAnalyzeService;
import com.example.demo.service.MissionService;
import com.example.demo.service.ProfileService;
import com.example.demo.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MissionController {

    private final AccountAnalyzeService accountAnalyzeService;
    private final SurveyService surveyService;
    private final MissionService missionService;
    private final ProfileService profileService;

    // 이번주 사용 금액과 저번주 미션 금액을 비교해서 미션 성공 실패를 알림.
    @RequestMapping("/isMissionSuccess/{missionId}")
    public void isSuccess(@PathVariable("missionId") int missionId){

        missionService.isMissionSuccess(missionId);

        // 미션 성공 or 실패 한 경우 profile update
        profileService.updateSuccess(missionId); // 데모를 위해 무조건 success만 했다고 가정
    }

    public void mission(String memberId) {
        String missionEntry = "";
        int missionMoney = 0;

        // 1. 분석테이블을 보고 사용한 항목은 모두 missionCandi에
        List<AccountAnalyzeDTO> dtos = accountAnalyzeService.findByMemberIdAndOkToUse(memberId,false);
        System.out.println(dtos);

        List<String> missionCandi = new ArrayList<>();

        int missionStart = 0;
        for (AccountAnalyzeDTO useEntry : dtos) {
            missionCandi.add(useEntry.getEntry());
            missionStart = Integer.parseInt(useEntry.getOrderWeek()) + 1;
        }
        System.out.println(missionCandi);

        //2. 지난 미션이 있으면 항목 삭제
        String lastMissionDate = Integer.toString(missionStart - 7);
        MissionDTO lastmission = missionService.findByMemberIdAndStartDate(memberId,lastMissionDate);

        if(lastmission != null){


            missionCandi.remove(lastmission.getMissionEntry());
            System.out.println("제거완료");
            System.out.println(lastmission.getMissionEntry());
        }

        System.out.println(missionCandi);

        //3. fixed_entry 삭제
        SurveyDTO surveyDTO = surveyService.findBySurveyId(memberId);

        String[] fixedEntry = surveyDTO.getFixedEntry().split(",");
        for (AccountAnalyzeDTO useEntry : dtos) {
            for (String fEntry : fixedEntry) {
                if (useEntry.getEntry().equals(fEntry)) {
                    missionCandi.remove(fEntry);
                }
            }
        }
        System.out.println(missionCandi);

        // 4. 목표 항목과 소비의 차가 가장 큰 항목을 고름
        // 4-1. 분석 테이블에 지난주가 없으면 그냥 4를 미션으로 줌
        int diff_max = 0;
        for (AccountAnalyzeDTO useEntry : dtos) {
            if(missionCandi.contains(useEntry.getEntry())) {
                if (useEntry.getEntry().equals(surveyDTO.getGoalEntry1())) {
                    if (useEntry.getTotalAmount() - surveyDTO.getGoalMoney1() > diff_max) {
                        diff_max = useEntry.getTotalAmount() - surveyDTO.getGoalMoney1();
                        missionEntry = surveyDTO.getGoalEntry1();
                        missionMoney = surveyDTO.getGoalMoney1();
                    }
                }
                if (useEntry.getEntry().equals(surveyDTO.getGoalEntry2())) {
                    if (useEntry.getTotalAmount() - surveyDTO.getGoalMoney2() > diff_max) {
                        diff_max = useEntry.getTotalAmount() - surveyDTO.getGoalMoney2();
                        missionEntry = surveyDTO.getGoalEntry2();
                        missionMoney = surveyDTO.getGoalMoney2();
                    }
                }
                if (useEntry.getEntry().equals(surveyDTO.getGoalEntry3())) {
                    if (useEntry.getTotalAmount() - surveyDTO.getGoalMoney3() > diff_max) {
                        diff_max = useEntry.getTotalAmount() - surveyDTO.getGoalMoney3();
                        missionEntry = surveyDTO.getGoalEntry3();
                        missionMoney = surveyDTO.getGoalMoney3();
                    }
                }
                System.out.println(missionEntry);
            }
        }
        // 4-1. 분석 테이블에 지난 주가 없으면 그냥 4 줌
        if (diff_max > 0 && lastmission == null) {
            missionMoney += ((diff_max / 2) / 1000) * 1000;

            System.out.println(missionEntry);
            System.out.println(missionMoney);

            int missionEnd = missionStart + 6;
            String startDate = Integer.toString(missionStart);
            String endDate = Integer.toString(missionEnd);

            //MissionDTO에 저장(missionId, memberId, missionEntry, missionMoney, now, startDate까지)
            MissionDTO missionDTO = missionService.makeMissionDTO(memberId, missionEntry, missionMoney, startDate, endDate);

            missionService.save(missionDTO);

        }//4-2. 분석 테이블에 지난 주가 있으면 지난 주보다 훨씬 많이 쓴 항목을 계산하고 4번과 비교한 후 4번보다 3배 이상 차이가 크다면 미션으로 줌
        else if(diff_max > 0 && lastmission != null){
            MissionDTO missionDTOWithLastWeek = missionService.compareWithLastWeek(memberId, missionCandi, diff_max, missionStart);
            if(missionDTOWithLastWeek != null){

                missionService.save(missionDTOWithLastWeek);
            }
            else{
                missionMoney += ((diff_max / 2) / 1000) * 1000;

                System.out.println(missionEntry);
                System.out.println(missionMoney);

                int missionEnd = missionStart + 6;
                String startDate = Integer.toString(missionStart);
                String endDate = Integer.toString(missionEnd);


                MissionDTO missionDTO = missionService.makeMissionDTO(memberId, missionEntry, missionMoney, startDate, endDate);

                missionService.save(missionDTO);
            }
        }
        //이게 5인 상황
        //5. 목표 항목이 모두 다 목표를 지켰으면
        else{
            System.out.println("목표금액보다 많이 쓴 항목이 없다.");

            int maxUse = 0;
            for (AccountAnalyzeDTO useEntry : dtos) {

                if (missionCandi.contains(useEntry.getEntry())) {
                    if (useEntry.getTotalAmount() > maxUse) {
                        missionEntry = useEntry.getEntry();
                        missionMoney = useEntry.getTotalAmount();
                    }
                }
            }
            missionMoney = (int) (missionMoney * 0.9);
            missionMoney = ((missionMoney / 1000) * 1000);

            System.out.println(missionEntry);
            System.out.println(missionMoney);

            int missionEnd = missionStart + 6;
            String startDate = Integer.toString(missionStart);
            String endDate = Integer.toString(missionEnd);


            MissionDTO missionDTO = missionService.makeMissionDTO(memberId, missionEntry, missionMoney, startDate, endDate);

            missionService.save(missionDTO);
        }


    }






}
