package org.tplus.logStarter.my_LogStarter.configuration;

import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Класс, представляющий настройки логирования, загружаемые из конфигурации Spring.
 * <p>
 * Этот класс используется для определения параметров логирования, таких как уровень логирования и включение/отключение логирования.
 * Он позволяет задавать настройки фильтрации и уровня логирования через файл конфигурации.
 * </p>
 *
 * <p>Параметры конфигурации:
 * <ul>
 *     <li>{@code enabled} - флаг включения логирования (тип данных: {@code Boolean}).</li>
 *     <li>{@code fillterLevel} - уровень фильтрации логирования (тип данных: {@code Level}).</li>
 *     <li>{@code logLevel} - уровень логирования (тип данных: {@code Level}).</li>
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
 * </pre>
 * </p>
 *
 * @param enabled      флаг, указывающий, включено ли логирование.
 * @param fillterLevel уровень фильтрации логов, определяющий минимальный уровень логируемых сообщений.
 * @param logLevel     уровень логирования, определяющий, на каком уровне будут логироваться сообщения.
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-08
 */
@ConfigurationProperties(prefix = "logging.settings")
public record LogProperties(Boolean enabled, Level fillterLevel, Level logLevel) {
    /**
     * Конструктор, автоматически вызываемый при инициализации объекта.
     * Устанавливает значения по умолчанию для параметров, если они не были указаны в конфигурации.
     *
     * <p>Если уровень фильтрации и уровень логирования не заданы, они будут установлены на {@code Level.INFO}.
     * Если {@code enabled} не задан, будет установлено значение {@code true} (логирование включено).</p>
     *
     * @param fillterLevel уровень фильтрации логирования.
     * @param logLevel     уровень логирования.
     */
    public LogProperties {
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
