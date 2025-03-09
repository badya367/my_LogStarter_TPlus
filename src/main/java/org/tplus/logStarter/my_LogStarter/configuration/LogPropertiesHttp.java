package org.tplus.logStarter.my_LogStarter.configuration;


import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Класс, представляющий настройки логирования для HTTP-запросов, загружаемые из конфигурации Spring.
 * <p>
 * Этот класс используется для определения параметров логирования HTTP-запросов, таких как включение/отключение логирования,
 * уровень фильтрации и уровень логирования. Конфигурации можно задать через файл {@code application.yml} или {@code application.properties}.
 * </p>
 *
 * <p>Параметры конфигурации:
 * <ul>
 *     <li>{@code enabled} - флаг включения логирования HTTP-запросов (тип данных: {@code Boolean}).</li>
 *     <li>{@code fillterLevel} - уровень фильтрации логирования для HTTP-запросов (тип данных: {@code Level}).</li>
 *     <li>{@code logLevel} - уровень логирования для HTTP-запросов (тип данных: {@code Level}).</li>
 * </ul>
 * </p>
 *
 * <p>Пример конфигурации в {@code application.yml} или {@code application.properties}:
 * <pre>
 * logging:
 *   settings-http:
 *     enabled: true
 *     fillterLevel: INFO
 *     logLevel: DEBUG
 * </pre>
 * </p>
 *
 * @param enabled      флаг, указывающий, включено ли логирование HTTP-запросов.
 * @param fillterLevel уровень фильтрации логов HTTP-запросов, определяющий минимальный уровень логируемых сообщений.
 * @param logLevel     уровень логирования HTTP-запросов, определяющий, на каком уровне будут логироваться сообщения.
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-08
 */
@ConfigurationProperties(prefix = "logging.settings-http")
public record LogPropertiesHttp(Boolean enabled, Level fillterLevel, Level logLevel) {
    /**
     * Конструктор, автоматически вызываемый при инициализации объекта.
     * Устанавливает значения по умолчанию для параметров, если они не были указаны в конфигурации.
     *
     * <p>Если {@code enabled} не задан, логирование будет включено по умолчанию.</p>
     * <p>Если {@code fillterLevel} и {@code logLevel} не заданы, они будут установлены на {@code Level.INFO}.</p>
     *
     * @param fillterLevel уровень фильтрации логирования для HTTP-запросов.
     * @param logLevel     уровень логирования для HTTP-запросов.
     */
    public LogPropertiesHttp {
        if (enabled == null) {
            enabled = Boolean.TRUE;
        }

        if (fillterLevel == null) {
            fillterLevel = Level.INFO;
        }

        if (logLevel == null) {
            logLevel = Level.INFO;
        }
    }
}
