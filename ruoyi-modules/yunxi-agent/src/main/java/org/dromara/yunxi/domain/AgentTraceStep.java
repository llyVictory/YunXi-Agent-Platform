package org.dromara.yunxi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

/**
 * Agent Trace 步骤表 yunxi_agent_trace_step。
 *
 * @author yunxi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yunxi_agent_trace_step")
public class AgentTraceStep extends TenantEntity {

    @TableId(value = "step_id")
    private Long stepId;

    private Long traceId;

    private Long parentStepId;

    private Integer stepOrder;

    private String stepName;

    private String stepType;

    private String eventType;

    private String status;

    private String inputSummary;

    private String outputSummary;

    private Long elapsedMs;

    private String errorCode;

    private String errorMessage;

    private String delFlag;
}
