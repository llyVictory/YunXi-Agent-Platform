package org.dromara.yunxi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * Agent 会话表 yunxi_agent_session。
 *
 * @author yunxi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yunxi_agent_session")
public class AgentSession extends TenantEntity {

    @TableId(value = "session_id")
    private Long sessionId;

    private Long userId;

    private String title;

    private String businessType;

    private String customerId;

    private String status;

    private Date lastMessageTime;

    private String delFlag;
}
