package org.dromara.yunxi.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.yunxi.domain.AgentTraceStep;

import java.io.Serial;
import java.io.Serializable;

/**
 * Agent Trace 步骤视图对象。
 *
 * @author yunxi
 */
@Data
@AutoMapper(target = AgentTraceStep.class)
public class AgentTraceStepVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long stepId;

    private Long traceId;

    private Long parentStepId;

    private Integer stepOrder;

    private String stepName;

    private String stepType;

    private String eventType;

    private String status;

    private Long elapsedMs;
}
