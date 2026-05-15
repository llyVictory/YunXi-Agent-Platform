package org.dromara.yunxi.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.yunxi.domain.vo.AgentTraceDetailVo;
import org.dromara.yunxi.domain.vo.AgentTraceVo;
import org.dromara.yunxi.mapper.AgentTraceMapper;
import org.dromara.yunxi.mapper.AgentTraceStepMapper;
import org.dromara.yunxi.service.IAgentTraceService;
import org.springframework.stereotype.Service;

/**
 * Agent Trace 服务实现。
 *
 * @author yunxi
 */
@RequiredArgsConstructor
@Service
public class AgentTraceServiceImpl implements IAgentTraceService {

    private final AgentTraceMapper traceMapper;

    private final AgentTraceStepMapper traceStepMapper;

    @Override
    public AgentTraceDetailVo queryTraceDetail(Long traceId) {
        AgentTraceVo trace = traceMapper.selectVoById(traceId);
        AgentTraceDetailVo detailVo = new AgentTraceDetailVo();
        detailVo.setTrace(trace);
        detailVo.setSteps(traceStepMapper.selectByTraceId(traceId));
        return detailVo;
    }
}
