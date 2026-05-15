package org.dromara.yunxi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Agent Trace 状态。
 *
 * @author yunxi
 */
@Getter
@AllArgsConstructor
public enum AgentTraceStatus {

    RUNNING("running", "运行中"),
    SUCCESS("success", "成功"),
    FAILED("failed", "失败");

    private final String code;

    private final String desc;
}
