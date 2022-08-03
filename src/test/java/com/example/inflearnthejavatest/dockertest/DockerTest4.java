package com.example.inflearnthejavatest.dockertest;

import com.example.inflearnthejavatest.domain.Member;
import com.example.inflearnthejavatest.domain.Study;
import com.example.inflearnthejavatest.member.MemberService;
import com.example.inflearnthejavatest.study.StudyRepository;
import com.example.inflearnthejavatest.study.StudyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * DB 컨테이너 관리를 Testcontainers 라이브러리에게 맡기기 4
 * - Testcontainers 의 다양한 기능 살펴보기
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@Slf4j
public class DockerTest4 {

    @Container
    static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB","studytest") // 환경변수 키를 이용해서 DB명을 명시한다.
            .withEnv("POSTGRES_HOST_AUTH_METHOD","trust") // 반드시 POSTGRES_PASSWORD 값을 지정하거나 POSTGRES_HOST_AUTH_METHOD=trust 설정을 해야 한다.
            .waitingFor(Wait.forListeningPort()) // 컨테니어 사용할 준비가 될 때까지 기다려 줌. 해당 포트가 사용할 수 있는 상태가 되면 테스트를 실행한다.
            //.waitingFor(Wait.forHttp("/hello")) // 특정 http 요청으로 특정 URI에 대한 응답이 오는지
            //.waitingFor(Wait.forLogMessage("start")) // 특정 로그메시지가 실행 됐을 때까지 대기
    ;

    @BeforeAll
    static void beforeAll() {
        // 컨테이너 내 로그 스트리밍 하기
        Slf4jLogConsumer slf4jLogConsumer = new Slf4jLogConsumer(log);
        postgreSQLContainer.followOutput(slf4jLogConsumer);
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("=======");
        System.out.println(postgreSQLContainer.getMappedPort(5432));

        // 컨테이너 내 쌓여있는 모든 로그 찍음(중복됨)
        System.out.println(postgreSQLContainer.getLogs());

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

        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        studyService.createNewStudy(1L, study);
        assertNotNull(study.getOwnerId());
        assertEquals(member.getId(), study.getOwnerId());
    }



    @Test
    void mockSubbingTest2() {
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
