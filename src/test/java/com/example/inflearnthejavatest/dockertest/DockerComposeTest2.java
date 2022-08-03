package com.example.inflearnthejavatest.dockertest;

import com.example.inflearnthejavatest.domain.Member;
import com.example.inflearnthejavatest.domain.Study;
import com.example.inflearnthejavatest.member.MemberService;
import com.example.inflearnthejavatest.study.StudyRepository;
import com.example.inflearnthejavatest.study.StudyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Docker compose 로 컨테이너 띄우기2
 * - 컨테이너 내 설정정보 가져와서 사용하기
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@Slf4j
@ContextConfiguration(initializers = DockerComposeTest2.ContainerPropertyInitializer.class) // 2.
public class DockerComposeTest2 {

    // 4.
    @Value("${container.port}") int port;

    @Container
    static DockerComposeContainer dockerComposeContainer =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("study-db", 5432); // 3. Context 갖다 쓸 때는 반드시 이렇게 서비스를 지정해줘야 함


    // 1. docker-compose 설정정보 Spring의 Environment 에 넣기
    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("container.port=" + dockerComposeContainer.getServicePort("study-db", 5432))
                    .applyTo(applicationContext.getEnvironment());
        }
    }

    @Mock
    MemberService memberService;

    @Autowired
    StudyRepository studyRepository;

    @Test
    void mockSubbingTest1() {
        System.out.println("==== port -> " + port);  // 5. 사용


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
}
