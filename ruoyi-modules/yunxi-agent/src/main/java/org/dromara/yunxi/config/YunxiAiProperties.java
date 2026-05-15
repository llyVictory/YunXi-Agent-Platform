package org.dromara.yunxi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * 云犀 AI 资源配置。
 *
 * @author yunxi
 */
@Data
@ConfigurationProperties(prefix = "yunxi.ai")
public class YunxiAiProperties {

    private ChatProperties chat = new ChatProperties();

    private EmbeddingProperties embedding = new EmbeddingProperties();

    private RerankProperties rerank = new RerankProperties();

    @Data
    public static class ChatProperties {

        private String provider = "openai-compatible";

        private String baseUrl = "https://new.lemonapi.site/v1";

        private String model = "[L]gemini-2.5-flash";

        private String apiKey;

        private Duration timeout = Duration.ofSeconds(30);
    }

    @Data
    public static class EmbeddingProperties {

        private String provider = "local";

        private String baseUrl = "http://172.26.70.234:18080";

        private String model = "./models/bge-small-zh-v1.5";

        private Duration timeout = Duration.ofSeconds(5);

        private int maxConcurrency = 20;
    }

    @Data
    public static class RerankProperties {

        private String provider = "local";

        private String baseUrl = "http://172.26.70.234:18080";

        private String model = "./models/bce-reranker-base_v1";

        private Duration timeout = Duration.ofSeconds(8);

        private int maxConcurrency = 10;

        private boolean fallbackEnabled = true;
    }
}
