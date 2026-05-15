package org.dromara.yunxi.config;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("dev")
public class YunxiAiPropertiesTest {

    @Test
    public void shouldExposeLocalAiServiceDefaults() {
        YunxiAiProperties properties = new YunxiAiProperties();

        assertThat(properties.getChat().getProvider()).isEqualTo("openai-compatible");
        assertThat(properties.getChat().getBaseUrl()).isEqualTo("https://new.lemonapi.site/v1");
        assertThat(properties.getChat().getModel()).isEqualTo("[L]gemini-2.5-flash");
        assertThat(properties.getChat().getTimeout()).isEqualTo(Duration.ofSeconds(30));

        assertThat(properties.getEmbedding().getProvider()).isEqualTo("local");
        assertThat(properties.getEmbedding().getBaseUrl()).isEqualTo("http://172.26.70.234:18080");
        assertThat(properties.getEmbedding().getModel()).isEqualTo("./models/bge-small-zh-v1.5");
        assertThat(properties.getEmbedding().getTimeout()).isEqualTo(Duration.ofSeconds(5));

        assertThat(properties.getRerank().getProvider()).isEqualTo("local");
        assertThat(properties.getRerank().getBaseUrl()).isEqualTo("http://172.26.70.234:18080");
        assertThat(properties.getRerank().getModel()).isEqualTo("./models/bce-reranker-base_v1");
        assertThat(properties.getRerank().getTimeout()).isEqualTo(Duration.ofSeconds(8));
        assertThat(properties.getRerank().isFallbackEnabled()).isTrue();
    }
}
