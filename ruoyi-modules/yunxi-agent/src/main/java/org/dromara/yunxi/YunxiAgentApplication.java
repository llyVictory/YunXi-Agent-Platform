package org.dromara.yunxi;

import org.dromara.yunxi.config.YunxiAiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 云犀 Agent 模块。
 *
 * @author yunxi
 */
@EnableConfigurationProperties(YunxiAiProperties.class)
@SpringBootApplication
public class YunxiAgentApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(YunxiAgentApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  云犀 Agent 模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
