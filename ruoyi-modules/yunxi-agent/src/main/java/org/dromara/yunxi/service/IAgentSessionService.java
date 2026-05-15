package org.dromara.yunxi.service;

import org.dromara.yunxi.domain.bo.AgentSessionCreateBo;
import org.dromara.yunxi.domain.vo.AgentSessionVo;

/**
 * Agent 会话服务。
 *
 * @author yunxi
 */
public interface IAgentSessionService {

    AgentSessionVo createSession(AgentSessionCreateBo bo);
}
