package org.dromara.yunxi.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.yunxi.domain.AgentMessage;
import org.dromara.yunxi.domain.vo.AgentMessageVo;

import java.util.List;

/**
 * Agent 消息 Mapper。
 *
 * @author yunxi
 */
public interface AgentMessageMapper extends BaseMapperPlus<AgentMessage, AgentMessageVo> {

    default List<AgentMessageVo> selectBySessionId(Long sessionId) {
        return selectVoList(new LambdaQueryWrapper<AgentMessage>()
            .eq(AgentMessage::getSessionId, sessionId)
            .orderByAsc(AgentMessage::getSequenceNo)
            .orderByAsc(AgentMessage::getCreateTime));
    }
}
