-- Create table
create table ORDEREXECUTEFORMINFO
(
  orderexecuteforminfo_id     NUMBER(38) not null,
  plannedexecution_id         NUMBER(38) not null,
  order_execute_form_cat      VARCHAR2(8) not null,
  orderperformlocationmstr_id NUMBER(38) not null,
  defunct_ind                 CHAR(1) default 'N' not null,
  created_by                  NUMBER(38) not null,
  created_datetime            DATE default SYSDATE not null,
  last_updated_by             NUMBER(38) not null,
  last_updated_datetime       DATE default SYSDATE not null
)
tablespace DATA_OM
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table ORDEREXECUTEFORMINFO
  is '医嘱执行表单主记录';
-- Add comments to the columns 
comment on column ORDEREXECUTEFORMINFO.orderexecuteforminfo_id
  is '医嘱执行表单记录ID';
comment on column ORDEREXECUTEFORMINFO.plannedexecution_id
  is '医嘱执行档ID';
comment on column ORDEREXECUTEFORMINFO.order_execute_form_cat
  is 'CAT= OEC 医嘱执行表单';
comment on column ORDEREXECUTEFORMINFO.orderperformlocationmstr_id
  is '医嘱执行地点ID';
comment on column ORDEREXECUTEFORMINFO.defunct_ind
  is '删除标识';
comment on column ORDEREXECUTEFORMINFO.created_by
  is '创建用户ID';
comment on column ORDEREXECUTEFORMINFO.created_datetime
  is '创建时间';
comment on column ORDEREXECUTEFORMINFO.last_updated_by
  is '最后更新用户ID';
comment on column ORDEREXECUTEFORMINFO.last_updated_datetime
  is '最后更新时间';
-- Create/Recreate indexes 
create index NDX_OEFI_PE on ORDEREXECUTEFORMINFO (PLANNEDEXECUTION_ID)
  tablespace INDEX_OM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table ORDEREXECUTEFORMINFO
  add constraint PK_ORDEREXECUTEFORMINFO primary key (ORDEREXECUTEFORMINFO_ID)
  using index 
  tablespace INDEX_OM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table ORDEREXECUTEFORMINFO
  add constraint NDX_OEFI_PE foreign key (PLANNEDEXECUTION_ID)
  references PLANNEDEXECUTION (PLANNEDEXECUTION_ID);
