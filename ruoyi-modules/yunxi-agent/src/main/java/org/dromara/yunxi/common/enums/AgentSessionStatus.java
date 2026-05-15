package org.dromara.yunxi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Agent 会话状态。
 *
 * @author yunxi
 */
@Getter
@AllArgsConstructor
public enum AgentSessionStatus {

    ACTIVE("active", "进行中"),
    ARCHIVED("archived", "已归档");

    private final String code;

    private final String desc;
}
