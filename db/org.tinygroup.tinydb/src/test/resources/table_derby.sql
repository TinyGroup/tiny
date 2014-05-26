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
--First　delete　the　tables　if　they　exist.　
--Ignore　the　table　does　not　exist　error　if　present　
drop table ANIMAL;
drop table A_BRANCH;
drop table A_PEOPLE;
drop table incrementer;
drop table one_to_more1;
drop table one_to_more2;
drop table one_to_more3;
drop table more_to_one1;
drop table more_to_one2;
drop table more_to_one3;
drop table more_to_one4;
--prompt Creating ANIMAL...
create table ANIMAL
(
  id     VARCHAR(32) primary key  not null,
  name   VARCHAR(32),
  length int
);
--prompt Creating A_BRANCH...
create table A_BRANCH
(
  branch_id   VARCHAR(32) primary key not null,
  branch_name VARCHAR(32)
);
--prompt Creating A_PEOPLE...
create table A_PEOPLE
(
  people_id   VARCHAR(32)  primary key not null,
  people_name VARCHAR(32),
  branch      VARCHAR(32),
  people_age  int
);
--prompt Creating incrementer...
create table incrementer
(
  value int generated always as identity, 
  dummy char(1)
);
--prompt Creating one_to_more1...
create table one_to_more1
(
  id     VARCHAR(32) primary key  not null,
  name   VARCHAR(32)
);
--prompt Creating one_to_more2...
create table one_to_more2
(
  id     VARCHAR(32) primary key  not null,
  name   VARCHAR(32),
  relation_id VARCHAR(32)
);
--prompt Creating one_to_more3...
create table one_to_more3
(
  id     VARCHAR(32) primary key  not null,
  name   VARCHAR(32),
  relation_id VARCHAR(32)
);
--prompt Creating more_to_one1...
create table more_to_one1
(
  id     VARCHAR(32) primary key  not null,
  name   VARCHAR(32),
  relation_id1 VARCHAR(32),
  relation_id2 VARCHAR(32)
);
--prompt Creating more_to_one2...
create table more_to_one2
(
  id     VARCHAR(32) primary key  not null,
  name   VARCHAR(32),
  relation_id VARCHAR(32)
);
--prompt Creating more_to_one3...
create table more_to_one3
(
  id     VARCHAR(32) primary key  not null,
  name   VARCHAR(32),
  relation_id VARCHAR(32)
);
--prompt Creating more_to_one4...
create table more_to_one4
(
  id     VARCHAR(32) primary key  not null,
  name   VARCHAR(32),
  relation_id VARCHAR(32)
);
--prompt Disabling triggers for ANIMAL...
--alter table ANIMAL disable all triggers;
--prompt Disabling triggers for A_BRANCH...
--alter table A_BRANCH disable all triggers;
--prompt Disabling triggers for A_PEOPLE...
--alter table A_PEOPLE disable all triggers;
--prompt Loading ANIMAL...
--prompt Table is empty
--prompt Loading A_BRANCH...
--prompt Table is empty
--prompt Loading A_PEOPLE...
--prompt Table is empty
--prompt Enabling triggers for ANIMAL...
--alter table ANIMAL enable all triggers;
--prompt Enabling triggers for A_BRANCH...
--alter table A_BRANCH enable all triggers;
--prompt Enabling triggers for A_PEOPLE...
--alter table A_PEOPLE enable all triggers;
--set feedback on
--set define on
--prompt Done.
