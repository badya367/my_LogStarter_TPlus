package org.tplus.logStarter.my_LogStarter.aspects;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.tplus.logStarter.my_LogStarter.configuration.LogProperties;

/**
 * Аспект для логирования выполнения методов, аннотированных {@code @LogExecution} и {@code @LogExecutionTime}.
 *
 * <p>Позволяет:
 * <ul>
 *     <li>Логировать вызовы методов перед выполнением.</li>
 *     <li>Логировать исключения, выброшенные методами.</li>
 *     <li>Логировать успешное выполнение методов и их результаты.</li>
 *     <li>Измерять и логировать время выполнения методов.</li>
 * </ul>
 *
 * <p>Использует настройки логирования из {@link LogProperties}.</p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-08
 */

@Aspect
@AllArgsConstructor
@Slf4j
public class LoggingAspect {
    private final LogProperties logProperties;

    /**
     * Определяет точку среза для методов, аннотированных {@code @LogExecution}.
     */
    @Pointcut("@annotation(org.tplus.logStarter.my_LogStarter.aspects.annotations.LogExecution)")
    public void methodsToLog() {
    }

    /**
     * Логирует вызов метода перед его выполнением.
     *
     * @param joinPoint информация о вызываемом методе
     */
    @Before("methodsToLog()")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        logAtLevel("Before: Метод вызывается - {}",
                joinPoint.getSignature().toShortString());
    }

    /**
     * Логирует исключение, выброшенное методом.
     *
     * @param ex выброшенное исключение
     */
    @AfterThrowing(pointcut = "methodsToLog()", throwing = "ex")
    public void logAfterThrowing(Exception ex) {
        log.error("AfterThrowing: Метод выбросил исключение - {}",
                ex.getMessage());
    }

    /**
     * Логирует успешное выполнение метода и его возвращаемый результат.
     *
     * @param result результат выполнения метода
     */
    @AfterReturning(pointcut = "methodsToLog()", returning = "result")
    public void logAfterReturning(Object result) {
        logAtLevel("AfterReturning: Метод успешно завершился. Результат: {}",
                result);
    }

    /**
     * Измеряет время выполнения метода, аннотированного {@code @LogExecutionTime}.
     *
     * @param joinPoint информация о вызываемом методе
     * @return результат выполнения метода
     * @throws Throwable если метод выбрасывает исключение
     */
    @Around("@annotation(org.tplus.logStarter.my_LogStarter.aspects.annotations.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        logAtLevel("Вызов метода: {}", methodName);

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            logAtLevel("Метод {} выполнен за {} мс",
                    joinPoint.getSignature().toShortString(), executionTime);

            return result;
        } catch (Throwable ex) {
            logAtLevel("Метод вызвал исключение, время работы метода: {} ms",
                    System.currentTimeMillis() - start);
            throw ex;
        }
    }

    /**
     * Логирует сообщение на уровне, указанном в настройках логирования.
     *
     * @param message сообщение для логирования
     * @param args    аргументы сообщения
     */
    private void logAtLevel(String message, Object... args) {
        log.atLevel(logProperties.logLevel()).log(message, args);
    }
}
