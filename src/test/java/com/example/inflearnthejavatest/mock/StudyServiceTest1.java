package com.example.inflearnthejavatest.mock;

import com.example.inflearnthejavatest.member.MemberService;
import com.example.inflearnthejavatest.study.StudyRepository;
import com.example.inflearnthejavatest.study.StudyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

/**
 * Mock 객체 만들기
 * 아직 구현체가 없을 때 목 객체를 만든다.
 */
@ExtendWith(MockitoExtension.class)
class StudyServiceTest1 {


    @Test
    void createStudyService() {

        // 방법1. Mock 객체생성 방법1
        MemberService memberService = mock(MemberService.class);
        StudyRepository studyRepository = mock(StudyRepository.class);

        StudyService studyService = new StudyService(memberService, studyRepository);
    }


    // 방법2. Mock 객체생성 방법2
    // 클래스에 반드시 @ExtendWith(MockitoExtension.class) 확장팩을 등록해줘야 객체생성된다.
    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

    @Test
    void createStudyService2() {
        StudyService studyService = new StudyService(memberService, studyRepository);
    }

    // 방법3. 특정 테스트 메서드 안에서만 사용하고 싶을 때 - 파라미터로 받아서 쓸 수 있다.
    // 클래스에 반드시 @ExtendWith(MockitoExtension.class) 확장팩을 등록해줘야 객체생성된다.
    @Test
    void createStudyService3(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
    }
}