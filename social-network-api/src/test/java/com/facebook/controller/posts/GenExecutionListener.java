package com.facebook.controller.posts;

import lombok.extern.log4j.Log4j2;
import com.facebook.utils.Gen;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Слухач для генерації даних перед запуском тестів.
 */
@Log4j2
public class GenExecutionListener extends AbstractTestExecutionListener {
    /**
     * Генерація даних перед запуском усіх тестів.
     *
     * @param testContext Контекст поточного тесту.
     */
    @Override
    public void beforeTestClass(TestContext testContext) {
        ApplicationContext context = testContext.getApplicationContext();
        log.info("Data generation is starting...");
        Gen.of(context);
    }

}
