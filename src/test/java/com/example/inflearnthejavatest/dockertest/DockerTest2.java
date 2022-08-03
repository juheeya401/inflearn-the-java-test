package com.example.inflearnthejavatest.dockertest;

import com.example.inflearnthejavatest.domain.Member;
import com.example.inflearnthejavatest.domain.Study;
import com.example.inflearnthejavatest.member.MemberService;
import com.example.inflearnthejavatest.study.StudyRepository;
import com.example.inflearnthejavatest.study.StudyService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * DB 컨테이너 관리를 Testcontainers 라이브러리에게 맡기기 2
 * - JUnit5 확장팩을 이용해서 어노테이션을 이용해서 컨테이너 시작,종료 자동화하기
 * - @Testcontainers, @Container
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers // 1.
public class DockerTest2 {

    @Container // 2.
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("studytest"); // DB명을 명시한다.

    // 3. beforeAll 에서 항상 모든 데이터를 지워주므로 이전 테스트의 데이터에 영향을 받지 않게 할 수 있다.
    @BeforeEach
    void beforeEach() {
        studyRepository.deleteAll();
    }


    @Mock
    MemberService memberService;

    @Autowired
    StudyRepository studyRepository;

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

        studyService.createNewStudy(1L, study);
        assertNotNull(study.getOwnerId());
        assertEquals(member.getId(), study.getOwnerId());
    }
}
