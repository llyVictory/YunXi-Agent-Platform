package org.dromara.yunxi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.yunxi.domain.AgentSession;

/**
 * Agent 会话创建对象。
 *
 * @author yunxi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AgentSession.class, reverseConvertGenerate = false)
public class AgentSessionCreateBo extends BaseEntity {

    @Size(max = 120, message = "会话标题长度不能超过{max}个字符")
    private String title;

    @Size(max = 50, message = "业务类型长度不能超过{max}个字符")
    private String businessType;

    @Size(max = 64, message = "客户编号长度不能超过{max}个字符")
    private String customerId;
}
