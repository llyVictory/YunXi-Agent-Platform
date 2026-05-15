package org.dromara.yunxi.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.yunxi.domain.vo.AgentMessageVo;
import org.dromara.yunxi.mapper.AgentMessageMapper;
import org.dromara.yunxi.service.IAgentMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Agent 消息服务实现。
 *
 * @author yunxi
 */
@RequiredArgsConstructor
@Service
public class AgentMessageServiceImpl implements IAgentMessageService {

    private final AgentMessageMapper baseMapper;

    @Override
    public List<AgentMessageVo> listBySessionId(Long sessionId) {
        return baseMapper.selectBySessionId(sessionId);
    }
}
