package com.example.inflearnthejavatest.mock;

import com.example.inflearnthejavatest.domain.Member;
import com.example.inflearnthejavatest.domain.Study;
import com.example.inflearnthejavatest.domain.StudyStatus;
import com.example.inflearnthejavatest.member.MemberService;
import com.example.inflearnthejavatest.study.StudyRepository;
import com.example.inflearnthejavatest.study.StudyService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Mock 최종 연습문제
 */
@ExtendWith(MockitoExtension.class)
class ExamStubbing2 {
    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개해야한다")
    @Test
    void openStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바 테스트");
        assertNull(study.getOpenedDateTime());

        // todo studyRepository Mock 객체의 save메소드 호출시 study를 리턴하도록 한다.
        given(studyRepository.save(study)).willReturn(study);

        // When
        Study opendStudy = studyService.openStudy(study);

        // Then
        // todo study의 status가 OPEND로 변경됐는지 확인
        assertEquals(StudyStatus.OPENED, opendStudy.getStatus());
        // todo study의 dopendDateTime이 null이 아닌지 확인
        assertNotNull(opendStudy.getOpenedDateTime());
        // todo memberService의 notify(study)가 호출됐는지 확인.
        then(memberService).should().notify(study);

    }
}