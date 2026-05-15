package org.dromara.yunxi.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Agent Trace 回放详情。
 *
 * @author yunxi
 */
@Data
public class AgentTraceDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private AgentTraceVo trace;

    private List<AgentTraceStepVo> steps = new ArrayList<>();
}
