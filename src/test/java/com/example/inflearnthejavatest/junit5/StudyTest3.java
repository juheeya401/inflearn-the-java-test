package com.example.inflearnthejavatest.junit5;

import com.example.inflearnthejavatest.domain.Study;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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

// 방법 1. @ExtendWith = 선언적으로 등록하는 방법
@ExtendWith(FindSlowTestExtension.class)
public class StudyTest3 {


    // 실행결과 : '이 [create] 메서드 겁나 오래 걸려니다. @SlowTest 붙여라' 라고 나옴
    @Test
    void slowTest1() throws InterruptedException {
        Thread.sleep(1005L); // 1초가 넘기 때문에 확장팩으로 인해 slowTest 라고 인식함
        Study study = new Study();
        assertNotNull(study);
        System.out.println("create");
    }

    // 실행결과 : slowTest이지만, 이미 @SlowTest 가 붙어있기 때문에 권고사항이 뜨지 않는다
    @SlowTest
    @Test
    void slowTest2() throws InterruptedException {
        Thread.sleep(1005L); // 1초가 넘기 때문에 확장팩으로 인해 slowTest 라고 인식함 => 하지만 이미 @SlowTest를 붙여서(확장팩에 어노테이션 체크 로직이 있음) 경고문구 뜨지 않음
        Study study = new Study();
        assertNotNull(study);
        System.out.println("create");
    }
}
