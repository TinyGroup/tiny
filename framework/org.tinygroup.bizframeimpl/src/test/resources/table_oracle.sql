--
--  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
--
--  Licensed under the GPL, Version 3.0 (the "License");
--  you may not use this file except in compliance with the License.
--  You may obtain a copy of the License at
--
--       http://www.gnu.org/licenses/gpl.html
--
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--  See the License for the specific language governing permissions and
--  limitations under the License.
--

--prompt PL/SQL Developer import file
--prompt Created on 2013年7月30日 by renhui
--set feedback off
--set define off
drop table T_USER;
drop table T_FUNCTION;
drop table T_USER_T_FUNCTION;
drop sequence SEQ_T_USER;
--prompt Creating T_USER...
create table T_USER
(
  user_id     VARCHAR2(32) not null,
  name   VARCHAR2(32),
  title  VARCHAR2(32),
  description    VARCHAR2(32)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_USER
  add constraint USER_ID primary key (user_id)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--prompt Creating T_FUNCTION...
create table T_FUNCTION
(
  fun_id     VARCHAR2(32) not null,
  name   VARCHAR2(32),
  title  VARCHAR2(32),
  description    VARCHAR2(32)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_FUNCTION
  add constraint FUNCTION_ID primary key (fun_id)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
  --prompt Creating T_USER_T_FUNCTION...
create table T_USER_T_FUNCTION
(
  user_fun_id VARCHAR2(32) not null,
  user_id     VARCHAR2(32),
  fun_id   VARCHAR2(32),
  status   VARCHAR(1)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table T_USER_T_FUNCTION
  add constraint USER_FUNCTION_ID primary key (user_fun_id)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
--prompt creat sequence  
create sequence SEQ_T_USER
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;
--set feedback on
--set define on
--prompt Done.
