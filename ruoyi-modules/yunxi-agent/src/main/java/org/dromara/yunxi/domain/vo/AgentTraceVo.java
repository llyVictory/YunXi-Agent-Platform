package org.dromara.yunxi.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.yunxi.domain.AgentTrace;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Agent Trace 视图对象。
 *
 * @author yunxi
 */
@Data
@AutoMapper(target = AgentTrace.class)
public class AgentTraceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long traceId;

    private Long sessionId;

    private String businessType;

    private String status;

    private Date startedAt;

    private Date finishedAt;

    private Long elapsedMs;

    private String errorCode;

    private String errorMessage;
}
