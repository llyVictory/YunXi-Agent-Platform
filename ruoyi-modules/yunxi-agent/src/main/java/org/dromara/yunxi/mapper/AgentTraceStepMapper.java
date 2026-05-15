package org.dromara.yunxi.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.yunxi.domain.AgentTraceStep;
import org.dromara.yunxi.domain.vo.AgentTraceStepVo;

import java.util.List;

/**
 * Agent Trace 步骤 Mapper。
 *
 * @author yunxi
 */
public interface AgentTraceStepMapper extends BaseMapperPlus<AgentTraceStep, AgentTraceStepVo> {

    default List<AgentTraceStepVo> selectByTraceId(Long traceId) {
        return selectVoList(new LambdaQueryWrapper<AgentTraceStep>()
            .eq(AgentTraceStep::getTraceId, traceId)
            .orderByAsc(AgentTraceStep::getStepOrder)
            .orderByAsc(AgentTraceStep::getStepId));
    }
}
