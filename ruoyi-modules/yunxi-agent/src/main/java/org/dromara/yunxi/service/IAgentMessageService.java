package org.dromara.yunxi.service;

import org.dromara.yunxi.domain.vo.AgentMessageVo;

import java.util.List;

/**
 * Agent 消息服务。
 *
 * @author yunxi
 */
public interface IAgentMessageService {

    List<AgentMessageVo> listBySessionId(Long sessionId);
}
