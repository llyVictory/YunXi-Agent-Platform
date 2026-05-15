package org.dromara.yunxi.service;

import org.dromara.yunxi.domain.vo.AgentTraceDetailVo;

/**
 * Agent Trace 服务。
 *
 * @author yunxi
 */
public interface IAgentTraceService {

    AgentTraceDetailVo queryTraceDetail(Long traceId);
}
