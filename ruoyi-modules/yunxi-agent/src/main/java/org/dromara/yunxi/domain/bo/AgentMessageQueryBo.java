package org.dromara.yunxi.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Agent 消息查询对象。
 *
 * @author yunxi
 */
@Data
public class AgentMessageQueryBo {

    @NotNull(message = "会话ID不能为空")
    private Long sessionId;
}
