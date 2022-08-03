package com.example.inflearnthejavatest.junit5;

import com.example.inflearnthejavatest.domain.Study;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

/**
 * JUnit5 조건에 따라 테스트 실행하기
 */
class StudyTest2 {

    @Test
    @DisplayName("실행환경에 따라 테스트하기1")
    void create() {
        String test_env = System.getenv("TEST_ENV");
        //System.out.println("test_env = " + test_env);

        System.out.println("Test1");

        // 조건이 ture 일 때만 그 밑에 테스트코드를 실행한다.
        assumeTrue("LOCAL".equalsIgnoreCase(test_env));
    }

    @Test
    @DisplayName("실행환경에 따라 테스트하기2")
    void create2() {
        String test_env = System.getenv("TEST_ENV");

        // assumingThat(조건, 실행할 테스트) 으로 환경별로 다르게 테스트 가능하다.
        assumingThat("LOCAL".equalsIgnoreCase(test_env), () -> {
            System.out.println("Test2 - local");
            Study study = new Study(100);
            assertThat(study.getLimitCount()).isGreaterThan(0);
        });
        assumingThat("DEV".equalsIgnoreCase(test_env), () -> {
            System.out.println("Test2 - dev");
            Study study = new Study(10);
            assertThat(study.getLimitCount()).isGreaterThan(0);
        });
    }

    @Test
    @DisplayName("실행환경에 따라 테스트하기3 - 어노테이션 방법")
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10, JRE.JAVA_11})
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    void create3() {
        System.out.println("Test3 - local");
        Study study = new Study(100);
        assertThat(study.getLimitCount()).isGreaterThan(0);
    }


    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
        System.out.println("");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("");
        System.out.println("after all");
    }
}