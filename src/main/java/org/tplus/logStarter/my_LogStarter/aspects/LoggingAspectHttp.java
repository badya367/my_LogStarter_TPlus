package org.tplus.logStarter.my_LogStarter.aspects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.tplus.logStarter.my_LogStarter.configuration.LogPropertiesHttp;

import java.util.Enumeration;

/**
 * Аспект для логирования HTTP-запросов и ответов, аннотированных {@code @LogExecutionHttp}.
 *
 * <p>Позволяет:
 * <ul>
 *     <li>Логировать HTTP-запросы перед их выполнением.</li>
 *     <li>Логировать параметры запроса, заголовки и тело.</li>
 *     <li>Логировать HTTP-ответы после их выполнения.</li>
 *     <li>Логировать статус ответа, заголовки и тело.</li>
 * </ul>
 *
 * <p>Использует настройки логирования из {@link LogPropertiesHttp}.</p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-08
 */
@Aspect
@AllArgsConstructor
@Slf4j
public class LoggingAspectHttp {
    private final LogPropertiesHttp logPropertiesHttp;

    /**
     * Определяет точку среза для методов, аннотированных {@code @LogExecutionHttp}.
     */
    @Pointcut("@annotation(org.tplus.logStarter.my_LogStarter.aspects.annotations.LogExecutionHttp)")
    public void httpLogMethods() {
    }

    /**
     * Логирует HTTP-запрос перед его выполнением.
     */
    @Before("httpLogMethods()")
    public void logRequest() {

        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) return;

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n= HTTP запрос  =\n");
        logMessage.append("Метод: ").append(request.getMethod()).append("\n");
        logMessage.append("Путь: ").append(request.getRequestURI()).append("\n");

        if (!request.getParameterMap().isEmpty()) {
            logMessage.append("Параметры запроса: ").append(request.getParameterMap()).append("\n");
        }

        logMessage.append("Заголовки:\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logMessage.append("  ").append(headerName).append(": ")
                    .append(request.getHeader(headerName)).append("\n");
        }

        if (request.getMethod().equalsIgnoreCase("POST") ||
                request.getMethod().equalsIgnoreCase("PUT")) {
            try {
                String body = new String(((ContentCachingRequestWrapper) request).getContentAsByteArray());
                logMessage.append("Тело запроса: ").append(body).append("\n");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                logMessage.append("Не удалось логировать тело запроса").append("\n");
            }
        }

        logAtLevel(logMessage.toString());
    }

    /**
     * Логирует HTTP-ответ после выполнения запроса.
     *
     * @param result тело ответа
     */
    @AfterReturning(pointcut = "httpLogMethods()", returning = "result")
    public void logResponse(Object result) {

        HttpServletResponse response = getCurrentHttpResponse();
        if (response == null) return;

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n= HTTP ответ =\n");
        logMessage.append("Статус ответа: ").append(response.getStatus()).append("\n");

        response.getHeaderNames().forEach(headerName ->
                logMessage.append("  ").append(headerName).append(": ")
                        .append(response.getHeader(headerName)).append("\n")
        );

        logMessage.append("Тело ответа: ").append(result).append("\n");

        logAtLevel(logMessage.toString());
    }

    /**
     * Получает текущий HTTP-запрос.
     *
     * @return текущий {@link HttpServletRequest} или {@code null}, если запрос отсутствует
     */
    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attributes != null) ? attributes.getRequest() : null;
    }

    /**
     * Получает текущий HTTP-ответ.
     *
     * @return текущий {@link HttpServletResponse} или {@code null}, если ответ отсутствует
     */
    private HttpServletResponse getCurrentHttpResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attributes != null) ? attributes.getResponse() : null;
    }

    /**
     * Логирует сообщение на уровне, указанном в настройках логирования.
     *
     * @param message сообщение для логирования
     * @param args    аргументы сообщения
     */
    private void logAtLevel(String message, Object... args) {
        log.atLevel(logPropertiesHttp.logLevel()).log(message, args);
    }
}
