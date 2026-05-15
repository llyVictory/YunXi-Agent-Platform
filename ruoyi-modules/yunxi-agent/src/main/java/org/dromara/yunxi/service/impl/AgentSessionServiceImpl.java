package org.dromara.yunxi.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.yunxi.common.constant.YunxiConstants;
import org.dromara.yunxi.common.enums.AgentSessionStatus;
import org.dromara.yunxi.domain.AgentSession;
import org.dromara.yunxi.domain.bo.AgentSessionCreateBo;
import org.dromara.yunxi.domain.vo.AgentSessionVo;
import org.dromara.yunxi.mapper.AgentSessionMapper;
import org.dromara.yunxi.service.IAgentSessionService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Agent 会话服务实现。
 *
 * @author yunxi
 */
@RequiredArgsConstructor
@Service
public class AgentSessionServiceImpl implements IAgentSessionService {

    private final AgentSessionMapper baseMapper;

    @Override
    public AgentSessionVo createSession(AgentSessionCreateBo bo) {
        AgentSession session = MapstructUtils.convert(bo, AgentSession.class);
        session.setTenantId(YunxiConstants.DEMO_TENANT_ID);
        session.setUserId(YunxiConstants.DEMO_USER_ID);
        session.setStatus(AgentSessionStatus.ACTIVE.getCode());
        session.setLastMessageTime(new Date());
        baseMapper.insert(session);
        return baseMapper.selectVoById(session.getSessionId());
    }
}
