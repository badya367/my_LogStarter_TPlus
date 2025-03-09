package org.tplus.logStarter.my_LogStarter.aspects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для логирования времени выполнения метода.
 * <p>
 * Применяется к методам, для которых нужно измерить и залогировать время их выполнения.
 * Аспект, связанный с этой аннотацией, будет вычислять время, затраченное на выполнение метода,
 * и выводить информацию о времени выполнения в лог.
 * </p>
 * <p>
 * Пример использования:
 * <pre>
 * {@code
 * @LogExecutionTime
 * public void myMethod() {
 *     // метод, для которого будет залогировано время выполнения
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
public @interface LogExecutionTime {
}
