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

select
       c.constraint_name
       , max(r.constraint_name) as r_constraint_name
       , max(c.owner)           as owner
       , max(c.table_name)      as table_name
       , c.column_name          as column_name
       , max(r.owner)           as r_owner
       , max(r.table_name)      as r_table_name
       , max(r.column_name)     as r_column_name
       , max(a.constraint_type)
 from sys.all_constraints a
 join sys.all_cons_columns c on (c.constraint_name = a.constraint_name and c.owner = a.owner)
 join sys.all_cons_columns r on (r.constraint_name = a.r_constraint_name and r.owner = a.r_owner and r.position = c.position)
 where
          a.r_owner =                   :f1
      and a.constraint_type = 'r'
 group by c.constraint_name, rollup (c.column_name)
	  