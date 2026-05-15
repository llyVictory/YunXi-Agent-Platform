-- ----------------------------
-- 云犀 Agent 独立业务库
-- ----------------------------
create database if not exists yunxi_agent_platform default character set utf8mb4 collate utf8mb4_general_ci;

use yunxi_agent_platform;

-- ----------------------------
-- Agent 会话表
-- ----------------------------
create table yunxi_agent_session
(
    session_id        bigint(20)    not null                 comment '会话ID',
    tenant_id         varchar(20)   default '000000'         comment '租户编号',
    user_id           bigint(20)    not null                 comment '用户ID',
    title             varchar(120)  default ''               comment '会话标题',
    business_type     varchar(50)   default ''               comment '业务类型',
    customer_id       varchar(64)   default ''               comment '客户编号',
    status            varchar(20)   default 'active'         comment '会话状态',
    last_message_time datetime                              comment '最后消息时间',
    create_dept       bigint(20)                             comment '创建部门',
    create_by         bigint(20)                             comment '创建者',
    create_time       datetime                               comment '创建时间',
    update_by         bigint(20)                             comment '更新者',
    update_time       datetime                               comment '更新时间',
    del_flag          char(1)      default '0'               comment '删除标志',
    primary key (session_id),
    key idx_yunxi_session_user (tenant_id, user_id, create_time),
    key idx_yunxi_session_customer (tenant_id, customer_id)
) engine=innodb comment = '云犀 Agent 会话表';

-- ----------------------------
-- Agent 消息表
-- ----------------------------
create table yunxi_agent_message
(
    message_id    bigint(20)   not null                 comment '消息ID',
    tenant_id     varchar(20)  default '000000'         comment '租户编号',
    session_id    bigint(20)   not null                 comment '会话ID',
    trace_id      bigint(20)                            comment 'Trace ID',
    user_id       bigint(20)   not null                 comment '用户ID',
    role          varchar(20)  not null                 comment '消息角色',
    content       text                                  comment '消息内容',
    content_type  varchar(20)  default 'text'           comment '内容类型',
    sequence_no   int          default 0                comment '消息序号',
    status        varchar(20)  default 'success'        comment '消息状态',
    create_dept   bigint(20)                            comment '创建部门',
    create_by     bigint(20)                            comment '创建者',
    create_time   datetime                              comment '创建时间',
    update_by     bigint(20)                            comment '更新者',
    update_time   datetime                              comment '更新时间',
    del_flag      char(1)      default '0'              comment '删除标志',
    primary key (message_id),
    key idx_yunxi_message_session (tenant_id, session_id, sequence_no),
    key idx_yunxi_message_trace (tenant_id, trace_id)
) engine=innodb comment = '云犀 Agent 消息表';

-- ----------------------------
-- Agent Trace 主表
-- ----------------------------
create table yunxi_agent_trace
(
    trace_id      bigint(20)   not null                 comment 'Trace ID',
    tenant_id     varchar(20)  default '000000'         comment '租户编号',
    session_id    bigint(20)   not null                 comment '会话ID',
    user_id       bigint(20)   not null                 comment '用户ID',
    business_type varchar(50)  default ''               comment '业务类型',
    status        varchar(20)  default 'running'        comment 'Trace 状态',
    started_at    datetime                              comment '开始时间',
    finished_at   datetime                              comment '结束时间',
    elapsed_ms    bigint(20)                            comment '总耗时毫秒',
    error_code    varchar(64)                           comment '错误码',
    error_message varchar(500)                          comment '错误信息',
    create_dept   bigint(20)                            comment '创建部门',
    create_by     bigint(20)                            comment '创建者',
    create_time   datetime                              comment '创建时间',
    update_by     bigint(20)                            comment '更新者',
    update_time   datetime                              comment '更新时间',
    del_flag      char(1)      default '0'              comment '删除标志',
    primary key (trace_id),
    key idx_yunxi_trace_session (tenant_id, session_id, create_time)
) engine=innodb comment = '云犀 Agent Trace 主表';

-- ----------------------------
-- Agent Trace 步骤表
-- ----------------------------
create table yunxi_agent_trace_step
(
    step_id        bigint(20)   not null                 comment '步骤ID',
    tenant_id      varchar(20)  default '000000'         comment '租户编号',
    trace_id       bigint(20)   not null                 comment 'Trace ID',
    parent_step_id bigint(20)                            comment '父步骤ID',
    step_order     int          not null                 comment '步骤顺序',
    step_name      varchar(80)  not null                 comment '步骤名称',
    step_type      varchar(30)  not null                 comment '步骤类型',
    event_type     varchar(50)                           comment '事件类型',
    status         varchar(20)  default 'running'        comment '步骤状态',
    input_summary  varchar(1000)                         comment '输入摘要',
    output_summary varchar(1000)                         comment '输出摘要',
    elapsed_ms     bigint(20)                            comment '耗时毫秒',
    error_code     varchar(64)                           comment '错误码',
    error_message  varchar(500)                          comment '错误信息',
    create_dept    bigint(20)                            comment '创建部门',
    create_by      bigint(20)                            comment '创建者',
    create_time    datetime                              comment '创建时间',
    update_by      bigint(20)                            comment '更新者',
    update_time    datetime                              comment '更新时间',
    del_flag       char(1)      default '0'              comment '删除标志',
    primary key (step_id),
    key idx_yunxi_trace_step_order (tenant_id, trace_id, step_order),
    key idx_yunxi_trace_step_parent (tenant_id, parent_step_id)
) engine=innodb comment = '云犀 Agent Trace 步骤表';
