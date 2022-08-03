package com.example.inflearnthejavatest.mock;

import com.example.inflearnthejavatest.domain.Member;
import com.example.inflearnthejavatest.domain.Study;
import com.example.inflearnthejavatest.member.MemberService;
import com.example.inflearnthejavatest.study.StudyRepository;
import com.example.inflearnthejavatest.study.StudyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Mock 객체 확인하기.
 *
 * - Mock 객체가 호출 됐는지 여부
 * - 호출됏으면 몇 번
 */
@ExtendWith(MockitoExtension.class)
class StudyServiceTest3 {

    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

    @Test
    void mockSubbingTest1() {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Study study = new Study(10, "java");

        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@naver.com");

        // todo memberService 의 findById() 메소드를 1L 값으로 호출하면 member 객체를 리턴하도록 stubbing
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        // todo studyRepository 객체에 save() 메소드를 study 객체로 호출하면 study 객체 그대로 리턴하도록 Stubbing
        when(studyRepository.save(study)).thenReturn(study);

        studyService.createNewStudy(1L, study);
        assertNotNull(study.getOwnerId());
        assertEquals(member.getId(), study.getOwnerId());

        /**Mock 객체 확인하기 시작**/
        // 특정 메소드가 실행됐는지, 몇 번 실행됐는지 체크
        verify(memberService, times(1)).notify(study); // 무조건 1번
        verify(memberService, times(1)).notify(member); // 무조건 1번
        verify(memberService, never()).validate(any());

        // 메소드들이 순서대로 실행됐는지 체크
        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);
        inOrder.verify(memberService).notify(member);
    }

    @Test
    void mockSubbingTest2() {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Study study = new Study(10, "java");

        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@naver.com");

        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(studyRepository.save(study)).thenReturn(study);

        studyService.createNewStudy(1L, study);
        assertNotNull(study.getOwnerId());
        assertEquals(member.getId(), study.getOwnerId());

        /**Mock 객체 확인하기 시작**/
        verify(memberService, times(1)).notify(study);

        // 더이상 memberService가 호출되면 안된다.
        verifyNoMoreInteractions(memberService); // 에러발생함. 더이상 memberService를 사용하면 안되는데 createNewStudy() 메소드에서는 memberService.notify(member); 를 또 실행하기 때문이다
    }
}