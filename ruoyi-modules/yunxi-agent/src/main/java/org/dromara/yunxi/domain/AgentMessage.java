package org.dromara.yunxi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

/**
 * Agent 消息表 yunxi_agent_message。
 *
 * @author yunxi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yunxi_agent_message")
public class AgentMessage extends TenantEntity {

    @TableId(value = "message_id")
    private Long messageId;

    private Long sessionId;

    private Long traceId;

    private Long userId;

    private String role;

    private String content;

    private String contentType;

    private Integer sequenceNo;

    private String status;

    private String delFlag;
}
