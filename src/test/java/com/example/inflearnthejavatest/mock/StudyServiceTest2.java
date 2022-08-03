package com.example.inflearnthejavatest.mock;

import com.example.inflearnthejavatest.domain.Member;
import com.example.inflearnthejavatest.domain.Study;
import com.example.inflearnthejavatest.member.MemberService;
import com.example.inflearnthejavatest.study.StudyRepository;
import com.example.inflearnthejavatest.study.StudyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Mock 스터빙(행동 조작) Stubbing
 */
@ExtendWith(MockitoExtension.class)
class StudyServiceTest2 {

    @Test
    void mockSubbingTest1(@Mock MemberService memberService,
                          @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@naver.com");

        /**MemberService클래스의 findById() 메서드를 스터빙 한 것임**/
        // 파라미터, 리턴값 모두 지정 가능
        //when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(memberService.findById(any())).thenReturn(Optional.of(member));

        Study study = new Study(10, "java");

        Optional<Member> byId = memberService.findById(1L);
        Optional<Member> byId2 = memberService.findById(1L);
        assertEquals("test@naver.com", byId.get().getEmail());
        assertEquals("test@naver.com", byId2.get().getEmail());


        /**subbing - 예외 발생시키기**/
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L);
        });

        /**subbing - 호출 순서에 따라 행동을 지정할 수 있다.**/
        when(memberService.findById(any()))
                .thenReturn(Optional.of(member))
                .thenThrow(new RuntimeException())
                .thenReturn(Optional.empty());

        // 첫번째 호출
        Optional<Member> byId1 = memberService.findById(1L);
        assertEquals("test@naver.com", byId1.get().getEmail());

        // 두번째 호출
        assertThrows(RuntimeException.class, () -> {
            memberService.findById(1L);
        });

        // 세번째 호출
        assertEquals(Optional.empty(), memberService.findById(1L));

    }
}