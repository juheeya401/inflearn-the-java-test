package com.example.inflearnthejavatest.dockertest;

import com.example.inflearnthejavatest.study.StudyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 27. Testcontainers, 컨테이너 정보를 스프링 테스트에서 참조하기
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@Slf4j
@ContextConfiguration(initializers = DockerTest5.ContainerPropertyInitializer.class)
public class DockerTest5 {

    @Autowired
    StudyRepository studyRepository;

    @Container
    static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB","studytest")
            .withEnv("POSTGRES_HOST_AUTH_METHOD","trust")
            .waitingFor(Wait.forListeningPort());

    @BeforeAll
    static void beforeAll() {
        Slf4jLogConsumer slf4jLogConsumer = new Slf4jLogConsumer(log);
        postgreSQLContainer.followOutput(slf4jLogConsumer);
    }

    @BeforeEach
    void beforeEach() {
        studyRepository.deleteAll();
    }


    @Autowired
    private Environment environment;

    @Value("${container.port}")
    private String containerPort;

    @DisplayName("컨테이너 설정정보 꺼내써보기")
    @Test
    void testProperteis() {
        System.out.println("======================================");
        System.out.println(environment.getProperty("container.port"));  // 사용방법1. 직접 Environment에서 꺼낸다.
        System.out.println("containerPort=" + containerPort);           // 사용방법2. @Value 어노테이션을 이용한다.
    }

    // 컨테이너 내 정보를 Spring Context 에 담기
    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("container.port=" + postgreSQLContainer.getMappedPort(5432))
                    .applyTo(applicationContext.getEnvironment());
            ;
        }
    }
}
