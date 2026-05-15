package org.dromara.yunxi.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.yunxi.domain.vo.AgentTraceDetailVo;
import org.dromara.yunxi.service.IAgentTraceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Agent Trace 后台接口。
 *
 * @author yunxi
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/traces")
public class AdminTraceController {

    private final IAgentTraceService traceService;

    @GetMapping("/{traceId}")
    public R<AgentTraceDetailVo> detail(@NotNull @PathVariable Long traceId) {
        return R.ok(traceService.queryTraceDetail(traceId));
    }
}
