package org.dromara.yunxi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * Agent Trace 表 yunxi_agent_trace。
 *
 * @author yunxi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yunxi_agent_trace")
public class AgentTrace extends TenantEntity {

    @TableId(value = "trace_id")
    private Long traceId;

    private Long sessionId;

    private Long userId;

    private String businessType;

    private String status;

    private Date startedAt;

    private Date finishedAt;

    private Long elapsedMs;

    private String errorCode;

    private String errorMessage;

    private String delFlag;
}
