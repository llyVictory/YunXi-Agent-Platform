package org.dromara.yunxi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Agent 消息角色。
 *
 * @author yunxi
 */
@Getter
@AllArgsConstructor
public enum AgentMessageRole {

    USER("user", "用户"),
    ASSISTANT("assistant", "助手"),
    SYSTEM("system", "系统"),
    TOOL("tool", "工具");

    private final String code;

    private final String desc;
}
