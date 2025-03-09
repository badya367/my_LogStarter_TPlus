package org.tplus.logStarter.my_LogStarter.aspects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для логирования HTTP-запросов.
 * <p>
 * Применяется к методам, которые обрабатывают HTTP-запросы. Аспект, связанный с этой аннотацией,
 * будет логировать информацию о запросе и его ответе, включая данные о статусе выполнения.
 * </p>
 * <p>
 * Пример использования:
 * <pre>
 * {@code
 * @LogExecutionHttp
 * public ResponseEntity<String> handleRequest(HttpServletRequest request) {
 *     // обработка HTTP-запроса, выполнение которого будет залогировано
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
public @interface LogExecutionHttp {
}
