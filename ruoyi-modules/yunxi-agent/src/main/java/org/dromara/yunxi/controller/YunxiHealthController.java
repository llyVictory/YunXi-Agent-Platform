package org.dromara.yunxi.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.yunxi.config.YunxiAiProperties;
import org.dromara.yunxi.domain.vo.YunxiHealthVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 云犀 Agent 基础探活接口。
 *
 * @author yunxi
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/health")
public class YunxiHealthController {

    private static final String SERVICE_NAME = "ruoyi-yunxi-agent";

    private final YunxiAiProperties yunxiAiProperties;

    @GetMapping
    public R<YunxiHealthVo> health() {
        YunxiHealthVo healthVo = new YunxiHealthVo();
        healthVo.setServiceName(SERVICE_NAME);
        healthVo.setStatus("UP");
        healthVo.setChatProvider(yunxiAiProperties.getChat().getProvider());
        healthVo.setEmbeddingProvider(yunxiAiProperties.getEmbedding().getProvider());
        healthVo.setRerankProvider(yunxiAiProperties.getRerank().getProvider());
        return R.ok(healthVo);
    }
}
