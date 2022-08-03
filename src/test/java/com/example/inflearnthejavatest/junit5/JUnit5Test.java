package com.example.inflearnthejavatest.junit5;

import com.example.inflearnthejavatest.domain.Study;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.util.ObjectUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 연습문제
 * 2. JUnit5 jupiter 세가지 모듈 중 테스트 실행하는 런처, 테스트 엔진의 API를 제공하는 모듈 = junit platform
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스 인스턴스를 각 메서드마다 공유됨
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JUnit5Test {

    private Study study;

    @Test
    void create_new_study() {
        study = new Study();
        // 중간에 멈추지 않고 모든 테스트 실행 후 결과를 출력한다.
        assertAll(
                () -> assertEquals("테스트1","테스트1"),
                () -> assertTrue(!ObjectUtils.isEmpty(study))
        );
    }

    // @Retention() // 런타임 전략
    @Test
    void test_instance() {
        assertTrue(!ObjectUtils.isEmpty(study));
    }
}
