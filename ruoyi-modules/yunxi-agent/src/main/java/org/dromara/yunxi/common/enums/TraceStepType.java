package org.dromara.yunxi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Trace 步骤类型。
 *
 * @author yunxi
 */
@Getter
@AllArgsConstructor
public enum TraceStepType {

    WORKFLOW("workflow", "工作流"),
    LLM("llm", "大模型"),
    TOOL("tool", "工具"),
    RAG("rag", "检索增强"),
    SYSTEM("system", "系统");

    private final String code;

    private final String desc;
}
