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

with
  reports_to_101 (eid, emp_last, mgr_id, reportlevel) as
  (
     (select employee_id, last_name, manager_id, 0 reportlevel
     from employees
     where employee_id = 101)
     union all
     (select e.employee_id, e.last_name, e.manager_id, reportlevel+1
     from reports_to_101 r, employees e
     where r.eid = e.manager_id)
  )
select eid, emp_last, mgr_id, reportlevel
from reports_to_101 r, auto a
where r.c1 = a.c2
order by reportlevel, eid

