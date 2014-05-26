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

select t1.department_id, t2.* 
   from hr_info t1, 
   table
   (
    cast
    (
        multiset
        (
            select t3.last_name, t3.department_id, t3.salary 
            from people t3
            where t3.department_id = t1.department_id
        )
        as people_tab_typ
    )
) t2

