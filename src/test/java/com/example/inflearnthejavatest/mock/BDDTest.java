package com.example.inflearnthejavatest.mock;

import com.example.inflearnthejavatest.domain.Member;
import com.example.inflearnthejavatest.domain.Study;
import com.example.inflearnthejavatest.member.MemberService;
import com.example.inflearnthejavatest.study.StudyRepository;
import com.example.inflearnthejavatest.study.StudyService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * BDD 스타일의 API
 *
 * given -> when -> then
 */
public class BDDTest {


    @Mock
    MemberService memberService;
    @Mock
    StudyRepository studyRepository;

    @Disabled("BDD 함수")
    @Test
    void mockSubbingTest2() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Study study = new Study(10, "java");

        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@naver.com");

        //when(memberService.findById(1L)).thenReturn(Optional.of(member));
        //when(studyRepository.save(study)).thenReturn(study);
        /*
        여기까지가 모두 given 영역이다
        하지만 메서드명이 when()이라서 헷갈린다.
        모키토에서도 BDD에 맞는 메서드를 제공한다.
        */

        // 모키토에서 제공하는 given() 메소드
        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        // When
        studyService.createNewStudy(1L, study);

        // Then
        // 모키토에서 제공하는 then() 메소드를 이용할 수 있다.
        assertEquals(member.getId(), study.getOwnerId());
        then(memberService).should(times(1)).notify(study); //verify(memberService, times(1)).notify(study);과 동일
        then(memberService).shouldHaveNoMoreInteractions();  //verifyNoMoreInteractions(memberService); 과 동일
        // 기존 함수보다 위에 메서드가 명확함.

    }
}
