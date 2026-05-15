package org.dromara.yunxi.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.yunxi.domain.AgentSession;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Agent 会话视图对象。
 *
 * @author yunxi
 */
@Data
@AutoMapper(target = AgentSession.class)
public class AgentSessionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long sessionId;

    private String title;

    private String businessType;

    private String customerId;

    private String status;

    private Date createTime;
}
