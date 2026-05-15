package org.dromara.yunxi.controller;

import org.dromara.common.core.domain.R;
import org.dromara.yunxi.config.YunxiAiProperties;
import org.dromara.yunxi.domain.vo.YunxiHealthVo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("dev")
public class YunxiHealthControllerTest {

    @Test
    public void shouldReturnModuleAndProviderStatus() {
        YunxiAiProperties properties = new YunxiAiProperties();
        YunxiHealthController controller = new YunxiHealthController(properties);

        R<YunxiHealthVo> response = controller.health();

        assertThat(response.getCode()).isEqualTo(R.SUCCESS);
        assertThat(response.getData().getServiceName()).isEqualTo("ruoyi-yunxi-agent");
        assertThat(response.getData().getStatus()).isEqualTo("UP");
        assertThat(response.getData().getChatProvider()).isEqualTo("openai-compatible");
        assertThat(response.getData().getEmbeddingProvider()).isEqualTo("local");
        assertThat(response.getData().getRerankProvider()).isEqualTo("local");
    }
}
