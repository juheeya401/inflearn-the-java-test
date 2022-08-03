package com.example.inflearnthejavatest.junit5;

import com.example.inflearnthejavatest.domain.Study;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JUnit5 확장모델
 * 4에 비해 단순해 졌다.
 *
 * 5 = Extension 모델 하나로 통합됨
 * 4 = @RunWith(Runner), TestRule, MethodRule
 *
 * 확장팩 등록 종류(여러가지가 있다)
 * 방법 1. @ExtendWith
 * 방법 2. @RegisterExtension
 *
 * study sample
 * FindSlowTestExtension.java 실행이 오래걸리는 테스트를 찾아내는 익스텐션 만들기
 */

// 방법 2. @RegisterExtension = 선언적으로 등록하는 방법
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudyTest4 {

    // 인스턴스를 만들어주기만 하면 자동으로 이 클래스 내 테스트 실행시 감지함
    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension = new FindSlowTestExtension(1000L);

    @DisplayName("슬로우 테스트1")
    @Test
    void slowTest1() throws InterruptedException {
        Thread.sleep(1005L);
        Study study = new Study();
        assertNotNull(study);
        System.out.println("create1");
    }

    @DisplayName("슬로우 테스트2")
    @SlowTest
    @Test
    void slowTest2() throws InterruptedException {
        Thread.sleep(1005L);
        Study study = new Study();
        assertNotNull(study);
        System.out.println("create2");
    }
}
