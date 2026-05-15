package org.dromara.yunxi.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.yunxi.domain.bo.AgentSessionCreateBo;
import org.dromara.yunxi.domain.vo.AgentMessageVo;
import org.dromara.yunxi.domain.vo.AgentSessionVo;
import org.dromara.yunxi.service.IAgentMessageService;
import org.dromara.yunxi.service.IAgentSessionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Agent 会话接口。
 *
 * @author yunxi
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/agent/sessions")
public class AgentSessionController {

    private final IAgentSessionService sessionService;

    private final IAgentMessageService messageService;

    @PostMapping
    public R<AgentSessionVo> create(@Validated @RequestBody AgentSessionCreateBo bo) {
        return R.ok(sessionService.createSession(bo));
    }

    @GetMapping("/{sessionId}/messages")
    public R<List<AgentMessageVo>> messages(@NotNull @PathVariable Long sessionId) {
        return R.ok(messageService.listBySessionId(sessionId));
    }
}
