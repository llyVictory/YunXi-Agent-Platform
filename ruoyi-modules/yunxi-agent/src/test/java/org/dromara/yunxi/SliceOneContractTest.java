package org.dromara.yunxi;

import org.dromara.yunxi.common.constant.YunxiConstants;
import org.dromara.yunxi.common.enums.AgentMessageRole;
import org.dromara.yunxi.common.enums.AgentSessionStatus;
import org.dromara.yunxi.common.enums.AgentTraceStatus;
import org.dromara.yunxi.common.enums.TraceStepType;
import org.dromara.yunxi.domain.bo.AgentMessageQueryBo;
import org.dromara.yunxi.domain.bo.AgentSessionCreateBo;
import org.dromara.yunxi.domain.vo.AgentTraceDetailVo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("dev")
public class SliceOneContractTest {

    @Test
    public void shouldExposeSliceOneEnumsAndConstants() {
        assertThat(YunxiConstants.DEMO_TENANT_ID).isEqualTo("000000");
        assertThat(AgentSessionStatus.ACTIVE.getCode()).isEqualTo("active");
        assertThat(AgentMessageRole.USER.getCode()).isEqualTo("user");
        assertThat(AgentTraceStatus.RUNNING.getCode()).isEqualTo("running");
        assertThat(TraceStepType.WORKFLOW.getCode()).isEqualTo("workflow");
    }

    @Test
    public void shouldExposeSessionAndTraceContracts() {
        AgentSessionCreateBo createBo = new AgentSessionCreateBo();
        createBo.setTitle("投诉沟通辅助");
        createBo.setBusinessType("complaint");
        createBo.setCustomerId("C10001");

        AgentMessageQueryBo queryBo = new AgentMessageQueryBo();
        queryBo.setSessionId(1L);

        AgentTraceDetailVo detailVo = new AgentTraceDetailVo();

        assertThat(createBo.getBusinessType()).isEqualTo("complaint");
        assertThat(queryBo.getSessionId()).isEqualTo(1L);
        assertThat(detailVo.getSteps()).isEmpty();
    }

    @Test
    public void shouldDocumentSliceOneDatabaseObjects() throws Exception {
        Path sqlPath = Path.of(System.getProperty("user.dir"))
            .resolve("../../script/sql/yunxi-agent-platform.sql")
            .normalize();
        String sql = Files.readString(sqlPath, StandardCharsets.UTF_8);

        assertThat(sql).contains("create database if not exists yunxi_agent_platform");
        assertThat(sql).contains("create table yunxi_agent_session");
        assertThat(sql).contains("create table yunxi_agent_message");
        assertThat(sql).contains("create table yunxi_agent_trace");
        assertThat(sql).contains("create table yunxi_agent_trace_step");
        assertThat(sql).contains("step_order");
        assertThat(sql).contains("parent_step_id");
        assertThat(sql).contains("event_type");
    }
}
