package org.dromara.yunxi.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.yunxi.domain.AgentMessage;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Agent 消息视图对象。
 *
 * @author yunxi
 */
@Data
@AutoMapper(target = AgentMessage.class)
public class AgentMessageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long messageId;

    private Long sessionId;

    private Long traceId;

    private String role;

    private String content;

    private String contentType;

    private Integer sequenceNo;

    private Date createTime;
}
