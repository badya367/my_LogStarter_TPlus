package org.tplus.logStarter.my_LogStarter.aspects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для логирования выполнения метода.
 * <p>
 * Применяется к методам для указания, что их выполнение должно быть логировано.
 * Аспект, связанный с этой аннотацией, будет логировать информацию о вызове метода, его завершении и возможных исключениях.
 * </p>
 * <p>
 * Пример использования:
 * <pre>
 * {@code
 * @LogExecution
 * public void myMethod() {
 *     // метод, выполнение которого будет залогировано
 * }
 * }
 * </pre>
 * </p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-08
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecution {
}