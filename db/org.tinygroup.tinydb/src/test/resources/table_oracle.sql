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
drop table ANIMAL;
drop table A_BRANCH;
drop table A_PEOPLE;
drop table one_to_more1;
drop table one_to_more2;
drop table one_to_more3;
drop table more_to_one1;
drop table more_to_one2;
drop table more_to_one3;
drop table more_to_one4;
drop sequence seq_default;
--prompt Creating ANIMAL...
create table ANIMAL
(
  id     VARCHAR2(32) not null,
  name   VARCHAR2(32),
  sex    VARCHAR2(32),
  length NUMBER
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
alter table ANIMAL
  add constraint ANIMAL_ID primary key (ID)
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

--prompt Creating A_BRANCH...
create table A_BRANCH
(
  branch_id   VARCHAR2(32) not null,
  branch_name VARCHAR2(32)
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
alter table A_BRANCH
  add constraint A_BRANCH_ID primary key (BRANCH_ID)
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

--prompt Creating A_PEOPLE...
create table A_PEOPLE
(
  people_id   VARCHAR2(32) not null,
  people_name VARCHAR2(32),
  branch      VARCHAR2(32),
  people_age  NUMBER
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
alter table A_PEOPLE
  add constraint A_PEOPLE_ID primary key (PEOPLE_ID)
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
--prompt Creating one_to_more1...
create table one_to_more1
(
  id   VARCHAR2(32) not null,
  name VARCHAR2(32)
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
alter table one_to_more1
  add constraint ONE_MORE_ID_1 primary key (id)
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
--prompt Creating one_to_more1...
create table one_to_more2
(
  id   VARCHAR2(32) not null,
  name VARCHAR2(32),
  relation_id VARCHAR2(32)
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
alter table one_to_more2
  add constraint ONE_MORE_ID_2 primary key (id)
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
--prompt Creating one_to_more3...
create table one_to_more3
(
  id   VARCHAR2(32) not null,
  name VARCHAR2(32),
  relation_id VARCHAR2(32)
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
alter table one_to_more3
  add constraint ONE_MORE_ID_3 primary key (id)
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
--prompt Creating more_to_one1...
create table more_to_one1
(
  id   VARCHAR2(32) not null,
  name VARCHAR2(32),
  relation_id1 VARCHAR(32),
  relation_id2 VARCHAR(32)
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
alter table more_to_one1
  add constraint MORE_ONE_ID_1 primary key (id)
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
--prompt Creating more_to_one2...
create table more_to_one2
(
  id   VARCHAR2(32) not null,
  name VARCHAR2(32),
  relation_id VARCHAR2(32)
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
alter table more_to_one2
  add constraint MORE_ONE_ID_2 primary key (id)
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
--prompt Creating more_to_one3...
create table more_to_one3
(
  id   VARCHAR2(32) not null,
  name VARCHAR2(32),
  relation_id VARCHAR2(32)
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
alter table more_to_one3
  add constraint MORE_ONE_ID_3 primary key (id)
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
--prompt Creating more_to_one4...
create table more_to_one4
(
  id   VARCHAR2(32) not null,
  name VARCHAR2(32),
  relation_id VARCHAR2(32)
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
alter table more_to_one4
  add constraint MORE_ONE_ID_4 primary key (id)
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
create sequence seq_default minvalue 1000 maxvalue 99999999 start with 1000 increment by 1 nocache; 

--prompt Disabling triggers for ANIMAL...
alter table ANIMAL disable all triggers;
--prompt Disabling triggers for A_BRANCH...
alter table A_BRANCH disable all triggers;
--prompt Disabling triggers for A_PEOPLE...
alter table A_PEOPLE disable all triggers;
--prompt Loading ANIMAL...
--prompt Table is empty
--prompt Loading A_BRANCH...
--prompt Table is empty
--prompt Loading A_PEOPLE...
--prompt Table is empty
--prompt Enabling triggers for ANIMAL...
alter table ANIMAL enable all triggers;
--prompt Enabling triggers for A_BRANCH...
alter table A_BRANCH enable all triggers;
--prompt Enabling triggers for A_PEOPLE...
alter table A_PEOPLE enable all triggers;
--set feedback on
--set define on
--prompt Done.
