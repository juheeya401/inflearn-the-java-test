package com.example.inflearnthejavatest.junit5;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

/**
 * 실행이 오래걸리는 테스트를 찾아내는 익스텐션 만들기
 */
public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private final long THRESHOLD;

    public FindSlowTestExtension(long THRESHOLD) {
        this.THRESHOLD = THRESHOLD;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);
        store.put("START_TIME", System.currentTimeMillis());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Method requiredTestMethod = context.getRequiredTestMethod();
        String methodName = requiredTestMethod.getName();
        SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);

        ExtensionContext.Store store = getStore(context);
        Long start_time = store.remove("START_TIME", long.class);
        long duration = System.currentTimeMillis() - start_time;
        if (duration > THRESHOLD && annotation == null) {
            System.out.printf("이 [%s] 메서드 겁나 오래 걸려니다. @SlowTest 붙여라\n", methodName);
        }
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        String className = context.getRequiredTestClass().getName();
        String methodName = context.getRequiredTestMethod().getName();

        // store : 데이터를 넣고 빼는 용도의 인터페이스
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(className, methodName));
        return store;
    }
}
