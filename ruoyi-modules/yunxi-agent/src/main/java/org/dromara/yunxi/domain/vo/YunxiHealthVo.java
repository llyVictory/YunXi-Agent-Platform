package org.dromara.yunxi.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 云犀 Agent 探活信息。
 *
 * @author yunxi
 */
@Data
public class YunxiHealthVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String serviceName;

    private String status;

    private String chatProvider;

    private String embeddingProvider;

    private String rerankProvider;
}
