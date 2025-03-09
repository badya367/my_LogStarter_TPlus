package org.tplus.logStarter.my_LogStarter.configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.tplus.logStarter.my_LogStarter.aspects.LoggingAspect;
import org.tplus.logStarter.my_LogStarter.aspects.LoggingAspectHttp;

import java.io.IOException;

/**
 * Конфигурация для автоматической настройки логирования в приложении.
 * <p>
 * Этот класс автоматически настраивает аспекты для логирования в приложении, включая логирование выполнения методов и HTTP-запросов.
 * Также включает фильтр для кэширования HTTP-запросов.
 * </p>
 *
 * <p>Основные компоненты, создаваемые в этом классе:
 * <ul>
 *     <li>{@code LoggingAspect} — аспект для логирования выполнения методов, настроенный через параметры конфигурации.</li>
 *     <li>{@code LoggingAspectHttp} — аспект для логирования HTTP-запросов, настроенный через параметры конфигурации.</li>
 *     <li>{@code OncePerRequestFilter} — фильтр, который кэширует HTTP-запросы для дальнейшего логирования их содержимого.</li>
 * </ul>
 * </p>
 *
 * <p>Пример конфигурации в {@code application.yml} или {@code application.properties}:
 * <pre>
 * logging:
 *   settings:
 *     enabled: true
 *     fillterLevel: INFO
 *     logLevel: DEBUG
 *
 * logging:
 *   settings-http:
 *     enabled: true
 *     fillterLevel: INFO
 *     logLevel: DEBUG
 * </pre>
 * </p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-08
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties({LogProperties.class, LogPropertiesHttp.class})
public class LoggingAutoConfigurer {
    /**
     * Создает и настраивает аспект логирования для методов приложения.
     * Аспект будет активирован, если в конфигурации {@code logging.settings.enabled} установлено {@code true}.
     * Также уровень логирования устанавливается в соответствии с параметрами {@code fillterLevel}.
     *
     * @param logProperties конфигурация, содержащая параметры логирования.
     * @return настроенный экземпляр {@link LoggingAspect}.
     */
    @Bean
    @ConditionalOnProperty(prefix = "logging.settings", name = "enabled", havingValue = "true", matchIfMissing = true)
    public LoggingAspect loggingAspect(LogProperties logProperties) {
        Logger log = (Logger) LoggerFactory.getLogger(LoggingAspect.class);

        log.setLevel(Level.convertAnSLF4JLevel(logProperties.fillterLevel()));
        return new LoggingAspect(logProperties);
    }

    /**
     * Создает и настраивает аспект логирования для HTTP-запросов.
     * Аспект будет активирован, если в конфигурации {@code logging.settings-http.enabled} установлено {@code true}.
     * Также уровень логирования устанавливается в соответствии с параметрами {@code fillterLevel}.
     *
     * @param logPropertiesHttp конфигурация для HTTP-логирования, содержащая параметры логирования.
     * @return настроенный экземпляр {@link LoggingAspectHttp}.
     */
    @Bean
    @ConditionalOnProperty(prefix = "logging.settings-http", name = "enabled", havingValue = "true", matchIfMissing = true)
    public LoggingAspectHttp loggingAspectHttp(LogPropertiesHttp logPropertiesHttp) {
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspectHttp.class);

        logger.setLevel(Level.convertAnSLF4JLevel(logPropertiesHttp.fillterLevel()));
        return new LoggingAspectHttp(logPropertiesHttp);
    }

    /**
     * Создает фильтр, который кэширует HTTP-запросы для их дальнейшего логирования.
     * Этот фильтр используется для обертки HTTP-запросов в {@link ContentCachingRequestWrapper}, чтобы их содержимое
     * можно было легко извлечь и логировать.
     *
     * @return экземпляр фильтра для кэширования HTTP-запросов.
     * @throws IOException      в случае ошибки ввода-вывода.
     * @throws ServletException в случае ошибки обработки сервлета.
     */
    @Bean
    public OncePerRequestFilter requestCachingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws IOException, ServletException {
                HttpServletRequest wrappedRequest = new ContentCachingRequestWrapper(request);
                filterChain.doFilter(wrappedRequest, response);
            }
        };
    }
}
