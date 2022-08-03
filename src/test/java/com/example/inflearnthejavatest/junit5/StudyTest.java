package com.example.inflearnthejavatest.junit5;

import com.example.inflearnthejavatest.domain.Study;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JUnit5 기본 어노테이션
 */
class StudyTest {

    @Disabled // 테스트 제외
    @Test
    void create() {
        Study study = new Study();
        assertNotNull(study);
        System.out.println("create");
    }

    @Test
    void updtae() {
        System.out.println("update");
    }

    // 반드시 static
    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }

    // 반드시 static
    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("after each");
    }
}